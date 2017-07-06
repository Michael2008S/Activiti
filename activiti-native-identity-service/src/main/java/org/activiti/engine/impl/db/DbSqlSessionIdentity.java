/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.db;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.cache.EntityCache;
import org.activiti.engine.impl.persistence.entity.PropertyEntity;

public class DbSqlSessionIdentity extends DbSqlSession {

    public DbSqlSessionIdentity(DbSqlSessionFactory dbSqlSessionFactory, EntityCache entityCache) {
        super(dbSqlSessionFactory, entityCache);
    }

    @Override
    public void dbSchemaCreate() {
        if (dbSqlSessionFactory.isDbIdentityUsed()) {
            dbSchemaCreateIdentity();
        }
    }

    @Override
    public String dbSchemaUpdate() {

        PropertyEntity dbVersionProperty = selectById(PropertyEntity.class, "schema.version");
        String dbVersion = dbVersionProperty.getValue();

        // Determine index in the sequence of Activiti releases
        int matchingVersionIndex = findMatchingVersionIndex(dbVersion);

        if (matchingVersionIndex < 0) {
            throw new ActivitiException("Could not update Activiti database schema: unknown version from database: '" + dbVersion + "'");
        }

        boolean isUpgradeNeeded = (matchingVersionIndex != (ACTIVITI_VERSIONS.size() - 1));

        if (dbSqlSessionFactory.isDbIdentityUsed()) {
            if (isIdentityTablePresent()) {
                //TODO: don't see this path ever being needed and not sure how it would work if it were with engine seperate
                if(isUpgradeNeeded) {
                    dbSchemaUpgrade("identity", matchingVersionIndex);
                    return "upgraded Activiti Identity Management to " + ProcessEngine.VERSION;
                }
            } else{
                dbSchemaCreate();
                return "created Activiti Identity Management tables for " + ProcessEngine.VERSION;
            }
        }
        return null;
    }

    @Override
    public void dbSchemaDrop() {
        if (dbSqlSessionFactory.isDbIdentityUsed()) {
            executeMandatorySchemaResource("drop", "identity");
        }
    }

    @Override
    public void dbSchemaPrune() {
        if (isIdentityTablePresent() && dbSqlSessionFactory.isDbIdentityUsed()) {
            executeMandatorySchemaResource("drop", "identity");
        }
    }


    public boolean isIdentityTablePresent() {
        return isTablePresent("ACT_ID_USER");
    }

    protected void dbSchemaCreateIdentity() {
        executeMandatorySchemaResource("create", "identity");
    }

    @Override
    public void dbSchemaCheckVersion() {
        String errorMessage = null;
        if (dbSqlSessionFactory.isDbIdentityUsed() && !isIdentityTablePresent()) {
            errorMessage = addMissingComponent(errorMessage, "identity");
        }
        if (errorMessage != null) {
            throw new ActivitiException("Activiti database problem: " + errorMessage);
        }
    }

}

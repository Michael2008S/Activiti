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

package org.activiti.engine.impl.persistence.entity;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.TablePageQueryImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationIdentityImpl;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.persistence.AbstractManagerForIdentity;
import org.activiti.engine.management.TableMetaData;
import org.activiti.engine.management.TablePage;
import org.activiviti.engine.identity.Group;
import org.activiviti.engine.identity.User;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableDataManagerForIdentityImpl extends AbstractManagerForIdentity implements TableDataManager {

    public TableDataManagerForIdentityImpl(ProcessEngineConfigurationIdentityImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    private static Logger log = LoggerFactory.getLogger(TableDataManagerForIdentityImpl.class);

    public static Map<Class<?>, String> apiTypeToTableNameMap = new HashMap<Class<?>, String>();
    public static Map<Class<? extends Entity>, String> entityToTableNameMap = new HashMap<Class<? extends Entity>, String>();


    static {
        // runtime

        // Identity module
        entityToTableNameMap.put(GroupEntity.class, "ACT_ID_GROUP");
        entityToTableNameMap.put(MembershipEntity.class, "ACT_ID_MEMBERSHIP");
        entityToTableNameMap.put(UserEntity.class, "ACT_ID_USER");
        entityToTableNameMap.put(IdentityInfoEntity.class, "ACT_ID_INFO");

        // identity
        apiTypeToTableNameMap.put(Group.class, "ACT_ID_GROUP");
        apiTypeToTableNameMap.put(User.class, "ACT_ID_USER");

        // TODO: Identity skipped for the moment as no SQL injection is provided
        // here
    }

    protected DbSqlSession getDbSqlSession() {
        return getSession(DbSqlSession.class);
    }


    @Override
    public Map<String, Long> getTableCount() {
        Map<String, Long> tableCount = new HashMap<String, Long>();
        try {
            for (String tableName : getTablesPresentInDatabase()) {
                tableCount.put(tableName, getTableCount(tableName));
            }
            log.debug("Number of rows per activiti table: {}", tableCount);
        } catch (Exception e) {
            throw new ActivitiException("couldn't get table counts", e);
        }
        return tableCount;
    }

    @Override
    public List<String> getTablesPresentInDatabase() {
        List<String> tableNames = new ArrayList<String>();
        Connection connection = null;
        try {
            connection = getDbSqlSession().getSqlSession().getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = null;
            try {
                log.debug("retrieving activiti tables from jdbc metadata");
                String databaseTablePrefix = getDbSqlSession().getDbSqlSessionFactory().getDatabaseTablePrefix();
                String tableNameFilter = databaseTablePrefix + "ACT_%";
                if ("postgres".equals(getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {
                    tableNameFilter = databaseTablePrefix + "act_%";
                }
                if ("oracle".equals(getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {
                    tableNameFilter = databaseTablePrefix + "ACT" + databaseMetaData.getSearchStringEscape() + "_%";
                }

                String catalog = null;
                if (getProcessEngineConfiguration().getDatabaseCatalog() != null && getProcessEngineConfiguration().getDatabaseCatalog().length() > 0) {
                    catalog = getProcessEngineConfiguration().getDatabaseCatalog();
                }

                String schema = null;
                if (getProcessEngineConfiguration().getDatabaseSchema() != null && getProcessEngineConfiguration().getDatabaseSchema().length() > 0) {
                    if ("oracle".equals(getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {
                        schema = getProcessEngineConfiguration().getDatabaseSchema().toUpperCase();
                    } else {
                        schema = getProcessEngineConfiguration().getDatabaseSchema();
                    }
                }

                tables = databaseMetaData.getTables(catalog, schema, tableNameFilter, DbSqlSession.JDBC_METADATA_TABLE_TYPES);
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    tableName = tableName.toUpperCase();
                    tableNames.add(tableName);
                    log.debug("  retrieved activiti table name {}", tableName);
                }
            } finally {
                tables.close();
            }
        } catch (Exception e) {
            throw new ActivitiException("couldn't get activiti table names using metadata: " + e.getMessage(), e);
        }
        return tableNames;
    }

    protected long getTableCount(String tableName) {
        log.debug("selecting table count for {}", tableName);
        Long count = (Long) getDbSqlSession().selectOne("selectTableCount", Collections.singletonMap("tableName", tableName));
        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TablePage getTablePage(TablePageQueryImpl tablePageQuery, int firstResult, int maxResults) {

        TablePage tablePage = new TablePage();

        @SuppressWarnings("rawtypes")
        List tableData = getDbSqlSession().getSqlSession().selectList("selectTableData", tablePageQuery, new RowBounds(firstResult, maxResults));

        tablePage.setTableName(tablePageQuery.getTableName());
        tablePage.setTotal(getTableCount(tablePageQuery.getTableName()));
        tablePage.setRows((List<Map<String, Object>>) tableData);
        tablePage.setFirstResult(firstResult);

        return tablePage;
    }

    @Override
    public String getTableName(Class<?> entityClass, boolean withPrefix) {
        String databaseTablePrefix = getDbSqlSession().getDbSqlSessionFactory().getDatabaseTablePrefix();
        String tableName = null;

        if (Entity.class.isAssignableFrom(entityClass)) {
            tableName = entityToTableNameMap.get(entityClass);
        } else {
            tableName = apiTypeToTableNameMap.get(entityClass);
        }
        if (withPrefix) {
            return databaseTablePrefix + tableName;
        } else {
            return tableName;
        }
    }

    @Override
    public TableMetaData getTableMetaData(String tableName) {
        TableMetaData result = new TableMetaData();
        try {
            result.setTableName(tableName);
            DatabaseMetaData metaData = getDbSqlSession().getSqlSession().getConnection().getMetaData();

            if ("postgres".equals(getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {
                tableName = tableName.toLowerCase();
            }

            String catalog = null;
            if (getProcessEngineConfiguration().getDatabaseCatalog() != null && getProcessEngineConfiguration().getDatabaseCatalog().length() > 0) {
                catalog = getProcessEngineConfiguration().getDatabaseCatalog();
            }

            String schema = null;
            if (getProcessEngineConfiguration().getDatabaseSchema() != null && getProcessEngineConfiguration().getDatabaseSchema().length() > 0) {
                if ("oracle".equals(getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {
                    schema = getProcessEngineConfiguration().getDatabaseSchema().toUpperCase();
                } else {
                    schema = getProcessEngineConfiguration().getDatabaseSchema();
                }
            }

            ResultSet resultSet = metaData.getColumns(catalog, schema, tableName, null);
            while(resultSet.next()) {
                boolean wrongSchema = false;
                if (schema != null && schema.length() > 0) {
                    for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i+1);
                        if ("TABLE_SCHEM".equalsIgnoreCase(columnName) || "TABLE_SCHEMA".equalsIgnoreCase(columnName)) {
                            if (schema.equalsIgnoreCase(resultSet.getString(resultSet.getMetaData().getColumnName(i+1))) == false) {
                                wrongSchema = true;
                            }
                            break;
                        }
                    }
                }

                if (wrongSchema == false) {
                    String name = resultSet.getString("COLUMN_NAME").toUpperCase();
                    String type = resultSet.getString("TYPE_NAME").toUpperCase();
                    result.addColumnMetaData(name, type);
                }
            }

        } catch (SQLException e) {
            throw new ActivitiException("Could not retrieve database metadata: " + e.getMessage());
        }

        if (result.getColumnNames().isEmpty()) {
            // According to API, when a table doesn't exist, null should be returned
            result = null;
        }
        return result;
    }

}



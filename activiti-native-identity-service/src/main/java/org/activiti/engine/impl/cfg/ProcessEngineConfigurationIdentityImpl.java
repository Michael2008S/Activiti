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

package org.activiti.engine.impl.cfg;

import org.activiti.engine.impl.IdentityServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.GroupEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.MembershipEntityManager;
import org.activiti.engine.impl.persistence.entity.MembershipEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.activiti.engine.impl.persistence.entity.UserEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.data.HistoricVariableInstanceDataManager;
import org.activiti.engine.impl.persistence.entity.data.IdentityInfoDataManager;
import org.activiti.engine.impl.persistence.entity.data.UserDataManager;
import org.activiti.engine.impl.persistence.entity.data.impl.MybatisIdentityInfoDataManager;
import org.activiti.engine.impl.persistence.entity.data.impl.MybatisUserDataManager;
import org.org.activiti.engine.IdentityService;

public class ProcessEngineConfigurationIdentityImpl extends ProcessEngineConfigurationImpl {

    protected IdentityService identityService = new IdentityServiceImpl();
    protected UserDataManager userDataManager;
    protected GroupEntityManager groupEntityManager;
    protected MembershipEntityManager membershipEntityManager;
    protected UserEntityManager userEntityManager;
    protected IdentityInfoEntityManager identityInfoEntityManager;
    protected HistoricVariableInstanceDataManager historicVariableInstanceDataManager;
    protected IdentityInfoDataManager identityInfoDataManager;

    public void initServices() {
        super.initServices();
        initService(identityService);
    }

    public void initDataManagers(){
        super.initDataManagers();
        if (userDataManager == null) {
            userDataManager = new MybatisUserDataManager(this);
        }
        if (identityInfoDataManager == null) {
            identityInfoDataManager = new MybatisIdentityInfoDataManager(this);
        }
    }

    public void initEntityManagers() {
        super.initEntityManagers();
        if (groupEntityManager == null) {
            groupEntityManager = new GroupEntityManagerImpl(this, groupDataManager);
        }
        if (membershipEntityManager == null) {
            membershipEntityManager = new MembershipEntityManagerImpl(this, membershipDataManager);
        }
        if (userEntityManager == null) {
            userEntityManager = new UserEntityManagerImpl(this, userDataManager);
        }
        if (identityInfoEntityManager == null) {
            identityInfoEntityManager = new IdentityInfoEntityManagerImpl(this, identityInfoDataManager);
        }
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public ProcessEngineConfigurationImpl setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
        return this;
    }

    public GroupEntityManager getGroupEntityManager() {
        return groupEntityManager;
    }

    public ProcessEngineConfigurationImpl setGroupEntityManager(GroupEntityManager groupEntityManager) {
        this.groupEntityManager = groupEntityManager;
        return this;
    }

    public MembershipEntityManager getMembershipEntityManager() {
        return membershipEntityManager;
    }

    public ProcessEngineConfigurationImpl setMembershipEntityManager(MembershipEntityManager membershipEntityManager) {
        this.membershipEntityManager = membershipEntityManager;
        return this;
    }

    public UserEntityManager getUserEntityManager() {
        return userEntityManager;
    }

    public ProcessEngineConfigurationImpl setUserEntityManager(UserEntityManager userEntityManager) {
        this.userEntityManager = userEntityManager;
        return this;
    }

    public UserDataManager getUserDataManager() {
        return userDataManager;
    }

    public ProcessEngineConfigurationImpl setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
        return this;
    }


    public IdentityInfoEntityManager getIdentityInfoEntityManager() {
        return identityInfoEntityManager;
    }

    public ProcessEngineConfigurationImpl setIdentityInfoEntityManager(IdentityInfoEntityManager identityInfoEntityManager) {
        this.identityInfoEntityManager = identityInfoEntityManager;
        return this;
    }
}

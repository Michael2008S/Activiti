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

package org.org.activiti.engine;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class ProcessEngineIdentityConfigurationImpl extends ProcessEngineConfigurationImpl {

    protected MembershipDataManager membershipDataManager;
    protected GroupDataManager groupDataManager;

    public void initDataManagers() {
        super.initDataManagers();
        if (groupDataManager == null) {
            groupDataManager = new MybatisGroupDataManager(this);
        }
        if (membershipDataManager == null) {
            membershipDataManager = new MybatisMembershipDataManager(this);
        }
    }

    public MembershipDataManager getMembershipDataManager() {
        return membershipDataManager;
    }

    public ProcessEngineConfigurationImpl setMembershipDataManager(MembershipDataManager membershipDataManager) {
        this.membershipDataManager = membershipDataManager;
        return this;
    }

    public GroupDataManager getGroupDataManager() {
        return groupDataManager;
    }

    public ProcessEngineConfigurationImpl setGroupDataManager(GroupDataManager groupDataManager) {
        this.groupDataManager = groupDataManager;
        return this;
    }
}

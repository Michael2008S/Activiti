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

package org.activiti.engine.impl.persistence;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationIdentityImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntityManager;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntityManager;
import org.activiti.engine.impl.persistence.entity.MembershipEntityManager;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

public abstract class AbstractManagerForIdentity {

    protected ProcessEngineConfigurationIdentityImpl processEngineConfiguration;

    public AbstractManagerForIdentity(ProcessEngineConfigurationIdentityImpl configuration){
        this.processEngineConfiguration = configuration;
    }

    protected UserEntityManager getUserIdentityEntityManager() {
        return getProcessEngineConfiguration().getUserEntityManager();
    }

    protected GroupEntityManager getGroupEntityManager() {
        return getProcessEngineConfiguration().getGroupEntityManager();
    }

    protected IdentityInfoEntityManager getIdentityInfoEntityManager() {
        return getProcessEngineConfiguration().getIdentityInfoEntityManager();
    }

    protected MembershipEntityManager getMembershipEntityManager() {
        return getProcessEngineConfiguration().getMembershipEntityManager();
    }

    protected AttachmentEntityManager getAttachmentEntityManager() {
        return getProcessEngineConfiguration().getAttachmentEntityManager();
    }

    protected CommentEntityManager getCommentEntityManager() {
        return getProcessEngineConfiguration().getCommentEntityManager();
    }

    protected ProcessEngineConfigurationIdentityImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    protected <T> T getSession(Class<T> sessionClass) {
        return getCommandContext().getSession(sessionClass);
    }

    protected CommandContext getCommandContext() {
        return Context.getCommandContext();
    }

}

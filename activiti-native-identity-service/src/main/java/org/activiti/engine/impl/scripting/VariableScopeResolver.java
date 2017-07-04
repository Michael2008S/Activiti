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

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.scripting;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationIdentityImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**


 */
public class VariableScopeIdentityResolver extends VariableScopeResolver implements Resolver {

  protected ProcessEngineConfigurationIdentityImpl processEngineConfiguration;

  protected static final String identityServiceKey = "identityServiceKey";

  protected static final List<String> KEYS = Arrays.asList(
          processEngineConfigurationKey, runtimeServiceKey, taskServiceKey,
          repositoryServiceKey, managementServiceKey, historyServiceKey, formServiceKey, identityServiceKey);

  public VariableScopeIdentityResolver(ProcessEngineConfigurationIdentityImpl processEngineConfiguration, VariableScope variableScope) {
      super(processEngineConfiguration,variableScope);
  }

    public Object get(Object key) {
    if (variableScopeKey.equals(key)) {
      return variableScope;
    } else if (processEngineConfigurationKey.equals(key)) {
      return processEngineConfiguration;
    } else if (runtimeServiceKey.equals(key)) {
      return processEngineConfiguration.getRuntimeService();
    } else if (taskServiceKey.equals(key)) {
      return processEngineConfiguration.getTaskService();
    } else if (repositoryServiceKey.equals(key)) {
      return processEngineConfiguration.getRepositoryService();
    } else if (managementServiceKey.equals(key)) {
      return processEngineConfiguration.getManagementService();
    } else if (formServiceKey.equals(key)) {
      return processEngineConfiguration.getFormService();
    } else if (identityServiceKey.equals(key)) {
      return processEngineConfiguration.getIdentityService();
    }

    return variableScope.getVariable((String) key);
  }

  public boolean containsKey(Object key) {
    return variableScopeKey.equals(key) || KEYS.contains(key)|| variableScope.hasVariable((String) key);
  }
}

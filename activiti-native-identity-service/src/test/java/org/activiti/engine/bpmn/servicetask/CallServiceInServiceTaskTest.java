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
package org.activiti.engine.bpmn.servicetask;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

/**

 */
public class CallServiceInServiceTaskTest extends PluggableActivitiTestCase {


  @Deployment
  public void testMultipleServiceInvocationsFromDelegate() {
    runtimeService.startProcessInstanceByKey("multipleServiceInvocations");

    // The service task should have created a user which is part of the
    // admin group
    User user = identityService.createUserQuery().singleResult();
    assertEquals("Kermit", user.getId());
    Group group = identityService.createGroupQuery().groupMember(user.getId()).singleResult();
    assertNotNull(group);
    assertEquals("admin", group.getId());

    // Cleanup
    identityService.deleteUser("Kermit");
    identityService.deleteGroup("admin");
    identityService.deleteMembership("Kermit", "admin");
  }

}

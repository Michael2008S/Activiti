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
package org.activiti.examples.mgmt;

import java.util.Map;

import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;

/**
 * Test case for the various operations of the {@link ManagementService}
 * 


 */
public class ManagementServiceTest extends PluggableActivitiTestCase {

  public void testTableCount() {
    Map<String, Long> tableCount = managementService.getTableCount();

    String tablePrefix = processEngineConfiguration.getDatabaseTablePrefix();


    assertEquals(new Long(0), tableCount.get(tablePrefix + "ACT_ID_GROUP"));
    assertEquals(new Long(0), tableCount.get(tablePrefix + "ACT_ID_MEMBERSHIP"));
    assertEquals(new Long(0), tableCount.get(tablePrefix + "ACT_ID_USER"));
  }

}

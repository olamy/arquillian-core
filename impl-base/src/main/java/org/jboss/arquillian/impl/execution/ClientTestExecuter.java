/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.impl.execution;

import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.impl.execution.event.ExecutionEvent;
import org.jboss.arquillian.impl.execution.event.LocalExecutionEvent;
import org.jboss.arquillian.impl.execution.event.RemoteExecutionEvent;
import org.jboss.arquillian.spi.client.deployment.DeploymentScenario;
import org.jboss.arquillian.spi.core.Event;
import org.jboss.arquillian.spi.core.Instance;
import org.jboss.arquillian.spi.core.annotation.Inject;
import org.jboss.arquillian.spi.core.annotation.Observes;
import org.jboss.arquillian.spi.event.suite.Test;

/**
 * TestExecuter for running on the client side. Can switch between Local and Remote test execution.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ClientTestExecuter
{
   @Inject
   private Event<ExecutionEvent> executionEvent;
   
   @Inject
   private Instance<DeploymentScenario> deploymentScenario;

   public void execute(@Observes Test event) throws Exception
   {
      DeploymentScenario scenario = deploymentScenario.get();
      // TODO: DeploymentScenario should not depend on RunModeType API, this should be pr Deployment
      if(scenario.getRunMode() == RunModeType.AS_CLIENT) 
      {
         executionEvent.fire(new LocalExecutionEvent(event.getTestMethodExecutor()));
      }
      else
      {
         executionEvent.fire(new RemoteExecutionEvent(event.getTestMethodExecutor()));
      }
   }
}
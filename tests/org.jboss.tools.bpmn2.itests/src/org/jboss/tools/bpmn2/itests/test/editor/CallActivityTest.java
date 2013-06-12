package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.other.CallActivity;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5OutputParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5Parameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.ParameterVariableMapping;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;

import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CallActivity", project="EditorTestProject")
public class CallActivityTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		JBPM5Process process = new JBPM5Process("BPMN2-CallActivity");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ConstructType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new JBPM5Parameter("subX", null, new ParameterVariableMapping("BPMN2-CallActivity/x")));
		call.addParameterMapping(new JBPM5OutputParameter("subY", null, new ParameterVariableMapping("BPMN2-CallActivity/y")));
		
		call.append("EndProcess", ConstructType.END_EVENT);
		
		EndEvent end = new EndEvent("EndProcess");
		/*
		 * ISSUE: Terminate event definition not present
		 */
//		end.addEventDefinition(new TerminateEventDefinition());
	}
	
}
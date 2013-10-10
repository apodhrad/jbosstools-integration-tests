package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for creating various bindings
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@RunWith(SwitchyardSuite.class)
public class OpenEditorTest {

	public static final String PROJECT = "test_project";
	public static int COUNT = 10;

	@Test
	public void openEditorTest() {
		for (int i = 0; i < COUNT; i++) {
			editorSaveAndClose();
			new SwitchYardProjectWizard(PROJECT + i).create();
			new SwitchYardEditor().addComponent("Component");
			new Component("Component").select();
			editorSaveAndClose();
			new ProjectExplorer().getProject(PROJECT + i).delete(true);
		}
	}

	private static void editorSaveAndClose() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}
}

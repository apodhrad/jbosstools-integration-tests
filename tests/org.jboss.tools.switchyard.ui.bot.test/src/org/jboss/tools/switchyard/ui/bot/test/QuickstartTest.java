package org.jboss.tools.switchyard.ui.bot.test;

import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@CleanWorkspace
@Perspective(name = "Java EE")
// @Server(type = Type.ALL, state = State.RUNNING)
@RunWith(SwitchyardSuite.class)
public class QuickstartTest extends SWTBotTestCase {

	public static final String QUICKSTART_DIR = "/home/apodhrad/Projects/server-installer/switchyard-1.0.0.final/target/jboss-eap-6.1/quickstarts";

	@Test
	public void beanServiceTest() {
		String path = "bean-service";
		testQuickstart(path);
	}

	@Test
	public void bpelJmsBinding() {
		String path = "bpel-service/jms_binding";
		testQuickstart(path);
	}

	@Test
	public void bpelLoanApproval() {
		String path = "bpel-service/loan_approval";
		testQuickstart(path);
	}

	@Test
	public void bpelSayHello() {
		String path = "bpel-service/say_hello";
		testQuickstart(path);
	}

	@Test
	public void bpelSimpleCorrelation() {
		String path = "bpel-service/simple_correlation";
		testQuickstart(path);
	}

	@Test
	public void bpelXtsSubordinateWsba() {
		String path = "bpel-service/xts_subordinate_wsba";
		testQuickstart(path);
	}

	@Test
	public void bpelXtsWsat() {
		String path = "bpel-service/xts_wsat";
		testQuickstart(path);
	}

	@Test
	public void bpmService() {
		String path = "bpm-service";
		testQuickstart(path);
	}

	@Test
	public void camelAmqpBinding() {
		String path = "camel-amqp-binding";
		testQuickstart(path);
	}

	@Test
	public void camelBinding() {
		String path = "camel-binding";
		testQuickstart(path);
	}

	@Test
	public void camelBusCdi() {
		String path = "camel-bus-cdi";
		testQuickstart(path);
	}

	@Test
	public void camelFtpBinding() {
		String path = "camel-ftp-binding";
		testQuickstart(path);
	}

	@Test
	public void camelJaxb() {
		String path = "camel-jaxb";
		testQuickstart(path);
	}

	@Test
	public void camelJmsBinding() {
		String path = "camel-jms-binding";
		testQuickstart(path);
	}

	@Test
	public void camelJpaBinding() {
		String path = "camel-jpa-binding";
		testQuickstart(path);
	}

	@Test
	public void camelMailBinding() {
		String path = "camel-mail-binding";
		testQuickstart(path);
	}

	@Test
	public void camelNettyBinding() {
		String path = "camel-netty-binding";
		testQuickstart(path);
	}

	@Test
	public void camelQuartzBinding() {
		String path = "camel-quartz-binding";
		testQuickstart(path);
	}

	@Test
	public void camelService() {
		String path = "camel-service";
		testQuickstart(path);
	}

	@Test
	public void camelSoapProxy() {
		String path = "camel-soap-proxy";
		testQuickstart(path);
	}

	@Test
	public void camelSqlBinding() {
		String path = "camel-sql-binding";
		testQuickstart(path);
	}

	@Test
	public void httpBinding() {
		String path = "http-binding";
		testQuickstart(path);
	}

	@Test
	public void jcaInflowActivemq() {
		String path = "jca-inflow-activemq";
		testQuickstart(path);
	}

	@Test
	public void jcaInflowHornetq() {
		String path = "jca-inflow-hornetq";
		testQuickstart(path);
	}

	@Test
	public void jcaOutboundActivemq() {
		String path = "jca-outbound-activemq";
		testQuickstart(path);
	}

	@Test
	public void jcaOutboundHornetq() {
		String path = "jca-outbound-hornetq";
		testQuickstart(path);
	}

	@Test
	public void remoteInvoker() {
		String path = "remote-nvoker";
		testQuickstart(path);
	}

	@Test
	public void restBinding() {
		String path = "rest-binding";
		testQuickstart(path);
	}

	@Test
	public void rulesCamelCbr() {
		String path = "rules-camel-cbr";
		testQuickstart(path);
	}

	@Test
	public void rulesInterview() {
		String path = "rules-interview";
		testQuickstart(path);
	}

	@Test
	public void rulesInterviewContainer() {
		String path = "rules-interview-container";
		testQuickstart(path);
	}

	@Test
	public void soapAddressing() {
		String path = "soap-addressing";
		testQuickstart(path);
	}

	@Test
	public void soapAttachment() {
		String path = "soap-attachment";
		testQuickstart(path);
	}

	@Test
	public void soapBindingRpc() {
		String path = "soap-binding-rpc";
		testQuickstart(path);
	}

	@Test
	public void soapMtom() {
		String path = "soap-mtom";
		testQuickstart(path);
	}

	@Test
	public void transformJaxb() {
		String path = "transform-jaxb";
		testQuickstart(path);
	}

	@Test
	public void transformJson() {
		String path = "transform-json";
		testQuickstart(path);
	}

	@Test
	public void transformSmooks() {
		String path = "transform-smooks";
		testQuickstart(path);
	}

	@Test
	public void transformXslt() {
		String path = "transform-xslt";
		testQuickstart(path);
	}

	@Test
	public void validateXml() {
		String path = "validate-xml";
		testQuickstart(path);
	}

	// TODO: Don't forget on demos

	private static void testQuickstart(String path) {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		List<Project> projects = packageExplorer.getProjects();
		for (Project project : projects) {
			project.delete(false);
		}

		ImportMavenProject wizard = new ImportMavenProject();
		wizard.open();
		try {
			wizard.setRootDirectory(QUICKSTART_DIR + "/" + path);
		} catch (WaitTimeoutExpiredException wtee) {
			wizard.cancel();
			fail("Couldn't find a maven project at '" + path + "'");
		}
		wizard.finish();

		String msg = "Project at '" + path + "' contains error(s)";
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<TreeItem> errors = problemsView.getAllErrors();
		assertTrue(msg, errors.isEmpty());
	}

	public static class ImportMavenProject extends ImportWizardDialog {

		public ImportMavenProject() {
			super("Maven", "Existing Maven Projects");
		}

		@Override
		public void open() {
			super.open();
			new WaitUntil(new ShellWithTextIsActive("Import Maven Projects"));
		}

		public void setRootDirectory(String path) {
			new DefaultCombo("Root Directory:").setText(path);
			new PushButton("Refresh").click();
			new WaitUntil(new WaitCondition() {

				private Tree tree = new DefaultTree();

				@Override
				public boolean test() {
					return !tree.getItems().isEmpty();
				}

				@Override
				public String description() {
					return "Tree is empty";
				}
			}, TimePeriod.LONG);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
}

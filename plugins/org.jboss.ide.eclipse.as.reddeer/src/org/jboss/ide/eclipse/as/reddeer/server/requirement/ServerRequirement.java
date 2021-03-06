package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.log4j.Logger;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.FamilyAS;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.FamilyEAP;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.ServerFamily;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.DefineNewServerWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.JBossRuntimeWizardPage;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

/**
 * 
 * @author psrna
 * @author Radoslav Rabara
 *
 */

public class ServerRequirement implements Requirement<Server>, CustomConfiguration<ServerRequirementConfig> {

	private static final Logger LOGGER = Logger.getLogger(ServerRequirement.class);
	
	private static ConfiguredServerInfo currentState;
	
	private ServerRequirementConfig config;
	private Server server;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
		ServerReqState state() default ServerReqState.RUNNING;
	}
	
	
	@Override
	public boolean canFulfill() {
		//Server can be always added.
		//However, when something goes wrong then AssertionError is thrown
		return true;
	}

	@Override
	public void fulfill() {
		try {
			if(currentState == null) {
				//close welcome screen
				try {
					new WorkbenchView("Welcome").close();
				} catch(Exception e){
					//do nothing
				}

				throw new ServerIsNotSetException();
			} else {
				if(!config.equals(currentState.getConfig())) {
					//different config = different server
					removeLastRequiredServer();
					throw new ServerIsNotSetException();
				} else {
					//the same config = the same server
					setupServerState();
				} 
			}
		} catch(ServerIsNotSetException e) {
			LOGGER.info("Setup server");
			setupServerAdapter();
			currentState = new ConfiguredServerInfo(getServerNameLabelText(), config);
			if(server.state() == ServerReqState.RUNNING){
				startServer();
			}
		}
	}

	private void setupServerState() throws NoServerFoundException {
		LOGGER.info("Checking the state of the server '"+currentState.getServerName()+"'");
		
		org.jboss.reddeer.eclipse.wst.server.ui.view.Server serverInView = getConfiguredServer();
		
		ServerState state = serverInView.getLabel().getState();
		ServerReqState requiredState = server.state();
		switch(state) {
			case STARTED:
				if(requiredState == ServerReqState.STOPPED)
					serverInView.stop();
				break;
			case STOPPED:
				if(requiredState == ServerReqState.RUNNING)
					serverInView.start();
				break;
			default:
				new AssertionError("It was expected to have server in "
					+ServerState.STARTED+" or "+ServerState.STOPPED+"state."
							+ " Not in state "+state+".");
		}
	}
	
	private void removeLastRequiredServer() throws NoServerFoundException {		
		org.jboss.reddeer.eclipse.wst.server.ui.view.Server serverInView = getConfiguredServer();
		//if server is not stopped than stop the server
		if(serverInView.getLabel().getState() == ServerState.STARTED) {
			serverInView.stop();
		}
		//remove server added by last requirement
		serverInView.delete();
		//current state = there is no server defined
		currentState = null;
	}
	
	private org.jboss.reddeer.eclipse.wst.server.ui.view.Server getConfiguredServer()
				throws NoServerFoundException {
		ServersView serversView = new ServersView();
		final String serverName = currentState.getServerName();
		try {
			return serversView.getServer(serverName);
		} catch(EclipseLayerException e) {
			//server had been removed
			LOGGER.warn("Server had been removed "+serverName+".");
			throw new NoServerFoundException();
		}
	}
	
	@Override
	public void setDeclaration(Server server) {
		this.server = server;
	}
	

	@Override
	public Class getConfigurationClass() {
		return ServerRequirementConfig.class;
	}

	@Override
	public void setConfiguration(ServerRequirementConfig config) {
		this.config = config;
	}

	public ServerRequirementConfig getConfig() {
		return this.config;
	}
	
	public String getServerTypeLabelText() {
		ServerFamily server = config.getServerFamily();
		if (server instanceof FamilyAS) {
			if (server.getVersion().equals("8.0")) {
				return "WildFly 8.0 (Experimental)";
			}
		}
		if (server instanceof FamilyEAP) {
			if (server.getVersion().equals("6.1")
					|| server.getVersion().equals("6.2")) {
				return config.getServerFamily().getLabel() + " " + "6.1+";
			}
		}
		return config.getServerFamily().getLabel() + " "
				+ config.getServerFamily().getVersion();
	}
	
	public String getServerNameLabelText() {
		return getServerTypeLabelText() + " Server";
	}

	public String getRuntimeNameLabelText() {
		return getServerTypeLabelText() + " Runtime";
	}

	public void setupServerAdapter() {
		NewServerWizardDialog serverW = new NewServerWizardDialog();
		try {
			serverW.open();
			
			DefineNewServerWizardPage sp = new DefineNewServerWizardPage(serverW);
	
			sp.selectType(config.getServerFamily().getCategory(),
					getServerTypeLabelText());
			checkIfThereIsAnyOtherServerWithTheSameType();
			
			sp.setName(getServerNameLabelText());
			checkTheServerName();
			
			serverW.next();
			
			JBossRuntimeWizardPage rp = new JBossRuntimeWizardPage();
			
			rp.setRuntimeName(getRuntimeNameLabelText());
			checkTheServerName();
			
			rp.setRuntimeDir(config.getRuntime());
			checkTheHomeDirectory();
			
			checkOtherErrors();
			
			serverW.finish();
		} catch(AssertionError e) {
			serverW.cancel();
			throw e;
		}
	}
	
	private void checkIfThereIsAnyOtherServerWithTheSameType() {
		try {
			//combo box indicate other servers with the same type
			Combo combo = new DefaultCombo();
			throw new AssertionError("There is another server with the same type.\n"
					+ "Type: "+getServerTypeLabelText()+"\n"
					+ "Present server: "+combo.getText());
		} catch(SWTLayerException e) {
			//combo box is not present so there is not any other server with the same type
		}
	}

	private void checkTheServerName() {
		String text = new org.jboss.reddeer.swt.impl.text.DefaultText(3).getText();
		if(text.contains("The server name is already in use. Specify a different name.")) {
			throw new AssertionError("The server name '"+getServerNameLabelText()+"' is already in use.");
		}
		if(text.contains("The name field must not be blank")) {
			throw new AssertionError("The server name '"+getServerNameLabelText()+"' is empty.");
		}
	}
	
	private void checkTheHomeDirectory() {
		String text = new org.jboss.reddeer.swt.impl.text.DefaultText(3).getText();
		if(text.contains("The home directory does not exist or is not a directory.")) {
			throw new AssertionError("The home directory '"+config.getRuntime()+"'"
					+" does not exist or is not a directory.");
		}
		if(text.contains("The home directory is missing a required file or folder:")) {
			throw new AssertionError("The home directory '"+config.getRuntime()+"'"
					+" is missing a required file or folder:"+text.split(":")[1]);
		}
	}
	
	private void checkOtherErrors() {
		String text = new org.jboss.reddeer.swt.impl.text.DefaultText(3).getText();
		if(text.contains("No valid JREs found for execution environment")) {
			throw new AssertionError(text);
		}
	}
	
	public void startServer() {
		ServersView sw = new ServersView();
		sw.getServer(getServerNameLabelText()).start();
	}
	
	/**
	 * Internal exception indicates that server must be set
	 * 
	 * @author rrabara
	 */
	private class ServerIsNotSetException extends Exception {
		
	}
	
	/**
	 * Internal exception indicates aborting of procedure due to missing server
	 * 
	 * @author rrabara
	 * @see ServerIsNotSetException
	 */
	private class NoServerFoundException extends ServerIsNotSetException {
		
	}
}

package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.bpel.ui.bot.test.ExampleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ExampleTest.class
})
@RunWith(BPELSuite.class)
public class ExampleTests extends TestSuite {

}

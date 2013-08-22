package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * 
 * @author apodhrad
 * 
 */
public class TableIsNotEmpty implements WaitCondition {

	@Override
	public boolean test() {
		return new DefaultTable().rowCount() > 0;
	}

	@Override
	public String description() {
		return "Table is empty";
	}

}

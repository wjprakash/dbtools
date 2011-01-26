
package com.nayaware.dbtools.querybuilder.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class JoinDeleteCommand extends Command {

	private Join join;
	private QueryData queryData;

	public JoinDeleteCommand(QueryData queryData, Join join) {
		this.join = join;
		this.queryData = queryData;
	}

	@Override
	public void execute() {
		queryData.removeJoin(join);
	}
}

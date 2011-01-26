
package com.nayaware.dbtools.querybuilder.commands;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class JoinCreateCommand extends Command {
	private QbColumnNode source;
	private QbColumnNode target;
	private QueryData queryData;

	public JoinCreateCommand(QueryData queryData, QbColumnNode source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel(Messages.getString("JoinCreateCommand.0")); //$NON-NLS-1$
		this.source = source;
		this.queryData = queryData;
	}

	@Override
	public boolean canExecute() {
		// disallow source -> source connections
		if ((source == null) || (target == null) || source.equals(target)) {
			return false;
		}
		// return false, if the source -> target connection exists already
		for (Iterator<Join> iter = source.getSourceJoins().iterator(); iter
				.hasNext();) {
			Join conn = iter.next();
			if (conn.getTarget().equals(target)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void execute() {
		if (source.getColumnNode().getColumn().getType() != target
				.getColumnNode().getColumn().getType()) {
			ErrorManager
					.showError(Messages.getString("JoinCreateCommand.1")); //$NON-NLS-1$
			return;

		} else {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			JoinTypeSelectionDialog joinTypeSelectionDialog = new JoinTypeSelectionDialog(
					shell);
			int ret = joinTypeSelectionDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				int joinType = joinTypeSelectionDialog.getJoinType();
				queryData.addJoin(source, target, joinType);
			}
		}
	}

	@Override
	public void redo() {
		// connection.reconnect();
	}

	/**
	 * Set the target end point for the connection.
	 */
	public void setTarget(QbColumnNode target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	@Override
	public void undo() {
		// connection.disconnect();
	}
}

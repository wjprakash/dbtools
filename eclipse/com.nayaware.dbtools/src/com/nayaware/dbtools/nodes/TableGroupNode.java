
package com.nayaware.dbtools.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.actions.OpenTableDesignerAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.actions.ViewSchemaDiagramAction;
import com.nayaware.dbtools.api.IAbstractDatabaseObject;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Table Group Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class TableGroupNode extends AbstractNode {

	public static final String TABLE_GROUP_REFRESH_JOB = Messages.getString("TableGroupNode.0"); //$NON-NLS-1$

	private WaitingNode waitingNode = new WaitingNode();

	public TableGroupNode(IAbstractDatabaseObject databaseObject) {
		super(databaseObject);
		setDisplayName(Messages.getString("TableGroupNode.1")); //$NON-NLS-1$
		setImageKey(ImageUtils.TABLE_GROUP);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected synchronized void initializeChildren() {
		if (initializing) {
			return;
		}
		initializing = true;
		super.initializeChildren();
		Job dbJob = new Job(TABLE_GROUP_REFRESH_JOB) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					List<ITable> tableList = null;
					if (getDatbaseObject() instanceof ISchema) {
						ISchema schema = (ISchema) getDatbaseObject();
						tableList = schema.getTableList();
					} else if (getDatbaseObject() instanceof IConnection) {
						IConnection database = (IConnection) getDatbaseObject();
						tableList = database.getTableList();
					}

					if (tableList != null) {
						List<AbstractNode> children = new ArrayList<AbstractNode>();
						for (ITable table : tableList) {
							AbstractNode child = new TableNode(table);
							child.setParent(TableGroupNode.this);
							children.add(child);
						}
						setChildren(children);
					}
				} catch (final Exception exc) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ErrorManager.showException(exc);
						}
					});
				} finally {
					initializing = false;
					removeChild(waitingNode);
				}
				return Status.OK_STATUS;
			}
		};
		dbJob.setPriority(Job.SHORT);
		dbJob.setSystem(true);
		dbJob.schedule(refreshDelay);
		addChild(waitingNode);
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this),
				null, // Separator
				new OpenTableDesignerAction(this),
				null, // Separator
				new ViewSchemaDiagramAction(this),
				new OpenSqlEditorAction(this), new OpenQueryBuilderAction(this) };
	}

	public synchronized List<AbstractNode> getTableNodeList() {
		return getTableNodeList(false);
	}

	public synchronized List<AbstractNode> getTableNodeList(boolean refresh) {
		if (refresh) {
			initializeChildren();
		}
		return children;
	}
}

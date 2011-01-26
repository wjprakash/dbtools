
package com.nayaware.dbtools.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.actions.CopyTableAction;
import com.nayaware.dbtools.actions.DropTableAction;
import com.nayaware.dbtools.actions.ExportCsvAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.actions.TruncateTableAction;
import com.nayaware.dbtools.actions.ViewTableDataAction;
import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Table Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNode extends AbstractNode {

	private WaitingNode waitingNode = new WaitingNode();

	public final static String COLUMN_ADDED = "columnAdded"; //$NON-NLS-1$
	public final static String COLUMN_REMOVED = "columnRemoved"; //$NON-NLS-1$

	public static final String TABLE_REFRESH_JOB = Messages.getString("TableNode.2"); //$NON-NLS-1$

	public TableNode(ITable table) {
		super(table);
		setImageKey(ImageUtils.TABLE);
	}

	public String getQualifiedName() {
		return getTable().getQualifiedName();
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

		Job dbJob = new Job(TABLE_REFRESH_JOB) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					if (getDatbaseObject() instanceof ITable) {
						ITable table = (ITable) getDatbaseObject();
						List<IColumn> columnList = table.getColumnList(true);
						List<AbstractNode> children = new ArrayList<AbstractNode>();
						for (IColumn column : columnList) {
							AbstractNode child = new ColumnNode(column);
							child.setParent(TableNode.this);
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
				new ViewTableDataAction(this), new ExportCsvAction(this),
				null, // Separator
				new TruncateTableAction(this), new CopyTableAction(this),
				new DropTableAction(this), };
	}

	public synchronized List<AbstractNode> getColumnNodeList() {
		return children;
	}

	public synchronized List<AbstractNode> getColumnNodeList(boolean refresh) {
		if (refresh && (children == null)) {
			initializeChildren();
		}
		return children;
	}

	public void addColumnNode(ColumnNode columnNode) {
		IColumn column = columnNode.getColumn();
		column.setTable(getTable());
		column.setDatabaseInfo(getTable().getDatabaseInfo());
		getTable().addColumn(column);
		columnNode.setParent(this);
		addChild(columnNode);
		firePropertyChange(COLUMN_ADDED, null, columnNode);
	}

	public ITable getTable() {
		return (ITable) getDatbaseObject();
	}

	public ColumnNode findColumnNodeByName(String columnName) {
		if (children != null)
			for (AbstractNode node : children) {
				if (node instanceof ColumnNode) {
					if (columnName.equals(node.getName())) {
						return (ColumnNode) node;
					}
				}
			}
		return null;
	}

	@Override
	public void handleDoubleClick(TreeViewer viewer) {
		new ViewTableDataAction(this).run();
	}
}

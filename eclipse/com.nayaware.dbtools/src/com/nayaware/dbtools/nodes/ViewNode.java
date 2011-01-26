
package com.nayaware.dbtools.nodes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IView;
import com.nayaware.dbtools.model.View;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The View Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ViewNode extends AbstractNode {

	private WaitingNode waitingNode = new WaitingNode();

	public ViewNode(IView view) {
		super(view);
		setImageKey(ImageUtils.VIEW);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();

		Job dbJob = new Job("viewColumnFetchJob") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {

				try {
					if (getDatbaseObject() instanceof IView) {
						IView view = (View) getDatbaseObject();
						List<IColumn> columnList = view.getColumnList(true);
						List<AbstractNode> children = new ArrayList<AbstractNode>();
						for (IColumn column : columnList) {
							AbstractNode child = new ColumnNode(column);
							child.setParent(ViewNode.this);
							children.add(child);
						}
						setChildren(children);
					}
				} catch (final SQLException exc) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ErrorManager.showException(exc);
						}
					});
					removeChild(waitingNode);
				}
				return Status.OK_STATUS;
			}
		};
		dbJob.schedule(refreshDelay);
		addChild(waitingNode);
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this) };
	}
}

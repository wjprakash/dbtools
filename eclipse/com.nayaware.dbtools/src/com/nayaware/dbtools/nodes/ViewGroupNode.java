
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

import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IAbstractDatabaseObject;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.IView;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The View Group Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ViewGroupNode extends AbstractNode {

	private WaitingNode waitingNode = new WaitingNode();

	public ViewGroupNode(IAbstractDatabaseObject databaseObject) {
		super(databaseObject);
		setDisplayName(Messages.getString("ViewGroupNode.0")); //$NON-NLS-1$
		setImageKey(ImageUtils.VIEW_GROUP);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		Job dbJob = new Job("tableFetchJob") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					List<IView> viewList = null;
					if (getDatbaseObject() instanceof ISchema) {
						ISchema schema = (ISchema) getDatbaseObject();
						viewList = schema.getViewList();
					} else if (getDatbaseObject() instanceof IConnection) {
						IConnection database = (IConnection) getDatbaseObject();
						viewList = database.getViewList();
					}
					if (viewList != null) {
						List<AbstractNode> children = new ArrayList<AbstractNode>();
						for (IView view : viewList) {
							AbstractNode child = new ViewNode(view);
							child.setParent(ViewGroupNode.this);
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
		return new Action[] { new RefreshAction(this), null, // Separator
				new OpenSqlEditorAction(this), new OpenQueryBuilderAction(this) };
	}

	public List<AbstractNode> getViewNodeList() {
		return getViewNodeList(false);
	}

	public synchronized List<AbstractNode> getViewNodeList(boolean refresh) {
		if (refresh) {
			initializeChildren();
		}
		return children;
	}
}

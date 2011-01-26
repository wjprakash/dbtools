
package com.nayaware.dbtools.nodes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

import com.nayaware.dbtools.api.IAbstractDatabaseObject;
import com.nayaware.dbtools.model.AbstractDatabaseObject;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Abstract Node for the Connection Explorer View
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class AbstractNode implements IAdaptable {
	private String displayName;
	protected AbstractNode parent;
	private String imageKey;
	protected List<AbstractNode> children;
	private IAbstractDatabaseObject datbaseObject;

	public final static String NODE_MODIFIED = Messages.getString("AbstractNode.0"); //$NON-NLS-1$
	public final static String NODE_CHILDREN_MODIFIED = Messages.getString("AbstractNode.1"); //$NON-NLS-1$
	public static final String NODE_NAME_MODIFIED = Messages.getString("AbstractNode.2"); //$NON-NLS-1$
	public static final String EXPAND_TO = Messages.getString("AbstractNode.3"); //$NON-NLS-1$

	protected int refreshDelay = 0;
	protected boolean initializing = false;

	// Location and dimension of the node in the schema designer
	private Point location;
	private Dimension dimension;

	protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	private boolean leafNode = false;

	public AbstractNode(IAbstractDatabaseObject obj, boolean leafNode) {
		datbaseObject = obj;
		if (getDisplayName() == null) {
			setDisplayName(obj.getName());
		}
		this.leafNode = leafNode;
	}

	public AbstractNode(IAbstractDatabaseObject obj) {
		this(obj, false);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		List<PropertyChangeListener> listeners = Arrays
				.asList(propertyChangeSupport.getPropertyChangeListeners());
		if (!listeners.contains(listener)) {
			propertyChangeSupport.addPropertyChangeListener(listener);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void firePropertyChange(String prop, Object old, Object newValue) {
		if (propertyChangeSupport.hasListeners(prop)) {
			propertyChangeSupport.firePropertyChange(prop, old, newValue);
		}
	}

	public IAbstractDatabaseObject getDatbaseObject() {
		return datbaseObject;
	}

	public void setDatbaseObject(AbstractDatabaseObject datbaseObject) {
		this.datbaseObject = datbaseObject;
	}

	public void setName(String name) {
		String prevName = datbaseObject.getName();
		datbaseObject.setName(name);
		if (prevName.equals(getDisplayName())) {
			setDisplayName(name);
		}
		propertyChangeSupport
				.firePropertyChange(NODE_NAME_MODIFIED, null, this);
	}

	public String getName() {
		return datbaseObject.getName();
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		propertyChangeSupport
				.firePropertyChange(NODE_NAME_MODIFIED, null, this);
	}

	public String getDisplayName() {
		return displayName;
	}

	public synchronized void addChild(AbstractNode child) {
		addChild(child, false);
	}

	public synchronized void addChild(AbstractNode child, boolean expandTo) {
		if (children == null) {
			children = new ArrayList<AbstractNode>();
		}
		children.add(child);
		child.setParent(this);
		if (!(child instanceof WaitingNode)) {
			if (expandTo) {
				propertyChangeSupport.firePropertyChange(EXPAND_TO, null, this);
			} else {
				propertyChangeSupport.firePropertyChange(
						NODE_CHILDREN_MODIFIED, null, this);
			}
		}
	}

	public synchronized void removeChild(AbstractNode child) {
		if (children != null) {
			children.remove(child);
			child.setParent(null);
			if (!(child instanceof WaitingNode)) {
				if (children.isEmpty()) {
					if (getParent() != null) {
						getParent().refresh();
					}
				}
			}
			propertyChangeSupport.firePropertyChange(NODE_CHILDREN_MODIFIED,
					null, child);
			child = null;
		}
	}

	public synchronized AbstractNode[] getChildren() {
		if (children == null) {
			initializeChildren();
		}
		return children.toArray(new AbstractNode[children
				.size()]);
	}

	public void setChildren(List<AbstractNode> children) {
		this.children = children;
		propertyChangeSupport.firePropertyChange(NODE_CHILDREN_MODIFIED, null,
				this);
	}

	public synchronized boolean hasChildren() {
		if (children != null) {
			return children.size() > 0;
		} else if (leafNode) {
			return false;
		}
		return true;
	}

	public void setParent(AbstractNode parent) {
		this.parent = parent;
	}

	public AbstractNode getParent() {
		return parent;
	}

	public Image getIcon() {
		return ImageUtils.getIcon(imageKey);
	}

	/**
	 * @param imageKey
	 *            the imageKey to set
	 */
	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
		propertyChangeSupport.firePropertyChange(NODE_MODIFIED, null, this);
	}

	/**
	 * @return the imageKey
	 */
	public String getImageKey() {
		return imageKey;
	}

	public void fillContextMenu(IMenuManager manager) {
		Action[] contextActions = getActions();
		for (Action action : contextActions) {
			if (action == null) {
				manager.add(new Separator());
			} else {
				manager.add(action);
			}
		}
	}

	/**
	 * get the list of actions applicable to this node
	 */
	public Action[] getActions() {
		return new Action[0];
	}

	/**
	 * Do lazy initialization of children
	 */
	protected void initializeChildren() {
		children = new ArrayList<AbstractNode>();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public void refresh() {
		try {
			datbaseObject.refresh();
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
			return;
		}
		initializeChildren();
		propertyChangeSupport.firePropertyChange(NODE_CHILDREN_MODIFIED, null,
				this);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Dimension getSize() {
		return dimension;
	}

	public void setSize(Dimension dimension) {
		this.dimension = dimension;
	}

	public void handleDoubleClick(TreeViewer viewer) {
		if (!datbaseObject.getDatabaseInfo().isConnected()) {
			refresh();
		}
		viewer.expandToLevel(this, 1);
	}

	public void expandViewTo() {
		propertyChangeSupport.firePropertyChange(EXPAND_TO, null, this);
	}

	/**
	 * Delay the refreshing of the children by certain amount of time
	 * 
	 * @param delay
	 *            (in milliseconds)
	 */
	public void setRefreshDelay(int delay) {
		refreshDelay = delay;
	}

	public void delete() {
		if (getDatbaseObject().delete()) {
			getParent().removeChild(this);
		}
	}
}
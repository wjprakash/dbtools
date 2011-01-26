
package com.nayaware.dbtools.editors.sql;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Input for the SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorInput extends PlatformObject implements IEditorInput {

	private String script = ""; //$NON-NLS-1$

	private IDatabaseInfo databaseInfo;
	private AbstractNode node;

	public SqlEditorInput(IDatabaseInfo dbInfo) {
		this(dbInfo, null, ""); //$NON-NLS-1$
	}

	public SqlEditorInput(IDatabaseInfo dbInfo, String script) {
		this(dbInfo, null, script); //$NON-NLS-1$
	}

	public SqlEditorInput(IDatabaseInfo dbInfo, AbstractNode node, String script) {
		this.script = script;
		databaseInfo = dbInfo;
		this.node = node;
	}

	public String getScript() {
		return script;
	}

	public AbstractNode getNode() {
		return node;
	}

	public void setNode(AbstractNode node) {
		this.node = node;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return Messages.getString("SqlEditorInput.3") + databaseInfo.getName() + Messages.getString("SqlEditorInput.4"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return Messages.getString("SqlEditorInput.5") + databaseInfo.getName() + Messages.getString("SqlEditorInput.6") //$NON-NLS-1$ //$NON-NLS-2$
				+ databaseInfo.getConnectionConfig().getUrl() + Messages.getString("SqlEditorInput.7"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(String.class)) {
			return script;
		}
		return null;
	}
}


package com.nayaware.dbtools.nodes;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IScript;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Script Group Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ScriptGroupNode extends AbstractNode {

	public ScriptGroupNode(IConnection connection) {
		super(connection);
		setDisplayName(Messages.getString("ScriptGroupNode.0")); //$NON-NLS-1$
		setImageKey(ImageUtils.SCRIPTS_FOLDER);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		IConnection connection = (IConnection) getDatbaseObject();
		List<IScript> scriptList = connection.getScriptList();
		for (IScript script : scriptList) {
			addChild(new ScriptNode(script));
		}
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this), null, // Separator
				new OpenSqlEditorAction(this) };
	}

	public void addScriptNode(ScriptNode scriptNode) {
		IConnection connection = (IConnection) getDatbaseObject();
		connection.addScript(scriptNode.getScript());
		addChild(scriptNode, true);
	}
}

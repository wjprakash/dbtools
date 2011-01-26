
package com.nayaware.dbtools.nodes;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.nayaware.dbtools.actions.DeleteAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.api.IScript;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Node wrapper for the Script model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ScriptNode extends AbstractNode {

	public ScriptNode(IScript script) {
		super(script, true);
		setImageKey(ImageUtils.SCRIPT);
	}

	public IScript getScript() {
		return (IScript) getDatbaseObject();
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new OpenSqlEditorAction(this), null,
				new DeleteAction(this) };
	}

	@Override
	public void handleDoubleClick(TreeViewer viewer) {
		new OpenSqlEditorAction(this).run();
	}
}

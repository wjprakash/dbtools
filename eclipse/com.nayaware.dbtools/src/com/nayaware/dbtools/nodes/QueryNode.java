
package com.nayaware.dbtools.nodes;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.nayaware.dbtools.actions.DeleteAction;
import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.api.IQuery;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Node wrapper for the Script model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryNode extends AbstractNode {

	public QueryNode(IQuery query) {
		super(query, true);
		setImageKey(ImageUtils.QUERY);
	}

	public IQuery getQuery() {
		return (IQuery) getDatbaseObject();
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new OpenQueryBuilderAction(this), null,
				new DeleteAction(this) };
	}

	@Override
	public void handleDoubleClick(TreeViewer viewer) {
		new OpenQueryBuilderAction(this).run();
	}
}

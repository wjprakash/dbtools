
package com.nayaware.dbtools.nodes;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IQuery;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Queries Group Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class QueryGroupNode extends AbstractNode {

	public QueryGroupNode(IConnection connection) {
		super(connection);
		setDisplayName(Messages.getString("QueryGroupNode.0")); //$NON-NLS-1$
		setImageKey(ImageUtils.QUERIES_FOLDER);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		IConnection connection = (IConnection) getDatbaseObject();
		List<IQuery> queryList = connection.getQueryList();
		for (IQuery query : queryList) {
			addChild(new QueryNode(query));
		}
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this), null, // Separator
				new OpenQueryBuilderAction(this) };
	}

	public void addQueryNode(QueryNode queryNode) {
		IConnection connection = (IConnection) getDatbaseObject();
		connection.addQuery(queryNode.getQuery());
		addChild(queryNode, true);
	}
}


package com.nayaware.dbtools.querybuilder.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.nayaware.dbtools.nodes.WaitingNode;
import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Schema diagram viewer edit part factory
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderEditPartFactory implements EditPartFactory {

	private QueryData queryData;

	public QueryBuilderEditPartFactory(QueryData queryData) {
		this.queryData = queryData;
	}

	public EditPart createEditPart(EditPart parentPart, Object model) {
		EditPart editPart = null;
		if (model instanceof QueryData) {
			editPart = new QueryBuilderEditPart((QueryData) model);
		}
		if (model instanceof QbTableNode) {
			editPart = new QbTableNodeEditPart((QbTableNode) model, queryData);
		}
		if (model instanceof QbColumnNode) {
			editPart = new QbColumnNodeEditPart((QbColumnNode) model, queryData);
		}
		if (model instanceof WaitingNode) {
			editPart = new QbWaitingNodeEditPart((WaitingNode) model);
		}
		if (model instanceof Join) {
			editPart = new QbJoinEditPart((Join) model, queryData);
		}
		if (model instanceof WaitingNode) {
			editPart = new QbBlankPageEditPart((WaitingNode) model);
		}
		if (editPart != null) {
			return editPart;
		} else {
			throw new IllegalArgumentException(model.toString());
		}
	}
}

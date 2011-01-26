
package com.nayaware.dbtools.nodes;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Column Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ColumnNode extends AbstractNode {

	public static final String COLUMN_MODIFIED = "columnModified"; //$NON-NLS-1$

	public ColumnNode(IColumn column) {
		super(column, true);
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this) };
	}

	@Override
	public Image getIcon() {
		IColumn column = getColumn();
		if (column.isPrimaryKey()) {
			setImageKey(ImageUtils.PRIMARY_KEY_COLUM);
		} else if (column.isForeignKey()) {
			setImageKey(ImageUtils.FOREIGN_KEY_COLUM);
		} else if (column.isIndexKey()) {
			setImageKey(ImageUtils.INDEX_COLUM);
		} else if (!column.isNullAllowed()) {
			setImageKey(ImageUtils.NOT_NULL_COLUM);
		} else {
			setImageKey(ImageUtils.COLUMN);
		}
		return super.getIcon();
	}

	public IColumn getColumn() {
		return (IColumn) getDatbaseObject();
	}

	public String getQualifiedName() {
		return getColumn().getQualifiedName();
	}

	public void fireColumnModifiedEvent() {
		firePropertyChange(COLUMN_MODIFIED, null, this);
	}
}


package com.nayaware.dbtools.schemadesigner.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeDirectEditManager extends DirectEditManager {

	private ColumnNodeEditPart columnNodeEditPart;

	public ColumnNodeDirectEditManager(ColumnNodeEditPart columnNodeEditPart) {
		super(columnNodeEditPart, null, new ColumnNodeCellEditorLocator(
				columnNodeEditPart));
		this.columnNodeEditPart = columnNodeEditPart;
	}

	@Override
	protected void initCellEditor() {
		getCellEditor().setValue(
				((SdColumnNode) columnNodeEditPart.getModel()).getName());
		Text text = (Text) getCellEditor().getControl();
		text.selectAll();
	}

	@Override
	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.None);
	}

	private static class ColumnNodeCellEditorLocator implements
			CellEditorLocator {
		ColumnNodeEditPart columnNodeEditPart;

		public ColumnNodeCellEditorLocator(ColumnNodeEditPart tableNodeEditPart) {
			this.columnNodeEditPart = tableNodeEditPart;
		}

		public void relocate(CellEditor celleditor) {
			IFigure figure = columnNodeEditPart.getFigure();
			Text text = (Text) celleditor.getControl();
			Rectangle rect = figure.getBounds().getCopy();
			figure.translateToAbsolute(rect);
			text.setBounds(rect.x + 5, rect.y + 5, rect.width - 10, 20);
		}
	}

}

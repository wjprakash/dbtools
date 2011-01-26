
package com.nayaware.dbtools.schemadesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Details panel that show up at the bottom of the designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DetailPanel {

	private TableColumnDetailPanel tableColumnDetailPanel;
	private TableDetailsPanel tableDetailsPanel;
	private SchemaDetailsPanel schemaDetailsPanel;

	private Composite detailsPanel;

	private StackLayout stackLayout;

	private Composite parent;
	private Schemata schemata;

	public DetailPanel(Composite parent, Schemata schemata) {
		this.parent = parent;
		this.schemata = schemata;
	}

	private void createPanel() {
		if (detailsPanel == null) {
			detailsPanel = new Composite(parent, SWT.FILL);
			stackLayout = new StackLayout();
			detailsPanel.setLayout(stackLayout);

			tableColumnDetailPanel = new TableColumnDetailPanel(detailsPanel, schemata);
			tableDetailsPanel = new TableDetailsPanel(detailsPanel, schemata);
			schemaDetailsPanel = new SchemaDetailsPanel(detailsPanel, schemata);
			stackLayout.topControl = schemaDetailsPanel;
			detailsPanel.setVisible(true);
			detailsPanel.layout(true);
			if (parent instanceof SashForm) {
				((SashForm) parent).setWeights(new int[] { 4, 1 });
			}
			parent.layout(true);
		}
	}

	public synchronized void setColumnNode(SdColumnNode sdColumnNode) {
		createPanel();
		tableColumnDetailPanel.setColumnNode(sdColumnNode);
		stackLayout.topControl = tableColumnDetailPanel;
		detailsPanel.layout(true);
	}

	public synchronized void setTableNode(SdTableNode sdTableNode) {
		createPanel();
		tableDetailsPanel.setTableNode(sdTableNode);
		stackLayout.topControl = tableDetailsPanel;
		detailsPanel.layout(true);
	}

	public synchronized void setSchemata(Schemata schemata) {
		createPanel();
		schemaDetailsPanel.setSchemata(schemata);
		stackLayout.topControl = schemaDetailsPanel;
		detailsPanel.layout(true);
	}
}


package com.nayaware.dbtools.schemadesigner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Panel that displays the details of Table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableDetailsPanel extends Composite implements
		PropertyChangeListener {

	private Text tableNameText;
	private SdTableNode currentTableNode;

	private Schemata schemata;
	
	public TableDetailsPanel(Composite parent, final Schemata schemata) {
		super(parent, SWT.BORDER);
		this.schemata = schemata;
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		final Label tableNameLabel = new Label(this, SWT.NONE);
		final GridData gd_tableNameLabel = new GridData();
		tableNameLabel.setLayoutData(gd_tableNameLabel);
		tableNameLabel.setText(Messages.getString("TableDetailsPanel.0")); //$NON-NLS-1$

		tableNameText = new Text(this, SWT.BORDER);
		final GridData gd_tableNameText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		tableNameText.setLayoutData(gd_tableNameText);
		tableNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				currentTableNode.setName(tableNameText.getText().trim());
				schemata.setDirty(true);
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setTableNode(SdTableNode sdTableNode) {
		if (currentTableNode != null) {
			currentTableNode.removePropertyChangeListener(this);
		}
		currentTableNode = sdTableNode;
		currentTableNode.addPropertyChangeListener(this);
		tableNameText.setText(currentTableNode.getName());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (AbstractNode.NODE_NAME_MODIFIED.equals(evt.getPropertyName())) {
			tableNameText.setText(currentTableNode.getName());
		}
	}

}

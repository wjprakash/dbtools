
package com.nayaware.dbtools.schemadesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * Panel that displasy the detailsof schemata
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDetailsPanel extends Composite {

	private Text dbNameText;
	private Schemata schemata;

	public Schemata getSchemata() {
		return schemata;
	}

	public SchemaDetailsPanel(Composite parent, final Schemata schemata) {
		super(parent, SWT.BORDER);
		
		this.schemata = schemata;
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		final Label databaseNameLabel = new Label(this, SWT.NONE);
		databaseNameLabel.setText(Messages.getString("SchemaDetailsPanel.0")); //$NON-NLS-1$

		dbNameText = new Text(this, SWT.BORDER);
		final GridData gd_dbNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		dbNameText.setLayoutData(gd_dbNameText);
		dbNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				schemata.setName(dbNameText.getText().trim());
				schemata.setDirty(true);
			}
		});
		//
	}

	public void setSchemata(Schemata schemata) {
		this.schemata = schemata;
		dbNameText.setText(schemata.getName());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

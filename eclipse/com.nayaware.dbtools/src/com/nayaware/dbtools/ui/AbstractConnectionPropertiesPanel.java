
package com.nayaware.dbtools.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.nayaware.dbtools.api.IConnectionConfig;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public abstract class AbstractConnectionPropertiesPanel extends Composite {

	protected IConnectionConfig currentConnectionConfig;

	public AbstractConnectionPropertiesPanel(Composite parent,
			IConnectionConfig connectionConfig) {
		super(parent, SWT.FILL);
		this.currentConnectionConfig = connectionConfig;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public abstract IConnectionConfig getConnectionConfig();

	public abstract void setConnectionConfig(IConnectionConfig connectionConfig);

}

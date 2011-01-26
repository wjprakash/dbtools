
package com.nayaware.dbtools.tests;

import java.sql.SQLException;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.core.ConnectionConfig;
import com.nayaware.dbtools.ui.CreateServerlessDatabaseDialog;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class EmbeddedDerbyCreationTest extends ApplicationWindow {

	/**
	 * Create the application window
	 */
	public EmbeddedDerbyCreationTest() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		final Button createDerbyDatabaseButton = new Button(container, SWT.NONE);
		createDerbyDatabaseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String name1 = "Local Derby";
				String username1 = "";
				String password1 = "";
				CreateServerlessDatabaseDialog derbyConnectionDialog = new CreateServerlessDatabaseDialog(
						getShell(), IConnectionType.DERBY_EMBEDDED);
				derbyConnectionDialog.open();
				String url1 = derbyConnectionDialog.getUrl();
				System.out.println(url1);
				IConnectionConfig conConfig = new ConnectionConfig(name1, url1,
						IConnectionType.DERBY_EMBEDDED, username1, password1);
				ConnectionManager connectionManager = ConnectionManager
						.getInstance();
				try {
					connectionManager.createConnection(conConfig);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		createDerbyDatabaseButton.setText("Create Derby Connection");
		createDerbyDatabaseButton.setBounds(50, 46, 186, 32);
		//
		return container;
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			EmbeddedDerbyCreationTest window = new EmbeddedDerbyCreationTest();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(300, 200);
	}

	private void createActions() {
	}

}

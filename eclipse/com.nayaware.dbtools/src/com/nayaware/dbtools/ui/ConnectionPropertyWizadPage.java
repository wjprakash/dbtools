
package com.nayaware.dbtools.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.util.ConnectionConfigTemplates;

/**
 * Wizard page to display the connection information
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionPropertyWizadPage extends WizardPage {

	private Text connectionNameText;
	private Combo driverSelectionCombo;
	private String connectionName = Messages
			.getString("ConnectionPropertyWizadPage.0"); //$NON-NLS-1$

	private static ConnectionConfigTemplates connectionConfigTemplates = new ConnectionConfigTemplates();

	private IConnectionConfig currentTemplateConnectionConfig;

	private AbstractConnectionPropertiesPanel connectionPropertiesPanel;
	private AbstractConnectionPropertiesPanel fileConnectionPropertiesPanel;

	private AbstractConnectionPropertiesPanel currentPropertiesPanel;

	private Composite panelComposite;
	private StackLayout stackLayout = new StackLayout();

	public ConnectionPropertyWizadPage(IConnectionConfig connectionConfig) {
		super("wizardPage"); //$NON-NLS-1$
		this.currentTemplateConnectionConfig = fillTemplateConnectionConfig(connectionConfig);
		initialize();
		connectionName = connectionConfig.getDisplayName();
	}

	public ConnectionPropertyWizadPage() {
		super("wizardPage"); //$NON-NLS-1$
		this.currentTemplateConnectionConfig = connectionConfigTemplates
				.getTemplate(0);
		initialize();
		connectionName = ConnectionManager.getInstance()
				.getUniqueConnectionName();
		currentTemplateConnectionConfig.setName(connectionName);
	}

	private void initialize() {
		setTitle(Messages.getString("ConnectionPropertyWizadPage.3")); //$NON-NLS-1$
		setDescription(Messages.getString("ConnectionPropertyWizadPage.4")); //$NON-NLS-1$
		setPageComplete(true);
	}

	private IConnectionConfig fillTemplateConnectionConfig(
			IConnectionConfig connectionConfig) {
		IConnectionConfig templateConnectionConfig = connectionConfigTemplates
				.findTemplateConnectionConfig(connectionConfig.getDbType());
		templateConnectionConfig.setName(connectionConfig.getName());
		templateConnectionConfig.setDatabase(connectionConfig.getDatabase());
		templateConnectionConfig.setHostname(connectionConfig.getHostname());
		templateConnectionConfig.setPassword(connectionConfig.getPassword());
		templateConnectionConfig.setPort(connectionConfig.getPort());
		templateConnectionConfig.setDatabaseFilePath(connectionConfig
				.getDatabaseFilePath());
		templateConnectionConfig.setUrl(connectionConfig.getUrl());
		return templateConnectionConfig;
	}

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		setControl(container);

		Composite top = new Composite(container, SWT.NULL);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 2;
		top.setLayout(gridLayout_2);
		top.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label connectionNameLabel = new Label(top, SWT.NONE);
		connectionNameLabel.setText(Messages
				.getString("ConnectionPropertyWizadPage.5")); //$NON-NLS-1$

		connectionNameText = new Text(top, SWT.BORDER);
		connectionNameText.setText(connectionName);
		final GridData gd_connectionNameText = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		connectionNameText.setLayoutData(gd_connectionNameText);

		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginRight = 10;
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 10;
		gridLayout.marginBottom = 5;
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);

		final Label driverLabel = new Label(group, SWT.NONE);
		driverLabel
				.setText(Messages.getString("ConnectionPropertyWizadPage.6")); //$NON-NLS-1$

		final Composite composite = new Composite(group, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.verticalSpacing = 0;
		gridLayout_1.marginHeight = 0;
		gridLayout_1.marginWidth = 0;
		gridLayout_1.horizontalSpacing = 0;
		gridLayout_1.numColumns = 2;
		composite.setLayout(gridLayout_1);

		driverSelectionCombo = new Combo(composite, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		driverSelectionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setConnectionConfig(connectionConfigTemplates
						.getTemplate(driverSelectionCombo.getSelectionIndex()));
			}
		});
		driverSelectionCombo.setItems(connectionConfigTemplates
				.getTemplateNames());
		driverSelectionCombo.select(connectionConfigTemplates.getTemplates()
				.indexOf(currentTemplateConnectionConfig));

		final GridData gd_driverSelectionCombo = new GridData(
				GridData.FILL_HORIZONTAL);
		driverSelectionCombo.setLayoutData(gd_driverSelectionCombo);

		// TODO: Add this after the dialog is implemented
		// final Button addDriverButton = new Button(composite, SWT.NONE);
		// addDriverButton.setText("Add");

		panelComposite = new Composite(group, SWT.NONE);
		panelComposite.setLayout(stackLayout);
		final GridData gd_panelComposite = new GridData(SWT.FILL, SWT.CENTER,
				true, false, 2, 1);
		panelComposite.setLayoutData(gd_panelComposite);

		fileConnectionPropertiesPanel = new FileConnectionProprtiesPanel(
				panelComposite, currentTemplateConnectionConfig);
		fileConnectionPropertiesPanel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		connectionPropertiesPanel = new ConnectionPropertiesPanel(
				panelComposite, currentTemplateConnectionConfig);
		connectionPropertiesPanel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		final Button testConnectionButton = new Button(group, SWT.NONE);
		testConnectionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				ConnectionManager connectionManager = ConnectionManager
						.getInstance();
				try {
					connectionManager.createConnection(currentPropertiesPanel
							.getConnectionConfig());
					setMessage(Messages
							.getString("ConnectionPropertyWizadPage.7")); //$NON-NLS-1$
					setErrorMessage(null);
				} catch (Exception exc) {
					// Connection failed
					// setMessage("Connection Failed!");
					setErrorMessage(exc.getLocalizedMessage());
				}
			}
		});
		testConnectionButton.setText(Messages
				.getString("ConnectionPropertyWizadPage.8")); //$NON-NLS-1$
		setPropertiesPanel();
	}

	private void setPropertiesPanel() {
		if (!currentTemplateConnectionConfig.getConnectionType()
				.isNetworkDatabase()) {
			currentPropertiesPanel = fileConnectionPropertiesPanel;
		} else {
			currentPropertiesPanel = connectionPropertiesPanel;
		}
		currentPropertiesPanel
				.setConnectionConfig(currentTemplateConnectionConfig);
		stackLayout.topControl = currentPropertiesPanel;
		panelComposite.layout(true);
	}

	public IConnectionConfig getConnectionConfig() {
		IConnectionConfig config = currentPropertiesPanel.getConnectionConfig();
		config.setDisplayName(connectionNameText.getText());
		return config;
	}

	public void setConnectionConfig(IConnectionConfig connectionConfig) {
		connectionName = connectionConfig.getName();
		this.currentTemplateConnectionConfig = connectionConfig;
		setPropertiesPanel();
	}
}


package com.nayaware.dbtools.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.editors.sql.SqlEditor;
import com.nayaware.dbtools.editors.sql.SqlEditorInput;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.Column;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.viewers.SqlSourceViewer;

/**
 * Dialog that helps to design a Connection Table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableDesignerDialog extends TitleAreaDialog {

	private SqlSourceViewer sqlScriptViewer;
	private Text tableNameText;
	private Table table;
	private com.nayaware.dbtools.model.Table dbTable;
	private String[] columnProperties = { "PK", "Name", "Data Type", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"NOT NULL", "AUTO INC", "Default Value", "Flags" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private String COLUMN_BASE_NAME = Messages
			.getString("TableDesignerDialog.7"); //$NON-NLS-1$

	private List<ISqlType> sqlTypes;
	private SqlExecutor sqlExecutor;

	private TabItem sqlTabItem;
	private TabFolder tabFolder;

	public TableDesignerDialog(Shell parentShell,
			com.nayaware.dbtools.model.Table table) {
		super(parentShell);
		dbTable = table;
		sqlExecutor = new SqlExecutor(dbTable.getDatabaseInfo(), ""); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		final Composite composite_1 = new Composite(container, SWT.NONE);
		final GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER,
				true, false);
		gd_composite_1.heightHint = 39;
		composite_1.setLayoutData(gd_composite_1);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		composite_1.setLayout(gridLayout);

		final Label tableLabel = new Label(composite_1, SWT.NONE);
		final GridData gd_tableLabel = new GridData(SWT.RIGHT, SWT.BOTTOM,
				false, false);
		gd_tableLabel.heightHint = 19;
		gd_tableLabel.widthHint = 41;
		tableLabel.setLayoutData(gd_tableLabel);
		tableLabel.setText(Messages.getString("TableDesignerDialog.9")); //$NON-NLS-1$

		tableNameText = new Text(composite_1, SWT.BORDER);
		tableNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR) {
					dbTable.setName(tableNameText.getText());
				}
			}
		});
		tableNameText.setText(dbTable.getName());
		tableNameText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				dbTable.setName(tableNameText.getText());
			}
		});
		final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text.widthHint = 267;
		tableNameText.setLayoutData(gd_text);

		tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				updateScript();
			}
		});
		final GridData gd_tabFolder = new GridData(SWT.CENTER, SWT.CENTER,
				false, false);
		gd_tabFolder.heightHint = 425;
		gd_tabFolder.widthHint = 671;
		tabFolder.setLayoutData(gd_tabFolder);

		final TabItem designTabItem = new TabItem(tabFolder, SWT.NONE);
		designTabItem.setText(Messages.getString("TableDesignerDialog.10")); //$NON-NLS-1$

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		designTabItem.setControl(composite);

		table = new Table(composite, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		createTableColumns(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.getHorizontalBar().setVisible(false);

		final TableViewer tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnProperties);

		try {
			createCellEditors(tableViewer, table);
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
		tableViewer.setCellModifier(new CellModifier(tableViewer));
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(dbTable);

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				Column newColumn = new Column(dbTable.getDatabaseInfo(),
						dbTable, getUniqueColumnName());
				dbTable.addColumn(newColumn);
				updateScript();
				tableViewer.add(newColumn);
				tableViewer.reveal(newColumn);
				tableViewer.setSelection(new StructuredSelection(newColumn),
						true);
			}
		});

		final ToolBar toolBar = new ToolBar(composite, SWT.NONE);

		final ToolItem addColumn = new ToolItem(toolBar, SWT.PUSH);
		addColumn.setImage(ImageUtils.getIcon(ImageUtils.ADD));
		addColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Column newColumn = new Column(dbTable.getDatabaseInfo(),
						dbTable, getUniqueColumnName());
				dbTable.addColumn(newColumn);
				updateScript();
				tableViewer.add(newColumn);
				tableViewer.reveal(newColumn);
				tableViewer.setSelection(new StructuredSelection(newColumn),
						true);
			}
		});
		addColumn.setToolTipText(Messages.getString("TableDesignerDialog.11")); //$NON-NLS-1$
		addColumn.setSelection(true);

		final ToolItem deleteSelections = new ToolItem(toolBar, SWT.PUSH);
		deleteSelections.setImage(ImageUtils.getIcon(ImageUtils.DELETE));
		deleteSelections.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IColumn oldColumn = (IColumn) ((IStructuredSelection) tableViewer
						.getSelection()).getFirstElement();
				IColumn sibling = dbTable.removeColumn(oldColumn);
				updateScript();
				tableViewer.remove(oldColumn);
				if (sibling != null) {
					tableViewer.reveal(sibling);
					tableViewer.setSelection(new StructuredSelection(sibling),
							true);
				}
			}
		});
		deleteSelections.setToolTipText(Messages
				.getString("TableDesignerDialog.12")); //$NON-NLS-1$

		sqlTabItem = new TabItem(tabFolder, SWT.NONE);
		sqlTabItem.setText(Messages.getString("TableDesignerDialog.13")); //$NON-NLS-1$

		Composite sourceViewerComposite = new Composite(tabFolder, SWT.BORDER);
		final GridLayout sourceViewerCompositeGridLayout = new GridLayout();
		sourceViewerCompositeGridLayout.verticalSpacing = 0;
		sourceViewerCompositeGridLayout.marginWidth = 0;
		sourceViewerCompositeGridLayout.marginHeight = 0;
		sourceViewerCompositeGridLayout.horizontalSpacing = 0;
		sourceViewerComposite.setLayout(sourceViewerCompositeGridLayout);
		sourceViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		sqlTabItem.setControl(sourceViewerComposite);

		// sqlScriptViewer = new SqlSourceViewer(sourceSashForm);
		sqlScriptViewer = new SqlSourceViewer(sourceViewerComposite);
		sqlScriptViewer.setEditable(false);

		sqlScriptViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		sqlScriptViewer.getTextWidget().setBackground(
				sourceViewerComposite.getBackground());

		Button editUsingSqlButton = new Button(sourceViewerComposite, SWT.NONE);
		editUsingSqlButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false));
		editUsingSqlButton.setText(Messages.getString("TableDesignerDialog.0")); //$NON-NLS-1$
		editUsingSqlButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String title = Messages.getString("TableDesignerDialog.1"); //$NON-NLS-1$
				String message = Messages.getString("TableDesignerDialog.2"); //$NON-NLS-1$
				Shell shell = TableDesignerDialog.this.getShell();
				if (MessageDialog.openConfirm(shell, title, message)) {
					openInSqlEditor();
				}
			}
		});

		setTitle(Messages.getString("TableDesignerDialog.14")); //$NON-NLS-1$
		setMessage(Messages.getString("TableDesignerDialog.15")); //$NON-NLS-1$

		Column newColumn = new Column(dbTable.getDatabaseInfo(), dbTable, "id"); //$NON-NLS-1$
		newColumn.setPrimaryKeyFlag(true);
		newColumn.setNullAllowed(false);
		newColumn.setAutoIncrement(true);

		IConnectionType connectionType = dbTable.getDatabaseInfo()
				.getConnectionConfig().getConnectionType();

		newColumn.setSqlType(dbTable.getDatabaseInfo().findSqlTypeByName(
				connectionType.getIntegerSqlType()));
		dbTable.addColumn(newColumn);
		tableViewer.add(newColumn);
		tableViewer.reveal(newColumn);
		tableViewer.setSelection(new StructuredSelection(newColumn), true);
		return container;
	}

	private void openInSqlEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		String sqlScript = sqlScriptViewer.getDocument().get();
		SqlEditorInput input = new SqlEditorInput(dbTable.getDatabaseInfo(),
				sqlScript);
		try {
			close();
			page.openEditor(input, SqlEditor.ID, true);
		} catch (PartInitException exc) {
			ErrorManager.showException(exc);
		}
	}

	public static Color getColor(int r, int g, int b) {
		return getColor(new RGB(r, g, b));
	}

	/**
	 * Returns a color given its RGB value
	 * 
	 * @param rgb
	 *            RGB The RGB value of the color
	 * @return Color The color matching the RGB value
	 */
	public static Color getColor(RGB rgb) {
		Display display = Display.getCurrent();
		return new Color(display, rgb);
	}

	private void createTableColumns(Table table) {
		final TableColumn pkColumn = new TableColumn(table, SWT.NONE);
		pkColumn.setResizable(false);
		pkColumn.setWidth(30);
		pkColumn.setText(columnProperties[0]);

		final TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setResizable(false);
		nameColumn.setWidth(175);
		nameColumn.setText(columnProperties[1]);

		final TableColumn dataTypeColumn = new TableColumn(table, SWT.NONE);
		dataTypeColumn.setResizable(false);
		dataTypeColumn.setWidth(100);
		dataTypeColumn.setText(columnProperties[2]);

		final TableColumn notNullColumn = new TableColumn(table, SWT.NONE);
		notNullColumn.setResizable(false);
		notNullColumn.setWidth(68);
		notNullColumn.setText(columnProperties[3]);

		final TableColumn autoIncColumn = new TableColumn(table, SWT.NONE);
		autoIncColumn.setResizable(false);
		autoIncColumn.setWidth(68);
		autoIncColumn.setText(columnProperties[4]);

		final TableColumn defaultValueColumn = new TableColumn(table, SWT.NONE);
		defaultValueColumn.setResizable(false);
		defaultValueColumn.setWidth(100);
		defaultValueColumn.setText(columnProperties[5]);

		// final TableColumn flagsColumn = new TableColumn(table, SWT.NONE);
		// flagsColumn.setResizable(false);
		// flagsColumn.setWidth(100);
		// flagsColumn.setText(columnProperties[6]);
	}

	private void createCellEditors(TableViewer tableViewer, Table table)
			throws SQLException {
		// Create the cell editors
		CellEditor[] editors = new CellEditor[7];

		// Column 1 : Primary Key (Checkbox)
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : Column Name (Free text)
		TextCellEditor textEditor1 = new TextCellEditor(table);
		((Text) textEditor1.getControl()).setTextLimit(60);
		((Text) textEditor1.getControl())
				.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						// Make sure the names are unique
					}
				});
		editors[1] = textEditor1;

		// Column 3 : SQL Types (Combo Box)

		sqlTypes = dbTable.getDatabaseInfo().getSqlTypes();

		editors[2] = new ComboBoxCellEditor(table, getTypeNames(),
				SWT.READ_ONLY);

		// Column 4 : NOT NULL (Checkbox)
		editors[3] = new CheckboxCellEditor(table);

		// Column 5 : AUTO INC (Checkbox)
		editors[4] = new CheckboxCellEditor(table);

		// Column 6 : Default Value (Free text)
		TextCellEditor textEditor2 = new TextCellEditor(table);
		((Text) textEditor2.getControl()).setTextLimit(60);
		((Text) textEditor2.getControl())
				.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						// Make sure the default values are as per type
					}
				});
		editors[5] = textEditor2;

		// // Column 7 : Flags (Free text)
		// TextCellEditor textEditor3 = new TextCellEditor(table);
		// ((Text) textEditor3.getControl()).setTextLimit(60);
		// ((Text) textEditor2.getControl())
		// .addVerifyListener(new VerifyListener() {
		// public void verifyText(VerifyEvent e) {
		// // Make sure the default values are as per type
		// }
		// });
		// editors[6] = textEditor3;

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages
				.getString("TableDesignerDialog.17"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 660);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	class ContentProvider implements IStructuredContentProvider {
		ITable dbTable;

		public Object[] getElements(Object inputElement) {
			if (dbTable != null) {
				try {
					return dbTable.getColumns(false);
				} catch (SQLException exc) {
					ErrorManager.showException(exc);
				}
			}
			return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof ITable) {
				dbTable = (ITable) newInput;
			} else {
				dbTable = null;
			}
		}
	}

	class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof IColumn) {
				Column column = (Column) element;
				String result = ""; //$NON-NLS-1$
				switch (columnIndex) {

				case 0: // PK
					// result = "pk";
					break;
				case 1: // Name
					result = column.getName();
					break;
				case 2: // Type
					result = column.getType().getName();
					break;
				case 3: // NOT NULL
					break;
				case 4: // AUTO INC
					break;
				case 5: // Default Value
					result = column.getDefaultValue();
					break;
				case 6: // Flags
					result = column.getFlags();
				}
				return result;
			}
			return element.toString();
		}

		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			IColumn column = (IColumn) element;
			switch (columnIndex) {
			case 0: // PK
				result = getImage(column.isPrimaryKey());
				break;
			case 1: // Name
				break;
			case 2: // Type
				break;
			case 3: // NOT NULL
				result = getImage(!column.isNullAllowed());
				break;
			case 4: // AUTO INC
				result = getImage(column.isAutoIncrement());
				break;
			case 5: // Default Value
				break;
			case 6: // Flags
			default:
			}
			return result;
		}

		private Image getImage(boolean isSelected) {
			String key = isSelected ? ImageUtils.CHECKED : ImageUtils.UNCHECKED;
			return ImageUtils.getIcon(key);
		}
	}

	class CellModifier implements ICellModifier {
		TableViewer tableViewer;

		public CellModifier(TableViewer tableViewer) {
			this.tableViewer = tableViewer;
		}

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnProperties).indexOf(property);

			Object result = null;
			Column column = (Column) element;

			switch (columnIndex) {
			case 0: // PK
				result = new Boolean(column.isPrimaryKey());
				break;
			case 1: // Name
				result = column.getName();
				break;
			case 2: // Type
				result = new Integer(sqlTypes.indexOf(column.getType()));
				break;
			case 3: // NOT NULL
				result = new Boolean(column.isNullAllowed());
				break;
			case 4: // AUTO INC
				result = new Boolean(column.isAutoIncrement());
				break;
			case 5: // Default Value
				result = column.getDefaultValue();
				break;
			case 6: // Flags
				result = column.getFlags();
			default:
				result = ""; //$NON-NLS-1$
			}
			return result;
		}

		public void modify(Object element, String property, Object value) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnProperties).indexOf(property);
			TableItem item = (TableItem) element;
			Column column = (Column) item.getData();
			switch (columnIndex) {
			case 0: // PK
				column.setPrimaryKeyFlag(((Boolean) value).booleanValue());
				break;
			case 1: // Name
				column.setName(((String) value).trim());
				break;
			case 2: // Type
				int index = ((Integer) value).intValue();
				column.setSqlType(sqlTypes.get(index));
				break;
			case 3: // NOT NULL
				column.setNullAllowed(((Boolean) value).booleanValue());
				break;
			case 4: // AUTO INC
				column.setAutoIncrement(((Boolean) value).booleanValue());
				break;
			case 5: // Default Value
				column.setDefaultValue(((String) value).trim());
				break;
			case 6: // Flags
				column.setFlags(((String) value).trim());
			default:
			}
			updateScript();
			tableViewer.update(column, null);
		}
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.CANCEL_ID) {
			this.close();
			return;
		}
		if (buttonId == IDialogConstants.OK_ID) {
			updateScript();
			sqlExecutor.setScript(sqlScriptViewer.getDocument().get());
			Job dbJob = new Job("tableDesignerJob") { //$NON-NLS-1$
				@Override
				public IStatus run(IProgressMonitor monitor) {
					final ExecutionStatus execStatus = sqlExecutor.execute();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if (execStatus.hasExceptions()) {
								ErrorManager.showException(execStatus
										.getExceptions().get(0));
							} else {
								close();
							}
						}
					});
					return Status.OK_STATUS;
				}
			};
			dbJob.schedule();
		}
	}

	private void updateScript() {
		ISqlHelper sqlHelper = dbTable.getDatabaseInfo().getConnectionConfig()
				.getConnectionType().getSqlHelper();
		dbTable.setName(tableNameText.getText());
		if (sqlScriptViewer != null) {
			boolean generateUseSchema = true;
			IConnectionConfig connectionConfig = dbTable.getDatabaseInfo()
					.getConnectionConfig();
			if ((connectionConfig.getDbType() == IConnectionType.SQLITE)
					|| (connectionConfig.getDbType() == IConnectionType.DERBY_EMBEDDED)) {
				generateUseSchema = false;
			}
			sqlScriptViewer.getDocument().set(
					sqlHelper.generateTableCreateStatement(dbTable,
							generateUseSchema));
		}
	}

	private String getUniqueColumnName() {
		int count = 0;
		String newColumnName = COLUMN_BASE_NAME + ++count;
		List<String> columnNames = dbTable.getColumnNames();
		while (columnNames.contains(newColumnName)) {
			newColumnName = COLUMN_BASE_NAME + count++;
		}
		return newColumnName;
	}

	private String[] getTypeNames() {
		List<String> names = new ArrayList<String>();
		if ((sqlTypes != null) && (sqlTypes.size() > 0)) {
			for (ISqlType sqlType : sqlTypes) {
				names.add(sqlType.getName());
			}
		}
		return names.toArray(new String[names.size()]);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String title = Messages.getString("TableDesignerDialog.21"); //$NON-NLS-1$
		try {
			if (dbTable.getDatabaseInfo().hasSchemaSupport()) {
				title += "(" + dbTable.getSchema().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (SQLException exc) {
			// DO nothing, JDBC driver doesn't like checking for schema
		}
		newShell.setText(title);
		newShell.setImage(ImageUtils.getIcon(ImageUtils.TABLE_EDITOR));
	}
}

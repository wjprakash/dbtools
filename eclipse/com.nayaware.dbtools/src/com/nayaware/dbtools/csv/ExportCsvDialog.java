
package com.nayaware.dbtools.csv;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Dialog to export table data to a CVS file
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExportCsvDialog extends TitleAreaDialog {

	private boolean headerRow = true;
	private String fileName;
	private char columnSeparator = ',';
	private ITable table;
	private boolean quote = true;

	public ExportCsvDialog(Shell parentShell, ITable table) {
		super(parentShell);
		this.table = table;
		fileName = System.getProperty("user.home") + "/" + table.getName() //$NON-NLS-1$ //$NON-NLS-2$
				+ ".csv"; //$NON-NLS-1$
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		setTitleImage(ImageUtils.getIcon(ImageUtils.EXPORT_CSV));
		setMessage(Messages.getString("ExportCsvDialog.3")); //$NON-NLS-1$
		setTitle(Messages.getString("ExportCsvDialog.4")); //$NON-NLS-1$

		final Group csvOptionsGroup = new Group(container, SWT.NONE);
		csvOptionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		csvOptionsGroup.setLayout(gridLayout);
		csvOptionsGroup.setText(Messages.getString("ExportCsvDialog.5")); //$NON-NLS-1$

		final Button writeAHeaderButton = new Button(csvOptionsGroup, SWT.CHECK);
		writeAHeaderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				headerRow = writeAHeaderButton.getSelection();
			}
		});
		writeAHeaderButton.setSelection(true);
		writeAHeaderButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 2, 1));
		writeAHeaderButton.setText(Messages.getString("ExportCsvDialog.6")); //$NON-NLS-1$

		final Button quoteColumnValuesButton = new Button(csvOptionsGroup,
				SWT.CHECK);
		quoteColumnValuesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				quote = quoteColumnValuesButton.getSelection();
			}
		});
		quoteColumnValuesButton.setSelection(true);
		final GridData gd_quoteColumnValuesButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 2, 1);
		quoteColumnValuesButton.setLayoutData(gd_quoteColumnValuesButton);
		quoteColumnValuesButton.setText(Messages.getString("ExportCsvDialog.7")); //$NON-NLS-1$

		final Label columnLabel = new Label(csvOptionsGroup, SWT.NONE);
		columnLabel.setLayoutData(new GridData());
		columnLabel.setText(Messages.getString("ExportCsvDialog.8")); //$NON-NLS-1$

		Text columnSeparatorText = new Text(csvOptionsGroup, SWT.BORDER);
		columnSeparatorText.setText(","); //$NON-NLS-1$
		columnSeparatorText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String columnSeparatorStr = ((Text) event.getSource())
						.getText();
				if (columnSeparatorStr.length() != 1) {
					((Text) event.getSource()).setBackground(parent
							.getDisplay().getSystemColor(SWT.COLOR_RED));
				} else {
					((Text) event.getSource()).setBackground(parent
							.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				}
				if (columnSeparatorStr.length() > 0)
					columnSeparator = columnSeparatorStr.charAt(0);
				else
					columnSeparator = ',';
			}
		});
		final GridData gd_text = new GridData(20, SWT.DEFAULT);
		gd_text.minimumWidth = 20;
		columnSeparatorText.setLayoutData(gd_text);

		final Composite composite = new Composite(container, SWT.NONE);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 3;
		composite.setLayout(gridLayout_1);

		final Label csvFileNameLabel = new Label(composite, SWT.NONE);
		csvFileNameLabel.setText(Messages.getString("ExportCsvDialog.10")); //$NON-NLS-1$

		final Text cvsFileText = new Text(composite, SWT.BORDER);
		cvsFileText.setText(fileName);
		final GridData gd_cvsFileText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		cvsFileText.setLayoutData(gd_cvsFileText);

		final Button browseButton = new Button(composite, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.csv" }); //$NON-NLS-1$
				dialog.setFilterNames(new String[] { Messages.getString("ExportCsvDialog.12") }); //$NON-NLS-1$
				dialog.setFilterPath(cvsFileText.getText());
				fileName = dialog.open();
				if (fileName != null) {
					cvsFileText.setText(fileName);
				}
			}
		});
		browseButton.setText(Messages.getString("ExportCsvDialog.13")); //$NON-NLS-1$
		return area;
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(ImageUtils.getIcon(ImageUtils.CSV));
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			IProgressService progressService = PlatformUI.getWorkbench()
					.getProgressService();
			try {
				progressService.busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							doExport();
						} catch (final Exception exc) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									ErrorManager.showException(exc);
								}
							});
						}
					}
				});
			} catch (InvocationTargetException exc) {
				ErrorManager.showException(exc);
			} catch (InterruptedException exc) {
				ErrorManager.showException(exc);
			}
			close();
			return;
		}
		super.buttonPressed(buttonId);
	}

	private void doExport() throws IOException, SQLException,
			ClassNotFoundException {
		File outputFile = new File(fileName);
		ITableData tabledata = table.getData();
		CsvWriter csvWriter = new CsvWriter();
		csvWriter.write(outputFile, tabledata, headerRow, columnSeparator,
				quote);
	}

}

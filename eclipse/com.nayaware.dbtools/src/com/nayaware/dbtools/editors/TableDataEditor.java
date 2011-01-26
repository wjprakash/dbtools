
package com.nayaware.dbtools.editors;

import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.api.ITableRowData;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.tests.DataTableViewerTest;
import com.nayaware.dbtools.ui.TableDataFormDialog;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.viewers.DataTableViewer;

/**
 * Editor that displays the Table data and helps to modify it
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableDataEditor extends EditorPart {

	public static String ID = "com.nayaware.dbtools.tableDataEditor"; //$NON-NLS-1$

	private ITableData tableData;
	private TableViewer tableViewer;

	private ToolItem details;
	private ToolItem pageSizeToolItem;

	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		setSite(site);
		setInput(editorInput);
		if (editorInput instanceof TableDataEditorInput) {
			TableDataEditorInput tableDataEditorInput = (TableDataEditorInput) editorInput;
			tableData = tableDataEditorInput.getTableData();
		}
		setPartName(editorInput.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		final ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.BORDER);
		final GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		gd_toolBar.horizontalIndent = 10;
		toolBar.setLayoutData(gd_toolBar);

		final ToolItem addRecord = new ToolItem(toolBar, SWT.PUSH);
		addRecord.setImage(ImageUtils.getIcon(ImageUtils.ADD));
		addRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					Shell shell = parent.getShell();
					TableDataFormDialog tableDataFormDialog = new TableDataFormDialog(
							shell, tableData.createNewTableRowData(), false);
					int ret = tableDataFormDialog.open();
					if (ret == IDialogConstants.OK_ID) {
						ITableRowData newRowData = tableDataFormDialog
								.getTableRowData();
						ExecutionStatus execStatus = tableData
								.appendTableRow(tableDataFormDialog
										.getTableRowData());
						if (execStatus.hasExceptions()) {
							ErrorManager.showException(execStatus
									.getExceptions().get(0));
						} else {
							tableViewer.add(newRowData);
							tableViewer.reveal(newRowData);
							tableViewer.setSelection(new StructuredSelection(
									newRowData), true);
						}
					}
				} catch (Exception exc) {
					ErrorManager.showException(exc);
				}
			}
		});
		addRecord.setToolTipText(Messages.getString("TableDataEditor.1")); //$NON-NLS-1$
		addRecord.setSelection(true);

		final ToolItem deleteSelections = new ToolItem(toolBar, SWT.PUSH);
		deleteSelections.setImage(ImageUtils.getIcon(ImageUtils.DELETE));
		deleteSelections.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				ITableRowData oldRowData = (ITableRowData) ((IStructuredSelection) tableViewer
						.getSelection()).getFirstElement();
				Shell shell = parent.getShell();
				 
				String message = Messages.getString("TableDataEditor.2"); //$NON-NLS-1$
				String title = Messages.getString("TableDataEditor.3"); //$NON-NLS-1$
				if (MessageDialog.openConfirm(shell, title, message)) {
					ExecutionStatus execStatus = tableData
							.deleteTableRow(oldRowData);
					if (execStatus.hasExceptions()) {
						ErrorManager.showException(execStatus.getExceptions()
								.get(0));
					} else {
						tableViewer.remove(oldRowData);
						// tableViewer.reveal(nextRowData);
						// tableViewer.setSelection(new StructuredSelection(
						// nextRowData), true);
					}
				}
			}
		});
		deleteSelections.setEnabled(false);
		deleteSelections
				.setToolTipText(Messages.getString("TableDataEditor.4")); //$NON-NLS-1$

		final ToolItem updateSelected = new ToolItem(toolBar, SWT.PUSH);
		updateSelected.setImage(ImageUtils.getIcon(ImageUtils.UPDATE));
		updateSelected.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					Shell shell = parent.getShell();
					ITableRowData currentRowData = (ITableRowData) ((IStructuredSelection) tableViewer
							.getSelection()).getFirstElement();
					// There may not be a selection
					if (currentRowData != null) {
						ITableRowData updateRowData = tableData
								.createNewTableRowData();

						updateRowData.setTableColumnData(currentRowData
								.getTableColumnData());

						TableDataFormDialog tableDataFormDialog = new TableDataFormDialog(
								shell, updateRowData, true);
						int ret = tableDataFormDialog.open();
						if (ret == IDialogConstants.OK_ID) {
							ExecutionStatus execStatus = tableData
									.updateTableRow(currentRowData,
											tableDataFormDialog
													.getTableRowData());
							if (execStatus.hasExceptions()) {
								ErrorManager.showException(execStatus
										.getExceptions().get(0));
							} else {
								tableViewer.refresh(currentRowData);
								tableViewer.reveal(currentRowData);
								tableViewer
										.setSelection(new StructuredSelection(
												currentRowData), true);
							}
						}
					}
				} catch (SQLException exc) {
					ErrorManager.showException(exc);
				} catch (ClassNotFoundException exc) {
					ErrorManager.showException(exc);
				}
			}
		});
		updateSelected.setEnabled(false);
		updateSelected.setToolTipText("Update Selected Record"); //$NON-NLS-1$

		new ToolItem(toolBar, SWT.SEPARATOR);

		final ToolItem firstPageToolItem = new ToolItem(toolBar, SWT.PUSH);
		firstPageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				tableData.firstPage();
				try {
					tableViewer.setInput(tableData.getPageData());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setDetails();
			}
		});
		firstPageToolItem.setImage(ImageUtils.getIcon(
				DataTableViewerTest.class, ImageUtils.FIRST_PAGE));

		final ToolItem previousPageToolItem = new ToolItem(toolBar, SWT.PUSH);
		previousPageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					if (tableData.previousPage()) {
						tableViewer.setInput(tableData.getPageData());
						setDetails();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		previousPageToolItem.setImage(ImageUtils.getIcon(
				DataTableViewerTest.class, ImageUtils.PREVIOUS_PAGE));

		final ToolItem nextPageToolItem = new ToolItem(toolBar, SWT.PUSH);
		nextPageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					if (tableData.nextPage()) {
						tableViewer.setInput(tableData.getPageData());
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setDetails();
			}
		});
		nextPageToolItem.setImage(ImageUtils.getIcon(DataTableViewerTest.class,
				ImageUtils.NEXT_PAGE));

		final ToolItem lastPageToolItem = new ToolItem(toolBar, SWT.PUSH);
		lastPageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					tableData.lastPage();
					tableViewer.setInput(tableData.getPageData());
					setDetails();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		lastPageToolItem.setImage(ImageUtils.getIcon(DataTableViewerTest.class,
				ImageUtils.LAST_PAGE));

		new ToolItem(toolBar, SWT.SEPARATOR);

		pageSizeToolItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		pageSizeToolItem.setText(Messages.getString("TableDataEditor.0") + 25); //$NON-NLS-1$

		final Menu menu = new Menu(toolBar);

		final MenuItem pageSize25 = new MenuItem(menu, SWT.NONE);
		pageSize25.setText("25"); //$NON-NLS-1$
		pageSize25.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(25);
			}
		});

		final MenuItem pageSize50 = new MenuItem(menu, SWT.NONE);
		pageSize50.setText("50"); //$NON-NLS-1$
		pageSize50.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(50);
			}
		});

		final MenuItem pageSize100 = new MenuItem(menu, SWT.NONE);
		pageSize100.setText("100"); //$NON-NLS-1$
		pageSize100.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(100);
			}
		});

		final MenuItem pageSize200 = new MenuItem(menu, SWT.NONE);
		pageSize200.setText("200"); //$NON-NLS-1$
		pageSize200.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(200);
			}
		});

		final MenuItem pageSize500 = new MenuItem(menu, SWT.NONE);
		pageSize500.setText("500"); //$NON-NLS-1$
		pageSize500.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(500);
			}
		});

		addDropDown(pageSizeToolItem, menu);

		new ToolItem(toolBar, SWT.SEPARATOR);

		details = new ToolItem(toolBar, SWT.PUSH);
		setDetails();

		try {
			tableViewer = new DataTableViewer(parent, tableData
					.getColumnNames());
			tableViewer.setInput(tableData.getPageData());
			tableViewer
					.addSelectionChangedListener(new ISelectionChangedListener() {

						public void selectionChanged(SelectionChangedEvent event) {
							updateSelected.setEnabled(true);
							deleteSelections.setEnabled(true);
						}
					});
		} catch (Exception exc) {
			ErrorManager.showException(exc);
			Label erroLabel = new Label(parent, SWT.FILL);
			erroLabel
					.setText(Messages.getString("TableDataEditor.10") //$NON-NLS-1$
							+ exc.getLocalizedMessage());
		}
	}

	private void setPageSize(int pageSize) {
		pageSizeToolItem.setText(Messages.getString("TableDataEditor.11") + pageSize); //$NON-NLS-1$
		tableData.setPageSize(pageSize);
		try {
			tableViewer.setInput(tableData.getPageData());
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		setDetails();
	}

	private void addDropDown(final ToolItem item, final Menu menu) {
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.ARROW) {
					Rectangle rect = item.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = item.getParent().toDisplay(pt);
					menu.setLocation(pt.x, pt.y);
					menu.setVisible(true);
				}
			}
		});
	}

	private void setDetails() {
		try {
			String detailsText = Messages.getString("TableDataEditor.12") //$NON-NLS-1$
					+ tableData.getTotalRowCount() + Messages.getString("TableDataEditor.13") //$NON-NLS-1$
					+ tableData.getCurrentPage() + Messages.getString("TableDataEditor.14") //$NON-NLS-1$
					+ tableData.getTotalPages() + Messages.getString("TableDataEditor.15"); //$NON-NLS-1$
			details.setText(detailsText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}
}

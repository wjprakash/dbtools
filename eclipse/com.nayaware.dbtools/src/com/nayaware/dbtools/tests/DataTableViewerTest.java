
package com.nayaware.dbtools.tests;

import java.sql.SQLException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.viewers.DataTableViewer;

/**
 * Data Table Viewer Test
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DataTableViewerTest {

	protected Shell shell;

	private TableViewer tableViewer;

	private ITableData tableData;

	private ToolItem details;
	private ToolItem pageSizeToolItem;

	public DataTableViewerTest() throws SQLException {
		tableData = createTableData();
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataTableViewerTest window = new DataTableViewerTest();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 * 
	 * @throws SQLException
	 */
	protected void createContents() throws SQLException {
		shell = new Shell();
		shell.setLayout(new GridLayout());
		shell.setLocation(new Point(600, 300));
		shell.setSize(500, 375);
		shell.setText("SWT Application");

		final ToolBar toolBar = new ToolBar(shell, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

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
		pageSizeToolItem.setText("Page Size (25):");

		final Menu menu = new Menu(toolBar);

		final MenuItem pageSize25 = new MenuItem(menu, SWT.NONE);
		pageSize25.setText("25");
		pageSize25.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(25);
			}
		});

		final MenuItem pageSize50 = new MenuItem(menu, SWT.NONE);
		pageSize50.setText("50");
		pageSize50.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(50);
			}
		});

		final MenuItem pageSize100 = new MenuItem(menu, SWT.NONE);
		pageSize100.setText("100");
		pageSize100.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(100);
			}
		});

		final MenuItem pageSize200 = new MenuItem(menu, SWT.NONE);
		pageSize200.setText("200");
		pageSize200.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setPageSize(200);
			}
		});

		final MenuItem pageSize500 = new MenuItem(menu, SWT.NONE);
		pageSize500.setText("500");
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

		tableViewer = new DataTableViewer(shell, tableData.getColumnNames());
		tableViewer.setInput(tableData.getPageData());
	}

	private void setPageSize(int pageSize) {
		pageSizeToolItem.setText("Page Size: " + pageSize);
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

	private ITableData createTableData() throws SQLException {
		IConnectionConfig dbConfig = ConnectionUtils
				.createMySqlConnectionConfig();

		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		IConnection dbConnection = new Connection(dbInfo);
		ISchema schema = dbConnection.findSchemaByName("test");
		ITable dbTable = schema.findTableByName("City");
		return dbTable.getData();
	}

	private void setDetails() {
		try {
			String detailsText = "Total Records: "
					+ tableData.getTotalRowCount() + " (Page: "
					+ tableData.getCurrentPage() + " of "
					+ tableData.getTotalPages() + " )";
			details.setText(detailsText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

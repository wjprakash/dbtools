
package com.nayaware.dbtools.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.viewers.SqlSourceViewer;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorTest {
	private CTabFolder tabFolder;

	public void showWindow() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		final SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
		sashForm.setLayout(new FillLayout());

		final Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout());

		final Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				addResultTab(sashForm);
			}
		});
		button_2.setText("button");

		createSourceViewer(sashForm);

		shell.setSize(800, 800);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void main(String[] args) {
		try {
			SqlEditorTest cTabFolderTest = new SqlEditorTest();
			cTabFolderTest.showWindow();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void createSourceViewer(Composite parent) {
		// textArea = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		// textArea.setEditable(false);
		// textArea.setBackground(textArea.getDisplay().getSystemColor(
		// SWT.COLOR_INFO_BACKGROUND));
		// textArea.setForeground(textArea.getDisplay().getSystemColor(
		// SWT.COLOR_INFO_FOREGROUND));
		new SqlSourceViewer(parent);
	}

	private void createResultTabFolder(final Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.BORDER);
		tabFolder.setSimple(false);
		tabFolder.setUnselectedImageVisible(false);
		tabFolder.setUnselectedCloseVisible(false);
		tabFolder.setMinimizeVisible(true);
		tabFolder.setMaximizeVisible(true);
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override
			public void close(CTabFolderEvent event) {
				if (tabFolder.getItemCount() > 1) {
					return;
				} else {
					tabFolder.setVisible(false);
					parent.layout(true);
				}
			}
		});
	}

	private void addResultTab(SashForm sashForm) {
		if (tabFolder == null) {
			createResultTabFolder(sashForm);
		} else {
			if (!tabFolder.isVisible()) {
				tabFolder.setVisible(true);
			}
		}
		CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
		item.setText("Tab");
		Text text = new Text(tabFolder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setText("Text for item "
				+ "\n\none, two, three\n\nabcdefghijklmnop");
		item.setControl(text);
		sashForm.layout(true);
		item.getParent().setFocus();
		tabFolder.setSelection(item);
	}
}

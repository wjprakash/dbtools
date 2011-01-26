
package com.nayaware.dbtools.views;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.nayaware.dbtools.actions.CreateDerbyDatabaseAction;
import com.nayaware.dbtools.actions.CreateSqliteDatabaseAction;
import com.nayaware.dbtools.actions.ExplorerCollapseAllAction;
import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.OpenSchemaDesignerAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.PersistanceManager;
import com.nayaware.dbtools.viewers.ExplorerTreeViewer;

/**
 * Connection Explorer View
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class DbExplorerView extends ViewPart {
	private ExplorerTreeViewer explorerTreeViewer;

	/**
	 * This is a call back that will allow us to create the viewer and
	 * initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		explorerTreeViewer = new ExplorerTreeViewer(parent, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
				explorerTreeViewer.getControl(),
				"com.nayaware.dbtools.viewer"); //$NON-NLS-1$

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		explorerTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			public void selectionChanged(SelectionChangedEvent event) {
				IActionBars bars = getViewSite().getActionBars();
				IToolBarManager manager = bars.getToolBarManager();
				IContributionItem[]  contributionItems = manager.getItems();
				for (IContributionItem contributionItem : contributionItems) {
					contributionItem.update();
				}
			}
			
		});
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		if (!PersistanceManager.getInstance().isInitialized()) {
			PersistanceManager.getInstance().loadConnections();
		}
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
	}

	private void makeActions() {
		// TODO: Pre create all the actions
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(explorerTreeViewer.getControl());
		explorerTreeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, explorerTreeViewer);
	}

	/**
	 * Called when the menu is about to show. Call the selected node to fill
	 * with actions if applied
	 * 
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager) {
		AbstractNode node = (AbstractNode) ((StructuredSelection) explorerTreeViewer
				.getSelection()).getFirstElement();
		node.fillContextMenu(manager);

		// Other plug-ins can contribute there actions here
		// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Contribute actions to the action bar in the view
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		IMenuManager menuMgr = bars.getMenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillLocalPullDown(manager);
			}
		});
		fillLocalPullDown(menuMgr);
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * Contribute actions to the Pull Down menu in the action bar
	 * 
	 * @param manager
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(new CreateDerbyDatabaseAction(explorerTreeViewer));
		manager.add(new CreateSqliteDatabaseAction(explorerTreeViewer));
		manager.add(new Separator());
		manager.add(new OpenSchemaDesignerAction(explorerTreeViewer));
		manager.add(new OpenQueryBuilderAction(explorerTreeViewer));
		manager.add(new OpenSqlEditorAction(explorerTreeViewer));
	}

	/**
	 * Contribute actions to toolbar in the view action bar
	 * 
	 * @param manager
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new OpenSchemaDesignerAction(explorerTreeViewer));
		manager.add(new OpenQueryBuilderAction(explorerTreeViewer));
		manager.add(new OpenSqlEditorAction(explorerTreeViewer));
		manager.add(new Separator());
		manager.add(new ExplorerCollapseAllAction(explorerTreeViewer));
	}

	private void hookDoubleClickAction() {
		explorerTreeViewer
				.addDoubleClickListener(new ExplorerDoubleClickHandler());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		explorerTreeViewer.getControl().setFocus();
	}
}
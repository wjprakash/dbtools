
package com.nayaware.dbtools.actions;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.nayaware.dbtools.actions.messages"; //$NON-NLS-1$
	public static String DeleteAction_1;
	public static String DeleteAction_2;

	public static String EditDatabaseConnectionAction_1;
	public static String EditDatabaseConnectionAction_2;

	public static String RemoveDatabaseConnectionAction_0;
	public static String RemoveDatabaseConnectionAction_1;
	public static String RemoveDatabaseConnectionAction_2;
	public static String RemoveDatabaseConnectionAction_3;

	public static String CreateEmbeddedDerbyDatabaseAction_1;
	public static String CreateEmbeddedDerbyDatabaseAction_2;

	public static String CreateSqliteDatabaseAction_1;
	public static String CreateSqliteDatabaseAction_2;

	public static String LaunchMigrationToolAction_1;
	public static String LaunchMigrationToolAction_2;

	public static String DropDatabaseAction_0;
	public static String DropDatabaseAction_1;
	public static String DropDatabaseAction_2;
	public static String DropDatabaseAction_3;

	public static String CreateDatabaseAction_0;
	public static String CreateDatabaseAction_1;
	public static String CreateDatabaseAction_2;

	public static String ExplorerCollapseAllAction_2;

	public static String ExplorerExpandAllAction_2;

	public static String OpenSqlEditorAction_1;
	public static String OpenSqlEditorAction_2;

	public static String ExecuteSqlAction_1;
	public static String ExecuteSqlAction_2;

	public static String OpenSchemaDesignerAction_0;
	public static String OpenSchemaDesignerAction_1;
	public static String OpenSchemaDesignerAction_2;
	public static String OpenTableDesignerAction_0;

	public static String QueryBuilderAction_1;
	public static String QueryBuilderAction_2;

	public static String DropTableAction_0;
	public static String DropTableAction_1;
	public static String DropTableAction_2;
	public static String DropTableAction_3;

	public static String RefreshAction_1;
	public static String RefreshAction_2;

	public static String ViewSchemaDiagramAction_0;
	public static String ViewSchemaDiagramAction_1;
	public static String ViewSchemaDiagramAction_2;

	public static String AddConnectionAction_0;
	public static String AddConnectionAction_1;

	public static String ViewDataAction_1;
	public static String ViewDataAction_2;

	public static String CreateTableAction_1;
	public static String CreateTableAction_2;

	public static String TruncateTableAction_0;
	public static String TruncateTableAction_1;
	public static String TruncateTableAction_2;
	public static String TruncateTableAction_3;

	public static String CopyTableAction_0;
	public static String CopyTableAction_1;
	public static String CopyTableAction_2;
	public static String CopyTableAction_3;
	public static String CopyTableAction_4;
	public static String CopyTableAction_5;

	public static String ExportCsvAction_1;
	public static String ExportCsvAction_2;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

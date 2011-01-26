
package com.nayaware.dbtools.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableGroupNode;
import com.nayaware.dbtools.nodes.TableNode;

/**
 * @author Winston Prakash Version 1.0
 */
public class TableNodeRefreshTest {

	TableNodeRefreshTest() {
		Job.getJobManager()
				.addJobChangeListener(new RefreshJobChangeListener());
	}

	public void dumpDatabaseNodeChildren(IConnectionConfig dbConfig)
			throws SQLException {

		IDatabaseInfo databaseInfo = new DatabaseInfo(dbConfig);
		IConnection database = new Connection(databaseInfo);
		final ConnectionNode databaseNode = new ConnectionNode(database);
		databaseNode.setRefreshDelay(1000);
		databaseNode.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String property = evt.getPropertyName();
				if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
					List<AbstractNode> schemaNodes = databaseNode
							.getSchemaNodeList();
					System.out
							.println("Connection Node ("
									+ databaseNode.getDisplayName()
									+ ") refresh done.");
					for (AbstractNode node : schemaNodes) {
						System.out.println("Schema: " + node.getDisplayName());
						dumpSchemaNodeChildren((SchemaNode) node);
					}
				}
			}
		});
		List<AbstractNode> schemaNodes = databaseNode.getSchemaNodeList();
		for (AbstractNode node : schemaNodes) {
			System.out.println(node.getDisplayName());
		}
	}

	public void dumpSchemaNodeChildren(final SchemaNode schemaNode) {
		final TableGroupNode tabelGroupNode = schemaNode.getTableGroupNode();

		tabelGroupNode.setRefreshDelay(1000);
		tabelGroupNode.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String property = evt.getPropertyName();
				if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
					List<AbstractNode> tableNodes = tabelGroupNode
							.getTableNodeList();
					System.out.println("Table Group Node ("
							+ schemaNode.getDisplayName() + ") refresh done.");
					for (AbstractNode node : tableNodes) {
						System.out
								.println("   Table: " + node.getDisplayName());
						dumpTableNodeChildren((TableNode) node);
					}
				}
			}
		});
		List<AbstractNode> tableNodes = tabelGroupNode.getTableNodeList(true);
		for (AbstractNode node : tableNodes) {
			System.out.println(node.getDisplayName());
		}
	}

	public void dumpTableNodeChildren(final TableNode tableNode) {

		tableNode.setRefreshDelay(1000);
		tableNode.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String property = evt.getPropertyName();
				if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
					List<AbstractNode> columnNodes = tableNode
							.getColumnNodeList();
					System.out.println("Table Node ("
							+ tableNode.getDisplayName() + ") refresh done.");
					for (AbstractNode node : columnNodes) {
						System.out.println("   Column: "
								+ node.getDisplayName());
					}
				}
			}
		});
		List<AbstractNode> columnNodes = tableNode.getColumnNodeList(true);
		for (AbstractNode node : columnNodes) {
			System.out.println(node.getDisplayName());
		}
	}

	public static void main(String[] args) {
		TableNodeRefreshTest connectionTest = new TableNodeRefreshTest();
		try {
			connectionTest.dumpDatabaseNodeChildren(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(30000);
			System.out
					.println("Exiting from sleep. Test may not be completed correctly");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public class RefreshJobChangeListener extends JobChangeAdapter {

		List<Job> jobs = new CopyOnWriteArrayList<Job>();

		@Override
		public void aboutToRun(IJobChangeEvent event) {
			String jobName = event.getJob().getName();
			if (jobName.equals(TableNode.TABLE_REFRESH_JOB)) {
				jobs.add(event.getJob());
			}
		}

		@Override
		public void done(IJobChangeEvent event) {
			String jobName = event.getJob().getName();
			if (jobName.equals(TableNode.TABLE_REFRESH_JOB)) {
				jobs.remove(event.getJob());
				if (jobs.isEmpty()) {
					System.out
							.println("All refresh jobs completed. Terminating.");
					System.exit(0);
				}
			}
		}
	}
}

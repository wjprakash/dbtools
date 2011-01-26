package com.jfxtools.database.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.jfxtools.database.ConnectionManagerImpl;
import com.jfxtools.database.mysql.MySqlConnectionType;
import com.jfxtools.database.util.PersistanceManager;

/**
 * Connection Manager that maintains the information about added connections
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public abstract class ConnectionManager {

    private static ConnectionManager instance;

    public synchronized static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            List<IConnectionType> connectionTypes = instance.getAvailableConnectionTypes();
            connectionTypes.add(new MySqlConnectionType());
            try {
                PersistanceManager.getInstance().loadConnections();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instance;
    }

    public abstract IConnectionConfig createConnectionConfig(String name,
            String url, int type, String username, String password);

    public abstract IConnectionConfig createConnectionConfig(String name,
            String hostname, String port, String database, int type,
            String username, String password);

    public abstract void addConnectionManagerListener(
            ConnectionManagerListener listener);

    public abstract void removeConnectionManagerListener(
            ConnectionManagerListener listener);

    public abstract void addConnectionConfig(IConnectionConfig connectionConfig);

    public abstract void removeConnectionConfig(IConnectionConfig config);

    public abstract void updateConnectionConfig(
            IConnectionConfig connectionConfig);

    public abstract List<IConnectionConfig> getConnectionConfigList();

    public abstract Connection createConnection(IConnectionConfig config)
            throws SQLException;

    public abstract String getUniqueConnectionName();

    public abstract List<IConnectionType> getAvailableConnectionTypes();

    public abstract void setAvailableConnectionTypes(List<IConnectionType> connectionTypeList);

    public abstract IConnectionType findConnectionType(int dbType);

    public static interface ConnectionManagerListener {

        public void connectionConfigAdded(IConnectionConfig config);

        public void connectionConfigRemoved(IConnectionConfig config);

        public void connectionConfigModified(IConnectionConfig connectionConfig);
    }
}

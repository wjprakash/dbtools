package com.jfxtools.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.util.PersistanceManager;

/**
 * Manages the database connection configurations
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionManagerImpl extends ConnectionManager {

    private List<IConnectionConfig> connections = new ArrayList<IConnectionConfig>();
    private List<ConnectionManagerListener> listeners = new CopyOnWriteArrayList<ConnectionManagerListener>();
    private Map<String, Driver> driverMap = new HashMap<String, Driver>();
    private static final int TIME_OUT_SEC = 10;
    private static final String CONNECTION_BASE_NAME = Messages.getString("ConnectionManagerImpl.1");
    private List<IConnectionType> availableonnectionTypes;

    @Override
    public void addConnectionManagerListener(ConnectionManagerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeConnectionManagerListener(
            ConnectionManagerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addConnectionConfig(IConnectionConfig connectionConfig) {
        connections.add(connectionConfig);
        for (ConnectionManagerListener listener : listeners) {
            listener.connectionConfigAdded(connectionConfig);
        }
        try {
            PersistanceManager.getInstance().persistConnectionInfo(connectionConfig);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public IConnectionConfig createConnectionConfig(String name, String url,
            int type, String username, String password) {
        return new ConnectionConfig(name, url, type, username, password);
    }

    @Override
    public IConnectionConfig createConnectionConfig(String name,
            String hostname, String port, String database, int type,
            String username, String password) {
        return new ConnectionConfig(name, hostname, port, database, type,
                username, password);
    }

    @Override
    public void removeConnectionConfig(IConnectionConfig config) {
        connections.remove(config);
        for (ConnectionManagerListener listener : listeners) {
            listener.connectionConfigRemoved(config);
        }
        PersistanceManager.getInstance().removePersistedConnectionInfo(config);
    }

    @Override
    public void updateConnectionConfig(IConnectionConfig connectionConfig) {
        for (ConnectionManagerListener listener : listeners) {
            listener.connectionConfigModified(connectionConfig);
        }
        try {
            PersistanceManager.getInstance().updatePersistedConnectionInfo(
                    connectionConfig);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public synchronized List<IConnectionConfig> getConnectionConfigList() {
        return Collections.unmodifiableList(connections);
    }

    @Override
    public Connection createConnection(IConnectionConfig config)
            throws SQLException {

        Connection con = null;
        Driver driver;
        try {
            driver = getDriver(config.getDriverName());
        } catch (Exception exc) {
            throw new SQLException(Messages.getString("ConnectionManagerImpl.noDriver")
                    + config.getDriverName());
        }

        if (driver != null) {
            try {
                DriverManager.setLoginTimeout(TIME_OUT_SEC);
            } catch (Exception exc) {
                // Some drivers might not have implemented, in that case simply
                // ignore
                // ErrorManager.showException(exc);
            }
            Properties conProps = new Properties();
            conProps.setProperty("user", config.getUsername());
            conProps.setProperty("password", config.getPassword());
            con = driver.connect(config.getUrl(), conProps);
        } else {
            throw new IllegalStateException(
                    Messages.getString("ConnectionManagerImpl.noDriver") + config.getDriverName());
        }
        return con;
    }

    /**
     * Find the JDBC Driver for the given driver name
     *
     * @param driverName
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private Driver getDriver(String driverName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (driverMap.containsKey(driverName)) {
            return driverMap.get(driverName);
        } else {
            Class driverClass = getClass().getClassLoader().loadClass(
                    driverName);
            return (Driver) driverClass.newInstance();
        }
    }

    /**
     * Get the available connection types from the plugin extensions for the
     * "jdbcDriver" extension point
     */
    public synchronized List<IConnectionType> getAvailableConnectionTypes() {
        if (availableonnectionTypes == null) {
            availableonnectionTypes = new ArrayList<IConnectionType>();
            // Add available connection types
        }

        return availableonnectionTypes;
    }

    @Override
    public void setAvailableConnectionTypes(
            List<IConnectionType> connectionTypeList) {
        availableonnectionTypes = connectionTypeList;
    }

    @Override
    public String getUniqueConnectionName() {
        int count = 0;
        String uniqueName = CONNECTION_BASE_NAME + ++count;
        List<String> connectionNames = new ArrayList<String>(connections.size());

        for (IConnectionConfig config : connections) {
            connectionNames.add(config.getName());
        }

        while (connectionNames.contains(uniqueName)) {
            uniqueName = CONNECTION_BASE_NAME + count++;
        }

        return uniqueName;
    }

    public synchronized IConnectionType findConnectionType(int dbType) {
        if (availableonnectionTypes == null) {
            getAvailableConnectionTypes();
        }
        for (IConnectionType connectionTypeInfo : availableonnectionTypes) {
            if (dbType == connectionTypeInfo.getType()) {
                return connectionTypeInfo;
            }
        }
        return null;
    }
}

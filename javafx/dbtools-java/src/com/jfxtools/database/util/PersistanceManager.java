package com.jfxtools.database.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.jfxtools.database.ConnectionConfig;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.IQuery;
import com.jfxtools.database.api.ISchemaDiagram;
import com.jfxtools.database.api.IScript;
import com.jfxtools.database.model.Query;
import com.jfxtools.database.model.SchemaDiagram;
import com.jfxtools.database.model.Script;

/**
 * A manager that persists the information from the Connection manager
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PersistanceManager {

    private static PersistanceManager instance;
    private final String CONNECTION_PROPERTIES_FILE = "Connection_props.xml";
    private boolean initialized = false;

    public synchronized static PersistanceManager getInstance() {
        if (instance == null) {
            instance = new PersistanceManager();
        }
        return instance;
    }

    /**
     * MySql Sample connections for testing purpose
     */
    public ConnectionConfig createMySqlConnection() {
        String name = "Local MySql";
        String url = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "";
        ConnectionConfig config = new ConnectionConfig(name, url,
                IConnectionType.MYSQL, username, password);
        config.setHostname("localhost");
        config.setPort("3306");
        config.setPersistable(false);
        return config;
    }

    /**
     * Derby Sample connections for testing purpose
     */
    public ConnectionConfig createDerbyConnection() {
        String name = "Local Derby";
        String url = "jdbc:derby://localhost:1527/travel";
        String username = "travel";
        String password = "travel";
        ConnectionConfig config = new ConnectionConfig(name, url,
                IConnectionType.DERBY_CLIENT, username, password);
        config.setHostname("localhost");
        config.setPort("1527");
        config.setDatabase("travel");
        config.setPersistable(false);
        return config;
    }

    public void persistConnections() throws FileNotFoundException, IOException {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        List<IConnectionConfig> configs = connectionManager.getConnectionConfigList();
        for (IConnectionConfig config : configs) {
            persistConnectionInfo(config);
        }
    }

    public void loadConnections() throws InvalidPropertiesFormatException,
            FileNotFoundException, IOException {
        ConnectionManager connectionManager = ConnectionManager.getInstance();

        // TODO: Remove these test connections when the plugin is finalized
        connectionManager.addConnectionConfig(createMySqlConnection());
        //connectionManager.addConnectionConfig(createDerbyConnection());

        File storageDir = getStorageDir();
        File[] connectionDirs = storageDir.listFiles();
        for (File connectionDir : connectionDirs) {
            File propertiesFile = new File(connectionDir,
                    CONNECTION_PROPERTIES_FILE);
            if (propertiesFile.exists()) {

                Properties properties = new Properties();
                properties.loadFromXML(new BufferedInputStream(
                        new FileInputStream(propertiesFile)));
                IConnectionConfig config = new ConnectionConfig();
                config.setName(connectionDir.getName());
                config.setDisplayName((String) properties.get("displayName"));
                config.setDbType(Integer.parseInt((String) properties.get("type")));
                config.setUrl((String) properties.get("url"));
                config.setDriverName((String) properties.get("driverName"));
                config.setUsername((String) properties.get("username"));
                config.setPassword((String) properties.get("password"));
                config.setHostname((String) properties.get("hostname"));
                config.setPort((String) properties.get("port"));
                config.setDatabase((String) properties.get("database"));
                config.setDatabaseFilePath((String) properties.get("databaseFilePath"));
                connectionManager.addConnectionConfig(config);

            }
        }
        initialized = true;
    }

    public void removePersistedConnectionInfo(IConnectionConfig config) {
        File connectionDir = getConnectionDir(config);
        deleteDir(connectionDir);
    }

    public void updatePersistedConnectionInfo(IConnectionConfig connectionConfig)
            throws FileNotFoundException, IOException {
        File connectionDir = getConnectionDir(connectionConfig);
        File propertiesFile = new File(connectionDir,
                CONNECTION_PROPERTIES_FILE);
        if (propertiesFile.exists()) {
            propertiesFile.delete();
        }
        persistConnectionInfo(connectionConfig);
    }

    public void persistConnectionInfo(IConnectionConfig config)
            throws FileNotFoundException, IOException {
        if (config.isPersistable()) {
            File connectionDir = getConnectionDir(config);
            File propertiesFile = new File(connectionDir,
                    CONNECTION_PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.put("displayName", config.getDisplayName());
            properties.put("type", String.valueOf(config.getDbType()));
            properties.put("url", config.getUrl());
            properties.put("driverName", config.getDriverName());
            properties.put("username", config.getUsername());
            properties.put("password", config.getPassword());
            properties.put("hostname", config.getHostname());
            properties.put("port", config.getPort());
            properties.put("database", config.getDatabase());
            properties.put("databaseFilePath", config.getDatabaseFilePath());

            properties.storeToXML(new BufferedOutputStream(
                    new FileOutputStream(propertiesFile)), config.getName());
        }
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public File getStorageDir() {
        String os = System.getProperty("os.name").toLowerCase();
        StringBuffer filepath = new StringBuffer(System.getProperty("user.home"));
        if (os.indexOf("vista") != -1) {
            filepath.append(File.separator);
            filepath.append("appdata");
            filepath.append(File.separator);
            filepath.append("locallow");
        } else if (os.startsWith("mac")) {
            filepath.append(File.separator);
            filepath.append("Library");
            filepath.append(File.separator);
            filepath.append("Preferences");
        }
        filepath.append(File.separator);
        File userhome = new File(filepath.toString());
        File baseDir = new File(userhome, ".dbools");

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir;
    }

    public File getConnectionDir(IConnectionConfig connectionConfig) {
        File storageDir = getStorageDir();
        File connectionDir = new File(storageDir + "/"
                + connectionConfig.getName());
        if (!connectionDir.exists()) {
            connectionDir.mkdirs();
        }
        return connectionDir;
    }

    public File getScriptDir(IConnectionConfig connectionConfig) {
        File connectionDir = getConnectionDir(connectionConfig);
        File scriptDir = new File(connectionDir + "/scripts");

        if (!scriptDir.exists()) {
            scriptDir.mkdirs();
        }
        return scriptDir;
    }

    public List<IScript> getScripts(IDatabaseInfo databaseInfo) {
        File scriptDir = getScriptDir(databaseInfo.getConnectionConfig());
        File[] scripts = scriptDir.listFiles();
        List<IScript> sqlScripts = new ArrayList<IScript>();
        for (File file : scripts) {
            String fileName = file.getName();
            IScript script = new Script(databaseInfo, fileName.substring(0,
                    fileName.lastIndexOf(".")));
            script.setPath(file.getAbsolutePath());
            sqlScripts.add(script);
        }
        return sqlScripts;
    }

    public File getQueryDir(IConnectionConfig connectionConfig) {
        File connectionDir = getConnectionDir(connectionConfig);
        File queryDir = new File(connectionDir + "/queries");

        if (!queryDir.exists()) {
            queryDir.mkdirs();
        }
        return queryDir;
    }

    public List<IQuery> getQueries(IDatabaseInfo databaseInfo) {
        File queryDir = getQueryDir(databaseInfo.getConnectionConfig());
        File[] queries = queryDir.listFiles();
        List<IQuery> sqlQueries = new ArrayList<IQuery>();
        for (File file : queries) {
            String fileName = file.getName();
            IQuery script = new Query(databaseInfo, fileName.substring(0,
                    fileName.lastIndexOf(".")));
            script.setPath(file.getAbsolutePath());
            sqlQueries.add(script);
        }
        return sqlQueries;
    }

    public File getSchemaDiagramDir(IConnectionConfig connectionConfig) {
        File connectionDir = getConnectionDir(connectionConfig);
        File schemaDiagramDir = new File(connectionDir + "/schemaDiagrams");

        if (!schemaDiagramDir.exists()) {
            schemaDiagramDir.mkdirs();
        }
        return schemaDiagramDir;
    }

    public List<ISchemaDiagram> getSchemaDiagrams(IDatabaseInfo databaseInfo) {
        File schemaDiagramDir = getSchemaDiagramDir(databaseInfo.getConnectionConfig());
        File[] schemaDiagramFiles = schemaDiagramDir.listFiles();
        List<ISchemaDiagram> schemaDiagrams = new ArrayList<ISchemaDiagram>();
        for (File file : schemaDiagramFiles) {
            String fileName = file.getName();
            ISchemaDiagram diagram = new SchemaDiagram(databaseInfo, fileName.substring(0, fileName.lastIndexOf(".")));
            diagram.setPath(file.getAbsolutePath());
            schemaDiagrams.add(diagram);
        }
        return schemaDiagrams;
    }

    public boolean isInitialized() {
        return initialized;
    }
}

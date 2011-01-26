package com.jfxtools.database.model;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.ITableData;
import com.jfxtools.database.api.ITableRowData;
import com.jfxtools.database.execute.ExecutionStatus;
import com.jfxtools.database.execute.SqlExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Two dimensional Table Data representation
 * 
 * - Supports pagination. - While paginating if the database supports some form
 * of LIMIT and OFFSET, then data will be fetched from the database one page at
 * a time, else the complete data returned by the query will be fetched in a
 * single fetch and then the data will be paginated. In the second approach
 * there is a possibility of OME.
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableData implements ITableData {

    private int totalRowCount = -1;
    private ResultSet resultSet;
    private String name;
    private String[] columnNames;
    private Class[] columnTypes;
    private ITable table;
    private int pageSize = 25;
    private int currentRow = 1;
    private int fetchedPage;
    // private String sqlScript;
    boolean hasLimitSupport = false;
    private ITableRowData[] tableRowData;
    private IDatabaseInfo databaseInfo;

    public TableData(Table table) {
        databaseInfo = table.getDatabaseInfo();
        hasLimitSupport = databaseInfo.getConnectionConfig().getConnectionType().isLimitSupported();
        this.name = table.getName();
        this.table = table;
    }

    public TableData(String name, ResultSet resultSet) throws SQLException {
        this.name = name;
        this.resultSet = resultSet;
        // Force no pagination
        hasLimitSupport = false;
        initializeColumnNames();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ITable getTable() {
        return table;
    }

    public void setTable(ITable table) {
        this.table = table;
    }

    public synchronized int getTotalRowCount() throws SQLException {
        if (totalRowCount == -1) {
            if (table != null) {
                Connection connection = table.getDatabaseInfo().getConnection();
                Statement stmt = connection.createStatement();
                ISqlHelper sqlHelper = databaseInfo.getConnectionConfig().getConnectionType().getSqlHelper();

                ResultSet rowCountResultSet = stmt.executeQuery(sqlHelper.generateRowCountStatement(table));
                rowCountResultSet.next();
                totalRowCount = rowCountResultSet.getInt(1);
            } else {
                totalRowCount = getTableData().length;
            }
        }
        return totalRowCount;
    }

    public synchronized ITableRowData[] getTableData() throws SQLException {
        if (tableRowData == null) {
            resultSet = createResultSet(-1, -1);
            if (resultSet != null) {
                initializeColumnNames();
                List<ITableRowData> tableRowDataList = new ArrayList<ITableRowData>();
                while (resultSet.next()) {
                    tableRowDataList.add(getRowData());
                }
                tableRowData = (ITableRowData[]) tableRowDataList.toArray(new TableRowData[]{});
            } else {
                columnNames = new String[0];
                tableRowData = new ITableRowData[0];
            }
        }
        return tableRowData;
    }

    public synchronized ResultSet createResultSet(int start, int limit) throws SQLException {
        if ((databaseInfo != null) && (table != null)){
            ISqlHelper sqlHelper = databaseInfo.getConnectionConfig().getConnectionType().getSqlHelper();
            String selectScript = sqlHelper.generateSelectStatement(table,
                    start, limit);
            ExecutionStatus execStatus = new SqlExecutor(databaseInfo,
                    selectScript).execute();
            if (execStatus.hasResults()) {
                return execStatus.getResults().get(0);
            }else{
                if (execStatus.hasExceptions()) {
                    throw new SQLException(execStatus.getExceptions().get(0).getLocalizedMessage());
                }
            }
        }
        return null;
    }

    public boolean supportsPagination() {
        return true;
    }

    public int getTotalPages() throws SQLException {
        return getTotalRowCount() / pageSize + (getTotalRowCount() % pageSize > 0 ? 1 : 0);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        firstPage();
    }

    public int getCurrentPage() {
        return currentRow / pageSize + 1;
    }

    public void firstPage() {
        currentRow = 1;
    }

    public void lastPage() throws SQLException {
        int rem = getTotalRowCount() % pageSize;
        currentRow = getTotalRowCount() - (rem == 0 ? pageSize : rem) + 1;
    }

    public boolean nextPage() throws SQLException {
        if ((currentRow + pageSize) > (getTotalRowCount() - 1)) {
            return false;
        } else {
            currentRow += pageSize;
            return true;
        }
    }

    public boolean previousPage() throws SQLException {
        if ((currentRow - pageSize) < 0) {
            return false;
        } else {
            currentRow -= pageSize;
            return true;
        }
    }

    public synchronized ITableRowData[] getPageData() throws SQLException {
        if (hasLimitSupport) {
            if (fetchedPage == getCurrentPage()) {
                // Do not fetch if we already fetched and in the same page
                return tableRowData;
            }

            fetchedPage = getCurrentPage();

            resultSet = createResultSet(currentRow - 1, pageSize);
            if (resultSet != null) {
                initializeColumnNames();
                List<ITableRowData> tableRowDataList = new ArrayList<ITableRowData>();
                while (resultSet.next()) {
                    tableRowDataList.add(getRowData());
                }
                tableRowData = (ITableRowData[]) tableRowDataList.toArray(new TableRowData[]{});
            } else {
                columnNames = new String[0];
                tableRowData =  new ITableRowData[0];
            }
            return tableRowData;
        } else {
            ITableRowData[] tableData = getTableData();

            int start = currentRow - 1;
            int end = currentRow + (pageSize < getTotalRowCount() ? pageSize
                    : getTotalRowCount()) - 1;
            if (end > getTotalRowCount()) {
                end = getTotalRowCount();
            }
            ITableRowData[] pageData = new ITableRowData[end - start];
            for (int i = start; i < end; i++) {
                pageData[i - start] = tableData[i];
            }
            return pageData;
        }
    }

    public boolean nextRow() throws SQLException {
        return resultSet.next();
    }

    public ITableRowData getRowData() throws SQLException {
        ITableRowData tableRowData = new TableRowData(table);
        int columnCount = resultSet.getMetaData().getColumnCount();
        tableRowData.setColumnCount(columnCount);
        for (int i = 0; i < columnCount; i++) {
            String name = resultSet.getMetaData().getColumnLabel(i + 1);
            String javaType = resultSet.getMetaData().getColumnClassName(i + 1);
            int sqlType = resultSet.getMetaData().getColumnType(i + 1);
            IColumn column = null;
            if (table != null) {
                column = table.findColumnByName(name, true);
            }
            TableColumnData tableColumnData = new TableColumnData(column, name,
                    javaType, sqlType, getValue(i));
            if (column != null) {
                tableColumnData.setReadOnly(resultSet.getMetaData().isReadOnly(
                        i + 1));
                tableColumnData.setAutoIncrement(resultSet.getMetaData().isAutoIncrement(i + 1));
            }
            tableRowData.addTableColumnData(tableColumnData);
        }
        return tableRowData;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private void initializeColumnNames() throws SQLException {
        if (resultSet != null){
            ResultSetMetaData metaData = resultSet.getMetaData();
            columnNames = new String[metaData.getColumnCount()];
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columnNames[i] = metaData.getColumnLabel(i + 1);
            }
        }
    }

    public synchronized String[] getColumnNames() throws SQLException {
        if (columnNames == null) {
            if (resultSet == null) {
                resultSet = createResultSet(1, 1);
            }
            initializeColumnNames();
        }
        return columnNames;
    }

    private void initializeColumnTypes() throws SQLException {
        if (resultSet != null){
            ResultSetMetaData metaData = resultSet.getMetaData();
            columnTypes = new Class[metaData.getColumnCount()];
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                try {
                    columnTypes[i] = Class.forName(metaData.getColumnClassName(i + 1));
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public synchronized Class[] getColumnTypes() throws SQLException {
        if (columnTypes == null) {
            if (resultSet == null) {
                resultSet = createResultSet(1, 1);
            }
            initializeColumnTypes();
        }
        return columnTypes;
    }

    public Object getValue(int column) throws SQLException {

        Object obj = resultSet.getObject(column + 1);
        if (resultSet.getMetaData().getColumnClassName(column + 1).equals(
                "oracle.sql.TIMESTAMP")) { 
            try {
                // If we have an Oracle TIMESTAMP object, convert to standard
                // JDBC class
                // Use reflection to avoid runtime dependency
                Class<?> c = Class.forName("oracle.sql.TIMESTAMP"); 
                if (obj.getClass().getName().equals("oracle.sql.TIMESTAMP")) { 
                    Method m = c.getMethod("toJdbc", (Class[]) null); 
                    Object newObj = m.invoke(obj, (Object[]) null);
                    return newObj;
                } else {
                    return obj;
                }
            } catch (Exception e) {
                // ClassNotFoundException, NoSuchMethodException,
                // IllegalAccessException - all Ignored and original object
                // returned
                return obj;
            }

        } else {
            return obj;
        }
    }

    public ITableRowData createNewTableRowData() throws SQLException,
            ClassNotFoundException {
        ITableRowData tableRowData = new TableRowData(table);
        int columnCount;

        columnCount = resultSet.getMetaData().getColumnCount();
        tableRowData.setColumnCount(columnCount);
        for (int i = 0; i < columnCount; i++) {
            String name = resultSet.getMetaData().getColumnLabel(i + 1);
            String className = resultSet.getMetaData().getColumnClassName(i + 1);
            String javaType = resultSet.getMetaData().getColumnClassName(i + 1);
            int sqlType = resultSet.getMetaData().getColumnType(i + 1);
            IColumn column = table.findColumnByName(name, true);
            TableColumnData tableColumnData = new TableColumnData(column, name,
                    javaType, sqlType, manufacturePlaceholder(className));
            if (column != null) {
                tableColumnData.setReadOnly(resultSet.getMetaData().isReadOnly(
                        i + 1));
                tableColumnData.setAutoIncrement(resultSet.getMetaData().isAutoIncrement(i + 1));
            }
            tableRowData.addTableColumnData(tableColumnData);

        }

        return tableRowData;
    }

    public ExecutionStatus appendTableRow(ITableRowData tableRowData) throws SQLException {
        ISqlHelper sqlHelper = databaseInfo.getConnectionConfig().getConnectionType().getSqlHelper();
        String sqlScript = sqlHelper.generateTableRowInsertStatement(tableRowData);
        ExecutionStatus execStatus = new SqlExecutor(table.getDatabaseInfo(),
                sqlScript).execute();
        return execStatus;
    }

    public ExecutionStatus deleteTableRow(ITableRowData tableRowData) throws SQLException {
        ISqlHelper sqlHelper = databaseInfo.getConnectionConfig().getConnectionType().getSqlHelper();
        String sqlScript = sqlHelper.generateTableRowDeleteStatement(tableRowData);
        ExecutionStatus execStatus = new SqlExecutor(table.getDatabaseInfo(),
                sqlScript).execute();
        return execStatus;
    }

    public ExecutionStatus updateTableRow(ITableRowData currentRowData,
            ITableRowData updateRowData) throws SQLException {
        ISqlHelper sqlHelper = databaseInfo.getConnectionConfig().getConnectionType().getSqlHelper();
        String sqlScript = sqlHelper.generateTableRowUpdateStatement(
                currentRowData, updateRowData);
        ExecutionStatus execStatus = new SqlExecutor(table.getDatabaseInfo(),
                sqlScript).execute();
        return execStatus;
    }

    private Object manufacturePlaceholder(String className) {
        if (className.equals("java.sql.Date")) { // NOI18N 
            return new Date(new java.util.Date().getTime());
        } else if (className.equals("java.sql.Time")) { // NOI18N 
            return new Time(new java.util.Date().getTime());
        } else if (className.equals("java.sql.Timestamp")) { // NOI18N 
            return new Timestamp(new java.util.Date().getTime());
        }
        return null;
    }

    public void close() throws SQLException {
        resultSet.close();
    }
}

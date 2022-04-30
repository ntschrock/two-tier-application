import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;
import com.mysql.cj.jdbc.MysqlDataSource;

public class ResultTable extends AbstractTableModel {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	private String checkQuery;
	public boolean connectedToDataBase = false;
	private Connection oplog_connect;
	private Statement oplog_state;
	private int oplog_result;

	public ResultTable(Connection incomingConnection, String Query) throws SQLException, ClassNotFoundException {
		connection = incomingConnection;
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		connectedToDataBase = true;
	}

	public Class getColumClass(int column) throws IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}

		try {
			String className = metaData.getColumnClassName(column + 1);
			return Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Object.class;
	}

	public int getColumnCount() throws IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}
		try {
			return metaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getColumnName(int column) throws IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}
		try {
			return metaData.getColumnName(column + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public int getRowCount() throws IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}

		return numberOfRows;
	}

	public Object getValueAt(int row, int column) throws IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}
		try {
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return "";
	}

	public void setQuery(String query) throws SQLException, IllegalStateException {
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}

		resultSet = statement.executeQuery(query);
		metaData = resultSet.getMetaData();
		resultSet.last();
		numberOfRows = resultSet.getRow();

		oplog_connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/operationslog?useTimezone=true&serverTimezone=UTC", "root", "root");
		oplog_state = oplog_connect.createStatement();
		oplog_result = oplog_state.executeUpdate("update operationscount set num_queries = num_queries + 1;");
		oplog_connect.close();

		fireTableStructureChanged();
	}

	//
	public void setUpdate(String query) throws SQLException, IllegalStateException {
		int res;
		if (!connectedToDataBase) {
			throw new IllegalStateException("Not Connected to Database");
		}

		res = statement.executeUpdate(query);

		oplog_connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/operationslog?useTimezone=true&serverTimezone=UTC", "root", "root");
		oplog_state = oplog_connect.createStatement();
		oplog_result = oplog_state.executeUpdate("update operationscount set num_updates = num_updates + 1;");
		oplog_connect.close();

		fireTableStructureChanged();
	}

	//disconnects from the database
	public void disconnectFromDatabase() {
		if (!connectedToDataBase) {
			return;
		} else {
			try {
				statement.close();
				connection.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			} finally {
				connectedToDataBase = false;
			}
		}
	}
}
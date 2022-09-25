package me.felipe.clans.org.database;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLCore implements CoreDB {

    private Connection connection;
    private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    private Logger logger;

    public MySQLCore(String host, int port, String user, String password, String database, Plugin plugin) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.logger = plugin.getLogger();
        initialize();
    }

    private void initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true", user, password);
        } catch (ClassNotFoundException e) {
            logger.severe("ClassNotFoundException! " + e.getMessage());
        } catch (SQLException e) {
            logger.severe("SQLException! " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initialize();
            }
        } catch (SQLException exception) {
            initialize();
        }
        return connection;
    }

    @Override
    public boolean checkConnection() {
        return getConnection() != null;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            logger.severe("Failed to close data base connection! " + exception.getMessage());
        }
    }

    @Override
    public ResultSet select(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            logger.severe("Error at SQL Query: " + ex.getMessage());
            logger.severe("Query: " + query);
        }
        return null;
    }

    @Override
    public void insert(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL INSERT Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    @Override
    public void update(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL UPDATE Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    @Override
    public void delete(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL DELETE Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    @Override
    public Boolean execute(String query) {
        try {
            getConnection().createStatement().execute(query);
            return true;
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            logger.severe("Query: " + query);
            return false;
        }
    }

    @Override
    public Boolean existsTable(String table) {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            return tables.next();
        } catch (SQLException e) {
            logger.severe("Failed to check if table " + table + " exists: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean existsColumn(String table, String column) {
        try {
            ResultSet col = getConnection().getMetaData().getColumns(null, null, table, column);
            return col.next();
        } catch (Exception e) {
            logger.severe("Failed to check if column " + column + " exists in table " + table + " : " + e.getMessage());
            return false;
        }
    }
}

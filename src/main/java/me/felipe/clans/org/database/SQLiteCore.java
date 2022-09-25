package me.felipe.clans.org.database;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteCore implements CoreDB {

    private Plugin plugin;

    private Logger logger;
    private Connection connection;
    private String dbLocation;
    private String dbName;
    private File file;

    /**
     * @param dbLocation
     */
    public SQLiteCore(String dbLocation, Plugin plugin) {
        this.dbName = "pluginDatabase";
        this.dbLocation = dbLocation;
        this.logger = plugin.getLogger();
        this.plugin = plugin;
        initialize();
    }

    private void initialize() {
        if (file == null) {

            File dbFolder = new File(dbLocation);

            if (dbName.contains("/") || dbName.contains("\\") || dbName.endsWith(".db")) {
                logger.severe("The database name can not contain: /, \\, or .db");
                return;
            }

            if (!dbFolder.exists()) {
                dbFolder.mkdir();
            }

            file = new File(dbFolder.getAbsolutePath() + File.separator + dbName + ".db");

        }

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

        } catch (SQLException ex) {
            logger.severe("SQLite exception on initialize " + ex);
        } catch (ClassNotFoundException ex) {
            logger.severe("You need the SQLite library " + ex);
        }
    }

    /**
     * @return connection
     */
    @Override
    public Connection getConnection() {
        if (connection == null) {
            initialize();
        }

        return connection;
    }

    /**
     * @return whether connection can be established
     */
    @Override
    public boolean checkConnection() {
        return getConnection() != null;
    }

    /**
     * Close connection
     */
    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            logger.severe("Failed to close database connection! " + e.getMessage());
        }
    }

    /**
     * Execute a select statement
     *
     * @param query
     * @return
     */
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

    /**
     * Execute an insert statement
     *
     * @param query
     */
    @Override
    public void insert(String query) {
        try {
            getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL INSERT Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    /**
     * Execute an update statement
     *
     * @param query
     */
    @Override
    public void update(String query) {
        try {
            getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL UPDATE Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    /**
     * Execute a delete statement
     *
     * @param query
     */
    @Override
    public void delete(String query) {
        try {
            getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                logger.severe("Error at SQL DELETE Query: " + ex);
                logger.severe("Query: " + query);
            }
        }
    }

    /**
     * Execute a statement
     *
     * @param query
     * @return
     */
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

    /**
     * Check whether a table exists
     *
     * @param table
     * @return
     */
    public Boolean existsTable(String table) {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            return tables.next();
        } catch (SQLException e) {
            logger.severe("Failed to check if table " + table + " exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check whether a column exists
     *
     * @param table
     * @param column
     * @return
     */
    public Boolean existsColumn(String table, String column) {
        try {
            ResultSet col = getConnection().getMetaData().getColumns(null, null, table, column);
            return col.next();
        } catch (Exception e) {
            logger.severe("Failed to check if column " + column + " exists in table " + table + " : " + e.getMessage());
            return false;
        }
    }

    private void executeAsync(String query, String sqlType) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.createStatement().executeUpdate(query);
                    }
                } catch (SQLException ex) {
                    if (!ex.toString().contains("not return ResultSet")) {
                        logger.severe("[Thread] Error at SQL " + sqlType + " Query: " + ex);
                        logger.severe("[Thread] Query: " + query);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);

    }
}

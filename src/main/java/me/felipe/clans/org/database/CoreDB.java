package me.felipe.clans.org.database;

import java.sql.Connection;
import java.sql.ResultSet;

public interface CoreDB {

    Connection getConnection();

    boolean checkConnection();

    void close();

    ResultSet select(String query);

    void insert(String query);

    void update(String query);

    void delete(String query);

    Boolean execute(String query);

    Boolean existsTable(String table);

    Boolean existsColumn(String tabell, String colum);
}

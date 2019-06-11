package com.wolf.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DatabaseHandler {
    private Connection con;
    private String username;
    private String password;
    private String url;

    public Map<String, String> readProperties(String filename) throws IOException {
        FileInputStream fstream = new FileInputStream(filename);
        Properties props = new Properties();
        props.load(fstream);

        this.url = (String) props.getProperty("url");
        this.username = (String) props.getProperty("username");
        this.password = (String) props.getProperty("password");

        Map<String, String> properties = new HashMap<>();
        properties.put("url", this.url);
        properties.put("username", this.username);
        properties.put("password", this.password);
        return properties;
    }

    public Map<String, String> setDetails(Map<String, String> details) {
        if (details.containsKey("url")) {
            this.url = details.get("url");
        }
        if (details.containsKey("username")) {
            this.username = details.get("username");
        }
        if (details.containsKey("password")) {
            this.password = details.get("password");
        }
        return details;
    }

    public boolean doConnect() {
        try {
            String driverName = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://" + this.url;
            Class.forName(driverName);
            con = DriverManager.getConnection(url, this.username, this.password);
            System.out.println(con != null ? "connection established" : "connection failed");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("There is no respective jars : "
                    + cnfe.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            return !(con.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public ResultSet doStatement(String sqlStatement) throws SQLException {
        System.err.println("This method is deprecated! Please start using: doQuery(\"Statement\"); or doQuery(\"Statement\", List<Object> Items);");
        return this.doQuery(sqlStatement);
    }
    public ResultSet doQuery(String sqlStatement) throws SQLException {
        return this.doQuery(sqlStatement, new ArrayList<>());
    }
    public ResultSet doQuery(String sqlStatement, List<Object> inputs) throws SQLException {

        PreparedStatement pd = statementFinalised(sqlStatement, inputs);
        return pd.executeQuery();
    }

    @Deprecated
    public int doStatement(String sqlStatement, String updateType) throws SQLException {
        System.err.println("This method is deprecated! Please start using: doUpdate(\"Statement\"); or doUpdate(\"Statement\", List<Object> Items);");
        return doUpdate(sqlStatement);
    }
    public int doUpdate(String sqlStatement) throws SQLException {
        return doUpdate(sqlStatement, new ArrayList<>());
    }
    public int doUpdate(String sqlStatement, List<Object> items) throws SQLException {
        PreparedStatement pd = statementFinalised(sqlStatement, items);
        return pd.executeUpdate();
    }

    public boolean setAutoCommit(boolean bStatus) throws SQLException {
        this.con.setAutoCommit(bStatus);
        return this.con.getAutoCommit() == bStatus;
    }

    public void commitQueries() throws SQLException {
        this.con.commit();
    }

    public void rollback() throws SQLException {
        this.con.rollback();
    }

    private PreparedStatement statementFinalised(String sqlStatement, List<Object> inputs) throws SQLException {
        PreparedStatement pd = con.prepareStatement(sqlStatement);
        for (Object obj : inputs) {
            int pos = inputs.indexOf(obj) + 1;
            switch (obj.getClass().getSimpleName().toLowerCase()) {
                case "int":
                case "integer":
                    pd.setInt(pos, (int) obj);
                    break;
                case "double":
                    pd.setDouble(pos, (double) obj);
                    break;
                case "bigdecimal":
                    pd.setBigDecimal(pos, (BigDecimal) obj);
                    break;
                case "boolean":
                case "bool":
                    pd.setBoolean(pos, (Boolean) obj);
                    break;
                case "date":
                    pd.setDate(pos, (Date) obj);
                    break;
                case "list":
                case "arraylist":
                    pd.setArray(pos, (Array) obj);
                    break;
                case "string":
                default:
                    pd.setString( pos, obj.toString());
                    break;

            }
        }
        return pd;
    }

    public boolean doClose() {
        try {
            con = null;
            System.out.println("connection closed");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

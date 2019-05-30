package com.wolf.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseHandlerTest {

    DatabaseHandler dbh = null;
    String filename = "props.props";
    boolean testerUserCreated = false;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        dbh = new DatabaseHandler();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        dbh = null;
    }

    @org.junit.jupiter.api.Test
    void readProperties() throws IOException {
        Map<String, String> result = dbh.readProperties(filename);
        Map<String, String> shouldbe = new HashMap<>();
        shouldbe.put("url", "165.227.20.110/Playground");
        shouldbe.put("username", "airport");
        shouldbe.put("password", "airportdbpass");
        assertEquals(shouldbe, result);
    }

    @org.junit.jupiter.api.Test
    void setDetails() {
        Map<String, String> shouldbe = new HashMap<>();
        shouldbe.put("url", "165.227.20.110/Playground");
        shouldbe.put("username", "airport");
        shouldbe.put("password", "airportdbpass");
        assertEquals(shouldbe, dbh.setDetails(shouldbe));
    }

    @org.junit.jupiter.api.Test
    void doConnect() throws IOException {
        dbh.readProperties(filename);
        assertTrue(dbh.doConnect());
    }

    @org.junit.jupiter.api.Test
    void doStatement() throws SQLException, IOException {
        dbh.readProperties(filename);
        dbh.doConnect();
        assertNotNull(dbh.doStatement("SELECT * FROM Client;"));
    }

    @org.junit.jupiter.api.Test
    void doQuery() throws IOException, SQLException {
        dbh.readProperties(filename);
        dbh.doConnect();
        assertNotNull(dbh.doQuery("SELECT * FROM Client;"));
    }

    @org.junit.jupiter.api.Test
    void doQuery1() throws IOException, SQLException {
        dbh.readProperties(filename);
        dbh.doConnect();
        List<Object> items = new ArrayList<>();
        items.add(2);
        int count = 0;
        ResultSet rs = dbh.doQuery("SELECT * FROM `Transactions` WHERE `toAccountId` = ?;", items);
        while(rs.next()) {
            count++;
        }
        assertEquals(2, count);
    }

    @org.junit.jupiter.api.Test
    void doStatement1() throws IOException, SQLException {
        dbh.readProperties(filename);
        dbh.doConnect();
        assertTrue(dbh.doStatement("INSERT INTO Client (name, username, password) VALUES (\"TESTER\", \"TESTER\", \"TESTER\");", "update") > 0);
        testerUserCreated = true;
    }

    @org.junit.jupiter.api.Test
    void doUpdate() throws SQLException, IOException {
        // This test will check if the last test was run (creating a new user).
        dbh.readProperties(filename);
        dbh.doConnect();
        if (testerUserCreated) {
            assertEquals(1, dbh.doUpdate("UPDATE Client SET password=\"pword\" WHERE id = 7"));
        } else {
            assertEquals(1, dbh.doUpdate("INSERT INTO Client (name, username, password) VALUES (\"TESTER\", \"TESTER\", \"TESTER\");"));
            testerUserCreated = true;
        }
    }

    @org.junit.jupiter.api.Test
    void doUpdate1() throws IOException, SQLException {
        dbh.readProperties(filename);
        dbh.doConnect();
        // We are just going to assume that by now the test user has been added.
        List<Object> items = new ArrayList<>();
        items.add("TESTER");
        assertTrue(dbh.doUpdate("DELETE FROM Client WHERE name = ?", items) > 0);
    }

    @org.junit.jupiter.api.Test
    void doClose() throws IOException {
        dbh.readProperties(filename);
        dbh.doConnect();
        assertTrue(dbh.doClose());
    }
}
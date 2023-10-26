package com.xyzcorp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.*;

@Testcontainers
public class StudentDAOIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> container =
        new PostgreSQLContainer<>
            (DockerImageName.parse("postgres:14.5")).withDatabaseName("school");
    private static PGConnectionPoolDataSource source;
    public static StudentDAO studentDAO;

    @Test
    void testInsertRecord() throws SQLException {
        Student student = new Student("Chris", "Smith", "1011");
        System.out.println(studentDAO.persistStudent(student));
    }

    @BeforeAll
    static void beforeAll() {
        source = new PGConnectionPoolDataSource();
        source.setURL(container.getJdbcUrl());
        source.setUser(container.getUsername());
        source.setPassword(container.getPassword());
        source.setDatabaseName(container.getDatabaseName());
        studentDAO = new StudentDAO(source);
        studentDAO.createTable();
    }
}

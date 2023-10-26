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

    @Test
    void testInsertRecord() throws SQLException {
        Connection connection = source.getConnection();
        PreparedStatement preparedStatement =
            connection.prepareStatement
                ("INSERT INTO REGISTRATION (FIRSTNAME, LASTNAME, STUDENTID) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, "Chris");
        preparedStatement.setString(2, "Smith");
        preparedStatement.setString(3, "1001");

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        System.out.println(generatedKeys.getLong(1));
    }

    @BeforeAll
    static void beforeAll() {
        source = new PGConnectionPoolDataSource();
        source.setURL(container.getJdbcUrl());
        source.setUser(container.getUsername());
        source.setPassword(container.getPassword());
        source.setDatabaseName(container.getDatabaseName());

        try {
            var createTableSQL = """
                CREATE TABLE REGISTRATION (
                  ID        SERIAL      PRIMARY KEY,
                  FIRSTNAME TEXT        NOT NULL,
                  LASTNAME  TEXT        NOT NULL,
                  STUDENTID VARCHAR(20) NOT NULL
                );
                """;

            Connection connection = source.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
            boolean execute = preparedStatement.execute();
            System.out.printf("Table create? %b%n", execute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

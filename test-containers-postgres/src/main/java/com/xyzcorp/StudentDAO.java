package com.xyzcorp;

import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.*;

public class StudentDAO {
    private final PGConnectionPoolDataSource source;

    public StudentDAO(PGConnectionPoolDataSource source) {
        this.source = source;
    }

    public void createTable() {
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

    Long persistStudent(Student student) throws SQLException {
        Connection connection = source.getConnection();
        PreparedStatement preparedStatement =
            connection.prepareStatement
                ("INSERT INTO REGISTRATION (FIRSTNAME, LASTNAME, STUDENTID) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, student.firstName());
        preparedStatement.setString(2, student.lastName());
        preparedStatement.setString(3, student.studentID());
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getLong(1);
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}

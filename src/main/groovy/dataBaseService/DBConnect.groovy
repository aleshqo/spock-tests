package dataBaseService

import groovy.sql.Sql

class DBConnect { // todo: перенести в проперти и профайл

    private static final String DB_URL = 'localhost'
    private static final String DB_PORT = 9900
    private static final String DB_NAME = "test_installment"
    private static final String DB_USER = "postgres"
    private static final String DB_PASSWORD = "postgres"
    private static final String DB_PARAMS = "slmode=disable"

    private static final String jdbcUrl = "jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}?${DB_PARAMS}"
    private static final String dbDriver = "org.postgresql.Driver"

    static Sql getConnect() {
        Sql.newInstance(jdbcUrl, DB_USER, DB_PASSWORD, dbDriver)
    }
}

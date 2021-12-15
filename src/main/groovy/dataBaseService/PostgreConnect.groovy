package dataBaseService

import groovy.sql.Sql

import static dataBaseService.DBConnect.getConnect

class PostgreConnect {

    private Sql sqlConnect
    private String tableName
    private String query
    private def result

    PostgreConnect(String tableName) {
        this.tableName = tableName
    }

    def selectOne(Map params) {
        sqlConnect = getConnect()
        StringJoiner sj = new StringJoiner(" AND ")
        params.forEach({ k, v -> sj.add("$k = '$v'") })
        query = "SELECT * FROM ${tableName} WHERE ${sj.toString()}"
        result = sqlConnect.firstRow(query)
        sqlConnect.close()
        result
    }

    def selectOne(String query) {
        sqlConnect = getConnect()
        result = sqlConnect.firstRow(query)
        sqlConnect.close()
        result
    }

    def selectAll(Map params) {
        sqlConnect = getConnect()
        StringJoiner sj = new StringJoiner(" AND ")
        params.forEach({ k, v -> sj.add("$k = '$v'") })
        query = "SELECT * FROM ${tableName} WHERE ${sj.toString()}"
        result = sqlConnect.rows(query) as List<HashMap>
        sqlConnect.close()
        result
    }

    def selectAll(String query) {
        sqlConnect = getConnect()
        result = sqlConnect.rows(query)
        sqlConnect.close()
        result
    }

    def update(Map paramsToSet, Map paramsToWhere) {
        sqlConnect = getConnect()
        StringJoiner sjToSet = new StringJoiner(" AND ")
        StringJoiner sjWhere = new StringJoiner(" AND ")
        paramsToSet.forEach({ k, v -> sjToSet.add("$k = '$v'") })
        paramsToWhere.forEach({ k, v -> sjWhere.add("$k = '$v'") })
        query = "UPDATE ${tableName} set ${sjToSet.toString()} where ${sjWhere.toString()}"
        sqlConnect.executeUpdate(query)
        sqlConnect.close()
    }

    def deleteOne(Map paramsToDelete, Map paramsToWhere) {
        sqlConnect = getConnect()
        StringJoiner sjToDelete = new StringJoiner(" AND ")
        StringJoiner sjToToWhere = new StringJoiner(" AND ")
        paramsToDelete.forEach({ k, v -> sjToDelete.add("$k = '$v'") })
        paramsToWhere.forEach({ k, v -> sjToToWhere.add("$k = '$v'") })
        query = "DELETE FROM ${tableName} WHERE ${sjToDelete.toString()}"
        sqlConnect.execute(query)
        sqlConnect.close()
    }

    def deleteAll() {
        sqlConnect = getConnect()
        query = "DELETE FROM ${tableName}"
        sqlConnect.execute(query)
        sqlConnect.close()
    }
}

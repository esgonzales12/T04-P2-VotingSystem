package org.teamfour.dao;

public class QueryBuilder {
    private final StringBuilder query;
    private String tableName;

    public QueryBuilder() {
        this.query = new StringBuilder();
    }

    public QueryBuilder selectFrom(String tableName) {
        query.append("SELECT * FROM ").append(tableName).append(" ");
        this.tableName = tableName;
        return this;
    }

    public QueryBuilder deleteFrom(String tableName) {
        query.append("DELETE FROM ").append(tableName).append(" ");
        this.tableName = tableName;
        return this;
    }

    public QueryBuilder insertInto(String tableName) {
        query.append("INSERT INTO ").append(tableName).append(" ");
        return this;
    }

    public QueryBuilder values(String values) {
        query.append("VALUES ").append(values);
        return this;
    }

    public QueryBuilder values(String insertCols, String values) {
        query.append(insertCols).append(" ").append("VALUES ").append(values);
        return this;
    }

    public QueryBuilder where(String columnName, String eqValue) {
        query.append("WHERE ")
             .append(tableName)
             .append(".")
             .append(columnName)
             .append(" = ")
             .append(String.format("'%s'", eqValue));
        return this;
    }

    public String build() {
        return query.append(";").toString().trim();
    }
}

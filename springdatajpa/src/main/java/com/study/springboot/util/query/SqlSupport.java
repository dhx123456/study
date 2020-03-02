package com.study.springboot.util.query;


import com.study.springboot.util.query.datasource.DatabaseType;

public class SqlSupport {

    public static String getLikeColumn(String queryTerm) {
        if (queryTerm == null) {
            queryTerm = "";
        }
        return "%" + queryTerm + "%";
    }

    public static String getLikeColumnRight(String queryTerm) {
        if (queryTerm == null) {
            queryTerm = "";
        }
        return queryTerm + "%";
    }

    public static String generatePagingSql(
            DatabaseType databaseType, String sourceSelect, int pageNumber, int pageSize) {

        StringBuilder sql = new StringBuilder();
        switch (databaseType) {
            case MYSQL:
                sql.append(sourceSelect)
                        .append(" LIMIT ")
                        .append(pageNumber * pageSize)
                        .append(",")
                        .append(pageSize);
                break;
            case ORACLE:
                sql.append("SELECT * FROM ( ")
                        .append("SELECT pagequery.*,ROWNUM as TMP_ROW_NUM ")
                        .append("FROM ( ")
                        .append(sourceSelect)
                        .append(" ) pagequery")
                        .append(" ) where TMP_ROW_NUM>=" + pageNumber * pageSize + " ")
                        .append("and TMP_ROW_NUM<" + (pageNumber + 1) * pageSize);
                break;
            case POSTGRES:
                sql.append(sourceSelect)
                        .append(" LIMIT ")
                        .append(pageSize)
                        .append(" OFFSET ")
                        .append(pageNumber * pageSize);
                break;
            default:
                throw new IllegalArgumentException(
                        "Only support MySQL Oracle and Postgres can't support this one :"
                                + databaseType.getProductName());
        }
        return sql.toString();
    }

    public static String generateOraclePagingSql(String sourceSelect, int pageNumber, int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ")
                .append("SELECT pagequery.*,ROWNUM as TMP_ROW_NUM ")
                .append("FROM ( ")
                .append(sourceSelect)
                .append(" ) pagequery")
                .append(" ) where TMP_ROW_NUM>=" + pageNumber * pageSize + " ")
                .append("and TMP_ROW_NUM<" + (pageNumber + 1) * pageSize);
        return sql.toString();
    }
}

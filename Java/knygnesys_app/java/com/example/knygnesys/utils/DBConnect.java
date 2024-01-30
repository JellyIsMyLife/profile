package com.example.knygnesys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
    // TODO gal tiesiog variable Connection padaryti, kad nereikėtu kaskart iš naujo jungtis prie DB?
    //private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    //private static final String DB_URL = "jdbc:mariadb://localhost:3306/knygnesys";

    // Database credentials
    //private static final String USER = "root";
    //private static final String PASS = "password";
    //private static final String DB_URL = "jdbc:mariadb://knygnesys.clop3vhth17v.eu-north-1.rds.amazonaws.com:3306/knygnesys";
    //String jdbcUrl =                     "jdbc:mariadb://knygnesys.clop3vhth17v.eu-north-1.rds.amazonaws.com:3306/knygnesys"
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://knygos.clop3vhth17v.eu-north-1.rds.amazonaws.com:3306/knygnesys";

    // Database credentials
    private static final String USER = "admin";
    private static final String PASS = "knygos123";
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public static List<String> resultSetToList(ResultSet rs) throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                sb.append(rs.getString(i));
                if (i != columnCount) {
                    sb.append(", ");
                }
            }
            list.add(sb.toString());
        }

        return list;
    }

}

package com.example.knygnesys.client;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetBranchIdRunnable implements Runnable {
    private String itemId;
    private int branchId;

    public GetBranchIdRunnable(String itemId) {
        this.itemId = itemId;
    }

    public int getBranchId() {
        return branchId;
    }

    @Override
    public void run() {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            if (connection != null) {
                String query = "SELECT fk_Branch FROM books WHERE id = " + itemId;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    branchId = resultSet.getInt("fk_Branch");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

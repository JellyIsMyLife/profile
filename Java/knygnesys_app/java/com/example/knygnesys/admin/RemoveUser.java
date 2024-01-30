package com.example.knygnesys.admin;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveUser implements Runnable {

    private int userID;

    public RemoveUser(int id)
    {
        userID = id;
    }

    // TODO if orders are assigned to user, you can't really delete user.
    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to delete
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM users WHERE id = " + userID;
            statement.execute(sql);
            System.out.println("executed " + sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

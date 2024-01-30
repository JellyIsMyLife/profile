package com.example.knygnesys.admin;

import com.example.knygnesys.utils.DBConnect;
import com.example.knygnesys.utils.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GetUser implements Runnable {
    public CountDownLatch cdl;
    final int userID;
    public int result;
    public User user;

    public GetUser(CountDownLatch latch, int uID)
    {
        cdl = latch;
        this.userID = uID;
    }

    @Override
    public void run() {
        User temp = null;
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM users WHERE id = " + userID;

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of rows
            while (resultSet2.next()) {
                User user = new User(resultSet2.getInt("id"), resultSet2.getString("name"), resultSet2.getString("surname"),
                        resultSet2.getString("email"), resultSet2.getString("username"), resultSet2.getString("password"),
                        resultSet2.getInt("readers_id"), resultSet2.getString("address"), resultSet2.getInt("points"),
                        resultSet2.getInt("fk_Role"));
                temp = user;
            }
            conn.close();
            result = 1;
            this.user = temp;
            cdl.countDown();
        } catch (SQLException e) {
            result = 2;
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}
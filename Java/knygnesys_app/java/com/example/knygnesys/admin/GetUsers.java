package com.example.knygnesys.admin;

import com.example.knygnesys.utils.DBConnect;
import com.example.knygnesys.utils.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GetUsers implements Runnable {
    public CountDownLatch cdl;
    public ArrayList<User> result;

    public GetUsers(CountDownLatch latch)
    {
        cdl = latch;
    }

    @Override
    public void run() {
        ArrayList<User> userlist = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM users WHERE fk_Role != 1";

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of rows
            while (resultSet2.next()) {
                User user = new User(resultSet2.getInt("id"), resultSet2.getString("name"), resultSet2.getString("surname"),
                        resultSet2.getString("email"), resultSet2.getString("username"), resultSet2.getString("password"),
                        resultSet2.getInt("readers_id"), resultSet2.getString("address"), resultSet2.getInt("points"),
                        resultSet2.getInt("fk_Role"));
                userlist.add(user);
            }
            conn.close();
            result = userlist;
            cdl.countDown();
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

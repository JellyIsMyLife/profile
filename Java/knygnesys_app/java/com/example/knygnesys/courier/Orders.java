package com.example.knygnesys.courier;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.knygnesys.utils.Branch;
import com.example.knygnesys.utils.DBConnect;
import com.example.knygnesys.utils.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Orders implements Runnable {
    private final int branch;
    private final int orderState;
    private int userId;
    private List<Branch> branches;
    private final int loggedInUserId; // New parameter for logged-in user's id
    public CountDownLatch cdl;
    public ArrayList<Order> result;
    public void setUserId(int userId) {
        this.userId = userId;
    }
    private Context context;

    public Orders(CountDownLatch latch, int bran, int state, int loggedInUserId, Context context) {
        cdl = latch;
        branch = bran;
        orderState = state;
        this.loggedInUserId = loggedInUserId;
        this.context = context;
    }

    @Override
    public void run() {
        ArrayList<Order> orderlist = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            Statement statement = conn.createStatement();
            String sql;

            SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            int loggedInUserId = sharedPreferences.getInt("userId", 0); // Retrieve the logged-in user ID

            if (orderState == 1) {
                sql = "SELECT o.*, b.id AS branch_id, b.name AS branch_name, b.address AS branch_address " +
                        "FROM orders o " +
                        "INNER JOIN branches b ON o.fk_Branch = b.id " +
                        "WHERE o.fk_State = " + orderState;
            } else {
                sql = "SELECT o.*, b.id AS branch_id, b.name AS branch_name, b.address AS branch_address " +
                        "FROM orders o " +
                        "INNER JOIN branches b ON o.fk_Branch = b.id " +
                        "WHERE o.fk_State IN (2, 3)" + " AND o.courier_id = " + loggedInUserId;
            }
            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);

            while (resultSet2.next()) {
                Order order = new Order(resultSet2.getInt("id"), resultSet2.getInt("book_amount"), resultSet2.getString("created"),
                        resultSet2.getString("return_date"), resultSet2.getString("get_date"), resultSet2.getString("get_time"),
                        resultSet2.getString("address"), resultSet2.getInt("courier_id"), resultSet2.getInt("fk_State"),
                        resultSet2.getInt("fk_User"), resultSet2.getInt("fk_Branch"));
                orderlist.add(order);
            }
            conn.close();
            result = orderlist;
            cdl.countDown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

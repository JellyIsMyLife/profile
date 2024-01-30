package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;
import com.example.knygnesys.utils.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GetOrders implements Runnable {
    private final int branch;
    public CountDownLatch cdl;
    public ArrayList<Order> result;

    public GetOrders(CountDownLatch latch, int bran)
    {
        cdl = latch;
        branch = bran;
    }

    @Override
    public void run() {
        ArrayList<Order> orderlist = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM orders WHERE fk_Branch = " + branch + " AND fk_State = 2";

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of rows
            while (resultSet2.next()) {
                // TODO adjust this so username is shown?
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
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

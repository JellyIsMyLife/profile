package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UseOrder implements Runnable {
    final int orderID;
    final int courier;

    public UseOrder(int order, int courier)
    {
        this.orderID = order;
        this.courier = courier;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to update order state
            Statement statement = conn.createStatement();
            String sql = "Update users SET points = points - 1 WHERE id =" + courier;
            statement.executeUpdate(sql);
            System.out.println("executed " + sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

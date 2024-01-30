package com.example.knygnesys.courier;


import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class States implements Runnable {

    private int orderID;

    public States(int id)
    {
        orderID = id;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to update order state
            Statement statement = conn.createStatement();
            String sql = "UPDATE orders SET fk_State = 2 WHERE id = " + orderID;
            statement.executeUpdate(sql);
            System.out.println("executed " + sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}
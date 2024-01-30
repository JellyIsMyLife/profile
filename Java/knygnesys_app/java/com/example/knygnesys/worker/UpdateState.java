package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class UpdateState implements Runnable {

    private int orderID;
    private int state;

    public UpdateState(int id, int st)
    {
        orderID = id;
        state = st;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to update order state
            Statement statement = conn.createStatement();
            String sql = "UPDATE orders SET fk_State = " + state + " WHERE id = " + orderID;
            statement.executeUpdate(sql);
            System.out.println("executed " + sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

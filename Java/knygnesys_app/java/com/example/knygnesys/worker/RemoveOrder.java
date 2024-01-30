package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveOrder implements Runnable {

    private int orderID;

    public RemoveOrder(int id)
    {
        orderID = id;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            Statement statement = conn.createStatement();
            String sql = "Update books SET fk_Order = null, is_available = 1 WHERE fk_Order =" + orderID;
            statement.executeUpdate(sql);
            System.out.println("executed " + sql);
            String sql2 = "DELETE FROM orders WHERE id = " + orderID;
            statement.execute(sql2);
            System.out.println("executed " + sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}
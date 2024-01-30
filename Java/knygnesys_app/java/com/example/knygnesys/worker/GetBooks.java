package com.example.knygnesys.worker;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetBooks implements Runnable {

    private int orderID;
    public CountDownLatch cdl;
    public List<String> result;

    public GetBooks(CountDownLatch latch, int id)
    {
        cdl = latch;
        orderID = id;
    }

    @Override
    public void run() {
        List<String> rows = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM books WHERE fk_Order = " + orderID;

            ResultSet resultSet2 = statement.executeQuery(sql);
            System.out.println("executed " + sql);
            // Convert result set to list of rows
            while (resultSet2.next()) {
                String row = resultSet2.getString("title") + ",\n" + resultSet2.getString("author")+ ", "+ resultSet2.getString("id");
                rows.add(row);
                System.out.println(row);
            }
            conn.close();
            result = rows;
            cdl.countDown();
        } catch (SQLException e) {
            //e.printStackTrace();
            //Toast.makeText(TableActivity.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
        }
    }
}

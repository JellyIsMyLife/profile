package com.example.knygnesys.courier;

import com.example.knygnesys.utils.Book;
import com.example.knygnesys.utils.Branch;
import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Branches implements Runnable {
    private int orderID;
    public CountDownLatch cdl;
    public List<Branch> result;

    public Branches(CountDownLatch latch, int id) {
        cdl = latch;
        orderID = id;
    }

    @Override
    public void run() {
        List<Branch> branches = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            // Execute a query to retrieve data
            Statement statement = conn.createStatement();
            String sql = "SELECT b.id, b.name, b.address " +
                    "FROM branches b " +
                    "INNER JOIN books bk ON bk.fk_Branch = b.id " +
                    "WHERE bk.fk_Order = " + orderID;

            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("Executed: " + sql);

            // Convert result set to list of branches
            while (resultSet.next()) {
                Branch branch = new Branch(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address")
                );
                branches.add(branch);
                System.out.println(branch.getName());
            }

            conn.close();
            result = branches;
            cdl.countDown();
        } catch (SQLException e) {
            // Handle the exception appropriately
        }
    }
}

package com.example.knygnesys.client;


import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CreateOrder implements Runnable {
    int result;
    CountDownLatch cdl;
    int role;
    int orderId;
    List<String> cartItems;
    Context context; // Add a Context variable
    String address;
    String  date;
    String time;

    public CreateOrder(CountDownLatch latch, List<String> cart, String a, String  d, String t, Context context) {
        cdl = latch;
        cartItems = cart;
        this.context = context;
        address = a;
        date =d;
        time = t;
    }

    @Override
    public void run() {
        Connection connection = null;
        String formattedDate = "";
        SharedPreferences shared = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE); // Use the context reference to retrieve shared preferences
        int userId = (shared.getInt("userId", 100));
        int branchId = (shared.getInt("branchId", 100));
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formattedDate = currentDate.format(formatter);
            System.out.println("formated date");
            System.out.println(formattedDate);
        }

        try {
            connection = DBConnect.getConnection();
            if (connection != null) {
                try {
                    String query = "SELECT MAX(id) FROM orders";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    if (rs.next()) {
                        int lastId = rs.getInt(1);
                        orderId = lastId + 1;

                        System.out.println("GAUTAS userID ir branchId");
                        System.out.println(userId);
                        System.out.println(branchId);
                        // Insert the order with the orderI

                        query = "INSERT INTO orders (id, book_amount, created, return_date, fk_State, fk_User, fk_Branch, get_date, get_time, address) VALUES (" + orderId + ", " + cartItems.size() + ", '" + formattedDate + "', NULL, 1, " + userId + ", " + branchId + ", '" + date + "', '" + time + "', '" + address + "')";
                        Statement st2 = connection.createStatement();
                        int rows = st2.executeUpdate(query);
                        if (rows > 0) {
                            System.out.println("Order created successfully");
                        } else {
                            System.out.println("Failed to create order");
                        }

                        // Update items in the cart
                        for (String item : cartItems) {
                            String[] parts = item.split(",");
                            String bookId = parts[2];
                            Statement st3 = connection.createStatement();
                            query = "UPDATE books SET is_available = 0 WHERE id = " + bookId;
                            System.out.println(query);
                            st3.executeUpdate(query);
                            Statement st4 = connection.createStatement();
                            query = "UPDATE books SET fk_Order = " + orderId + " WHERE id = " + bookId;
                            System.out.println(query);
                            st4.executeUpdate(query);
                        }
                    } else {
                        // No orders exist yet, create the first one
                        orderId = 1;
                        query = "INSERT INTO orders (id, book_amount, created, return_date, fk_State, fk_User, fk_Branch, get_date, get_time, address) VALUES (" + orderId + ", " + cartItems.size() + ", '" + formattedDate + "', NULL, 1, " + userId + ", " + branchId + ", '" + date + "', '" + time + "', '" + address + "')";
                        Statement st2 = connection.createStatement();
                        int rows = st2.executeUpdate(query);
                        if (rows > 0) {
                            System.out.println("Order created successfully");
                        } else {
                            System.out.println("Failed to create order");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // No connection
                result = 3;
            }
        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        } finally {
            cdl.countDown();
        }
    }

}

package com.example.knygnesys.courier;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

public class CourierProfileEdit implements Runnable{
    CountDownLatch cdl;

    String userName;
    String name;
    String surname;
    String email;
    Context context;
    String address;
    String password;
    String readerId;

    public CourierProfileEdit(CountDownLatch latch, String userName, String name, String surname, String email, String address, String password, Context context){
        this.cdl = latch;
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.context = context;
    }

    @Override
    public void run() {
        Connection connection;
        try {
            String formattedDate = "";
            SharedPreferences shared = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            int userId = (shared.getInt("userId", 100));
            connection = DBConnect.getConnection();
            if (connection != null) {
                try {
                    // delete
                    String query = "SELECT * FROM users WHERE id = " + userId;
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    if (rs.next()) {
                        query = String.format("UPDATE users SET username = '%s',  name = '%s', surname = '%s', email = '%s', address = '%s', password = '%s', readers_id = '%s' WHERE id = '%s'", userName, name, surname, email, address, password,  userId);
                        Statement st2 = connection.createStatement();
                        int rowsUpdated =st2.executeUpdate(query);
                        if (rowsUpdated > 0) {
                            System.out.println("An existing user was updated successfully!");


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
            }
        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        } finally {
            cdl.countDown();
        }

    }
}
package com.example.knygnesys.courier;

import android.app.Activity;
import android.os.Looper;
import android.widget.TextView;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Handler;

public class Users implements Runnable {
    private int userId;
    private TextView points;
    private Activity activity;

    public Users(int userId, TextView points, Activity activity) {
        this.userId = userId;
        this.points = points;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            Statement statement = conn.createStatement();
            String sql = "SELECT points FROM users WHERE id = " + userId;
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int userPoints = resultSet.getInt("points");
                System.out.println("User Points: " + userPoints); // Print the points for debugging

                // Update the UI on the main thread
                activity.runOnUiThread(() -> points.setText(String.valueOf(userPoints)));
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception here
        }
    }
}

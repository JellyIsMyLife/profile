package com.example.knygnesys.courier;

import com.example.knygnesys.utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateCourierId implements Runnable {
    private int orderId;
    private int loggedInUserId;

    public UpdateCourierId(int orderId, int loggedInUserId) {
        this.orderId = orderId;
        this.loggedInUserId = loggedInUserId;
    }

    @Override
    public void run() {
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE orders SET courier_id = ? WHERE id = ?");
            statement.setInt(1, loggedInUserId);
            statement.setInt(2, orderId);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

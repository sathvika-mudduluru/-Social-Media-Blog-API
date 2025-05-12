package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message create(Message m) {
        try {
            
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)"
            );
            ps.setInt(1, m.getPosted_by());
            ps.setString(2, m.getMessage_text());
            ps.setLong(3, m.getTime_posted_epoch());
            ps.executeUpdate();

            // fetch the most recent one
            ps = conn.prepareStatement("SELECT * FROM message WHERE posted_by = ? ORDER BY message_id DESC LIMIT 1");
            ps.setInt(1, m.getPosted_by());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message findById(int id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message WHERE message_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message deleteById(int id) {
        Message existing = findById(id);
        if (existing == null) return null;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM message WHERE message_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existing;
    }

    public Message updateText(int id, String newText) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            ps.setString(1, newText);
            ps.setInt(2, id);
            ps.executeUpdate();
            return findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> findByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message WHERE posted_by = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
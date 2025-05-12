package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {

    public boolean existsByUsername(String username) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM account WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsById(int id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM account WHERE account_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account create(Account account) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?)");
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            return findByUsername(account.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account findByUsername(String username) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
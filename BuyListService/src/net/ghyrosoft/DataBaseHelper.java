package net.ghyrosoft;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 2/3/13
 * Time: 8:52 PM
 */
public class DataBaseHelper
{
    private static final String DB_HOST_NAME = "mysql-buylist.j.rsnx.ru";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "TVgvAVkRMO";
    private static final String DB_NAME = "buy-list-db";

    private static final String LIST_TABLE_NAME = "lists";
    private static final String LOGIN_TABLE_NAME = "logins";

    private Connection connection;

    public boolean register(String login, String password) throws Exception
    {
        if (!checkUserName(login))
        {
            return false;
        }

        String query = String.format("INSERT INTO %s (login, password) VALUES('%s', '%s')",
                LOGIN_TABLE_NAME,
                login,
                password);

        Statement st = connection.createStatement();
        st.executeUpdate(query);

        connection.close();

        return true;
    }

    public void unregister(String login, String session) throws Exception
    {
        int userId = getUserIdByLogin(login, session);
        if (userId == -1)
        {
            connection.close();
            return;
        }

        prepareConnection();

        String query = String.format("DELETE FROM %s WHERE login_id='%d'", LIST_TABLE_NAME, userId);
        Statement st = connection.createStatement();
        st.executeUpdate(query);

        query = String.format("DELETE FROM %s WHERE id='%d'", LOGIN_TABLE_NAME, userId);
        st = connection.createStatement();
        st.executeUpdate(query);

        connection.close();
    }

    public boolean checkUserName(String userName) throws Exception
    {
        boolean result = true;
        prepareConnection();

        String query = String.format("SELECT id FROM %s WHERE login='%s'", LOGIN_TABLE_NAME, userName);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        if (rs.next())
        {
            result = false;
        }

        connection.close();

        return result;
    }

    public String getSession(String user, String password) throws Exception
    {
        prepareConnection();

        String query = String.format("SELECT id, password FROM %s WHERE login='%s'", LOGIN_TABLE_NAME, user);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        if (rs.next())
        {
            String dbPass = rs.getString("password");
            int userId = rs.getInt("id");

            if (dbPass.equals(password))
            {
                String userSession = encodeSession(userId, user, password);

                connection.close();

                return userSession;
            }
        }

        connection.close();

        return null;
    }

    public List<String> getAllLists(int userId) throws Exception
    {
        List<String> listsNames = new ArrayList<>();

        prepareConnection();

        String query = String.format("SELECT name FROM %s WHERE login_id='%d'", LIST_TABLE_NAME, userId);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next())
        {
            String listName = rs.getString("name");
            listsNames.add(listName);
        }

        connection.close();
        return listsNames;
    }

    public String getListByName(String listName, int userId) throws Exception
    {
        prepareConnection();

        String query = String.format("SELECT list FROM %s WHERE login_id='%d' AND name='%s'", LIST_TABLE_NAME, userId, listName);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        String result = null;

        if (rs.next())
        {
            result = rs.getString("list");
        }

        connection.close();

        return result;
    }

    public int getUserIdByLogin(String userName, String session) throws Exception
    {
        prepareConnection();

        String query = String.format("SELECT id, password FROM %s WHERE login='%s'", LOGIN_TABLE_NAME, userName);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        if (rs.next())
        {
            String dbPass = rs.getString("password");
            int userId = rs.getInt("id");

            String userSession = encodeSession(userId, userName, dbPass);

            if (userSession.equals(session))
            {
                connection.close();
                return userId;
            }
        }

        connection.close();
        return -1;
    }

    private void prepareConnection() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        String url = String.format("jdbc:mysql://%s/%s", DB_HOST_NAME, DB_NAME);

        connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    }

    private String encodeSession(int userId, String user, String password) throws Exception
    {
        String userSession = String.format("%d%s%s", userId, user, password);

        byte[] sessionBytes = userSession.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(sessionBytes);
        StringBuilder sb = new StringBuilder();
        for (byte aHash : hash)
        {
            sb.append(Integer.toHexString((aHash & 0xFF) | 0x100).substring(1, 3));
        }

        return sb.toString();
    }
}

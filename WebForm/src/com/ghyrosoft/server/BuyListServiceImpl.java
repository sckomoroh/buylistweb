package com.ghyrosoft.server;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ghyrosoft.server.common.StringUtil;
import org.apache.log4j.Logger;

import com.ghyrosoft.client.BuyListService;
import com.ghyrosoft.client.exceptions.InvalidSessionException;
import com.ghyrosoft.client.exceptions.SqlException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BuyListServiceImpl extends RemoteServiceServlet implements
		BuyListService {
	private static final Logger logger = Logger.getLogger("com.foo");

	private static final String DATABASE_NAME = "buy-list-db";
	private static final String LIST_TABLE_NAME = "lists";
	private static final String LOGIN_TABLE_NAME = "logins";

	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "ROOT";
	private static final String DB_HOST_NAME = "localhost";
//	private static final String DATABASE_PASSWORD = "TVgvAVkRMO";
//	private static final String DB_HOST_NAME = "mysql-buylist.j.rsnx.ru";
	
	private static final String EMAIL_MESSAGE_TEMPLATE = "{0}\r\n\r\nBest regards,\r\nBuy list service administration";

	private Connection mConnection;

	@Override
	public boolean checkSession(String user, String session) throws Exception {
		info("Check session for user = '{0}', session = '{1}'", user, session);

		boolean result = getUserIdFromSession(user, session) != -1;

		if (result) {
			debug("The session '{0}' for user '{1}' is valid", session, user);
		} else {
			warn("The session '{0}' for user '{1}' is invalid", session, user);
		}

		return result;
	}

	@Override
	public String[] login(String user, String password) throws Exception {
		info("Try login user '{0}'", user);

		String[] res = null;

		try {
			prepareConnection();

			debug("Execute SELECT SQL query");

			String query = String.format(
					"SELECT id, password FROM %s WHERE login='%s'",
					LOGIN_TABLE_NAME, user);

			Statement st = mConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				String dbPass = rs.getString("password");
				int userId = rs.getInt("id");

				if (dbPass.equals(password)) {
					String userSession = encodeSession(userId, user, password);
					res = new String[] { user, userSession };

					info("The user '{0}' has been authorized, session '{1}'",
							user, userSession);
				} else {
					warn("The password for user '{0}' is incorrect", user);
				}
			} else {
				warn("User '{0}' not found in database", user);
			}

			mConnection.close();
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());
			throw exception;
		}

		return res;
	}

	@Override
	public boolean removeList(String listName, String user, String session)
			throws Exception {
		try {
			info("Try to remove list '{0}'", listName);

			validateSession(user, session);

			int userId = getUserIdFromSession(user, session);

			prepareConnection();

			debug("Execute DELETE SQL query");

			String query = String.format(
					"DELETE FROM %s WHERE login_id='%d' AND name='%s'",
					LIST_TABLE_NAME, userId, listName);

			Statement st = mConnection.createStatement();
			st.executeUpdate(query);

			mConnection.close();

			sendEmail("List removed",
					StringUtil.format("The list '{0}' has been removed", listName),
					user);

			return true;
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	@Override
	public void commitList(String listName, String xmlContent, String user,
			String session) throws Exception {
		try {
			info("Try to commit list '{0}'", listName);

			validateSession(user, session);

			int userId = getUserIdFromSession(user, session);

			prepareConnection();

			debug("Execute INSERT SQL query");

			String query = String
					.format("INSERT INTO lists (login_id, name, list) VALUES(\"%d\", \"%s\", \"%s\")",
							userId, listName, xmlContent);

			Statement st = mConnection.createStatement();
			st.executeUpdate(query);

			mConnection.close();
			
			sendEmail("New list created",
					StringUtil.format("The list '{0}' has been created", listName),
					user);
			
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	@Override
	public void updateList(String listName, String xmlContent, String user,
			String session) throws Exception {
		try {
			info("Try to update list '{0}'", listName);

			validateSession(user, session);

			int userId = getUserIdFromSession(user, session);

			prepareConnection();

			debug("Execute SELECT SQL query");

			String query = String
					.format("UPDATE lists SET list=\"%s\" WHERE name=\"%s\" AND login_id=\"%s\"",
							xmlContent, listName, userId);

			Statement st = mConnection.createStatement();
			st.executeUpdate(query);

			mConnection.close();
			
			sendEmail("List updated",
					StringUtil.format("The list '{0}' has been updated", listName),
					user);
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	@Override
	public List<String> getAllLists(String user, String session)
			throws Exception {
		try {
			info("Try to retrieve all lists of user '{0}'", user);

			validateSession(user, session);

			int userId = getUserIdFromSession(user, session);

			List<String> mListsNames = new ArrayList<String>();

			prepareConnection();

			debug("Execute SELECT SQL query");

			String query = String.format(
					"SELECT name FROM %s WHERE login_id='%d'", LIST_TABLE_NAME,
					userId);

			Statement st = mConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				String listName = rs.getString("name");
				mListsNames.add(listName);
			}

			mConnection.close();

			return mListsNames;
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	@Override
	public String getList(String listName, String user, String session)
			throws Exception {
		try {
			info("Try to get list '{0}' for user '{1}'", listName, user);

			validateSession(user, session);

			int userId = getUserIdFromSession(user, session);

			prepareConnection();

			debug("Execute SELECT SQL query");

			String query = String.format(
					"SELECT list FROM %s WHERE login_id='%d' AND name='%s'",
					LIST_TABLE_NAME, userId, listName);

			Statement st = mConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			String result = null;

			if (rs.next()) {
				result = rs.getString("list");

				debug("The list '{0}' has been retrieved", listName);
			} else {
				warn("The list '{0}' was not found", listName);
			}

			mConnection.close();

			return result;
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	@Override
	public boolean checkListName(String listName, String user, String session)
			throws Exception {
		try {
			info("Check list name '{0}' for user '{1}'", listName, user);

			validateSession(user, session);

			prepareConnection();

			debug("Execute SELECT SQL query");
			
			String query = String.format("SELECT name FROM %s WHERE name='%s'",
					LIST_TABLE_NAME, listName);

			Statement st = mConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			boolean result = false;

			if (!rs.next()) {
				debug("The list '{0}' was not found", listName);

				result = true;
			} else {
				debug("The list'{0}' was found", listName);
			}

			mConnection.close();

			return result;
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	private void prepareConnection() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			String url = String.format("jdbc:mysql://%s/%s", DB_HOST_NAME,
					DATABASE_NAME);

			info("Connect to MySQL using URL '{0}'", url);
			mConnection = DriverManager.getConnection(url, DATABASE_USER,
					DATABASE_PASSWORD);
		} catch (SQLException ex) {
			error("Exception: {0}", ex.getMessage());

			SqlException exception = new SqlException();
			exception.setMessage(ex.getMessage());

			throw exception;
		}
	}

	private void validateSession(String user, String session) throws Exception {
		info("Validate user = '{0}', session = '{1}", user, session);

		if (getUserIdFromSession(user, session) == -1) {
			warn("The user '{0}' was not found for session '{1}'", user,
					session);

			InvalidSessionException exception = new InvalidSessionException();
			exception.buildMessage(session, user);
			throw exception;
		}
		
		debug("Session is valid");
	}

	private int getUserIdFromSession(String userName, String session)
			throws Exception {
		info("Get user ID from session '{0}'", session);
		
		prepareConnection();

		debug("Execute SELECT SQL query");
		
		String query = String.format(
				"SELECT id, password FROM %s WHERE login='%s'",
				LOGIN_TABLE_NAME, userName);

		Statement st = mConnection.createStatement();
		ResultSet rs = st.executeQuery(query);

		if (rs.next()) {
			String dbPass = rs.getString("password");
			int userId = rs.getInt("id");

			String userSession = encodeSession(userId, userName, dbPass);

			if (userSession.equals(session)) {
				debug("User ID = '{0}'", userId);
				return userId;
			}
		}

		mConnection.close();
		
		debug("User ID was not found");

		return -1;
	}

	private String encodeSession(int userId, String user, String password)
			throws Exception {
		String userSession = String.format("%d%s%s", userId, user, password);

		byte[] sessionBytes = userSession.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hash = md.digest(sessionBytes);
		StringBuilder sb = new StringBuilder();
		for (byte aHash : hash) {
			sb.append(Integer.toHexString((aHash & 0xFF) | 0x100).substring(1,
					3));
		}

		return sb.toString();
	}

	private static void debug(String format, Object... arg) {
		logger.debug(StringUtil.format(format, arg));
	}

	private static void error(String format, Object... arg) {
		logger.error(StringUtil.format(format, arg));
	}

	private static void info(String format, Object... arg) {
		logger.info(StringUtil.format(format, arg));
	}

	private static void warn(String format, Object... arg) {
		logger.warn(StringUtil.format(format, arg));
	}

	private void sendEmail(String subject, String message, String reciever) {
		try
		{
			debug("Sending e-mail to '{0}'", reciever);
			
			String to = reciever;
			String from = "admin@j.rsnx.ru";
			String host = "localhost";
	
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			
			Session session = Session.getDefaultInstance(properties);
	
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			mimeMessage.setText(StringUtil.format(EMAIL_MESSAGE_TEMPLATE, message));
	
			// Send message
			Transport.send(mimeMessage);
		}
		catch (Exception ex)
		{
			error("Could not send e-mail due to '{0}'", ex.getMessage());
		}
	}
}

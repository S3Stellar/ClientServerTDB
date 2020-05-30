package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.services.CrudRepository;
import data.Message;

public class SqlLiteService implements CrudRepository<Message, String> {

	private Connection conn;

	public SqlLiteService() {
		connect();
	}

	@Override
	public Message[] read(String id) {
		List<Message> msgList = new ArrayList<>();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from messages where id = " + id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Message msg = new Message();
				msg.setId(result.getString(1));
				msg.setMsg(result.getString(2));
				msgList.add(msg);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgList.toArray(new Message[0]);
	}

	@Override
	public Message create(Message t) {
		String sqlInsert = "insert into messages VALUES (?,?)";
		String id = null;
		try {
			String stmt = "SELECT id FROM messages ORDER BY id DESC LIMIT 1";
			PreparedStatement getLastId = conn.prepareStatement(stmt);
			ResultSet idResult = getLastId.executeQuery();

			while (idResult.next()) {
				id = idResult.getString(1);
			}

			if (id == null)
				id = "1";
			else {
				Long idParse = Long.parseLong(id);
				idParse++;
				id = idParse.toString();
			}

			PreparedStatement pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, id);
			pst.setString(2, t.getMsg());
			pst.execute();
			t.setId(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return read(t.getId())[0];
	}

	@Override
	public void update(String id, Message data) {
		String sqlupdate = "UPDATE messages SET msg=? WHERE id =?";
		try {
			PreparedStatement pst = conn.prepareStatement(sqlupdate);
			pst.setString(1, data.getMsg());
			pst.setString(2, id);

			pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		String sqldelete = "DELETE FROM messages where id = ?";

		try {
			PreparedStatement pst = conn.prepareStatement(sqldelete);
			pst.setString(1, id);
			pst.execute();

		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			// db parameters
			String url = "jdbc:sqlite:C:/Users/Naor/Desktop/afekadb/afekadb.db";
			// create a connection to the database
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
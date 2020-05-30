package logic;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import dao.services.CrudRepository;
import data.Message;

public class MySqlService implements CrudRepository<Message, String> {

	private static Connection connect;

	public MySqlService() {
		ConectingToSQL();
	}

	@Override
	public Message[] read(String id) {
		List<Message> msgList = new ArrayList<>();
		try {
			PreparedStatement statement = connect.prepareStatement("select * from messages where id = " + id);
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
		String sqlInsert = "insert into afekadb.messages VALUES (?,?)";
		String id = null;
		try {
			String stmt = "SELECT id FROM messages ORDER BY id DESC LIMIT 1";
			PreparedStatement getLastId = connect.prepareStatement(stmt);
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

			PreparedStatement pst = connect.prepareStatement(sqlInsert);
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
			PreparedStatement pst = connect.prepareStatement(sqlupdate);
			pst.setString(1, data.getMsg());
			pst.setString(2, id);
			
			pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Doesn't work
	/*
	 * @Override public void delete(String id) { String sql =
	 * "DELETE FROM messages WHERE id = ?"; PreparedStatement pst; try { pst =
	 * connect.prepareStatement(sql); pst.setString(1, id); pst.execute(sql); }
	 * catch (SQLException e) { e.printStackTrace(); }
	 * 
	 * }
	 */

	
	@Override
	public void delete(String id) {
		String sqldelete = "DELETE FROM messages where id = ?";

		try {
			PreparedStatement pst = connect.prepareStatement(sqldelete);
			pst.setString(1, id);
			pst.execute();

		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void connection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Works");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ConectingToSQL() {

		connection();
		String host = "jdbc:mysql://localhost:3306/afekadb";
		String username = "root";
		String password = "Naor12amir+";

		try {
			connect = (Connection) DriverManager.getConnection(host, username, password);
			System.out.println("work");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

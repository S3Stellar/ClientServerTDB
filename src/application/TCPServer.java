package application;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import dao.LastIdValueDao;
import logic.MongoService;
import logic.MySqlService;
import logic.SqlLiteService;


class TCPServer {

	public static void main(String argv[]) throws Exception {

		MongoService mongoService = new MongoService(new LastIdValueDao());
		MySqlService mySqlService = new MySqlService();
		SqlLiteService sqlLiteService = new SqlLiteService();
		
		ServerSocket s = null;
		try {
			s = new ServerSocket(10000);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
		while (true) {
			Socket incoming = null;
			try {
				incoming = s.accept();
				new socketHandler(incoming, mongoService, mySqlService, sqlLiteService).start();
			} catch (IOException e) {
				System.out.println(e);
				s.close();
				continue;
			}
		}
	}
}

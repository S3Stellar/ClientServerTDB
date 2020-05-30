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
			System.out.println("serverSocket");
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}

		System.out.println("beforeWhile");
		while (true) {
			Socket incoming = null;
				System.out.println("beforeTry");
			try {
				System.out.println("beforeAccept");
				incoming = s.accept();
				//new Thread(new socketHandler(incoming)).start();
				new socketHandler(incoming, mongoService, mySqlService, sqlLiteService).start();
				System.out.println("localPort =" +s.getLocalPort() + ", Accept = " + s.accept());
			} catch (IOException e) {
				System.out.println(e);
				s.close();
				continue;
			}
			
			System.out.println(incoming);
		}
	}
}

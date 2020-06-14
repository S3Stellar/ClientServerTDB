package application;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import data.Message;
import logic.MongoService;
import logic.MySqlService;
import logic.SqlLiteService;

public class socketHandler extends Thread {
	private Socket incoming;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private MongoService mongoService;
	private SqlLiteService sqlLiteService;
	private MySqlService mySqlService;
	
	public socketHandler(Socket incoming, MongoService mongoService, MySqlService mySqlService, SqlLiteService sqlLiteService) {
		this.incoming = incoming;
		this.mongoService = mongoService;
		this.sqlLiteService = sqlLiteService;
		this.mySqlService = mySqlService;
		
		try {
			inFromClient = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			outToClient = new DataOutputStream(incoming.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String str;
			while (true) {
				str = inFromClient.readLine();
				if(str != null) {
					if(str.equals("CLOSE")) {
						incoming.close();
						this.interrupt();
						break;
					}

					System.out.println("We are in the server " + str);
				
					mySqlService.create(new Message(str.toUpperCase()));
					sqlLiteService.create(new Message(str.toUpperCase()));
					mongoService.create(new Message(str.toUpperCase()));
					
					outToClient.writeBytes(str.toUpperCase() + "\n");
				}
			}

		} catch (IOException e) {
			e.getMessage();
		}
	}
}

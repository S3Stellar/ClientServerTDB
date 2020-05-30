package application;

import logic.SqlLiteService;

public class ClassMain {

	public static void main(String[] args) {
		// MySqlService sqlService = new MySqlService();
		// System.out.println(sqlService.create(new Message("yossi")));
		// sqlService.update("2", new Message("joni the king"));
		// sqlService.delete("5");
		// System.out.println(sqlService.read("4")[0]);

		SqlLiteService sqlLiteService = new SqlLiteService();
		// sqlLiteService.create(new Message("moshe"));
		// sqlLiteService.delete("4");
		// sqlLiteService.update("5", new Message("hello"));
		System.out.println(sqlLiteService.read("6")[0]);

	}
}

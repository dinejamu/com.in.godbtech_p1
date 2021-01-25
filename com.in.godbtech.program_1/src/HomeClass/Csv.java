package HomeClass;

import java.io.*;
import java.sql.*;

public class Csv {

	public static void main(String[] args) {
		final String jdbcURL = "jdbc:mysql://localhost/Test";
		final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		final String username = "root";
		final String password = "World!1";

		String csvFilePath = "C:\\Users\\Dinesh V\\Desktop\\Books_new.csv";

		int batchSize = 20;

		Connection connection = null;

		try {
			Class.forName(jdbc_driver);

			connection = DriverManager.getConnection(jdbcURL, username, password);
			connection.setAutoCommit(false);

			String sql = "INSERT INTO csvfile (Remarks, Name, Marks) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
			String lineText = null;

			int count = 0;

			lineReader.readLine(); // skip header line

			while ((lineText = lineReader.readLine()) != null) {
				String[] data = lineText.split(",");
				String Remarks = data[0];
				String Name = data[1];
				String Marks = data[2];

				statement.setString(1, Remarks);
				statement.setString(2, Name);
				statement.setString(3, Marks);

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}
			}
			try {
				lineReader.close();
				connection.commit();

				long end = System.currentTimeMillis();

				System.out.printf("Import done in %d ms\n", +end);
				connection.rollback();
				connection.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}

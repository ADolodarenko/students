package org.dav.learn.students;

import java.sql.*;
import java.text.Collator;
import java.util.Locale;

public class Test
{
	public static void main(String[] args)
	{
		testJDBC();
	}
	
	static void testJDBC()
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			//Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://192.168.0.62:3306/students";  //?serverTimezone=UTC
			
			con = DriverManager.getConnection(url, "dolodarenko", "luisiana969");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM students");
			
			while (rs.next())
			{
				String str = rs.getString(1) + ":" + rs.getString(2);
				System.out.println(str);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// Эта часть позволяет нам закрыть все открытые ресуры
			// В противном случае возмжожны проблемы. Поэтому будьте
			// всегда аккуратны при работе с коннектами
			try
			{
				if (rs != null)
					rs.close();
				
				if (stmt != null)
					stmt.close();
				
				if (con != null)
					con.close();
			}
			catch (SQLException ex)
			{
				ex.printStackTrace();
				System.err.println("Error: " + ex.getMessage());
			}
		}
	}
	
	static void testCollator()
	{
		for (Locale locale : Collator.getAvailableLocales())
			System.out.println(locale);
		
		
		Collator collator = Collator.getInstance(new Locale("ru"));
		collator.setStrength(Collator.PRIMARY);
		System.out.println(collator.compare("ПАВЕЛ", "ПАВЕЛ"));
		System.out.println(collator.compare("ПАВЕЛ", "Павел"));
		System.out.println(collator.compare("ПАВЕЛ", "артем"));
		System.out.println("ПАВЕЛ".compareTo("артем"));
	}
}

package cn.edu.uestc.mysqlUtli.Main;

import java.io.*;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
	private String driverclass = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql:///test";
	private String user;
	private String passwd;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private Connection conn;

	private void getConnection() {
		try {
			Class.forName(driverclass);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				System.out.println("请输入用户名：");
				user = br.readLine();
				System.out.println("请输入密码:");
				passwd = br.readLine();
				
				conn = DriverManager.getConnection(url, user, passwd);
				System.out.println("连接成功！");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("链接异常！请检查Mysql是否启动，或者用户名或密码错误，请重新输入！");
				continue;
			}
		}
	}

	private void executeSql() {
		while (true) {
			try {
				System.out.println("请输入要执行的sql语句(quit退出程序)：");
				String sql = br.readLine();
				sql = sql.trim();
				if ("quit".equalsIgnoreCase(sql))
					quit();
				Statement statement = conn.createStatement();
				boolean hasResult = statement.execute(sql);
				if (hasResult) {
					System.out.println("查询结果:");
					ResultSet rs = statement.getResultSet();
					ResultSetMetaData rsd = rs.getMetaData();
					int columns = rsd.getColumnCount();
					for (int i = 1; i <= columns; i++) {
						System.out.print(rsd.getColumnName(i) + "\t");
						if (i != columns) {
							System.out.print("|");
						}
					}
					System.out.println();
					while (rs.next()) {
						for (int i = 1; i <= columns; i++) {
							System.out.print(rs.getString(i));
							if (i < columns)
								System.out.print("\t|");
						}
						System.out.println();
					}
					rs.close();
					statement.close();
				} else {
					System.out.println("该Sql语句影响了的记录有" + statement.getUpdateCount() + "条！");
				}
			} catch (IOException e) {
			} catch (SQLException e) {
				System.out.println("语法有误。");
				continue;
			}
		}
	}

	private void quit() {
		try {
			br.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("谢谢使用，再见!");
		System.exit(0);
	}

	public Mysql() {
		getConnection();
		executeSql();
	}

	public static void main(String[] args) {
		new Mysql();
	}

}

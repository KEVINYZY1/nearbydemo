package com.andieguo.location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;

import junit.framework.TestCase;

public class JDBC_Test extends TestCase {
	// ������̬ȫ�ֱ���
	private static Connection conn;
	private static Statement statement;
	double lat;
	double lng;
	int user;
	double ctime;

	public void recordInsert(){
		for(int i=0;i<1000;i++){
			randomDoubletest();
		}
	}
	
	public  void randomDoubletest() {
		Random random = new Random();
		DecimalFormat df = new DecimalFormat("0.000000");
		lat = random.nextDouble() * 180 - 90;// ���-90��90
		lng = random.nextDouble() * 360 - 180;// ���-180,180
		user = random.nextInt(20000);
		ctime = random.nextDouble() + random.nextInt(100000);
		lat = new Double(df.format(lat).toString());
		lng = new Double(df.format(lng).toString());
		df = new DecimalFormat("0.000");
		ctime = new Double(df.format(ctime).toString());

		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			System.out.println("lat:" + lat + ",lng:" + lng + ",user:" + user + ",ctime:" + ctime);
			String sql = "INSERT INTO index1(lat,lng,user,ctime)" + " VALUES (" + lat + "," + lng + "," + user + "," + ctime + ")"; // �������ݵ�sql���
			statement = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����
			int count = statement.executeUpdate(sql); // ִ�в��������sql��䣬�����ز������ݵĸ���
			conn.close(); // �ر����ݿ�����
		} catch (SQLException e) {
			System.out.println("��������ʧ��" + e.getMessage());
		}
	}

	public static void insert() {
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {

			String sql = "INSERT INTO staff(name, age, sex,address, depart, worklen,wage)" + " VALUES ('Tom1', 32, 'M', 'china','Personnel','3','3000')"; // �������ݵ�sql���
			statement = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����
			int count = statement.executeUpdate(sql); // ִ�в��������sql��䣬�����ز������ݵĸ���
			System.out.println("��staff���в��� " + count + " ������"); // �����������Ĵ�����
			conn.close(); // �ر����ݿ�����
		} catch (SQLException e) {
			System.out.println("��������ʧ��" + e.getMessage());
		}
	}

	public static Connection getConnection() {
		Connection con = null; // ���������������ݿ��Connection����
		try {
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/location", "root", "root");// ������������
		} catch (Exception e) {
			System.out.println("���ݿ�����ʧ��" + e.getMessage());
		}
		return con; // ���������������ݿ�����
	}
}

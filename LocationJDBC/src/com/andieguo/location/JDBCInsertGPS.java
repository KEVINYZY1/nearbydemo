package com.andieguo.location;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Random;

import junit.framework.TestCase;

public class JDBCInsertGPS extends TestCase {
	// ������̬ȫ�ֱ���
	private static Connection conn;
	double lat;
	double lng;
	
	public static Connection getConnection() {
		Connection con = null; // ���������������ݿ��Connection����
		try {
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nearby", "root", "root");// ������������
			System.out.println(con);
		} catch (Exception e) {
			System.out.println("���ݿ�����ʧ��" + e.getMessage());
		}
		return con; // ���������������ݿ�����
	}

	//����function��ʽһ
	public void testFun2(){
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		String func = "{? = call CustomerLevel(?)}";//���ܼ�{}
		try {
			CallableStatement cstmt = conn.prepareCall(func);
			cstmt.registerOutParameter(1, Types.VARCHAR);
			cstmt.setDouble(2, 50000000);
			cstmt.execute();
			System.out.println(cstmt.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//����function��ʽ��
	public void testFun(){
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		String func = "select CustomerLevel(?)";//���ܼ�{}
		try {
			CallableStatement cstmt = conn.prepareCall(func);
			cstmt.setDouble(1, 50000000);
			cstmt.execute();
			ResultSet rs = cstmt.getResultSet();
			if (rs.next()) {
				 System.out.println(rs.getString(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// select `geohash_encode`(30.5169562,114.3380906,8)
	/**
	 * latitude , longitude , precision
	 * @param latitude
	 * @param longitude
	 * @param precision
	 * @return
	 */
	public static String getGeoHash(double latitude,double longitude,int precision) {
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		String func = "select geohash_encode(?, ?,?)";//���ܼ�{}
		try {
			CallableStatement cstmt = conn.prepareCall(func);
			cstmt.setDouble(1, latitude);
			cstmt.setDouble(2, longitude);
			cstmt.setInt(3, precision);
			cstmt.execute();
			ResultSet rs = cstmt.getResultSet();
			if (rs.next()) {
				 return rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	//������������
	public void randomInsert() {
		for (int i = 0; i < 900; i++) {
			Random random = new Random();
			DecimalFormat df = new DecimalFormat("0.0000000");
			lat = random.nextDouble() * 0.0915219 + 30.6084781;// latitude 30.5169562-30.7000000
			lng = random.nextDouble() * 0.0809547 + 114.4190453;// longitude 114.3380906 -114.5000000
			lat = new Double(df.format(lat).toString());
			lng = new Double(df.format(lng).toString());

			String user_id = "359836040136" + String.valueOf((i + 100));
			insertUser(user_id, "posly" + i, "posly_" + i);
			insertGPS(user_id, String.valueOf(lng), String.valueOf(lat));
		}
	}

	//�����û�����
	public static void insertUser(String user_id, String name, String personal_note) {
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String usersql = "insert into user_info(id,name,personal_note) values(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(usersql);
			ps.setString(1, user_id);
			ps.setString(2, name);
			ps.setString(3, personal_note);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("����user����ʧ��" + e.getMessage());
		}
	}
	//����gps����
	@SuppressWarnings("deprecation")
	public static void insertGPS(String user_id, String longitude, String latitude) {
		conn = getConnection();
		try {
			String usersql = "insert into gps(user_id,longitude,latitude,geohash,time) values(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(usersql);
			ps.setString(1, user_id);
			ps.setString(2, longitude);
			ps.setString(3, latitude);
			ps.setString(4, getGeoHash(Double.valueOf(latitude),Double.valueOf(longitude),8));
			ps.setDate(5, new Date(2014,5,16));
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("����gps����ʧ��" + e.getMessage());
		} 
	}
	//��������geohash
	public static void findAndUpdate(){
		conn = getConnection();
		try {
			String usersql = "select user_id,geohash_encode(latitude,longitude, 8) as geohash from gps";
			PreparedStatement ps = conn.prepareStatement(usersql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String updatesql = "update gps set geohash = ? where user_id = ?";
				PreparedStatement updateps = conn.prepareStatement(updatesql);
				updateps.setString(1, rs.getString("geohash"));
				updateps.setString(2, rs.getString("user_id"));
				updateps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("����gps����ʧ��" + e.getMessage());
		}
	}
	
	public static void findGPSbyGeoHash(){
		conn = getConnection();
		try {
			String usersql = "SELECT g.*,u.* FROM gps  g INNER JOIN user_info u ON g.user_id=u.id where geohash like ?";
			PreparedStatement ps = conn.prepareStatement(usersql);
			ps.setString(1, "wt3qkx"+"%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("user_id")+","+rs.getString("geohash"));
			}
		} catch (SQLException e) {
			System.out.println("����GeoHash��ѯ����ʧ��" + e.getMessage());
		}
	}

}

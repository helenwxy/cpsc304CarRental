package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

import ca.ubc.cs304.model.ReturnModel;
import ca.ubc.cs304.model.VehicleModel;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private int confno = 17;
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

//	public void deleteBranch(int branchId) {
//		try {
//			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
//			ps.setInt(1, branchId);
//
//			int rowCount = ps.executeUpdate();
//			if (rowCount == 0) {
//				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
//			}
//
//			connection.commit();
//
//			ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}
//
//	public void insertBranch(BranchModel model) {
//		try {
//			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
//			ps.setInt(1, model.getId());
//			ps.setString(2, model.getName());
//			ps.setString(3, model.getAddress());
//			ps.setString(4, model.getCity());
//			if (model.getPhoneNumber() == 0) {
//				ps.setNull(5, java.sql.Types.INTEGER);
//			} else {
//				ps.setInt(5, model.getPhoneNumber());
//			}
//
//			ps.executeUpdate();
//			connection.commit();
//
//			ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}
//
//	public BranchModel[] getBranchInfo() {
//		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
//
//		try {
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
//
////    		// get info on ResultSet
////    		ResultSetMetaData rsmd = rs.getMetaData();
////
////    		System.out.println(" ");
////
////    		// display column names;
////    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
////    			// get column name and print it
////    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
////    		}
//
//			while(rs.next()) {
//				BranchModel model = new BranchModel(rs.getString("branch_addr"),
//													rs.getString("branch_city"),
//													rs.getInt("branch_id"),
//													rs.getString("branch_name"),
//													rs.getInt("branch_phone"));
//				result.add(model);
//			}
//
//			rs.close();
//			stmt.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//		}
//
//		return result.toArray(new BranchModel[result.size()]);
//	}
//
//	public void updateBranch(int id, String name) {
//		try {
//		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
//		  ps.setString(1, name);
//		  ps.setInt(2, id);
//
//		  int rowCount = ps.executeUpdate();
//		  if (rowCount == 0) {
//		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
//		  }
//
//		  connection.commit();
//
//		  ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}
	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public ArrayList<VehicleModel> getVehicleInfo(String vtname, String location) {
		ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			if (vtname.equals("") && location.equals("")) {
				ps = connection.prepareStatement(
						"SELECT vtname, make, model, year, location, city FROM vehicle " +
								"WHERE location = 'Richmond' ORDER BY vtname, make");
				rs = ps.executeQuery();
			} else if (vtname.equals("")) {
				ps = connection.prepareStatement(
						"SELECT vtname, make, model, year, location, city FROM vehicle " +
								"WHERE location = ? ORDER BY vtname, make");
				ps.setString(1, location);
				rs = ps.executeQuery();
			} else if (location.equals("")) {
				ps = connection.prepareStatement(
						"SELECT vtname, make, model, year, location, city FROM vehicle " +
								"WHERE vtname = ? ORDER BY location, make");
				ps.setString(1, vtname);
				rs = ps.executeQuery();
			} else {
				ps = connection.prepareStatement(
						"SELECT vtname, make, model, year, location, city FROM vehicle " +
								"WHERE vtname = ? AND location = ? ORDER BY make, model");
				ps.setString(1, vtname);
				ps.setString(2, location);
				rs = ps.executeQuery();
			}

			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getString("vtname"),
						rs.getString("make"),
						rs.getString("model"),
						rs.getInt("year"),
						rs.getString("location"),
						rs.getString("city"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}

	public int insertReservation(String phoneno, String vtname, String location, String fromdate, String fromtime, String todate, String totime) throws SQLException {
		confno ++;
		try {
			PreparedStatement ps = connection.prepareStatement("" +
					"SELECT COUNT(dlicense) FROM customer WHERE phone = ?");
			ps.setString(1,phoneno);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int numCustomer = rs.getInt(1);
			System.out.println("numCustomer: " + numCustomer);
			rs.close();
			ps.close();
			if (numCustomer == 0) {
				return 0;
			}
		} catch (SQLException e) {
			//
		}
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO reservation SELECT ?,?,c.dlicense,?,?,? FROM customer c WHERE c.phone = ? AND " +
							"EXISTS(SELECT v.vlicense FROM vehicle v WHERE ? = v.vtname AND ? = v.location AND v.status = 'Available')");
			ps.setInt(1, confno);
			ps.setString(2, vtname);
			ps.setTimestamp(3, java.sql.Timestamp.valueOf(fromdate + " " + fromtime + ":00"));
//			ps.setDate(3, java.sql.Date.valueOf(fromdate));
			ps.setTimestamp(4, java.sql.Timestamp.valueOf(todate + " " + totime + ":00"));
//			ps.setDate(4, java.sql.Date.valueOf(todate));
			ps.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.setString(6,phoneno);
			ps.setString(7,vtname);
			ps.setString(8,location);
			int numRow = ps.executeUpdate();
			System.out.println("num rows: " + numRow);
			connection.commit();
			ps.close();
			if (numRow == 0) {
				return -1;
			} else {
				return confno;
			}
		} catch (SQLException e) {
			confno--;
			rollbackConnection();
			throw e;
		}
	}

	public boolean insertNewCustomer(String dlicense, String name, String address, String phone) {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO customer values (?,?,?,?)");
			ps.setString(1, dlicense);
			ps.setString(2,name);
			ps.setString(3,address);
			ps.setString(4,phone);
			int numrow = ps.executeUpdate();
			System.out.println(numrow);
			connection.commit();
			ps.close();
			return true;
//			if (numrow == 1) {
//				return true;
//			} else {
//				return false;
//			}
		} catch (SQLException e) {
			rollbackConnection();
			return false;
		}
	}

	public int hasLicense(String vlicense) {
		try {
			PreparedStatement ps = connection.prepareStatement("" +
					"SELECT r.rid FROM rental r WHERE r.VLICENSE = ? AND " +
					"r.rid NOT IN (SELECT rt.rid FROM return rt)");
			ps.setString(1,vlicense);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
//				System.out.println("rid = " + rs.getInt(1));
				int rid = rs.getInt(1);
				System.out.println("rid = " + rid);
				rs.close();
				ps.close();
				return rid;
			}
			rs.close();
			ps.close();
			return -1;
		} catch (SQLException e) {
			return -1;
		}
	}

	public ReturnModel insertReturn(int rid, String vlicense, String odo, String tank) {
		ReturnModel model = null;
		int t = 0;
		if (tank.equals("Y")) {
			t = 0;
		}
		int odometer = 0;

		try
		{
			// the String to int conversion happens here
			odometer = Integer.parseInt(odo.trim());

			// print out the value after the conversion
//			System.out.println("int i = " + i);
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("NumberFormatException: " + nfe.getMessage());
			return null;
		}
		try {
			PreparedStatement ps = connection.prepareStatement("" +
					"SELECT (sysdate - r.fromdate), vt.hrate, vt.drate, vt.wrate, vt.hirate, vt.dirate, vt.wirate, r.FROMDATE, r.vlicense " +
					"FROM rental r, vehicle v, VEHICLETYPE vt WHERE r.rid = ? AND r.vlicense = v.vlicense AND v.vtname = vt.vtname");
//			PreparedStatement ps = connection.prepareStatement("INSERT INTO return " +
//					"SELECT r.rid, sysdate, ?,?, " +
//					"(trunc(((86400*(sysdate-r.FROMDATE))/60)/60)-24*(trunc((((86400*(sysdate-r.fromdate))/60)/60)/24))) " +
//					"FROM rental r, vehicle v, vehicletype vt WHERE r.VLICENSE = ? AND v.vlicense = ? AND v.v"+
//					"NOT EXISTS (SELECT * FROM return rt WHERE rt.rid = r.rid)");
			ps.setInt(1,rid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			float dateDifference = rs.getFloat(1);
			int hrate = rs.getInt(2);
			int drate = rs.getInt(3);
			int wrate = rs.getInt(4);
			int hirate = rs.getInt(5);
			int dirate = rs.getInt(6);
			int wirate = rs.getInt(7);
			String date = rs.getString(8);
			String license = rs.getString(9);

			System.out.println("time difference " + dateDifference);
			int weeks = (int)dateDifference / 7;
			int days = ((int) dateDifference) % 7;
			int hours = (int) (dateDifference * 24 % 24);
			float total = hrate*hours+hirate*hours+drate*days+dirate*days+wrate*weeks+wirate*weeks;
			System.out.println("week: " + weeks + "days: " + days + "hours :" + hours);
			System.out.println("hrate = " + hrate);
			System.out.println("date = " + date);


			PreparedStatement ps2 = connection.prepareStatement("INSERT INTO return " +
					"SELECT ?, sysdate, ?,?, ?" +
					"FROM rental r, vehicle v, VEHICLETYPE vt WHERE r.rid = ? AND r.vlicense = v.vlicense AND v.vtname = vt.vtname");
//			PreparedStatement ps2 = connection.prepareStatement("INSERT INTO return " +
//					"SELECT ?, sysdate, ?,?, ?*vt.hrate + ?*vt.drate + ?*vt.wrate + ?*vt.hirate + ?*vt.dirate + ?*vt.wirate + ?" +
//							"FROM rental r, vehicle v, VEHICLETYPE vt WHERE r.rid = ? AND r.vlicense = v.vlicense AND v.vtname = vt.vtname");
			ps2.setInt(1, rid);
			ps2.setInt(2, odometer);
			ps2.setInt(3, t);
			ps2.setFloat(4, total);
			ps2.setInt(5, rid);

			int numrow = ps2.executeUpdate();
			System.out.println(numrow);
			connection.commit();

			PreparedStatement ps3 = connection.prepareStatement("UPDATE vehicle SET status = 'Available', ODOMETER = ? WHERE vlicense = ?");
			ps3.setString(1,license);
			ps3.setInt(2, odometer);
			connection.commit();

			ps.close();
			ps2.close();
			ps3.close();

			model = new ReturnModel(hrate, drate, wrate, hirate, dirate, wirate, hours, days, weeks, date, total);
			return model;
//			PreparedStatement ps2 = connection.prepareStatement("" +
//					"SELECT r.rid FROM rental r WHERE r.VLICENSE = ? AND " +
//					"NOT EXISTS (SELECT * FROM return rt WHERE rt.rid = r.rid)");
		} catch (SQLException e) {
			return null;
		}
	}
}

package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.model.VehicleModel;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private int confno = 17;
	private int rid;
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
						rs.getString("location"));
				result.add(model);
			}

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

	public ReservationModel insertRentWithReservation(String confNo, String location, String cardName, String cardNumber, String cardExpiryDate) {
		// Check if confirmation number exist and return null if not found
		String vtname = null;
		String dlicense = null;
		String fromDate = null;
		String toDate = null;
		String rDate = null;
		String vlicense = null;
		int odometer = -1;
		// Location

		// Return reservation of given conformation number
		// 3) confirmation number -> dlicence, vtName, fromDate, toDate, rDate
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT r.dlicense, r.vtname, r.fromDate, r.toDate, r.rDate\n" +
							"FROM reservation r\n" +
							"WHERE r.confNo = ?");
			ps.setString(1, confNo);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dlicense = rs.getString("dlicense");
				vtname = rs.getString("vtname");
				fromDate = rs.getString("fromDate");
				toDate = rs.getString("toDate");
				rDate = rs.getString("rDate");
				System.out.println(dlicense + " " + vtname + " " + fromDate + " " + toDate + " " + rDate);
			} else {
				System.out.println("Customer of given confirmation number is not found :(");
				ReservationModel ResModel =
						new ReservationModel("Customer of given confirmation number is not found :(");
				return ResModel;
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (3)");
		}

		// Return vehicle based on given vehicle type and location
		// 4) vtname, location -> vlicense, odometer
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT v.vlicense, v.odometer FROM vehicle v " +
							"WHERE ROWNUM = 1 AND v.vtname = ? AND v.location = ? AND v.status = 'Available'");
			ps.setString(1, vtname);
			ps.setString(2, location);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vlicense = rs.getString("vlicense");
				odometer = rs.getInt("odometer");
				System.out.println(vlicense + " " + odometer);
			} else {
				System.out.println("Available vehicle of location and type does not exist :(");
				ReservationModel ResModel =
						new ReservationModel("Available vehicle of location and type does not exist :(");
				return ResModel;
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (4)");
		}

		// Return the max number of rid in vehicle
		// 8) confirmation number -> dlicence, vtName, fromDate, toDate
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT MAX(rt.rid) FROM rental rt");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				this.rid = rs.getInt(1) + 1;
				System.out.println("Max rid + 1: " + this.rid);
			} else {
				// If rental is empty
				this.rid = 1;
				System.out.println("Start rid: " + this.rid);
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (8)");
		}

		// Insert rent with reservation and input from user
		// 2) confirmation number, card name, card number, expiry date -> INSERT RENTAL
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO rental " +
                            "SELECT ?, v.vlicense, r.dlicense, r.fromDate, r.toDate, v.odometer, ?, ?, " +
                            "?, r.confno " +
                            "FROM reservation r, vehicle v " +
                            "WHERE ? = r.confno " +
								"AND v.vlicense = (" +
									"SELECT v2.VLICENSE " +
									"FROM vehicle v2 " +
									"WHERE v2.vtname = r.vtname AND v2.location = ? " +
										"AND v2.status = 'Available' AND ROWNUM = 1)");
			System.out.println("rid: " + rid);
			System.out.println("cardName: " + cardName);
			System.out.println("cardNumber: " + cardNumber);
			System.out.println("cardExpiryDate: " + cardExpiryDate);
			System.out.println("confNo: " + confNo);
			System.out.println("location: " + location);
			ps.setInt(1, rid);
			ps.setString(2, cardName);
			ps.setString(3, cardNumber);
			ps.setDate(4, Date.valueOf(cardExpiryDate + "-01"));
			ps.setInt(5, Integer.parseInt(confNo));
			ps.setString(6, location);
			int numRow = ps.executeUpdate();
			System.out.println("num rows: " + numRow);
			connection.commit();
			System.out.println("rental is made with num rows:");

			// Update car status to 'Rented'
            // 6) vlicense -> UPDATE VEHICLE
            try {
                PreparedStatement ps2 = connection.prepareStatement(
                        "UPDATE vehicle SET status = 'Rented' WHERE VLICENSE = ?");
                System.out.println(vlicense);
                ps2.setString(1, vlicense);
				System.out.println("Vehicle is updated in 'Rented'");
				ps2.executeUpdate();
				connection.commit();
            } catch (SQLException e) {
                rollbackConnection();
                System.out.println("Should not have reached here... (2)");
            }

		} catch (Exception e) {
			ReservationModel ResModel =
					new ReservationModel("Please insert valid input >:(");
			return ResModel;
		}

		ReservationModel ResModel = new ReservationModel(Integer.parseInt(confNo), vtname, dlicense, fromDate, toDate,
				rDate, rid, location);

		return ResModel;
	}

	public String insertRent(String dlicense, String fromDate, String fromTime, String toDate, String toTime, String vtname, String location, String cardName, String cardNumber, String cardExpiryDate) {
		String vlicense = null;
		int odometer = -1;

		// Check if the customer is in our database
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT * FROM customer c WHERE c.dlicense = ?");
			ps.setString(1, dlicense);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("Customer exist in our database :)");
			} else {
				System.out.println("Customer does not exist in our database :(");
				return "Not a registered user, please register first";
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (3)");
		}

		// Return vehicle based on given vehicle type and location
		// 4) vtname, location -> vlicense, odometer
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT v.vlicense, v.odometer FROM vehicle v " +
							"WHERE ROWNUM = 1 AND v.vtname = ? AND v.location = ? AND v.status = 'Available'");
			ps.setString(1, vtname);
			ps.setString(2, location);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vlicense = rs.getString("vlicense");
				odometer = rs.getInt("odometer");
				System.out.println(vlicense + " " + odometer);
			} else {
				System.out.println("Available vehicle of location and type does not exist :(");
				return "Available vehicle of location and type does not exist :(";
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (4)");
		}

		// Return the max number of rid in vehicle
		// 8) confirmation number -> dlicence, vtName, fromDate, toDate
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT MAX(rt.rid) FROM rental rt");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				this.rid = rs.getInt(1) + 1;
				System.out.println("Max rid + 1: " + this.rid);
			} else {
				// If rental is empty
				this.rid = 1;
				System.out.println("Start rid: " + this.rid);
			}
		} catch (SQLException e) {
			rollbackConnection();
			System.out.println("Should not have reached here... (8)");
		}

		// Insert rent without reservation and input from user
		// 2) driver license, from date, to date, card name, card number, expiry date, vehicle type, location -> INSERT RENTAL
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO rental SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, null " +
							"FROM vehicle v " +
							"WHERE ROWNUM = 1 " +
								"AND v.vtname = ? " +
								"AND v.location = ? " +
								"AND v.status = 'Available'");
//			System.out.println("rid: " + rid);
//			System.out.println("vlicense: " + vlicense);
//			System.out.println("dlicense: " + dlicense);
//			System.out.println("fromDate: " + fromDate);
//			System.out.println("toDate: " + toDate);
//			System.out.println("odometer: " + odometer);
//			System.out.println("cardName: " + cardName);
//			System.out.println("cardNumber: " + cardNumber);
//			System.out.println("cardExpiryDate: " + cardExpiryDate);
			ps.setInt(1, rid);
			ps.setString(2, vlicense);
			ps.setString(3, dlicense);
			ps.setTimestamp(4, java.sql.Timestamp.valueOf(fromDate + " " +  fromTime + ":00"));
			ps.setTimestamp(5, java.sql.Timestamp.valueOf(toDate + " " +  toTime + ":00"));
			ps.setInt(6, odometer);
			ps.setString(7, cardName);
			ps.setString(8, cardNumber);
			ps.setDate(9, Date.valueOf(cardExpiryDate + "-01"));
			ps.setString(10, vtname);
			ps.setString(11, location);
			int numRow = ps.executeUpdate();
			System.out.println("num rows: " + numRow);
			connection.commit();
			System.out.println("rental is made with num rows:");

			// Update car status to 'Rented'
			// 6) vlicense -> UPDATE VEHICLE
			try {
				PreparedStatement ps2 = connection.prepareStatement(
						"UPDATE vehicle SET status = 'Rented' WHERE VLICENSE = ?");
				System.out.println(vlicense);
				ps2.setString(1, vlicense);
				System.out.println("Vehicle is updated in 'Rented'");
				ps2.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				rollbackConnection();
				System.out.println("Should not have reached here... (2)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "Please insert valid input >:(";
		}
		String s = Integer.toString(rid);
		return "Successful: " + s;
	}
}

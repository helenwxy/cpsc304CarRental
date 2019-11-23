package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

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

  // the purpose of this is to display information on all vehicles rented out during the date
  // the entries are groups by branch and within each branch, the entires are group by vehicle category
  public ArrayList<VehicleModel> getRentalReportInfo1(String dateR, String locationR) {
    ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
    try {
      PreparedStatement ps = connection.prepareStatement("SELECT r.rid, r.vlicense, v.vtname, v.make, v.model, v.year, v.location, v.city " +
        "FROM vehicle v, rental r " +
        "WHERE v.vlicense = r.vlicense AND to_char(r.fromdate, 'YYYY-MM-DD') = ?");
      ps.setString(1,dateR);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        VehicleModel vm = new VehicleModel(rs.getInt(1), rs.getString(2),rs.getString(3),
          rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8));
        result.add(vm);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
//  public ArrayList<VehicleModel> getRentalReportInfo2(String dateR, String locationR) {
//    ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
//    PreparedStatement ps = null;
//    ResultSet rs = null;
//
//    try {
//      if (locationR == "") { // this is for all branches
//        ps = connection.prepareStatement(
//          "SELECT v.location, v.vtname, COUNT(r.rid) " +
//            "FROM rental r, vehicle v " +
//            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ?" +
//            "GROUP BY v.vtname, v.location"
//        );
//        ps.setDate(1, java.sql.Date.valueOf(dateR));
//        rs = ps.executeQuery();
//        while (rs.next()) {
//          VehicleModel model = new VehicleModel (
//            rs.getInt("rid"),
//            rs.getString("vlicense"),
//            rs.getString("vtname"),
//            rs.getString("make"),
//            rs.getString("model"),
//            rs.getInt("year"),
//            rs.getString("location"),
//            rs.getString("city"));
//          result.add(model);
//        }
////        rs.next();
//      } else { // this is for a single branch
//        ps = connection.prepareStatement(
//          "SELECT r.rid, r.vlicense, v.make, v.model, v.year, v.location, v.city " +
//            "FROM rental r, vehicle v " +
//            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? AND v.location = ?" +
//            "ORDER BY vtname"
//        );
//        ps.setDate(1, java.sql.Date.valueOf(dateR));
//        ps.setString(2, locationR);
//        rs = ps.executeQuery();
////        rs.next();
//        while (rs.next()) {
//          VehicleModel model = new VehicleModel (
//            rs.getInt("rid"),
//            rs.getString("vlicense"),
//            rs.getString("vtname"),
//            rs.getString("make"),
//            rs.getString("model"),
//            rs.getInt("year"),
//            rs.getString("location"),
//            rs.getString("city"));
//          result.add(model);
//        }
//      }
//      rs.close();
//      ps.close();
//    } catch (SQLException e) {
//      //
//    }
//    return result;
//  }
}

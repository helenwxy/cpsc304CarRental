package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

import ca.ubc.cs304.model.*;

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

		try {
			// the String to int conversion happens here
			odometer = Integer.parseInt(odo.trim());

			// print out the value after the conversion
//			System.out.println("int i = " + i);
		} catch (NumberFormatException nfe) {
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
			ps.setInt(1, rid);
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
			int weeks = (int) dateDifference / 7;
			int days = ((int) dateDifference) % 7;
			int hours = (int) (dateDifference * 24 % 24);
			float total = hrate * hours + hirate * hours + drate * days + dirate * days + wrate * weeks + wirate * weeks;
			System.out.println("week: " + weeks + "days: " + days + "hours :" + hours);
			System.out.println("hrate = " + hrate);
			System.out.println("date = " + date);

			// TODO: fix this!
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
			System.out.println("vehicle license: " + license);
			ps3.setInt(1, odometer);
			ps3.setString(2, license);
			ps3.executeUpdate();
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

  // the purpose of this is to display information on all vehicles rented out during the date
  // the entries are groups by branch and within each branch, the entires are group by vehicle category
  public ArrayList<VehicleModel> getRentalReportInfo1(String dateR, String locationR) {
    ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
    if (locationR.equals("")) { // all branches
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT r.rid, r.vlicense, v.vtname, v.make, v.model, v.year, v.location, v.city " +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? " +
            "ORDER BY v.location, v.vtname");
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
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT r.rid, r.vlicense, v.vtname, v.make, v.model, v.year, v.location, v.city " +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? AND v.location = ? " +
            "ORDER BY v.location, v.vtname");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          VehicleModel vm = new VehicleModel(rs.getInt(1), rs.getString(2),rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8));
          result.add(vm);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  // displays number of vehicle rented per category
  public ArrayList<ReportModel> getRentalReportInfo2(String dateR, String locationR) {
    ArrayList<ReportModel> result = new ArrayList<ReportModel>();
    if (locationR.equals("")) {
      try { // all branches
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.vtname, COUNT(r.rid)" +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? " +
            "GROUP BY v.vtname ");
        ps.setString(1,dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel (
            rs.getString(1),
            rs.getInt(2));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.vtname, COUNT(r.rid) " +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? AND v.location = ? " +
            "GROUP BY v.vtname  ");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel (
            rs.getString(1),
            rs.getInt(2));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  // displays number of vehicle rented per category
  public ArrayList<ReportModel> getRentalReportInfo3(String dateR, String locationR) {
    ArrayList<ReportModel> result = new ArrayList<ReportModel>();
    if (locationR.equals("")) {
      try { // all branches
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.location, COUNT(r.rid)" +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? " +
            "GROUP BY v.location ");
        ps.setString(1,dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel (
            rs.getInt(2),
          rs.getString(1));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.location, COUNT(r.rid) " +
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? AND v.location = ? " +
            "GROUP BY v.location  ");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel (
            rs.getInt(2),
            rs.getString(1));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  // displays number of vehicle rented for Company on dateR
  public ArrayList<ReportModel> getRentalReportInfo4(String dateR, String locationR) {
    ArrayList<ReportModel> result = new ArrayList<ReportModel>();
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT COUNT(r.rid) " +
            "FROM rental r " +
            "WHERE to_char(r.fromDate, 'YYYY-MM-DD') = ? ");
        ps.setString(1, dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel ("Total",
            rs.getInt(1));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    return result;
  }

  // the purpose of this is to display information on all vehicles rented out during the date
  // the entries are groups by branch and within each branch, the entires are group by vehicle category
  public ArrayList<VehicleModel> getReturnReportInfo1(String dateR, String locationR) {
    ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
    if (locationR.equals("")) { // all branches
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT r.rid, r.vlicense, v.vtname, v.make, v.model, v.year, v.location, v.city " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? " +
            "ORDER BY v.location, v.vtname");
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
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT r.rid, r.vlicense, v.vtname, v.make, v.model, v.year, v.location, v.city " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? AND v.location = ? " +
            "ORDER BY v.location, v.vtname");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          VehicleModel vm = new VehicleModel(rs.getInt(1), rs.getString(2),rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8));
          result.add(vm);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public ArrayList<ReportReturnModel> getReturnReportInfo2(String dateR, String locationR) {
    ArrayList<ReportReturnModel> result = new ArrayList<ReportReturnModel>();
    if (locationR.equals("")) {
      try { // all branches
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.vtname, COUNT(r.rid), SUM(t.value) " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? " +
            "GROUP BY v.vtname");
        ps.setString(1,dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportReturnModel model = new ReportReturnModel (
            rs.getString(1),
            rs.getInt(2),
            rs.getFloat(3));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.vtname, COUNT (r.rid), SUM(t.VALUE) " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? " +
            "AND v.location = ? " +
            "GROUP BY v.vtname");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportReturnModel model = new ReportReturnModel (
            rs.getString(1),
            rs.getInt(2),
            rs.getFloat(3));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public ArrayList<ReportReturnModel> getReturnReportInfo3(String dateR, String locationR) {
    ArrayList<ReportReturnModel> result = new ArrayList<ReportReturnModel>();
    if (locationR.equals("")) {
      try { // all branches
        PreparedStatement ps = connection.prepareStatement( // todo: Fix the SQL
          "SELECT v.location, COUNT (r.rid), SUM(t.VALUE) " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? " +
            "GROUP BY v.location");
        ps.setString(1,dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportReturnModel model = new ReportReturnModel (
            rs.getInt(2),
            rs.getFloat(3),
            rs.getString(1));
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else { // specific one branch
      try {
        PreparedStatement ps = connection.prepareStatement(
          "SELECT v.location, COUNT(r.rid), SUM(t.VALUE) " +
            "FROM rental r, vehicle v, return t " +
            "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? " +
            "AND v.location = ? " +
            "GROUP BY v.location ");
        ps.setString(1, dateR);
        ps.setString(2, locationR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportReturnModel model = new ReportReturnModel (
            rs.getInt(2),
            rs.getFloat(3),
            rs.getString(1) );
          result.add(model);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public ArrayList<ReportReturnModel> getReturnReportInfo4(String dateR, String locationR) {
    ArrayList<ReportReturnModel> result = new ArrayList<ReportReturnModel>();
    try { // all branches
      PreparedStatement ps = connection.prepareStatement(
        "SELECT COUNT(t.rid), SUM(t.VALUE) " +
          "FROM return t " +
          "WHERE to_char(t.rdate, 'YYYY-MM-DD') = ? ");
      ps.setString(1,dateR);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReportReturnModel model = new ReportReturnModel ("Grand Total", rs.getInt(1),
          rs.getFloat(2));
        result.add(model);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  // checks if a certain branch exists
  // uses the the participation constraint in ER where a branch with no cars cannot exist
  public boolean branchExists (String locationR) {
    boolean locexist = false;
    int branchHereNum = 0;
    try {
      PreparedStatement ps = connection.prepareStatement(
        "SELECT COUNT(*) " +
          "FROM vehicle v " +
          "WHERE v.location = ? ");
      ps.setString(1, locationR);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        branchHereNum = rs.getInt(1);
        locexist = (0 != branchHereNum);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return locexist;
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
							"WHERE r.confNo = ? AND ? NOT IN (SELECT rt.confno FROM rental rt)");
			ps.setString(1, confNo);
			ps.setString(2, confNo);
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
                            "SELECT ?, ?, r.dlicense, r.fromDate, r.toDate, ?, ?, ?, " +
                            "?, r.confno " +
                            "FROM reservation r WHERE ? = r.confno ");
			System.out.println("rid: " + rid);
			System.out.println("cardName: " + cardName);
			System.out.println("cardNumber: " + cardNumber);
			System.out.println("cardExpiryDate: " + cardExpiryDate);
			System.out.println("confNo: " + confNo);
			System.out.println("location: " + location);
			ps.setInt(1, rid);
			ps.setString(2, vlicense);
			ps.setInt(3, odometer);
			ps.setString(4, cardName);
			ps.setString(5, cardNumber);
			ps.setDate(6, Date.valueOf(cardExpiryDate + "-01"));
			ps.setInt(7, Integer.parseInt(confNo));
//			ps.setString(6, location);
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
					"INSERT INTO rental values (?, ?, ?, ?, ?, ?, ?, ?, ?, null) ");
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
//			ps.setString(10, vtname);
//			ps.setString(11, location);
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

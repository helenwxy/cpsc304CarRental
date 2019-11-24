package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

import ca.ubc.cs304.model.ReportModel;
import ca.ubc.cs304.model.ReportReturnModel;
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
            "FROM rental r, vehicle v " +
            "WHERE r.vlicense = v.vlicense AND to_char(r.fromDate, 'YYYY-MM-DD') = ? ");
        ps.setString(1, dateR);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          ReportModel model = new ReportModel (
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
      PreparedStatement ps = connection.prepareStatement( // todo: Fix the SQL
        "SELECT SUM(t.VALUE) " +
          "FROM rental r, vehicle v, return t " +
          "WHERE r.vlicense = v.vlicense AND r.rid = t.rid AND to_char(t.rdate, 'YYYY-MM-DD') = ? ");
      ps.setString(1,dateR);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReportReturnModel model = new ReportReturnModel (
          rs.getFloat(1));
        result.add(model);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
}

package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.ReportModel;
import ca.ubc.cs304.model.VehicleModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainWindow;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Rental implements LoginWindowDelegate, RentalTransactionDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	public Rental() {
		dbHandler = new DatabaseConnectionHandler();
	}

	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}

	/**
	 * LoginWindowDelegate Implementation
	 *
     * connects to Oracle database with supplied username and password
     */
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();
			MainWindow mainWindow = new MainWindow();
			mainWindow.showFrame(this);
//			TerminalTransactions transaction = new TerminalTransactions();
//			transaction.showMainMenu(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	/**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Insert a branch with the given info
	 */
//    public void insertBranch(BranchModel model) {
//    	dbHandler.insertBranch(model);
//    }
//
//    /**
//	 * TermainalTransactionsDelegate Implementation
//	 *
//	 * Delete branch with given branch ID.
//	 */
//    public void deleteBranch(int branchId) {
//    	dbHandler.deleteBranch(branchId);
//    }

    /**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Update the branch name for a specific ID
	 */

//    public void updateBranch(int branchId, String name) {
//    	dbHandler.updateBranch(branchId, name);
//    }

    /**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Displays information about varies bank branches.
	 */
//    public void showBranch() {
//    	BranchModel[] models = dbHandler.getBranchInfo();
//
//    	for (int i = 0; i < models.length; i++) {
//    		BranchModel model = models[i];
//
//    		// simplified output formatting; truncation may occur
//    		System.out.printf("%-10.10s", model.getId());
//    		System.out.printf("%-20.20s", model.getName());
//    		if (model.getAddress() == null) {
//    			System.out.printf("%-20.20s", " ");
//    		} else {
//    			System.out.printf("%-20.20s", model.getAddress());
//    		}
//    		System.out.printf("%-15.15s", model.getCity());
//    		if (model.getPhoneNumber() == 0) {
//    			System.out.printf("%-15.15s", " ");
//    		} else {
//    			System.out.printf("%-15.15s", model.getPhoneNumber());
//    		}
//
//    		System.out.println();
//    	}
//    }

    /**
	 * TerminalTransactionsDelegate Implementation
	 *
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
    	dbHandler.close();
    	dbHandler = null;

    	System.exit(0);
    }

	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		Rental rental = new Rental();
		rental.start();
	}

	@Override
	public ArrayList<VehicleModel> showQualifiedVehicle(String vtname, String location) {
		return dbHandler.getVehicleInfo(vtname, location);
	}

	@Override
	public int makeReservation(String phoneno, String vtname, String location, String fromdate, String fromtime, String todate, String totime) throws SQLException {
		return dbHandler.insertReservation(phoneno, vtname, location, fromdate, fromtime, todate, totime);
	}

	@Override
	public boolean insertNewCustomer(String dlicense, String name, String address, String phone) {
		return dbHandler.insertNewCustomer(dlicense, name, address, phone);
	}

  @Override
  public ArrayList<VehicleModel> showRentalReport1(String date, String location) {
    return dbHandler.getRentalReportInfo1(date, location);
  }

  @Override
  public ArrayList<ReportModel> showRentalReport2(String date, String location) {
    return dbHandler.getRentalReportInfo2(date, location);
  }

  @Override
  public ArrayList<ReportModel> showRentalReport3(String date, String location) {
    return dbHandler.getRentalReportInfo3(date, location);
  }

//  @Override
//  public ArrayList<ReportModel> showRentalReport4(String date, String location) {
//    return dbHandler.getRentalReportInfo4(date, location);
//  }
}

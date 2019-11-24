package ca.ubc.cs304.delegates;


import ca.ubc.cs304.model.ReportModel;
import ca.ubc.cs304.model.ReportReturnModel;
import ca.ubc.cs304.controller.Rental;
import ca.ubc.cs304.model.RentalModel;
import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface RentalTransactionDelegate {

    ArrayList<VehicleModel> showQualifiedVehicle(String vtname, String location);

    int makeReservation(String phoneno, String vtname, String location, String fromdate, String fromtime,
                            String todate, String totime) throws SQLException;

    boolean insertNewCustomer(String dlicense, String name, String address, String phone);

    // This report contains information on all the vehicles rented out during the day.
  // The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category.
  // this will bre able to deal with one or all branches
  ArrayList<VehicleModel> showRentalReport1(String date, String location);

  // Rental report displays number of vehicle rented per category
  // this will bre able to deal with one or all branches
  ArrayList<ReportModel> showRentalReport2(String date, String location);

  // Rental report displays number of vehicle rented per branch
  // this will bre able to deal with one or all branches
  ArrayList<ReportModel> showRentalReport3(String date, String location);

  // rental report displays new rentals for the whole company
  // this will bre able to deal with one or all branches
  ArrayList<ReportModel> showRentalReport4(String date, String location);

  // This report contains information on all the vehicles rented out during the day.
  // The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category.
  // this will bre able to deal with one or all branches
  ArrayList<VehicleModel> showReturnReport1(String date, String location);

  // Rental report displays number of vehicle rented per category
  // this will bre able to deal with one or all branches
  ArrayList<ReportReturnModel> showReturnReport2(String date, String location);

  // Rental report displays number of vehicle rented per branch
  // this will bre able to deal with one or all branches
  ArrayList<ReportReturnModel> showReturnReport3(String date, String location);

  // rental report displays new rentals for the whole company
  // this will bre able to deal with one or all branches
  ArrayList<ReportReturnModel> showReturnReport4(String date, String location);

  boolean branchExists (String location);

  ReservationModel makeRentalWithReservation(String confNo, String location, String cardName, String cardNumber, String cardExpiryDate);

  String makeRental(String dlicense, String fromDate, String fromTime, String toDate, String toTime, String vtname, String location, String carName, String cardNumber, String cardExpiryDate);
}


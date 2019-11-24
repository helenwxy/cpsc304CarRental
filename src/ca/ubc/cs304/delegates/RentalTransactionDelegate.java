package ca.ubc.cs304.delegates;

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

    ReservationModel makeRentalWithReservation(String confNo, String location, String cardName, String cardNumber, String cardExpiryDate);

    String makeRental(String dlicense, String fromDate, String fromTime, String toDate, String toTime, String vtname, String location, String carName, String cardNumber, String cardExpiryDate);
}


package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface RentalTransactionDelegate {

    ArrayList<VehicleModel> showQualifiedVehicle(String vtname, String location);

    int makeReservation(String phoneno, String vtname, String location, String fromdate, String fromtime,
                            String todate, String totime) throws SQLException;
}

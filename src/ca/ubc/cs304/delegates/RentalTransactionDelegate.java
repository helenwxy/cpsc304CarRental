package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.ReturnModel;
import ca.ubc.cs304.model.VehicleModel;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RentalTransactionDelegate {

    ArrayList<VehicleModel> showQualifiedVehicle(String vtname, String location);

    int makeReservation(String phoneno, String vtname, String location, String fromdate, String fromtime,
                            String todate, String totime) throws SQLException;

    boolean insertNewCustomer(String dlicense, String name, String address, String phone);

    int getRid(String vlicense);

    ReturnModel insertReturn(int rid, String vlicense, String odo, String tank);
}

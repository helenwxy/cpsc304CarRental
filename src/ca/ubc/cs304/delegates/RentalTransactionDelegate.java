package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import java.util.ArrayList;

public interface RentalTransactionDelegate {
    ArrayList<VehicleModel> showQualifiedVehicle(String vtname, String location);
}

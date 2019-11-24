package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewVehicleWindow extends JFrame implements ActionListener {
    String[] vtypeString = {"", "Economy", "Compact", "Mid-size", "Standard", "Full-size",
    "SUV", "Truck"};
    private static final int TEXT_FIELD_WIDTH = 10;
    private RentalTransactionDelegate delegate;
    JPanel panel = new JPanel();
    private JTextField locationField;
    private JTextField fromdateField;
    private JTextField todateField;
    private JTextField fromtimeField;
    private JTextField totimeField;
    private JButton submit;
    private JButton back;
    private JComboBox vtname;

    public ViewVehicleWindow() {
        super("Car Rental");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;

        JLabel vtnameLabel = new JLabel("Enter vehicle type: ");
        JLabel locationLabel = new JLabel("Enter your location: ");
        JLabel fromdateLabel = new JLabel("Enter pickup date (yyyy-mm-dd): ");
        JLabel fromtimeLabel = new JLabel("Enter pickup time (hh:mm in 24 hour): ");
        JLabel todateLabel = new JLabel("Enter return date (yyyy-mm-dd): ");
        JLabel totimeLabel = new JLabel("Enter return time (hh:mm in 24 hour): ");

        locationField = new JTextField(TEXT_FIELD_WIDTH);
        fromdateField = new JTextField(TEXT_FIELD_WIDTH);
        fromtimeField = new JTextField(TEXT_FIELD_WIDTH);
        todateField = new JTextField(TEXT_FIELD_WIDTH);
        totimeField = new JTextField(TEXT_FIELD_WIDTH);

        vtname = new JComboBox(vtypeString);
        submit = new JButton("submit");
        back = new JButton("back");
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        vtname.setSelectedIndex(0);

        // vtname label & combobox
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(vtnameLabel, c);
        panel.add(vtnameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(vtname, c);
        panel.add(vtname);

        // Location label & textbox
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(locationLabel, c);
        panel.add(locationLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(locationField, c);
        panel.add(locationField);

        // Time interval label & text
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(fromdateLabel, c);
        panel.add(fromdateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(fromdateField, c);
        panel.add(fromdateField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(fromtimeLabel, c);
        panel.add(fromtimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(fromtimeField, c);
        panel.add(fromtimeField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(todateLabel, c);
        panel.add(todateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(todateField, c);
        panel.add(todateField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(totimeLabel, c);
        panel.add(totimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(totimeField, c);
        panel.add(totimeField);
        // place the buttons button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(submit, c);
        panel.add(submit);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(back, c);
        panel.add(back);
//
//        // register login button with action event handler
//        loginButton.addActionListener(this);
        submit.addActionListener(this);
        back.addActionListener(this);
        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
//            ArrayList<VehicleModel> vehicle = delegate.showQualifiedVehicle((String)vtname.getSelectedItem(),locationField.getText());
            String[] fromDate = fromdateField.getText().split("-",-1);
            String[] toDate = fromdateField.getText().split("-",-1);
            Integer fromYear = Integer.valueOf(fromDate[0]);
            Integer toYear = Integer.valueOf(toDate[0]);
            if (fromYear < 2010 || toYear < 2010) {
                PopupBox.infoBox("Please enter a valid time period", "Error");
            } else {
                this.dispose();
                new DisplayVehicleWindow((String) vtname.getSelectedItem(), locationField.getText()).showFrame(delegate);
            }
        } else if (e.getSource() == back) {
            this.dispose();
            new CustomerWindow().showFrame(delegate);
        }
    }
}


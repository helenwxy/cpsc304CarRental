package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReservationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentWithReservationWindow extends JFrame implements ActionListener {
    private static final int TEXT_FIELD_WIDTH = 10;
    private RentalTransactionDelegate delegate;
    JPanel panel = new JPanel();
    private JTextField confNumrField;
    private JTextField locationField;
    private JTextField cardNameField;
    private JTextField cardNumField;
    private JTextField expDateField;
    private JButton rent = new JButton("rent");
    private JButton back = new JButton("back");

    public ClerkRentWithReservationWindow() {
        super("Car Rental");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel confNumLabel = new JLabel("Enter confirmation number: ");
        JLabel locationLabel = new JLabel("Enter location: ");
        JLabel cardNameLabel = new JLabel("Enter card name (Only accepts Mastercard / Visa): ");
        JLabel cardNumLabel = new JLabel("Enter card number: ");
        JLabel expDateLabel = new JLabel("Enter expiry date (yyyy-mm): ");

        confNumrField = new JTextField(TEXT_FIELD_WIDTH);
        locationField = new JTextField(TEXT_FIELD_WIDTH);
        cardNameField = new JTextField(TEXT_FIELD_WIDTH);
        cardNumField = new JTextField(TEXT_FIELD_WIDTH);
        expDateField = new JTextField(TEXT_FIELD_WIDTH);

        // Confirmation number label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(confNumLabel, c);
        panel.add(confNumLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(confNumrField, c);
        panel.add(confNumrField);

        // Location label & textbox
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(locationLabel, c);
        panel.add(locationLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(locationField, c);
        panel.add(locationField);

        // Card name label & text
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(cardNameLabel, c);
        panel.add(cardNameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(cardNameField, c);
        panel.add(cardNameField);

        // Card number label & text
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(cardNumLabel, c);
        panel.add(cardNumLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(cardNumField, c);
        panel.add(cardNumField);

        // Card expiry date label & text
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(expDateLabel, c);
        panel.add(expDateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(expDateField, c);
        panel.add(expDateField);

        // place the buttons button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(rent, c);
        panel.add(rent);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(back, c);
        panel.add(back);
//
//        // register login button with action event handler
//        loginButton.addActionListener(this);
        rent.addActionListener(this);
        back.addActionListener(this);

        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rent) {

            ReservationModel reservationModel = delegate.makeRentalWithReservation(confNumrField.getText(),
                    locationField.getText(), cardNameField.getText(), cardNumField.getText(), expDateField.getText());

            if (reservationModel.getErrorMsg() == null) {
                String msg = String.format("Rent ID : %d\n" +
                                "Date of reservation: %s\n" +
                                "Location: %s\n" +
                                "Vehicle type: %s\n" +
                                "Pickup Date: %s\n" +
                                "Return Date: %s",
                        reservationModel.getrid(),
                        reservationModel.getrDate(),
                        reservationModel.getlocation(),
                        reservationModel.getvtname(),
                        reservationModel.getfromDate(),
                        reservationModel.gettoDate());
                PopupBox.infoBox(msg, "Receipt");
            } else {
                String errorMsg = reservationModel.getErrorMsg();
                PopupBox.infoBox(errorMsg, "ERROR");
            }
        } else if (e.getSource() == back) {
            this.dispose();
            new ClerkRentReservationWindow().showFrame(delegate);
        }
    }
}


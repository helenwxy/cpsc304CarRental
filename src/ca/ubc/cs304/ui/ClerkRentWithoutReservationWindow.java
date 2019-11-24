package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReservationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentWithoutReservationWindow extends JFrame implements ActionListener {
    private static final int TEXT_FIELD_WIDTH = 10;
    private RentalTransactionDelegate delegate;
    JPanel panel = new JPanel();
    private JTextField dlicnseField;
    private JTextField fromDateField;
    private JTextField fromTimeField;
    private JTextField toDateField;
    private JTextField toTimeField;
    private JTextField vtnameField;
    private JTextField locationField;
    private JTextField cardNameField;
    private JTextField cardNumField;
    private JTextField expDateField;
    private JButton rent = new JButton("rent");
    private JButton back = new JButton("back");

    public ClerkRentWithoutReservationWindow() {
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

        JLabel dlicnseLabel = new JLabel("Enter driver license: ");
        JLabel vtnameLabel = new JLabel("Enter vehicle type: ");
        JLabel locationLabel = new JLabel("Enter location: ");
        JLabel fromDateLabel = new JLabel("Enter pickup date (YYYY-MM-DD): ");
        JLabel fromTimeLabel = new JLabel("Enter pickup time (HH:MM): ");
        JLabel toDateLabel = new JLabel("Enter return date (YYYY-MM-DD): ");
        JLabel toTimeLabel = new JLabel("Enter return time (HH:MM): ");
        JLabel cardNameLabel = new JLabel("Enter card name (Only accepts Mastercard / Visa): ");
        JLabel cardNumLabel = new JLabel("Enter card number: ");
        JLabel expDateLabel = new JLabel("Enter expiry date (yyyy-mm): ");

        dlicnseField = new JTextField(TEXT_FIELD_WIDTH);
        fromDateField = new JTextField(TEXT_FIELD_WIDTH);
        fromTimeField = new JTextField(TEXT_FIELD_WIDTH);
        toDateField = new JTextField(TEXT_FIELD_WIDTH);
        toTimeField = new JTextField(TEXT_FIELD_WIDTH);
        vtnameField = new JTextField(TEXT_FIELD_WIDTH);
        locationField = new JTextField(TEXT_FIELD_WIDTH);
        cardNameField = new JTextField(TEXT_FIELD_WIDTH);
        cardNumField = new JTextField(TEXT_FIELD_WIDTH);
        expDateField = new JTextField(TEXT_FIELD_WIDTH);

        // Driver license label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(dlicnseLabel, c);
        panel.add(dlicnseLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(dlicnseField, c);
        panel.add(dlicnseField);

        // Pickup date label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(fromDateLabel, c);
        panel.add(fromDateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(fromDateField, c);
        panel.add(fromDateField);

        // Pickup time label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(fromTimeLabel, c);
        panel.add(fromTimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(fromTimeField, c);
        panel.add(fromTimeField);

        // Return date label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(toDateLabel, c);
        panel.add(toDateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(toDateField, c);
        panel.add(toDateField);

        // Return time label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(toTimeLabel, c);
        panel.add(toTimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(toTimeField, c);
        panel.add(toTimeField);

        // Vehicle type label & textbook
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(vtnameLabel, c);
        panel.add(vtnameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(vtnameField, c);
        panel.add(vtnameField);

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

            String resultMsg = delegate.makeRental(dlicnseField.getText(), fromDateField.getText(), fromTimeField.getText(), toDateField.getText(), toTimeField.getText(), vtnameField.getText(),
                    locationField.getText(), cardNameField.getText(), cardNumField.getText(), expDateField.getText());

            if (resultMsg.contains("Successful")) {
                String popupMsg = String.format("Rent ID : %s\n" +
                                "Location: %s\n" +
                                "Vehicle type: %s\n" +
                                "Pickup Date: %s %s\n" +
                                "Return Date: %s %s",
                        resultMsg.split(" ")[1],
                        locationField.getText(),
                        vtnameField.getText(),
                        fromDateField.getText(),
                        fromTimeField.getText(),
                        toDateField.getText(),
                        toTimeField.getText());
                PopupBox.infoBox(popupMsg, "Receipt");
            } else {
                PopupBox.infoBox(resultMsg, "ERROR");
            }
        } else if (e.getSource() == back) {
            this.dispose();
            new ClerkRentReservationWindow().showFrame(delegate);
        }
    }
}


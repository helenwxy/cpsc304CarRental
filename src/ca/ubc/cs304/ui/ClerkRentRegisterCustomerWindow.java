package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Rental;
import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentRegisterCustomerWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    private static final int TEXT_FIELD_WIDTH = 10;
    private JPanel panel;
    private JButton submit;
    private JButton back;
    private JTextField dlicense;
    private JTextField name;
    private JTextField address;
    private JTextField phone;
    public ClerkRentRegisterCustomerWindow() {
        super("Register Customer");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name (First Last): ");
        JLabel dlicenseLabel = new JLabel("Driver's License");
        JLabel addressLabel = new JLabel("Address ");
        JLabel phoneLabel = new JLabel("Phone number (number only): ");

        name = new JTextField(TEXT_FIELD_WIDTH);
        dlicense = new JTextField(TEXT_FIELD_WIDTH);
        address = new JTextField(TEXT_FIELD_WIDTH);
        phone = new JTextField(TEXT_FIELD_WIDTH);
        submit = new JButton("submit");
        back = new JButton("back");
        panel = new JPanel();
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(nameLabel, c);
        panel.add(nameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(name, c);
        panel.add(name);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(dlicenseLabel, c);
        panel.add(dlicenseLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(dlicense, c);
        panel.add(dlicense);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(addressLabel, c);
        panel.add(addressLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(address, c);
        panel.add(address);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(phoneLabel, c);
        panel.add(phoneLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(phone, c);
        panel.add(phone);

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
        if (e.getSource() == back) {
            this.dispose();
            new ClerkRentReservationWindow().showFrame(delegate);
        } else if (e.getSource() == submit) {
            if (dlicense.getText().equals("") || name.getText().equals("") || address.getText().equals("")) {
                PopupBox.infoBox("Please ensure all information is entered!", "Information");
            } else if (!delegate.insertNewCustomer(dlicense.getText(), name.getText(), address.getText(), phone.getText())) {
                PopupBox.infoBox("Your account already exists", "Information");
            } else {
                PopupBox.infoBox("Registration successful", "Information");
                this.dispose();
                new ClerkRentWithoutReservationWindow().showFrame(delegate);
            }
        }
    }
}

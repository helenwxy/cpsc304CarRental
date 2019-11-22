package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentMainWindow extends JFrame implements ActionListener {
    private static final int TEXT_FIELD_WIDTH = 10;
    private RentalTransactionDelegate delegate;
    private JTextField confirmationField;
    private boolean isConfNumEmpty = true;

    JButton rent_button = new JButton("Rent");
    JButton newCustomer_button = new JButton("New Customer");
    JButton back = new JButton("back");
    JPanel panel = new JPanel();
    public ClerkRentMainWindow() {
        super("Clerk renting and return");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;

        JLabel confirmationLabel = new JLabel("Enter confirmation number: ");
        confirmationField = new JTextField(TEXT_FIELD_WIDTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(confirmationLabel, c);
        panel.add(confirmationLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(confirmationField, c);
        panel.add(confirmationField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(rent_button, c);
        panel.add(rent_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(newCustomer_button, c);
        panel.add(newCustomer_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(newCustomer_button, c);
        panel.add(back);

        rent_button.addActionListener(this);
        newCustomer_button.addActionListener(this);
        back.addActionListener(this);

        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newCustomer_button) {
            this.dispose();
            // TODO: need to change it new customer window
            new ClerkWindow().showFrame(delegate);
        } else if (e.getSource() == rent_button) {
            this.dispose();
            // TODO: need to provide a window for Clerk to input customer's info similarly to reservation
            new ClerkWindow().showFrame(delegate);
        } else if (e.getSource() == back) {
            this.dispose();
            new ClerkWindow().showFrame(delegate);
        }
    }
}

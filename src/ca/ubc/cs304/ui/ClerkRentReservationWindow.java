package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentReservationWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    JButton reserved_button = new JButton("Reserved customer");
    JButton nonreserved_button = new JButton("Non-reserved customer");
    JButton back = new JButton("back");
    JPanel panel = new JPanel();
    public ClerkRentReservationWindow() {
        super("Clerk Rent Reservation Window");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(reserved_button, c);
        panel.add(reserved_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(nonreserved_button, c);
        panel.add(nonreserved_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(back, c);
        panel.add(back);

        reserved_button.addActionListener(this);
        nonreserved_button.addActionListener(this);
        back.addActionListener(this);

        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reserved_button) {
            this.dispose();
            new ClerkRentWithReservationWindow().showFrame(delegate);
        } else if (e.getSource() == nonreserved_button) {
            this.dispose();
            new ClerkRentCustomerWindow().showFrame(delegate);
        } else if (e.getSource() == back) {
            this.dispose();
            new ClerkWindow().showFrame(delegate);
        }
    }
}

package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClerkRentCustomerWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    JButton new_button = new JButton("New customer");
    JButton existing_button = new JButton("Existing customer");
    JButton back = new JButton("back");
    JPanel panel = new JPanel();
    public ClerkRentCustomerWindow() {
        super("Clerk Rent Customer Window");
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
        gb.setConstraints(new_button, c);
        panel.add(new_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(existing_button, c);
        panel.add(existing_button);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(existing_button, c);
        panel.add(back);

        new_button.addActionListener(this);
        existing_button.addActionListener(this);
        back.addActionListener(this);

        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == new_button) {
            this.dispose();
            new ClerkRentRegisterCustomerWindow().showFrame(delegate);
        } else if (e.getSource() == existing_button) {
            this.dispose();
            new ClerkRentWithoutReservationWindow().showFrame(delegate);
        } else if (e.getSource() == back) {
            this.dispose();
            new ClerkRentReservationWindow().showFrame(delegate);
        }
    }
}

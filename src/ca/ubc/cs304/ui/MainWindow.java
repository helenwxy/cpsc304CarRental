package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    JButton b1 = new JButton("Customer");
    JButton b2= new JButton("Clerk");
    JPanel panel = new JPanel();
    public MainWindow() {
        super("Car Rental");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(b1, c);
        panel.add(b1);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(b2, c);
        panel.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            this.dispose();
            new CustomerWindow().showFrame(delegate);
        } else if (e.getSource() == b2) {
            this.dispose();
//            new ClerkWindow().showFrame(delegate);
            new ClerkWindow().showFrame(delegate);

        }
    }
}

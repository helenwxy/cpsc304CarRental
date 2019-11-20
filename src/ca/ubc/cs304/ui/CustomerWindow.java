package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    JButton b1 = new JButton("View Vehicles");
    JButton b2= new JButton("Reserve a vehicle");
    JButton back = new JButton("back");
    JPanel panel = new JPanel();
    public CustomerWindow() {
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

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(b2, c);
        panel.add(back);

        b1.addActionListener(this);
        b2.addActionListener(this);
        back.addActionListener(this);
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            this.dispose();
            new ViewVehicleWindow().showFrame(delegate);
        } else if (e.getSource() == b2) {
            this.dispose();
            new ReserveVehicleWindow();
        } else if (e.getSource() == back) {
            this.dispose();
            new MainWindow().showFrame(delegate);
        }
    }
}

package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportWindow extends JFrame implements ActionListener {
  JButton b1 = new JButton("Daily Rentals for All Branches");
  JButton b2 = new JButton("Daily Rentals for Current Branch");
  JButton b3 = new JButton("Daily Returns for All Branches");
  JButton b4 = new JButton("Daily Returns for Current Branch");
  JButton b5 = new JButton("back");
  JPanel panel = new JPanel();
  public ReportWindow() {
    super("Car Rental");
  }

  public void showFrame(RentalTransactionDelegate delegate) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(300,300);
    JPanel panel = new JPanel();
    this.setContentPane(panel);

    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    panel.setLayout(gb);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // this is for generating report daily rentals for All branches
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(b1, c);
    panel.add(b1);

    // this is for generating report daily rentals for current branch
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(b2, c);
    panel.add(b2);

    // this is for generating report daily returns for All branches
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(b3, c);
    panel.add(b3);

    // this is for generating report for daily returns for current branch
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(b4, c);
    panel.add(b4);

    // this is for Back
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(b5, c);
    panel.add(b5);

    b1.addActionListener(this); // rent All
    b2.addActionListener(this); // rent one branch
    b3.addActionListener(this); // return All
    b4.addActionListener(this); // return one branch
    b5.addActionListener(this); // back
    Dimension d = this.getToolkit().getScreenSize();
    Rectangle r = this.getBounds();
    this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
    this.setVisible(true);
  }

  // todo:
  @Override
  public void actionPerformed(ActionEvent e) {

  }
}

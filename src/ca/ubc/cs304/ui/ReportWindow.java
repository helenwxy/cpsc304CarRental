package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportWindow extends JFrame implements ActionListener {
  private RentalTransactionDelegate delegate;
  JButton b1 = new JButton("Get Report for Rentals");
  JButton b2 = new JButton("Get Report for Returns");
  JButton backbutton = new JButton("back");
  JPanel panel = new JPanel();
  public ReportWindow() {
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

    // this is for Back
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(backbutton, c);
    panel.add(backbutton);

    b1.addActionListener(this); // rent report
    b2.addActionListener(this); // return report
    backbutton.addActionListener(this); // back
    Dimension d = this.getToolkit().getScreenSize();
    Rectangle r = this.getBounds();
    this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == backbutton) {
      this.dispose();
      new ClerkWindow().showFrame(delegate);
    } else if (e.getSource() == b1) { // rental report
      this. dispose();
      new RentalReportWindow().showFrame(delegate);
    } else if (e.getSource() == b2) { // return report
      this.dispose();
      new ReportReturnWindow().showFrame(delegate);
    }
  }
}

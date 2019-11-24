package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RentalReportWindow extends JFrame implements ActionListener {
//  String[] vtypeString = {"", "Economy", "Compact", "Mid-sized", "Standard", "Full-size",
//    "SUV", "Truck"};
  private static final int TEXT_FIELD_WIDTH = 10;
  private RentalTransactionDelegate delegate;
  JPanel panel = new JPanel();
//  private JTextField vtnameField;
  private JTextField locationField;
  private JTextField rentaldateField;
  private JButton submit;
  private JButton back;
//  private JComboBox vtname;

  public RentalReportWindow() {
    super("RentalReportWindow");
  }

  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;

    JLabel vtnameLabel = new JLabel("Enter vehicle type: ");
    JLabel locationLabel = new JLabel("Enter your branch location (leave empty to get all branches): ");
    JLabel rentaldateaLabel = new JLabel("Enter rental date (yyyy-mm-dd): ");

//    vtnameField = new JTextField(TEXT_FIELD_WIDTH);
    locationField = new JTextField(TEXT_FIELD_WIDTH);
    rentaldateField = new JTextField(TEXT_FIELD_WIDTH);

//    vtname = new JComboBox(vtypeString);
    submit = new JButton("submit");
    back = new JButton("back");
    this.setContentPane(panel);

    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    panel.setLayout(gb);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//    vtname.setSelectedIndex(0);

    // vtname label & combobox
//    c.gridwidth = GridBagConstraints.RELATIVE;
//    c.insets = new Insets(10, 10, 5, 0);
//    gb.setConstraints(vtnameLabel, c);
//    panel.add(vtnameLabel);

//    c.gridwidth = GridBagConstraints.REMAINDER;
//    c.insets = new Insets(10, 0, 5, 10);
//    gb.setConstraints(vtname, c);
//    panel.add(vtname);

    // Location label & textbox
    c.gridwidth = GridBagConstraints.RELATIVE;
    c.insets = new Insets(0, 10, 10, 0);
    gb.setConstraints(locationLabel, c);
    panel.add(locationLabel);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(0, 0, 10, 10);
    gb.setConstraints(locationField, c);
    panel.add(locationField);

    // Time interval label & text
    c.gridwidth = GridBagConstraints.RELATIVE;
    c.insets = new Insets(0, 10, 10, 0);
    gb.setConstraints(rentaldateaLabel, c);
    panel.add(rentaldateaLabel);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(0, 0, 10, 10);
    gb.setConstraints(rentaldateField, c);
    panel.add(rentaldateField);

    // place the buttons button
    // submit button
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(submit, c);
    panel.add(submit);
    // back button
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 10, 10, 10);
    c.anchor = GridBagConstraints.CENTER;
    gb.setConstraints(back, c);
    panel.add(back);
//
//        // register login button with action event handler
//        loginButton.addActionListener(this);
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
    if (e.getSource() == submit) {
//            this.dispose();
            if (!delegate.branchExists(locationField.getText()) &&
            !locationField.getText().equals("")) {
              PopupBox.infoBox("We don't have a branch there", "No Branch");
            } else {
              this.dispose();
              new DisplayReportRentalWindow((String) rentaldateField.getText(), locationField.getText()).showFrame(delegate);
            }
    } else if (e.getSource() == back) {
      this.dispose();
      new ReportWindow().showFrame(delegate);
    }

  }
}


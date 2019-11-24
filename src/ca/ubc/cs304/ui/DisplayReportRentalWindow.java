package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayReportRentalWindow extends JFrame implements ActionListener {
  private JTable table1;
  private String date;
  private String location;
//  private JButton numVehicle;
  private RentalTransactionDelegate delegate;
  private JButton back;
  private JButton countCategory;
  private JButton countBranch;
  private JButton countCompany;
  private JScrollPane sp;

  public DisplayReportRentalWindow(String date, String location) {
    super("DisplayReportRentalWindow");
    this.date = date;
    this.location = location;
  }
  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;
    ArrayList<VehicleModel> vlist = delegate.showRentalReport1(date, location);
    back = new JButton("Back");
    countCategory = new JButton("Count Rents/category");
    countBranch = new JButton("Count Rents/branch");
    countCompany = new JButton("All Rents for the day");
    if (vlist.isEmpty()) {
      this.setLayout(new BorderLayout());
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("No rentals for the day."));
      JPanel botPanel = new JPanel();
      botPanel.add(back);
      botPanel.add(countCategory);
      botPanel.add(countBranch);
      botPanel.add(countCompany);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(botPanel, BorderLayout.SOUTH);
      back.addActionListener(this);
      countCategory.addActionListener(this);
      countBranch.addActionListener(this);
      countCompany.addActionListener(this);
      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
      this.setVisible(true);
    } else {
      this.setLayout(new BorderLayout());

      JPanel panel1 = new JPanel();
      GridBagLayout gb = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      JLabel label = new JLabel("Number of Available Vehicles: (click to expand)");

      JPanel panel2 = new JPanel();
      panel2.add(back);
      panel2.add(countCategory);
      panel2.add(countBranch);
      panel2.add(countCompany);

      String[] colNames = {"Rental Id", "Vehicle License", "Vehicle Type", "Make", "Model", "Year", "Location", "City"};
      String[][] array = new String[vlist.size()][];
      for (int i = 0; i < vlist.size(); i++) {
        VehicleModel v = vlist.get(i);
        String[] attr = {Integer.toString(v.getRid()), v.getVlicense(),
          v.getVtname(), v.getMake(), v.getModel(), String.valueOf(v.getYear()), v.getLocation(), v.getCity()};
        array[i] = attr;
      }
      table1 = new JTable(array, colNames);
      for (int column = 0; column < table1.getColumnCount(); column++)
      {
        TableColumn tableColumn = table1.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();
        int maxWidth = tableColumn.getMaxWidth();

        for (int row = 0; row < table1.getRowCount(); row++)
        {
          TableCellRenderer cellRenderer = table1.getCellRenderer(row, column);
          Component c = table1.prepareRenderer(cellRenderer, row, column);
          int width = c.getPreferredSize().width + table1.getIntercellSpacing().width;
          preferredWidth = Math.max(preferredWidth, width);

          //  We've exceeded the maximum width, no need to check other rows

          if (preferredWidth >= maxWidth)
          {
            preferredWidth = maxWidth;
            break;
          }
        }

        tableColumn.setPreferredWidth( preferredWidth );
      }
//            table.setBounds(30, 40, 200, 300);
      sp = new JScrollPane(table1);
      panel1.add(sp);

      this.add(panel1, BorderLayout.NORTH);
      this.add(panel2, BorderLayout.SOUTH);
      back.addActionListener(this);
      countCategory.addActionListener(this);
      countBranch.addActionListener(this);
      countCompany.addActionListener(this);

      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
      this.setVisible(true);
    }
   }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == back) {
    this.dispose();
    new ReportWindow().showFrame(delegate);
    } else if (e.getSource() == countCategory) {
      this.dispose();
      new DisplayReportRentalCountCategoryWindow(this.date,this.location).showFrame(delegate);
    } else if (e.getSource() == countBranch) {
      this.dispose();
      new DisplayReportRentalCountBranchWindow(this.date,this.location).showFrame(delegate);
    } else if (e.getSource() == countCompany) {
      this.dispose();
      new DisplayReportRentalCountCompanyWindow(this.date,this.location).showFrame(delegate);
    }
  }
}

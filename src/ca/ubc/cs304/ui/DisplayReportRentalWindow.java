package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReportModel;
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
//  private JButton countCategory;
//  private JButton countBranch;
//  private JButton countCompany;
  private JScrollPane sp;
  private JScrollPane sp2;
  private JTable table2;

  public DisplayReportRentalWindow(String date, String location) {
    super("DisplayReportRentalWindow");
    this.date = date;
    this.location = location;
  }
  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;
    ArrayList<VehicleModel> vlist = delegate.showRentalReport1(date, location);
    ArrayList<ReportModel> vlist3 = delegate.showRentalReport3(date, location);
    ArrayList<ReportModel> vlist2 = delegate.showRentalReport2(date, location);
    ArrayList<ReportModel> vlist4 = delegate.showRentalReport4(date, location);
    back = new JButton("Back");
//    countCategory = new JButton("Count Rents/category");
//    countBranch = new JButton("Count Rents/branch");
//    countCompany = new JButton("All Rents for the day");
    if (vlist.isEmpty()) {
      this.setLayout(new BorderLayout());
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("No rentals for the day."));
      JPanel botPanel = new JPanel();
      botPanel.add(back);
//      botPanel.add(countCategory);
//      botPanel.add(countBranch);
//      botPanel.add(countCompany);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(botPanel, BorderLayout.SOUTH);
      back.addActionListener(this);
//      countCategory.addActionListener(this);
//      countBranch.addActionListener(this);
//      countCompany.addActionListener(this);
      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
      this.setVisible(true);
    } else {
      this.setLayout(new BorderLayout());

      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      JPanel panel3 = new JPanel();

      panel3.add(back);
//      panel3.add(countCategory);
//      panel3.add(countBranch);
//      panel3.add(countCompany);

      String[] colNames = {
              "City", "Location", "Vehicle Type","Rental Id","Vehicle License","Make","Model","Year",
      };
      String[][] array = new String[vlist.size()][];
      for (int i = 0; i < vlist.size(); i++) {
        VehicleModel v = vlist.get(i);
        String[] attr = {
                v.getCity(), v.getLocation(),
                v.getVtname(),
                Integer.toString(v.getRid()),  v.getVlicense(), v.getMake(), v.getModel(),
                String.valueOf(v.getYear())
        };
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

      // Table 2-4
      String[] col2Names = {"Vehicle Type/Branch", "Count"};
      String[][] array2 = new String[vlist2.size()+vlist3.size()+vlist4.size()+2][];
      for (int i = 0; i < vlist2.size(); i++) {
        ReportModel r = vlist2.get(i);
        String[] attr = {
                r.getVtname(),
                Integer.toString(r.getCounter())
        };
        array2[i] = attr;
      }
      array2[vlist2.size()] = new String[]{"", ""};
      int j = 0;
      for (int i = vlist2.size()+1; i < vlist2.size()+vlist3.size()+1; i++) {
        ReportModel r = vlist3.get(j);
        String[] attr = {
                r.getLocation(),
                Integer.toString(r.getCounter())
        };
        j++;
        array2[i] = attr;
      }
      j = 0;
      array2[vlist2.size()+vlist3.size()+1] = new String[]{"", ""};

      if (!location.equals("")) {
        ReportModel r = vlist3.get(0);
        String[] attr = {
                r.getLocation(),
                Integer.toString(r.getCounter())
        };
        array2[vlist2.size() + vlist3.size() + 2] = attr;
      } else {
          ReportModel r = vlist4.get(0);
          String[] attr = {
                  r.getVtname(),
                  Integer.toString(r.getCounter())
          };
          array2[vlist2.size() + vlist3.size() + 2] = attr;
      }

      table2 = new JTable(array2, col2Names);
      for (int column = 0; column < table2.getColumnCount(); column++)
      {
        TableColumn tableColumn = table2.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();
        int maxWidth = tableColumn.getMaxWidth();

        for (int row = 0; row < table2.getRowCount(); row++)
        {
          TableCellRenderer cellRenderer = table2.getCellRenderer(row, column);
          Component c = table2.prepareRenderer(cellRenderer, row, column);
          int width = c.getPreferredSize().width + table2.getIntercellSpacing().width;
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
      sp2 = new JScrollPane(table2);
      panel2.add(sp2);

      this.add(panel1, BorderLayout.NORTH);
      this.add(panel2, BorderLayout.CENTER);
      this.add(panel3, BorderLayout.SOUTH);
      back.addActionListener(this);
//      countCategory.addActionListener(this);
//      countBranch.addActionListener(this);
//      countCompany.addActionListener(this);

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
//    } else if (e.getSource() == countCategory) {
//      this.dispose();
//      new DisplayReportRentalCountCategoryWindow(this.date,this.location).showFrame(delegate);
//    } else if (e.getSource() == countBranch) {
//      this.dispose();
//      new DisplayReportRentalCountBranchWindow(this.date,this.location).showFrame(delegate);
//    } else if (e.getSource() == countCompany) {
//      this.dispose();
//      new DisplayReportRentalCountCompanyWindow(this.date,this.location).showFrame(delegate);
//    }
    }
  }
}

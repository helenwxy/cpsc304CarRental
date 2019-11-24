package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReportModel;
import ca.ubc.cs304.model.ReportReturnModel;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayReturnReportWindow extends JFrame implements ActionListener {
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

  public DisplayReturnReportWindow(String date, String location) {
    super("DisplayReturnReportWindow");
    this.date = date;
    this.location = location;
  }

  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;
    ArrayList<VehicleModel> vlist = delegate.showReturnReport1(date, location);
    ArrayList<ReportReturnModel> vlist3 = delegate.showReturnReport3(date, location);
    ArrayList<ReportReturnModel> vlist2 = delegate.showReturnReport2(date, location);
    ArrayList<ReportReturnModel> vlist4 = delegate.showReturnReport4(date, location);
    back = new JButton("Back");
//    countCategory = new JButton("Count Returns & Revenue/category");
//    countBranch = new JButton("Count Returns & Revenue /branch");
//    countCompany = new JButton("Count Total Returns & Revenue");
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

      String[] colNames2 = {"Vehicle Type", "# of Cars", "Revenue"};
      String[][] array2 = new String[vlist2.size()+vlist3.size()+vlist4.size()+2][];
      for (int i = 0; i < vlist2.size(); i++) {
        ReportReturnModel r = vlist2.get(i);
        String[] attr = {
                r.getVtname(),
                Integer.toString(r.getCounter()),
                Float.toString(r.getMoney())
        };
        array2[i] = attr;
      }
      int j = 0;
      array2[vlist2.size()] = new String[]{"", "", ""};

      for (int i = vlist2.size()+1; i < vlist2.size()+vlist3.size()+1; i++) {
        ReportReturnModel r = vlist3.get(j);
        String[] attr = {
                r.getLocation(),
                Integer.toString(r.getCounter()),
                Float.toString(r.getMoney())
        };
        j++;
        array2[i] = attr;
      }
      j = 0;
      array2[vlist2.size()+vlist3.size()+1] = new String[]{"", "", ""};
      if (!location.equals("")) {
        ReportReturnModel r = vlist3.get(0);
        String[] attr = {
                r.getLocation(),
                Integer.toString(r.getCounter()),
                Float.toString(r.getMoney())
        };
        array2[vlist2.size()+vlist3.size()+2] = attr;
      } else {
          ReportReturnModel r = vlist4.get(0);
          String[] attr = {
                  r.getVtname(),
                  Integer.toString(r.getCounter()),
                  Float.toString(r.getMoney())
          };
          array2[vlist2.size() + vlist3.size() + 2] = attr;
      }

//            table.setBounds(30, 40, 200, 300);
      JTable table2 = new JTable(array2, colNames2);
      sp = new JScrollPane(table1);
      panel1.add(sp);
      JScrollPane sp2 = new JScrollPane(table2);
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
    }
//    } else if (e.getSource() == countCategory) {
//      this.dispose();
//      new DisplayReturnReportCountCategoryWindow(this.date,this.location).showFrame(delegate);
//    } else if (e.getSource() == countBranch) {
//      this.dispose();
//      new DisplayReturnReportRevenueBranchWindow(this.date,this.location).showFrame(delegate);
//    } else if (e.getSource() == countCompany) {
//      this.dispose();
//      new DisplayReturnGrandTotalWindow(this.date,this.location).showFrame(delegate);
//    }
  }
}

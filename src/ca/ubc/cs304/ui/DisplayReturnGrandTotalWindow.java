package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReportModel;
import ca.ubc.cs304.model.ReportReturnModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// https://piazza.com/class/jy5fkw4a7sbi?cid=724
public class DisplayReturnGrandTotalWindow extends JFrame implements ActionListener {
  private JTable table4;
  private String date;
  private String location;
  //  private JButton numVehicle;
  private RentalTransactionDelegate delegate;
  private JButton back;
  private JScrollPane sp;

  public DisplayReturnGrandTotalWindow(String date, String location) {
    super("DisplayReturnGrandTotalWindow");
    this.date = date;
    this.location = location;
  }

  // todo: copied from DisplayVehicleWindow. adjust.
  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;
    ArrayList<ReportReturnModel> vlist = delegate.showReturnReport4(date, location);
    back = new JButton("Back");
    if (vlist.isEmpty()) {
      this.setLayout(new BorderLayout());
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("No rentals for the day."));
      JPanel botPanel = new JPanel();
      botPanel.add(back);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(botPanel, BorderLayout.SOUTH);
      back.addActionListener(this);
      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
      this.setVisible(true);
    } else {
      this.setLayout(new BorderLayout());
      JPanel panel1 = new JPanel();
//      numVehicle = new JButton(String.valueOf(vlist.size()));
      GridBagLayout gb = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      JLabel label = new JLabel("Number of Available Vehicles: (click to expand)");
//      panel1.setLayout(gb);
//      panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//      cons.gridwidth = GridBagConstraints.RELATIVE;
//      cons.insets = new Insets(10, 10, 5, 0);
//      gb.setConstraints(label, cons);
//      panel1.add(label);

//      cons.gridwidth = GridBagConstraints.REMAINDER;
//      cons.insets = new Insets(10, 0, 5, 10);
//      gb.setConstraints(numVehicle, cons);
//      panel1.add(numVehicle);

      JPanel panel2 = new JPanel();
      panel2.add(back);

      String[] colNames = {"$$$"};
      String[][] array = new String[vlist.size()][];
      for (int i = 0; i < vlist.size(); i++) {
        ReportReturnModel r = vlist.get(i);
        String[] attr = {
          Float.toString(r.getMoney())
        };
        array[i] = attr;
      }
      table4 = new JTable(array, colNames);
      for (int column = 0; column < table4.getColumnCount(); column++)
      {
        TableColumn tableColumn = table4.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();
        int maxWidth = tableColumn.getMaxWidth();

        for (int row = 0; row < table4.getRowCount(); row++)
        {
          TableCellRenderer cellRenderer = table4.getCellRenderer(row, column);
          Component c = table4.prepareRenderer(cellRenderer, row, column);
          int width = c.getPreferredSize().width + table4.getIntercellSpacing().width;
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
      sp = new JScrollPane(table4);
      panel1.add(sp);

      this.add(panel1, BorderLayout.NORTH);
      this.add(panel2, BorderLayout.SOUTH);
      back.addActionListener(this);
//      numVehicle.addActionListener(this);
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
      new DisplayReturnReportWindow(this.date, this.location).showFrame(delegate);
    }
  }
}
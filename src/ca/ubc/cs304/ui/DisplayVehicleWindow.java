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

public class DisplayVehicleWindow extends JFrame implements ActionListener {
  private JTable table;
  private String vtname;
  private String location;
  private JButton numVehicle;
  private RentalTransactionDelegate delegate;
  private JButton back;
  private JScrollPane sp;

  public DisplayVehicleWindow(String vtname, String location) {
    super("DisplayVehicleWindow");
    this.vtname = vtname;
    this.location = location;
  }

  public void showFrame(RentalTransactionDelegate delegate) {
    this.delegate = delegate;
    ArrayList<VehicleModel> vlist = delegate.showQualifiedVehicle(vtname, location);
    back = new JButton("Back");
    if (vlist.isEmpty()) {
      this.setLayout(new BorderLayout());
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("Sorry, no vehicle found!"));
      JPanel botPanel = new JPanel();
      botPanel.add(back);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(botPanel, BorderLayout.SOUTH);
      back.addActionListener(this);
      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);
      this.setVisible(true);
    } else {
      this.setLayout(new BorderLayout());
      JPanel panel1 = new JPanel();
      numVehicle = new JButton(String.valueOf(vlist.size()));
      GridBagLayout gb = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      JLabel label = new JLabel("Number of Available Vehicles: (click to expand)");
      panel1.setLayout(gb);
      panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      cons.gridwidth = GridBagConstraints.RELATIVE;
      cons.insets = new Insets(10, 10, 5, 0);
      gb.setConstraints(label, cons);
      panel1.add(label);

      cons.gridwidth = GridBagConstraints.REMAINDER;
      cons.insets = new Insets(10, 0, 5, 10);
      gb.setConstraints(numVehicle, cons);
      panel1.add(numVehicle);

      JPanel panel2 = new JPanel();
      panel2.add(back);

      String[] colNames = {"Vehicle Type", "Make", "Model", "Year", "Location", "City"};
      String[][] array = new String[vlist.size()][];
      for (int i = 0; i < vlist.size(); i++) {
        VehicleModel v = vlist.get(i);
        String[] attr = {v.getVtname(), v.getMake(), v.getModel(), String.valueOf(v.getYear()), v.getLocation(), v.getCity()};
        array[i] = attr;
      }
      table = new JTable(array, colNames);
      for (int column = 0; column < table.getColumnCount(); column++) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();
        int maxWidth = tableColumn.getMaxWidth();

        for (int row = 0; row < table.getRowCount(); row++) {
          TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
          Component c = table.prepareRenderer(cellRenderer, row, column);
          int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
          preferredWidth = Math.max(preferredWidth, width);

          //  We've exceeded the maximum width, no need to check other rows

          if (preferredWidth >= maxWidth) {
            preferredWidth = maxWidth;
            break;
          }
        }

        tableColumn.setPreferredWidth(preferredWidth);
      }
//            table.setBounds(30, 40, 200, 300);
      sp = new JScrollPane(table);

      this.add(panel1, BorderLayout.NORTH);
      this.add(panel2, BorderLayout.SOUTH);
      back.addActionListener(this);
      numVehicle.addActionListener(this);
      this.pack();
      Dimension d = this.getToolkit().getScreenSize();
      Rectangle r = this.getBounds();
      this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);
      this.setVisible(true);
    }
  }

    @Override
    public void actionPerformed(ActionEvent e){
      if (e.getSource() == numVehicle) {
        this.getContentPane().removeAll();
        this.getContentPane().add(sp, BorderLayout.NORTH);
        this.getContentPane().add(back, BorderLayout.SOUTH);
        this.revalidate();
        this.pack();
        this.repaint();
      } else if (e.getSource() == back) {
        this.dispose();
        new CustomerWindow().showFrame(delegate);
      }
    }
}

package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.RentalTransactionDelegate;
import ca.ubc.cs304.model.ReturnModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnWindow extends JFrame implements ActionListener {
    private RentalTransactionDelegate delegate;
    private JButton submit;
    private JButton back;
    private JTextField vlicenseField;
    private JTextField odoField;
    private JTextField tankField;
    private JPanel panel = new JPanel();
    private static final int TEXT_FIELD_WIDTH = 10;

    public ReturnWindow() {
        super("Vehicle Return");
    }

    public void showFrame(RentalTransactionDelegate delegate) {
        this.delegate = delegate;

        JLabel vlicenseLabel = new JLabel("Enter vehicle license: ");
        JLabel odoLabel = new JLabel("Enter odometer: ");
        JLabel tankLabel = new JLabel("Is the tank full? (enter Y/N): ");

        vlicenseField = new JTextField(TEXT_FIELD_WIDTH);
        odoField = new JTextField(TEXT_FIELD_WIDTH);
        tankField = new JTextField(TEXT_FIELD_WIDTH);

        this.submit = new JButton("submit");
        this.back = new JButton("cancel");
        this.setContentPane(panel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(gb);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(vlicenseLabel, c);
        panel.add(vlicenseLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(vlicenseField, c);
        panel.add(vlicenseField);

        // vtname label & combobox
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(odoLabel, c);
        panel.add(odoLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 5, 10);
        gb.setConstraints(odoField, c);
        panel.add(odoField);

        // Location label & textbox
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(tankLabel, c);
        panel.add(tankLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(tankField, c);
        panel.add(tankField);

        // place the buttons button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(submit, c);
        panel.add(submit);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(back, c);
        panel.add(back);

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
            String vlicense = vlicenseField.getText();
            String odo = odoField.getText();
            String tank = tankField.getText();
            int rid = delegate.getRid(vlicense);
            if (rid < 0) {
                PopupBox.infoBox("Vehicle license not found. Please try again.", "Error");
            } else {
                System.out.println("aaa");
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                ReturnModel model = delegate.insertReturn(rid, vlicense,odo,tank);
                PopupBox.infoBox("Return processed!\n" + "Rental Transaction ID: " + rid
                        + "\nRental date: " + model.getDate() + "\nReturn date: " + date
                        + "\nWeeks total: " + model.getWeeks()
                        + "\nWeekly rate: " + model.getWrate() + "\nWeekly insurance rate: " + model.getWirate()
                        + "\nDays total: " + model.getDays()
                        + "\nDaily rate: " + model.getDrate() + "\nDaily insurance rate: " + model.getDirate()
                        + "\nHours total: " + model.getHours()
                        + "\nHourly rate: " + model.getHrate() + "\nHourly insurance rate: " + model.getHirate()
                        + "\nTotal charge: " + model.getTotal(), "Information");
            }
        } else if (e.getSource() == back) {
            this.dispose();
            new MainWindow().showFrame(delegate);
        }
    }
}

package ca.ubc.cs304.model;

public class ReturnModel {
    private int hrate;
    private int drate;
    private int wrate;
    private int hirate;
    private int dirate;
    private int wirate;
    private int hours;
    private int days;
    private int weeks;
    private String date;
    private float total;

    public ReturnModel(int hrate, int drate, int wrate, int hirate, int dirate, int wirate, int hours, int days, int weeks, String date, float total) {
        this.hrate = hrate;
        this.drate = drate;
        this.wrate = wrate;
        this.hirate = hirate;
        this.dirate = dirate;
        this.wirate = wirate;
        this.hours = hours;
        this.days = days;
        this.weeks = weeks;
        this.date = date;
        this.total = total;
    }

    public int getHrate() {
        return hrate;
    }

    public int getDrate() {
        return drate;
    }

    public int getWrate() {
        return wrate;
    }

    public int getHirate() {
        return hirate;
    }

    public int getDirate() {
        return dirate;
    }

    public int getWirate() {
        return wirate;
    }

    public int getHours() {
        return hours;
    }

    public int getDays() {
        return days;
    }

    public int getWeeks() {
        return weeks;
    }

    public String getDate() {
        return date;
    }

    public float getTotal() {
        return total;
    }
}

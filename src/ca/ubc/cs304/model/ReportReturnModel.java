package ca.ubc.cs304.model;

public class ReportReturnModel {
  private String location;
  private String vtname;
  private int counter;
  private float money;


  public ReportReturnModel(int counter, float money) {
    this.counter = counter;
    this.money = money;
  }

  public ReportReturnModel(float money) {
    this.money = money;
  }

  public ReportReturnModel(String vtname, float money) {
    this.vtname = vtname;
    this.money = money;
  }

  public ReportReturnModel(String vtname, int counter, String location) {
    this.vtname = vtname;
    this.counter = counter;
    this.location = location;
  }


  public String getLocation() {
    return location;
  }

  public String getVtname() {
    return vtname;
  }

  public int getCounter() {
    return counter;
  }

  public float getMoney() { return money; }
}

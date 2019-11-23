package ca.ubc.cs304.model;

public class ReportModel {
    private String location;
    private String vtname;
//    private String make;
//    private String model;
    private int counter;

  public ReportModel(String vtname, int counter) {
    this.vtname = vtname;
    this.counter = counter;
  }

  public ReportModel(String vtname, int counter, String location) {
    this.vtname = vtname;
    this.counter = counter;
    this.location = location;
  }

  public ReportModel(int counter, String location) {
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

}

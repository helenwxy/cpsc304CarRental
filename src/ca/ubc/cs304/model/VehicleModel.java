package ca.ubc.cs304.model;

public class VehicleModel {
  private int rid;
  private String vlicense;
  private String vtname;
  private String make;
  private String model;
  private int year;
  private String location;
  private String city;

  public int getRid() {
    return rid;
  }

  public String getVlicense() {
    return vlicense;
  }

  public VehicleModel(int rid, String vlicense, String vtname, String make, String model, int year, String location, String city) {
    this.rid = rid;
    this.vlicense = vlicense;
    this.vtname = vtname;
    this.make = make;
    this.model = model;
    this.year = year;
    this.location = location;
    this.city = city;
  }

  public VehicleModel(String vtname, String make, String model, int year, String location, String city) {
    this.vtname = vtname;
    this.make = make;
    this.model = model;
    this.year = year;
    this.location = location;
    this.city = city;
  }

  public String getVtname() {
    return vtname;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public int getYear() {
    return year;
  }

  public String getLocation() {
    return location;
  }

  public String getCity() {
    return city;
  }
}

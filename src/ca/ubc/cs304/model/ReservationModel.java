package ca.ubc.cs304.model;

public class ReservationModel {
    private int confNo;
    private String vtname;
    private String dlicense;
    private String fromDate;
    private String toDate;
    private String rDate;

    private int rid;
    private String location;
    private String errorMsg;

    public ReservationModel(int confNo, String vtname, String dlicense, String fromDate, String toDate, String rDate,
                            int rid, String location) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.rDate = rDate;
        this.rid = rid;
        this.location = location;
        this.errorMsg = null;
    }

    public ReservationModel(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getconfNo() {
        return confNo;
    }

    public String getvtname() {
        return vtname;
    }

    public String getdlicense() {
        return dlicense;
    }

    public String getfromDate() {
        return fromDate.substring(0, 16);
    }

    public String gettoDate() {
        return toDate.substring(0, 16);
    }

    public String getrDate() { return rDate.substring(0, 10); }

    public int getrid() {
        return rid;
    }

    public String getlocation() {
        return location;
    }

    public String getErrorMsg() { return errorMsg; }
}

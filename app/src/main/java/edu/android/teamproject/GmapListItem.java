package edu.android.teamproject;



public class GmapListItem {

    private String name;
    private String addr;
    private String lsind_Type;
    private String tel;

    private double mapx;
    private double mapy;

    public GmapListItem(){}


    public GmapListItem(String name, String addr, String lsind_Type, String tel, double mapx, double mapy) {
        this.name = name;
        this.addr = addr;
        this.lsind_Type = lsind_Type;
        this.tel = tel;
        this.mapx = mapx;
        this.mapy = mapy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }



    public String getLsind_Type() {
        return lsind_Type;
    }

    public void setLsind_Type(String lsind_Type) {
        this.lsind_Type = lsind_Type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    @Override
    public String toString() {
        return "GmapListItem{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", lsind_Type='" + lsind_Type + '\'' +
                ", tel_Number=" + tel +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                '}';
    }
}

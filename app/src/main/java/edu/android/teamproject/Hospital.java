package edu.android.teamproject;

public class Hospital {
    private String hospitalId; // 병원 아이디
    private String hospitalName; // 병원 이름
    private String hospitalAddress; // 병원 주소
    private String hospitalNumber; // 병원 번호
    private String hospitalXCode; // 병원 경도
    private String hospitalYCode; // 병원 위도

    public Hospital(String hopitalId, String hospitalName,
                    String hospitalAddress, String hospitalNumber,
                    String hospitalXCode, String hospitalYCode) {
        this.hospitalId = hopitalId;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalNumber = hospitalNumber;
        this.hospitalXCode = hospitalXCode;
        this.hospitalYCode = hospitalYCode;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public String getHospitalXCode() {
        return hospitalXCode;
    }

    public String getHospitalYCode() {
        return hospitalYCode;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                ", hospitalNumber='" + hospitalNumber + '\'' +
                ", hospitalXCode='" + hospitalXCode + '\'' +
                ", hospitalYCode='" + hospitalYCode + '\'' +
                '}';
    }

    public interface JsonContact {
        String VAR_ID = "ID";
        String VAR_NAME = "NM";
        String VAR_ADDRESS = "ADDR";
        String VAR_TEL = "TEL";
        String VAR_XCODE = "XCODE";
        String VAR_YCODE = "YCODE";
    }

}

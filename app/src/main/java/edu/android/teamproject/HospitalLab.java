package edu.android.teamproject;


import java.util.ArrayList;
import java.util.List;

public class HospitalLab {
    private static HospitalLab instance = null;
    private List<Hospital> hospitalList;

    private HospitalLab() {
        hospitalList = new ArrayList<>();
    }

    public static HospitalLab getInstance() {
        if (instance == null) {
            instance = new HospitalLab();
        }
        return instance;
    }
    public List<Hospital> getHospitalList() {
        return hospitalList;
    }



}

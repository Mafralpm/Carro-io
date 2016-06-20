package com.example.labm4.unifor_obdreaderii.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "obdreadings")
public class OBDReadings_db {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "RPM", canBeNull = true)
    private int RPM;

    @DatabaseField(columnName = "throttlePosition", canBeNull = true)
    private double throttlePosition;

    @DatabaseField(columnName = "timingAdvance", canBeNull = true)
    private double timingAdvance;

    @DatabaseField(columnName = "fuelConsumption", canBeNull = true)
    private double fuelConsumption;

    @DatabaseField(columnName = "massAirFlow", canBeNull = true)
    private double massAirFlow;


    //ORMLite needs a black constructor
    public OBDReadings_db() {
    }

    public OBDReadings_db(int RPM, Double throttlePosition, Double timingAdvance, Double fuelConsumption, Double massAirFlow) {
        this.RPM = RPM;
        this.throttlePosition = throttlePosition;
        this.timingAdvance = timingAdvance;
        this.fuelConsumption = fuelConsumption;
        this.massAirFlow = massAirFlow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public double getThrottlePosition() {
        return throttlePosition;
    }

    public void setThrottlePosition(double throttlePosition) {
        this.throttlePosition = throttlePosition;
    }

    public double getTimingAdvance() {
        return timingAdvance;
    }

    public void setTimingAdvance(double timingAdvance) {
        this.timingAdvance = timingAdvance;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getMassAirFlow() {
        return massAirFlow;
    }

    public void setMassAirFlow(double massAirFlow) {
        this.massAirFlow = massAirFlow;
    }
}

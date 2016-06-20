package com.example.labm4.unifor_obdreaderii.Models;

public class OBDReading_Sigleton {
    private static OBDReading_Sigleton mInstance = null;

    private int RPM;
    private double throttlePosition;
    private double timingAdvance;
    private double fuelConsumption;
    private double massAirFlow;

    private OBDReading_Sigleton() {
        RPM = 0;
        throttlePosition = 0.0;
        timingAdvance = 0.0;
        fuelConsumption = 0.0;
        massAirFlow = 0.0;
    }

    public static OBDReading_Sigleton getInstance() {
        if (mInstance == null) {
            mInstance = new OBDReading_Sigleton();
        }
        return mInstance;
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

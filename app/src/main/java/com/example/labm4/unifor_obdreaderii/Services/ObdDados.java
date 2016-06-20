package com.example.labm4.unifor_obdreaderii.Services;

import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import com.example.labm4.unifor_obdreaderii.Models.OBDReading_Sigleton;
import com.github.pires.obd.commands.control.TimingAdvanceObdCommand;
import com.github.pires.obd.commands.engine.EngineRPMObdCommand;
import com.github.pires.obd.commands.engine.MassAirFlowObdCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionObdCommand;
import com.github.pires.obd.commands.fuel.FuelConsumptionRateObdCommand;
import com.github.pires.obd.commands.protocol.EchoOffObdCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffObdCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolObdCommand;
import com.github.pires.obd.commands.protocol.TimeoutObdCommand;
import com.github.pires.obd.enums.ObdProtocols;

import android.os.Handler;

import java.io.IOException;

public class ObdDados {

    private Handler handler = new Handler();
    EngineRPMObdCommand engineRpmCommand;
    ThrottlePositionObdCommand throttlePositionObdCommand;
    FuelConsumptionRateObdCommand fuelConsumptionRateObdCommand;
    TimingAdvanceObdCommand timingAdvanceObdCommand;
    MassAirFlowObdCommand massAirFlowObdCommand;

    OBDSigleton_Metodos obdSigleton_metodos;

    public ObdDados() {
        engineRpmCommand = new EngineRPMObdCommand();
        throttlePositionObdCommand = new ThrottlePositionObdCommand();
        fuelConsumptionRateObdCommand = new FuelConsumptionRateObdCommand();
        timingAdvanceObdCommand = new TimingAdvanceObdCommand();
        massAirFlowObdCommand = new MassAirFlowObdCommand();

        obdSigleton_metodos = new OBDSigleton_Metodos();
    }

    public void permissoes(BluetoothSocket sock) throws IOException, InterruptedException {

            new EchoOffObdCommand().run(sock.getInputStream(), sock.getOutputStream());

            new LineFeedOffObdCommand().run(sock.getInputStream(), sock.getOutputStream());

            new TimeoutObdCommand(62).run(sock.getInputStream(), sock.getOutputStream());

            new SelectProtocolObdCommand(ObdProtocols.AUTO).run(sock.getInputStream(), sock.getOutputStream());
    }

    public void setDados(BluetoothSocket sock) {
        while (sock != null) {

            obdSigleton_metodos.RPM(engineRpmCommand, sock);

            obdSigleton_metodos.Borbuleta(throttlePositionObdCommand, sock);

            obdSigleton_metodos.Ignicao(timingAdvanceObdCommand, sock);

            obdSigleton_metodos.FluxoDeCombustivel(fuelConsumptionRateObdCommand, sock);

            obdSigleton_metodos.MassAir(massAirFlowObdCommand, sock);

        }
    }

    private class OBDSigleton_Metodos {

        public void RPM(EngineRPMObdCommand engineRPMCommand, BluetoothSocket sock) {
            try {
                engineRPMCommand.run(sock.getInputStream(), sock.getOutputStream());
                OBDReading_Sigleton.getInstance().setRPM(engineRPMCommand.getRPM());
            } catch (Exception e) {
                OBDReading_Sigleton.getInstance().setRPM(0);
            }
        }

        public void Borbuleta(ThrottlePositionObdCommand throttlePositionObdCommand, BluetoothSocket sock) {
            try {
                throttlePositionObdCommand.run(sock.getInputStream(), sock.getOutputStream());
                OBDReading_Sigleton.getInstance().setThrottlePosition(throttlePositionObdCommand.getPercentage());
            } catch (Exception e) {
                OBDReading_Sigleton.getInstance().setThrottlePosition(Double.valueOf(0));
            }
        }

        public void FluxoDeCombustivel(FuelConsumptionRateObdCommand fuelConsumptionRateObdCommand, BluetoothSocket sock) {
            try {
                fuelConsumptionRateObdCommand.run(sock.getInputStream(), sock.getOutputStream());
                OBDReading_Sigleton.getInstance().setFuelConsumption((double) fuelConsumptionRateObdCommand.getLitersPerHour());
            } catch (Exception e) {
                OBDReading_Sigleton.getInstance().setFuelConsumption(Double.valueOf(0));
            }
        }

        public void Ignicao(TimingAdvanceObdCommand timingAdvanceObdCommand, BluetoothSocket sock) {
            try {
                timingAdvanceObdCommand.run(sock.getInputStream(), sock.getOutputStream());
                OBDReading_Sigleton.getInstance().setTimingAdvance((double) timingAdvanceObdCommand.getPercentage());
            } catch (Exception e) {
                OBDReading_Sigleton.getInstance().setTimingAdvance(Double.valueOf(0));
            }
        }

        public void MassAir(MassAirFlowObdCommand massAirFlowObdCommand, BluetoothSocket sock) {
            try {
                massAirFlowObdCommand.run(sock.getInputStream(), sock.getOutputStream());
                OBDReading_Sigleton.getInstance().setMassAirFlow(massAirFlowObdCommand.getMAF());
            } catch (Exception e) {
                OBDReading_Sigleton.getInstance().setMassAirFlow(Double.valueOf(0));
            }
        }

    }
}

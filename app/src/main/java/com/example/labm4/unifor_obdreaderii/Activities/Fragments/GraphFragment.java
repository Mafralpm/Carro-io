package com.example.labm4.unifor_obdreaderii.Activities.Fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.labm4.unifor_obdreaderii.Controller.OBDReadingsController;
import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.example.labm4.unifor_obdreaderii.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_graph)
public class GraphFragment extends Fragment {

    List<OBDReadings_db> infosList;
    OBDReadings_db obdReadings_db;

    @ViewById
    GraphView graphic;

    @Bean
    OBDReadingsController obdReadingsController;

    private Bitmap bitmap;

    @AfterViews
    protected void CreateGraphic() {
        infosList = obdReadingsController.getReadings();
        int size = infosList.size();
        DataPoint[][] data = new DataPoint[4][size];
        int[] color = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
        String[] name = {"Timing Advance", "Throttle Position", "Mass Air Flow", "Fuel Consumption"};
        for (int i = 0; i < size; i++) {
            obdReadings_db = infosList.get(i);
            data[0][i] = new DataPoint(obdReadings_db.getRPM(), obdReadings_db.getTimingAdvance());
            data[1][i] = new DataPoint(obdReadings_db.getRPM(), obdReadings_db.getThrottlePosition());
            data[2][i] = new DataPoint(obdReadings_db.getRPM(), obdReadings_db.getMassAirFlow());
            data[3][i] = new DataPoint(obdReadings_db.getRPM(), obdReadings_db.getFuelConsumption());
        }
        for (int i = 0; i < data.length; i++) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data[i]);
            series.setColor(color[i]);
            series.setTitle(name[i]);
            graphic.addSeries(series);
            graphic.getLegendRenderer().setVisible(true);
            graphic.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }

    }

    public Bitmap getBitmap() {
        Bitmap snapshot;
        graphic.setDrawingCacheEnabled(true);
        graphic.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        graphic.buildDrawingCache();
        snapshot = Bitmap.createBitmap(graphic.getDrawingCache());
        return snapshot;
    }

}

package com.example.labm4.unifor_obdreaderii.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.labm4.unifor_obdreaderii.Controller.OBDReadingsController;
import com.example.labm4.unifor_obdreaderii.Models.ItensView;
import com.example.labm4.unifor_obdreaderii.Models.ItensView_;
import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class AdapterListView extends BaseAdapter {

    private List<OBDReadings_db> itens;

    @Bean
    OBDReadingsController obdReadingsController;

    @RootContext
    Context context;

    @AfterViews
    void initAdapter() {
        itens = obdReadingsController.getReadings();
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public OBDReadings_db getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItensView itensView;

        if (convertView == null) {
            itensView = ItensView_.build(context);
        } else {
            itensView = (ItensView) convertView;
        }

        itensView.bind(getItem(position));

        return itensView;

    }
}

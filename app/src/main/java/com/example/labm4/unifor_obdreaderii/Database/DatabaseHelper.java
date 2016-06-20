package com.example.labm4.unifor_obdreaderii.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.example.labm4.unifor_obdreaderii.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DatabaseName = "ObdReadings.db";

    private static final int DatabaseVersion = 1;

    Context context;

    public DatabaseHelper(Context context){
        super(context, DatabaseName, null, DatabaseVersion, R.raw.ormlite_config);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OBDReadings_db.class);
            TableUtils.createTableIfNotExists(connectionSource, OBDReadings_db.class);
        } catch (SQLException e) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            try {
                TableUtils.dropTable(connectionSource, OBDReadings_db.class, true);
                onCreate(database, connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void clearOBDReadings()
    {
        try {
            TableUtils.clearTable(connectionSource, OBDReadings_db.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

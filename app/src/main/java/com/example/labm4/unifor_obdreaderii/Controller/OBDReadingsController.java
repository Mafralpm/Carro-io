package com.example.labm4.unifor_obdreaderii.Controller;

import com.example.labm4.unifor_obdreaderii.Database.DatabaseHelper;
import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

@EBean
public class OBDReadingsController {

    @OrmLiteDao(helper = DatabaseHelper.class, model = OBDReadings_db.class)
    Dao<OBDReadings_db, Integer> obdReadsDAO;

    public void createRead(OBDReadings_db read) {
        try {
            obdReadsDAO.create(read);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OBDReadings_db> getReadings() {
        List<OBDReadings_db> readingsList = null;

        try {
            readingsList = obdReadsDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readingsList;
    }

    public int getSize() {
        List<OBDReadings_db> readingsList = null;

        try {
            readingsList = obdReadsDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readingsList.size();
    }

    public void clearAll() {
        try {
            obdReadsDAO.delete(obdReadsDAO.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

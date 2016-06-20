package com.example.labm4.unifor_obdreaderii.PDF;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class FileOperations {


    public FileOperations() {
        super();
    }

    public Boolean write(Context context, List<OBDReadings_db> lista, Bitmap bitmap) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String data = sdf.format(new Date());
        String nomeDoDocumento = "OBD Reader - " + data;
        try {
            File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/OBD Reader/" + nomeDoDocumento + ".pdf")));
            file.setReadOnly();

            if (!file.isDirectory()) {
                File contagemDirectory = new File(Environment.getExternalStorageDirectory() + "/OBD Reader");
                contagemDirectory.mkdirs();

                MediaScannerConnection.scanFile(context, new String[]{contagemDirectory.toString()}, null, null);

            }
            // step 1
            Document document = new Document();
            // step 2
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
            document.setPageSize(PageSize.A4);
            document.setMargins(10, 10, 100, 50);
            document.setMarginMirroring(true);
            // step 3
            document.open();
            // step 4

            // step 5
            document.add(createTable(lista));

            document.newPage();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setRotationDegrees(270);

            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            image.scaleToFit(documentWidth, documentHeight);
            image.setAlignment(Element.ALIGN_CENTER);

            document.add(image);

            // step 6
            document.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PdfPTable createTable(List<OBDReadings_db> lista) throws DocumentException {
        PdfPTable table = new PdfPTable(5);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("RPM"));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Throttler"));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Ignition Timing"));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Fuel"));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Mass Air"));
        table.addCell(cell);

        DecimalFormat fmt = new DecimalFormat("0.00");
        for (OBDReadings_db i : lista) {
            table.addCell(fmt.format(i.getRPM()));
            table.addCell(fmt.format(i.getThrottlePosition()));
            table.addCell(fmt.format(i.getTimingAdvance()));
            table.addCell(fmt.format(i.getFuelConsumption()));
            table.addCell(fmt.format(i.getMassAirFlow()));
        }
        return table;
    }

}
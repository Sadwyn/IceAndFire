package com.sadwyn.iceandfire;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sadwyn.iceandfire.models.Character;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.jar.Manifest;

import au.com.bytecode.opencsv.CSVWriter;


public class ExportDataService extends IntentService {

    public static final String TAG = "TAG";

    public ExportDataService(String name) {
        super(name);
    }

    public ExportDataService() {
        super("DataExportingThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) throw new AssertionError();
        List<Character> characters = Parcels.unwrap(intent.getParcelableExtra(Constants.SEND_TO_SERVICE_KEY));
        try {
            writeDataInCSV(characters);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "serviceGotInfo");
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroyService");
    }


    private void writeDataInCSV(List<Character> list) throws IOException {

            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "HeroesData.csv";
            String filePath = baseDir + File.separator + fileName;
            File csvFile = new File(filePath);
            FileWriter mFileWriter;
            CSVWriter writer;

            if (csvFile.exists() && !csvFile.isDirectory()) {
                mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }

            for (Character person : list) {
                String[] row = new String[]{
                        person.getName(),
                        person.getBorn(),
                        person.getGender(),
                        person.getFather(),
                        person.getMother(),
                        person.getDied()};
                writer.writeNext(row);
            }

       // else Toast.makeText(this, "Process Fallen, needs permission to external storage", Toast.LENGTH_SHORT).show();
    }
}



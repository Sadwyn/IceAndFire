package com.sadwyn.iceandfire;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.csvreader.CsvWriter;
import com.sadwyn.iceandfire.models.Character;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;




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
        String resultMessage;
        try {
           resultMessage = "Successfully saved" + writeDataInCSV(characters);
        } catch (IOException e) {
            resultMessage = "Error on Saving";
        }
        Log.i(TAG, "serviceGotInfo");

        String finalResultMessage = resultMessage;

        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText
                (ExportDataService.this.getApplicationContext(), finalResultMessage, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroyService");
    }


    private String writeDataInCSV(List<Character> list) throws IOException {

            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "HeroesData.csv";
            String filePath = baseDir + File.separator + fileName;
            FileWriter mFileWriter;
            CsvWriter writer;
            mFileWriter = new FileWriter(filePath);
            writer = new CsvWriter(mFileWriter,',');
            StringBuilder builder = new StringBuilder();

            for (Character person : list) {
                writePerson(writer, builder, person);
            }
            writer.flush();
            writer.close();
        return filePath;
    }

    private void writePerson(CsvWriter writer, StringBuilder builder, Character person) throws IOException {
        writer.write(person.getName());
        writer.write(person.getBorn());
        writer.write(person.getGender());
        writer.write(person.getFather());
        writer.write(person.getMother());
        writer.write(person.getDied());

        int iteration = 0;
        for (String alias : person.getAliases()) {
            iteration++;
           builder.append(alias);
            if(iteration != person.getAliases().size())
                builder.append(",");
        }
        writer.write(builder.toString());
        writer.endRecord();
        builder.setLength(0);
    }
}



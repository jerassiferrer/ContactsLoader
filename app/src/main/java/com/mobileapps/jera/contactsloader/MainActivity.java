package com.mobileapps.jera.contactsloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private static final int URL_LOADER = 1;
    private TextView callLogsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }





        loadCallsHistory();



    }

    private void loadCallsHistory() {



        Button btnCallLog = (Button) findViewById(R.id.btn_call_log);

        btnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLoaderManager().initLoader(URL_LOADER, null, MainActivity.this);
            }
        });

        callLogsTextView = (TextView) findViewById(R.id.call_logs);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {


        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                return null;
        }

    }



    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }





    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor managedCursor) {


        StringBuilder sb = new StringBuilder();

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("<h4>Call Log Details <h4>");
        sb.append("\n");
        sb.append("\n");

        sb.append("<table>");

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;

            int callTypeCode = Integer.parseInt(callType);
            switch (callTypeCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }

            sb.append("<tr>")
                    .append("<td>Phone Number: </td>")
                    .append("<td><strong>")
                    .append(phNumber)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Type:</td>")
                    .append("<td><strong>")
                    .append(dir)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Date & Time:</td>")
                    .append("<td><strong>")
                    .append(callDayTime)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Duration (Seconds):</td>")
                    .append("<td><strong>")
                    .append(callDuration)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</table>");

        managedCursor.close();

        callLogsTextView.setText(Html.fromHtml(sb.toString()));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted

                } else {
                    // Permission Denied

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





}

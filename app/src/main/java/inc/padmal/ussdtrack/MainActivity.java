package inc.padmal.ussdtrack;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LineChart chartMoney, chartData;
    String ussd = "*100" + Uri.encode("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chartMoney = findViewById(R.id.chartMoney);
        chartData = findViewById(R.id.chartData);

        startService(new Intent(getApplicationContext(), USSDService.class));

        LocalBroadcastManager.getInstance(this).registerReceiver(USSDReceiver,
                new IntentFilter("com.times.ussd.action.REFRESH"));

        SMSReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                SharedPreferences prefs = getSharedPreferences("USSD Tracker", MODE_PRIVATE);
                String DATAdata = prefs.getString("DATA", "");
                try {
                    JSONObject DATAJson = new JSONObject(DATAdata);
                    DATAJson.put(String.valueOf(System.currentTimeMillis()), messageText);
                    String updateJSONString = DATAJson.toString();
                    SharedPreferences.Editor editor = getSharedPreferences("USSD Tracker", MODE_PRIVATE).edit();
                    editor.putString("DATA", updateJSONString);
                    editor.apply();
                    updateDATAChart(DATAJson);
                } catch (JSONException e) {
                    JSONObject FirstJSON = new JSONObject();
                    try {
                        FirstJSON.put(String.valueOf(System.currentTimeMillis()), messageText);
                        String updateJSONString = FirstJSON.toString();
                        SharedPreferences.Editor editor = getSharedPreferences("USSD Tracker", MODE_PRIVATE).edit();
                        editor.putString("DATA", updateJSONString);
                        editor.apply();
                        updateDATAChart(FirstJSON);
                    } catch (JSONException j) {
                        Toast.makeText(getApplicationContext(), "Error in parsing message", Toast.LENGTH_LONG).show();
                    }
                }
                Log.d("Padmals", messageText);
            }
        });
    }

    private void updateDATAChart(JSONObject i) {
        List<Entry> entries = new ArrayList<>();
        Iterator<String> iter = i.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String Data = i.getString(key);
                entries.add(new Entry(Float.valueOf(key), Float.valueOf(Data)));
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error in rendering message", Toast.LENGTH_LONG).show();
            }
        }
        chartData.getDescription().setEnabled(false);
        HourAxisValueFormatter axisFormatter = new HourAxisValueFormatter(0);
        chartData.getXAxis().setValueFormatter(axisFormatter);
        LineDataSet dataSet = new LineDataSet(entries, "Data");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(1);
        LineData lineData = new LineData(dataSet);
        chartData.setData(lineData);
        chartData.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_review) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
            return true;
        } else if (id == R.id.action_clean) {
            SharedPreferences.Editor editor = getSharedPreferences("USSD Tracker", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            chartMoney.invalidate();
            chartMoney.clear();
            chartData.invalidate();
            chartData.clear();
        }
        super.onOptionsItemSelected(item);
        return true;
    }

    private BroadcastReceiver USSDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("USSD");
            String amount = message.substring(message.indexOf("Rs.") + 4, message.indexOf("will") - 1);

            SharedPreferences prefs = getSharedPreferences("USSD Tracker", MODE_PRIVATE);
            String MONEYdata = prefs.getString("MONEY", "");
            try {
                JSONObject MONEYJson = new JSONObject(MONEYdata);
                MONEYJson.put(String.valueOf(System.currentTimeMillis()), amount);
                String updateJSONString = MONEYJson.toString();
                SharedPreferences.Editor editor = getSharedPreferences("USSD Tracker", MODE_PRIVATE).edit();
                editor.putString("MONEY", updateJSONString);
                editor.apply();
                updateMONEYChart(MONEYJson);
            } catch (JSONException e) {
                JSONObject FirstJSON = new JSONObject();
                try {
                    FirstJSON.put(String.valueOf(System.currentTimeMillis()), amount);
                    String updateJSONString = FirstJSON.toString();
                    SharedPreferences.Editor editor = getSharedPreferences("USSD Tracker", MODE_PRIVATE).edit();
                    editor.putString("MONEY", updateJSONString);
                    editor.apply();
                    updateMONEYChart(FirstJSON);
                } catch (JSONException j) {
                    Toast.makeText(getApplicationContext(), "Error in parsing money", Toast.LENGTH_LONG).show();
                }
            }
            Log.d("Padmals", amount);
        }
    };

    private void updateMONEYChart(JSONObject i) {
        List<Entry> entries = new ArrayList<>();
        Iterator<String> iter = i.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String Data = i.getString(key);
                entries.add(new Entry(Float.valueOf(key), Float.valueOf(Data)));
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error in rendering money", Toast.LENGTH_LONG).show();
            }
        }
        chartMoney.getDescription().setEnabled(false);
        HourAxisValueFormatter axisFormatter = new HourAxisValueFormatter(0);
        chartMoney.getXAxis().setValueFormatter(axisFormatter);
        LineDataSet dataSet = new LineDataSet(entries, "Money");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(1);
        dataSet.setValueTextColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);
        chartMoney.setData(lineData);
        chartMoney.invalidate();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(USSDReceiver);
        super.onDestroy();
    }
}

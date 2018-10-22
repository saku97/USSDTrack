package inc.padmal.ussdtrack;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by knight on 10/22/18.
 */

public class Plotter {

    private HourAxisValueFormatter axisFormatter;

    private static final Plotter ourInstance = new Plotter();

    public static Plotter getInstance() {
        return ourInstance;
    }

    private Plotter() {
        axisFormatter = new HourAxisValueFormatter(0);
    }

    public void plotChart(Context context, JSONObject i, LineChart chart, String label, int color) {

        List<Entry> entries = new ArrayList<>();
        Iterator<String> iter = i.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String Data = i.getString(key);
                entries.add(new Entry(Float.valueOf(key), Float.valueOf(Data)));
            } catch (JSONException e) {
                Toast.makeText(context, "Error in Fetch", Toast.LENGTH_LONG).show();
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(1);
        dataSet.setValueTextColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);

        chart.getXAxis().setValueFormatter(axisFormatter);
        chart.getDescription().setEnabled(false);
        chart.setData(lineData);
        chart.invalidate();
    }

    public void loadCharts(Context context, LineChart chart, String data, int color, String label) {
        if (!data.isEmpty()) {
            try {
                JSONObject j = new JSONObject(data);
                plotChart(context, j, chart, label, color);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

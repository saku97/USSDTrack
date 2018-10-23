package inc.padmal.ussdtrack;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by knight on 10/23/18.
 */

class BackGrounds extends AsyncTask<Medium, Void, LineData> {

    @SuppressLint("StaticFieldLeak")
    private LineChart chart;

    private HourAxisValueFormatter axisFormatter;

    BackGrounds(LineChart chart) {
        this.chart = chart;
        axisFormatter = new HourAxisValueFormatter(0);
    }

    @Override
    protected LineData doInBackground(Medium... media) {
        Medium medium = media[0];

        List<Entry> entries = new ArrayList<>();
        Iterator<String> iter = medium.getI().keys();
        int i = 0;
        while (iter.hasNext()) {
            String key = iter.next();
            if (i > 1) {
                try {
                    String Data = String.valueOf(Float.valueOf(medium.getI().getString(key)));
                    if (!Data.equalsIgnoreCase(String.valueOf(entries.get(i - 2).getY()))) {
                        entries.add(new Entry(Float.valueOf(key), Float.valueOf(Data)));
                    } else {
                        i--;
                        entries.set(entries.size() - 1, new Entry(Float.valueOf(key), Float.valueOf(Data)));
                    }
                } catch (JSONException e) {
                    Toast.makeText(medium.getContext(), "Error in Fetch", Toast.LENGTH_LONG).show();
                }
            } else {
                try {
                    String Data = String.valueOf(Float.valueOf(medium.getI().getString(key)));
                    entries.add(new Entry(Float.valueOf(key), Float.valueOf(Data)));
                } catch (JSONException e) {
                    Toast.makeText(medium.getContext(), "Error in Fetch", Toast.LENGTH_LONG).show();
                }
            }
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, medium.getLabel());
        dataSet.setColor(medium.getColor());
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(1);
        dataSet.setValueTextColor(Color.BLACK);
        return new LineData(dataSet);
    }

    @Override
    protected void onPostExecute(LineData data) {
        super.onPostExecute(data);
        chart.getXAxis().setValueFormatter(axisFormatter);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate();
    }
}

package inc.padmal.ussdtrack;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by knight on 10/22/18.
 */

public class Plotter {

    private static final Plotter ourInstance = new Plotter();

    public static Plotter getInstance() {
        return ourInstance;
    }

    public void plotChart(Context context, JSONObject i, LineChart chart, String label,
                          int color, TextView tv, boolean mode) {
        Medium medium = new Medium(context, i, chart, label, color);
        BackGrounds backGrounds = new BackGrounds(chart, tv, mode);
        backGrounds.execute(medium);
    }

    public void loadCharts(Context context, LineChart chart, String data, int color, String label,
                           TextView tv, boolean mode) {
        if (!data.isEmpty()) {
            try {
                JSONObject j = new JSONObject(data);
                plotChart(context, j, chart, label, color, tv, mode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

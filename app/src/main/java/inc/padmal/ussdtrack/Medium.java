package inc.padmal.ussdtrack;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONObject;

/**
 * Created by knight on 10/23/18.
 */

public class Medium {

    Context context;
    JSONObject i;
    LineChart chart;
    String label;
    int color;

    public Medium(Context context, JSONObject i, LineChart chart, String label, int color) {
        this.context = context;
        this.i = i;
        this.chart = chart;
        this.label = label;
        this.color = color;
    }

    public Medium() {/**/}

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public JSONObject getI() {
        return i;
    }

    public void setI(JSONObject i) {
        this.i = i;
    }

    public LineChart getChart() {
        return chart;
    }

    public void setChart(LineChart chart) {
        this.chart = chart;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

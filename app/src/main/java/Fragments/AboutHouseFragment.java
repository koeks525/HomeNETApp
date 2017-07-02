package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutHouseFragment extends Fragment {

    private BarChart aboutHouseChart;
    private List<BarEntry> sampleEntries = new ArrayList<>();
    private BarDataSet barDataSet;
    private BarData barData;
    private XAxis xAxis;

    public AboutHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_about_house, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        aboutHouseChart = (BarChart) currentView.findViewById(R.id.AboutHouseBarChart);
        sampleEntries.add(new BarEntry(0, 50)); //Add sample house posts
        sampleEntries.add(new BarEntry(1, 6)); //Add sample message threads
        sampleEntries.add(new BarEntry(2, 20)); //Add sample users
        sampleEntries.add(new BarEntry(3, 4)); //Add sample photo galleries
        sampleEntries.add(new BarEntry(4, 2)); //Add sample video galleries
        sampleEntries.add(new BarEntry(5, 5)); //Add sample video calls made
        barDataSet = new BarDataSet(sampleEntries, "Overview Metrics");
        xAxis = aboutHouseChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String [] values = {"House Posts", "Conversations", "Users", "Photo Galleries", "Videos", "Calls"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
        barData = new BarData(barDataSet);
        aboutHouseChart.setData(barData);
        aboutHouseChart.invalidate();


    }

}

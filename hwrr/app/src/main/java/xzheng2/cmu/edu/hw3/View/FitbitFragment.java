package xzheng2.cmu.edu.hw3.View;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import xzheng2.cmu.edu.hw3.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FitbitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FitbitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitbitFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    OAuth oauth;
    HttpURLConnection conn;
    TextView userName;
    TextView calory;
    TextView dis;
    TextView steps;
    BarChart chart;


    public FitbitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FitbitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FitbitFragment newInstance(String param1, String param2) {
        FitbitFragment fragment = new FitbitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fitbit, container, false);
        ImageView fitlog = (ImageView)rootView.findViewById(R.id.fitlog);
        fitlog.setImageResource(R.drawable.fitbitlogo);

        dis = (TextView)rootView.findViewById(R.id.distance);
        steps = (TextView)rootView.findViewById(R.id.steps);
        calory = (TextView)rootView.findViewById(R.id.cal);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        TextView resultView = (TextView)rootView.findViewById(R.id.dateF);
        resultView.setText(currentDate);
        Button fitButton = (Button) rootView.findViewById(R.id.fitloginButton);
        fitButton.setOnClickListener(fitButtonClicked);
        // set the chart

        chart=(BarChart)rootView.findViewById(R.id.chart);
        chart.setDrawValueAboveBar(true);
        // set x axis
//        AxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextSize(10f);
//        xAxis.setTextColor(Color.RED);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setDrawGridLines(false);
//        YAxis yAxis = chart.getAxisLeft();
//        yAxis.setAxisMaxValue(10000f);
//        yAxis.setAxisMinValue(0f);
//        yAxis.setDrawAxisLine(false); // no axis line
//        yAxis.setDrawGridLines(false); // no grid lines
//        yAxis.setDrawZeroLine(true); // draw a zero line
//        chart.getAxisRight().setEnabled(false); // no right axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);




        YAxis leftAxis = chart.getAxisLeft();

        leftAxis.setLabelCount(8, false);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        rightAxis.setLabelCount(8, false);

        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        setData(12, 50);
        return rootView;
    }
    View.OnClickListener fitButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            oauth = new OAuth(getActivity());
            oauth.initialize("4mrdLpBc5SBgHQAtLeh5zbRS0aM");
            oauth.popup("fitbit", new OAuthCallback() {
                @Override
                public void onFinished(OAuthData data) {
                    if (!data.status.equals("success"))
                        Log.d("fit",data.error);
                    else {
//                        Log.d("fitbit test 1", "in on finished, data: "+ data.toString());
                        Log.d("fitbit token", data.token);
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                        HttpURLConnection con = null;
                        try {
                            URL object = new URL("https://api.fitbit.com/1/user/-/activities/date/today.json");
                            con = (HttpURLConnection) object.openConnection();
                            con.setRequestMethod("GET");
                            con.setRequestProperty("Authorization","Bearer "+data.token);
                            StringBuilder total = new StringBuilder();
                            String line = "";
                            try {
                                InputStream inputs= con.getInputStream();
                                BufferedReader r = new BufferedReader(new InputStreamReader(inputs));
                                while ((line = r.readLine()) != null) {
                                    total.append(line);
                                }
                                Log.d("onReady() total", "onReady() total: " + total);
                            } catch (Exception e) { e.printStackTrace(); }
                            parser(total.toString());
                        }catch (Exception e) { e.printStackTrace(); }

                    }

                }
            });

        }
    };

    public void parser(String input){
        String res = "";
        try {
            JSONObject jsonRootObject = new JSONObject(input);
            // get calories
            JSONObject summary = jsonRootObject.getJSONObject("summary");
            int calories = Integer.parseInt(summary.optString("caloriesOut").toString());
            Log.d("test cal",""+calories);
            calory.setText(""+calories);
            // get steps
            int step = Integer.parseInt(summary.getString("steps"));
            Log.d("test step",""+step);
            steps.setText(""+step);
            // get distance
            JSONArray jsonArray = summary.optJSONArray("distances");
            JSONObject total = jsonArray.getJSONObject(0);
            float distance = Float.parseFloat(total.optString("distance").toString());
            Log.d("test dis", ""+distance);
            dis.setText(""+distance);
            res = "steps= "+ steps +" \n calories= "+ calories +" \n distance= "+ distance +" \n ";
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setData(int count, float range) {

        float start = 0f;

        chart.getXAxis().setAxisMinValue(start);
        chart.getXAxis().setAxisMaxValue(start + count + 2);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i + 1f, val));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

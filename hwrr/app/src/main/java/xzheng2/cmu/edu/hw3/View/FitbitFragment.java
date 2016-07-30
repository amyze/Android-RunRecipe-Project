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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
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
        TextView userName = (TextView)rootView.findViewById(R.id.name);
        TextView dis = (TextView)rootView.findViewById(R.id.distance);
        TextView steps = (TextView)rootView.findViewById(R.id.steps);
        TextView cal = (TextView)rootView.findViewById(R.id.cal);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        TextView resultView = (TextView)rootView.findViewById(R.id.dateF);
        resultView.setText(currentDate);
        Button fitButton = (Button) rootView.findViewById(R.id.fitloginButton);
        fitButton.setOnClickListener(fitButtonClicked);

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
                        Log.d("fitbit token", data.provider);
                        Log.d("fitbit token", data.request.toString());
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                        // test1
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



//                            fit.setText(parser(total.toString()));


                        }catch (Exception e) { e.printStackTrace(); }

                    }

                }
            });

        }
    };

    public String parser(String input){
//        // parse Json
        String res = "";
        try {
            JSONObject jsonRootObject = new JSONObject(input);
            // get calories
            JSONObject summary = jsonRootObject.getJSONObject("summary");
            int calories = Integer.parseInt(summary.optString("caloriesOut").toString());
            calory.setText(""+calories);
            // get steps
            int step = Integer.parseInt(summary.getString("steps"));
            steps.setText(""+step);
            // get distance

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = summary.optJSONArray("distances");



            //Iterate the jsonArray and print the info of JSONObjects

            JSONObject total = jsonArray.getJSONObject(0);

            float distance = Float.parseFloat(total.optString("distance").toString());
            dis.setText(""+distance);




            res = "steps= "+ steps +" \n calories= "+ calories +" \n distance= "+ distance +" \n ";

            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

package xzheng2.cmu.edu.hw3.View;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.ViewActivity.ViewImgActivity;


//import android.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by zhengqian1 on 6/5/16.
 */
public class PVGPEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userName = "MobileApp4";
    private static int numTweets = 100;

    private OnFragmentInteractionListener mListener;
    public static final String ROW_ID = "row_id";
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
//    TweetTimelineListAdapter adapter;
//    private MyAdapter myAdapter;

    public static ArrayList<String> eventList = new ArrayList();
    public static List<Map<String,Object>> list;
    public static String[] tweetText;
    public static String[] tweetName;
    public static Date[] tweetDate;
    public static String[] imgURL;

//    public static TreeMap<String, String> map = new TreeMap<String, String>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
     // * @param param2 Parameter 2.
     * @return A new instance of fragment PVGPEventFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static PVGPEventFragment newInstance() {
        PVGPEventFragment fragment = new PVGPEventFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PVGPEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content




//        Log.d("PVGPLogFragment", sb1.toString());
    }
    public void getTimeline(String userName){
        ArrayList<String> timeline = new ArrayList<>();
       if (userName == null || userName.length()==0)
           return;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("OKoOmxo9Y4pGL4SDU1CTG3Eem");
        cb.setOAuthConsumerSecret("4ZZqw6eFe0HNQ6QgJ3rSJV61aGhGoYHYhMc6z6GTodoyednB6G");
        cb.setOAuthAccessToken("744183682508947456-XC2wxYQbAU3jnEk6TFD7x0OoCDUVN66");
        cb.setOAuthAccessTokenSecret("iHEnQNhiI8tBMxySzWJn5ua3Teb4XRCUtwU8rHjZeQitU");
        java.util.List statuses = null;

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();


        tweetText = new String[numTweets];
        tweetName = new String[numTweets];
        imgURL = new String[numTweets];
        tweetDate = new Date[numTweets];

        try {
            statuses = twitter.getUserTimeline();

        }
        catch(TwitterException e) {
        }
        if (statuses != null) {
            for (int i = 0; i < statuses.size(); i++) {
                Status status = (Status) statuses.get(i);
                User user = status.getUser();
                tweetName[i] = status.getUser().getName();
                tweetDate[i] = status.getCreatedAt();
                MediaEntity[]media = status.getMediaEntities();
                if(media!=null && media.length!=0) {
                    imgURL[i] = media[0].getMediaURL();
                }else imgURL[i]="";
//                    imgURL[i] = status.getUser().getMiniProfileImageURL();
//            imageIds[i]= R.drawable.ic_action_event;
                tweetText[i] = " " + status.getText();
                timeline.add(tweetText[i]);
//            System.out.println(tweetText[i]);
            }
        }

        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        View view = inflater.inflate(R.layout.actvity_list, container, false);
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Use hashMap to store the data for each tweet
            list = new ArrayList<Map<String,Object>>();
            getTimeline(userName);
            for(int i =0; i<numTweets;i++){
                Map<String,Object> map = new HashMap<String, Object>();
//                map.put("header",imageIds[i]);
                map.put("name",tweetName[i]);
                map.put("content",tweetText[i]);
                map.put("img",imgURL[i]);
                map.put("date",tweetDate[i]);
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,R.layout.list_adapter ,
                    new String[]{"name","content","img","date" },new int[]{R.id.name,R.id. desc,R.id.img,R.id.date});
            ListView listView = (ListView)view.findViewById(R.id.mylist);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent viewImg = new Intent(getActivity(), ViewImgActivity.class);

                    int index= (int)l;
                    viewImg.putExtra("urlString", imgURL[index]);
                    Log.d("EventListOnClick", String.valueOf(l));
                    startActivity(viewImg);
                }
            });
//            eventList = getTimeline(userName);
//            //  Set the adapter
//            mAdapter = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, android.R.id.text1, eventList);
//
//            mListView = (AbsListView) view.findViewById(android.R.id.list);
//            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        }
            return view;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    /*
    * Given a URL referring to an image, return a bitmap of that image
    */
//    private Bitmap getRemoteImage(final URL url) {
//        try {
//            final URLConnection conn = url.openConnection();
//            conn.connect();
//            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//            Bitmap bm = BitmapFactory.decodeStream(bis);
//            bis.close();
//            return bm;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

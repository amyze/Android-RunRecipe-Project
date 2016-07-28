package xzheng2.cmu.edu.hw3;

/**
 * Created by zhengqian1 on 6/5/16.
 */
//import com.twitter.sdk.android.Twitter;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import io.fabric.sdk.android.Fabric;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.lang.reflect.Field;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import xzheng2.cmu.edu.hw3.View.CanlanderFragment;
import xzheng2.cmu.edu.hw3.View.EventLogFragment;
import xzheng2.cmu.edu.hw3.View.FabricFragment;
import xzheng2.cmu.edu.hw3.View.FitbitFragment;
import xzheng2.cmu.edu.hw3.View.IDriftFragment;
import xzheng2.cmu.edu.hw3.View.PVGPEventFragment;
import xzheng2.cmu.edu.hw3.ViewActivity.FitbitLogin;


public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener, IDriftFragment.OnFragmentInteractionListener, PVGPEventFragment.OnFragmentInteractionListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "OKoOmxo9Y4pGL4SDU1CTG3Eem";
    private static final String TWITTER_SECRET = "4ZZqw6eFe0HNQ6QgJ3rSJV61aGhGoYHYhMc6z6GTodoyednB6G";



    public static final String TAG = "iDrift";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     //* {@link Fragment Pager Adapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar ab =getSupportActionBar();
//        ab.setDisplayShowHomeEnabled(true);
//        ab.setIcon(R.drawable.ic_launcher);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_home);



        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        setOverflowShowingAlways();

    }
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // implement onFragmentInteraction
    public void onFragmentInteraction(String id){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.common_title);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();




        switch (id) {



            case R.id.login:

                Toast.makeText(this, "login test", Toast.LENGTH_SHORT).show();
                Intent itemList = new Intent(HomeActivity.this, FitbitLogin.class);
                startActivity(itemList);


                return true;
            case R.id.logout:
                Toast.makeText(this, "logout test", Toast.LENGTH_SHORT).show();
//

                return true;

//


            default:
                return super.onOptionsItemSelected(item);
        }
        // return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     //* A {@link Fragment Pager Adapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    //FragmentPagerAdapter
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //////do something

            //return PlaceholderFragment.newInstance(position + 1);

            switch (position) {
                case 0:
                    return new CanlanderFragment();
                case 1:
//                    return new FabricFragment();
                    return new EventLogFragment();
                case 2:
                    return new FabricFragment();
                case 3:
                    return new FitbitFragment();

                default:
                    break;
            }
            return PlaceholderFragment.newInstance(position + 1);


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }


        //@Override
        public void onFragmentInteraction(Uri uri) {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "history";
                case 1:
                    return "Run".toUpperCase(l);
                case 2:
                    return "Recipe".toUpperCase(l);
                case 3:
                    return "Fitbit";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
    }

}

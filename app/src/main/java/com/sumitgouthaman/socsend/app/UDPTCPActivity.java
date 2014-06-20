package com.sumitgouthaman.socsend.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sumitgouthaman.socsend.app.general_utils.EndPoint;
import com.sumitgouthaman.socsend.app.general_utils.ParseMessage;
import com.sumitgouthaman.socsend.app.general_utils.Validation;
import com.sumitgouthaman.socsend.app.socket_utils.UDPSender;

import java.util.Locale;


public class UDPTCPActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udptcp);

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
                            .setTabListener(this)
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.udptc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == 0) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int section_number = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = null;
            switch (section_number) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_udp, container, false);
                    setupUDP(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_tcp, container, false);
                    setupTCP(rootView);
                    break;
            }
            return rootView;
        }

        private void setupUDP(final View rootView) {
            ImageButton udpSend = (ImageButton) rootView.findViewById(R.id.imageButton_udp_send);
            udpSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ipAddress = ((EditText) rootView.findViewById(R.id.editText_udp_ip_address)).getText().toString().trim();
                    String ipValidation = Validation.validateIP(getActivity(), ipAddress);
                    if (!(ipValidation == null)) {
                        Toast.makeText(getActivity(), ipValidation, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String portStr = ((EditText) rootView.findViewById(R.id.editText_udp_port)).getText().toString().trim();
                    String portValidation = Validation.validatePort(getActivity(), portStr);
                    if (!(portValidation == null)) {
                        Toast.makeText(getActivity(), portValidation, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int port = Integer.parseInt(portStr);
                    String message = ((EditText) rootView.findViewById(R.id.editText_udp_message)).getText().toString();
                    RadioButton udpBytes = (RadioButton) rootView.findViewById(R.id.radioButton_udp_bytes);
                    if (udpBytes.isChecked()) {
                        CheckBox hexFormat = (CheckBox) rootView.findViewById(R.id.checkBox_udp_hex);
                        byte[] bytes;
                        if (!hexFormat.isChecked()) {
                            bytes = ParseMessage.parseBytes(message);
                        } else {
                            bytes = ParseMessage.parseBytesHex(message);
                        }
                        if (!(bytes == null)) {
                            EndPoint endPoint = new EndPoint(ipAddress, port);
                            //UDPSender.send(endPoint, bytes); //Needs asynctask
                        } else {
                            Toast.makeText(getActivity(), R.string.message_invalid, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (message.length() > 0) {
                            Toast.makeText(getActivity(), R.string.message_invalid, Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            EndPoint endPoint = new EndPoint(ipAddress, port);
                            //UDPSender.send(endPoint, message); //Needs asynctask
                        }
                    }
                }
            });
        }

        private void setupTCP(final View rootView) {
            ImageButton tcpSend = (ImageButton) rootView.findViewById(R.id.imageButton_tcp_send);
            tcpSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ipAddress = ((EditText) rootView.findViewById(R.id.editText_udp_ip_address)).getText().toString().trim();
                    String ipValidation = Validation.validateIP(getActivity(), ipAddress);
                    if (!(ipValidation == null)) {
                        Toast.makeText(getActivity(), ipValidation, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String portStr = ((EditText) rootView.findViewById(R.id.editText_udp_port)).getText().toString().trim();
                    String portValidation = Validation.validatePort(getActivity(), portStr);
                    if (!(portValidation == null)) {
                        Toast.makeText(getActivity(), portValidation, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int port = Integer.parseInt(portStr);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.title_tcp), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_udp).toUpperCase(l);
                case 1:
                    return getString(R.string.title_tcp).toUpperCase(l);
            }
            return null;
        }
    }

}

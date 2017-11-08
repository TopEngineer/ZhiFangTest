package com.ipr.zhifangtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZhifangTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZhifangTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhifangTestFragment extends Fragment {
    private static final String TAG = "ZhifangTestFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ZhifangTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhifangTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZhifangTestFragment newInstance(String param1, String param2) {
        ZhifangTestFragment fragment = new ZhifangTestFragment();
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
    private MyViewPager viewPager;
    private List<Fragment> frags;
    private ZhiFangAdapter adapter;
    private static String START_ORDER_HEAD = "55AAC100";
    private static String START_ORDER_FOOT = "000000000000000000000000000000000000000000000000";

    private String sgValue = "170.0";
    private String tzValue = "66.2";
    private String genderValue;
    private String ageValue;
    private String startOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhifang_test, container, false);
        viewPager = (MyViewPager) view.findViewById(R.id.viewpager);
        frags = new ArrayList<>();
        /*frags.add(new SgTzTestFragment().setOnSwitListener(new SgTzTestFragment.OnSwitListener() {
            @Override
            public void onSwitch(String str1, String str2) {
                sgValue = str1;
                tzValue = str2;
                switchPager();
            }
        }));*/
        frags.add(new InputSgTzFragment().setOnSwitListener(new InputSgTzFragment.OnSwitListener() {
            @Override
            public void onSwitch(String str1, String str2) {
                sgValue = str1;
                tzValue = str2;
                switchPager();
            }
        }));
        frags.add(new SexAgeFragment().setOnSwitListener(new SexAgeFragment.OnSwitListener() {
            @Override
            public void onSwitch(String gender, String age) {
               /* genderValue = gender;
                ageValue = age;*/
//                String sg = buLing(getHexStr(sgValue.substring(0, sgValue.indexOf("."))), 2).toUpperCase();
//                Log.d(TAG, "sg 四舍五入: "+new BigDecimal(sgValue).setScale(0, BigDecimal.ROUND_HALF_UP));
//                String sg = buLing(getHexStr(""+new BigDecimal(sgValue).setScale(0, BigDecimal.ROUND_HALF_UP)), 2).toUpperCase();
//                String tz =DataUtil.bytesToHexString(DataUtil.hexStringToBytes(Integer.toHexString(Integer.parseInt(tzValue))));

                String sg = buLing(getHexStr(""+new BigDecimal(sgValue).setScale(0, BigDecimal.ROUND_HALF_UP)), 2).toUpperCase();
//                String sg = buLing(getHexStr(sgValue.replace(".", "")), 4).substring(2, 4).toUpperCase() + buLing(getHexStr(sgValue.replace(".", "")), 4).substring(0, 2).toUpperCase();
                String tz = buLing(getHexStr(tzValue.replace(".", "")), 4).substring(2, 4).toUpperCase() + buLing(getHexStr(tzValue.replace(".", "")), 4).substring(0, 2).toUpperCase();
                String ageV = buLing(getHexStr(age), 2).toUpperCase();
                String genderV = buLing(getHexStr(gender), 2).toUpperCase();
                Log.d(TAG, "sg： " + sg + "  tz:" + tz + " age：" + ageV + " gender" + genderV);
                startOrder = START_ORDER_HEAD + sg + tz + ageV + genderV + START_ORDER_FOOT;
                startOrder = startOrder + jiaoYan(startOrder);
                Intent intent = new Intent();
                intent.setAction("com.ipr.send");
                intent.putExtra("msg", startOrder);
                getActivity().sendBroadcast(intent);
                switchPager();
            }
        }));
        frags.add(new ZhiFangResultTestFragment());
        adapter = new ZhiFangAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        return view;
    }

    private String getHexStr(String value) {
        return Integer.toHexString(Integer.parseInt(value));
    }

    private String buLing(String str, int wei) {
        if (str.length() < wei) {
            String ling = "";
            for (int i = 0; i < wei - str.length(); i++) {
                ling = ling + "0";
            }
            return ling + str;
        }
        return str;
    }

    private String jiaoYan(String order) {
        byte[] bys = DataUtil.hexStringToBytes(order);
        byte b = 0;
        for (int i = 0; i < 33; i++) {
            b += bys[i];
        }
        b = (byte) (255 - b + 1);
        return DataUtil.toHex(b);
//        Log.d(TAG, "jiaoYan: "+DataUtil.toHex(b));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void switchPager() {
//        if (!(viewPager.getCurrentItem()+1 == viewPager.getChildCount()))
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    class ZhiFangAdapter extends FragmentStatePagerAdapter {

        public ZhiFangAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object)   {
            return POSITION_NONE;
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

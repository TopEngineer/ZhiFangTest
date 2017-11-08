package com.ipr.zhifangtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SgTzTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SgTzTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SgTzTestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SgTzTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SgTzTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SgTzTestFragment newInstance(String param1, String param2) {
        SgTzTestFragment fragment = new SgTzTestFragment();
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
    private TextView tv_sg_v;
    private TextView tv_tz_v;
    private TextView tv_cancel;
    private TextView tv_ok;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sg_tz_test, container, false);
        tv_sg_v = (TextView) view.findViewById(R.id.tv_sg_v);
        tv_tz_v = (TextView) view.findViewById(R.id.tv_tz_v);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.ipr.send");
                intent.putExtra("msg", HWConstant.START_MEASURE_ORDER);
                getActivity().sendBroadcast(intent);
            }
        });
//        EventBus.getDefault().post(new MsgEvent(0));
        EventBus.getDefault().register(this);
        return view;
    }


    private String sgValue;
    private String tzValue;
    private String sgResult;
    private String tzResult;
    private boolean isFirst = true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(HWMsgEvent event) {
        switch (event.getType()) {
            case 5://测量结束 C0
                //                handler.removeMessages(0);
//                handler.sendEmptyMessageDelayed(0, 1000);
                String[] mesureResultStr = event.getMsg().split(",");
                sgValue = mesureResultStr[0];
                tzValue = mesureResultStr[1];
                sgResult = sgValue;
                tzResult = tzValue;
                tv_sg_v.setText(sgResult);
                tv_tz_v.setText(tzResult);
                if (switListener!=null){
                    switListener.onSwitch(sgValue,tzValue);
                }
                break;
            case 6://测量数据 A0
                if (isFirst){
                    isFirst = false;
                }
                String[] mesureStr = event.getMsg().split(",");
                sgValue = mesureStr[0];
                tzValue = mesureStr[1];
                tv_sg_v.setText(sgValue);
                tv_tz_v.setText(tzValue);

                break;
        }
    }
    private OnSwitListener switListener;
    public SgTzTestFragment setOnSwitListener(OnSwitListener switListener){
        this.switListener = switListener;
        return this;
    }
    public interface OnSwitListener{
        void onSwitch(String str1, String str2);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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

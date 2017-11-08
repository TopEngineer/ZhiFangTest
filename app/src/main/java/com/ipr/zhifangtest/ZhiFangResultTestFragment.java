package com.ipr.zhifangtest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZhiFangResultTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZhiFangResultTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhiFangResultTestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ZhiFangResultTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhiFangResultTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZhiFangResultTestFragment newInstance(String param1, String param2) {
        ZhiFangResultTestFragment fragment = new ZhiFangResultTestFragment();
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
    private TextView tv_zfl;
    private TextView tv_dx;
    private TextView tv_tx;
    private TextView tv_bmi;
    private TextView tv_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhi_fang_result_test, container, false);
        tv_zfl = (TextView) view.findViewById(R.id.tv_zfl);
        tv_dx = (TextView) view.findViewById(R.id.tv_dx);
        tv_tx = (TextView) view.findViewById(R.id.tv_tx);
        tv_bmi = (TextView) view.findViewById(R.id.tv_bmi);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        EventBus.getDefault().register(this);
        return view;
    }

    private String[] msgs;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onMoonEvent(ZhiFangEvent event) {
//        int type = event.getType();
//        Toast.makeText(getActivity(), "脂肪 "+event.getMsg(), Toast.LENGTH_SHORT).show();
        switch (event.getType()){
            case 0:
                msgs = event.getMsg().split("_");
                tv_zfl.setText(Double.parseDouble(msgs[0])+"%");
                tv_dx.setText(msgs[2]);
                tv_bmi.setText(msgs[1]);
                tv_tx.setText(msgs[4]);
                break;
            case 1:
                Toast.makeText(getActivity(), event.getMsg(), Toast.LENGTH_SHORT).show();
                break;
        }
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

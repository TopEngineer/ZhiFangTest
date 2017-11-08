package com.ipr.zhifangtest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputSgTzFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputSgTzFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputSgTzFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InputSgTzFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputSgTzFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputSgTzFragment newInstance(String param1, String param2) {
        InputSgTzFragment fragment = new InputSgTzFragment();
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

    private EditText et_sg;
    private EditText et_tz;
    private TextView tv_cancel;
    private TextView tv_ok;
    public static DecimalFormat df = new DecimalFormat("#0.0");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_sg_tz, container, false);
        et_sg = (EditText) view.findViewById(R.id.et_sg);
        et_tz = (EditText) view.findViewById(R.id.et_tz);
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
                String sgValue = et_sg.getText().toString().trim();
                String tzValue = et_tz.getText().toString().trim();
                if (TextUtils.isEmpty(sgValue)||Double.parseDouble(sgValue)>255){
                    Toast.makeText(getActivity(),"身高输入有误，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tzValue)||Double.parseDouble(tzValue)>255){
                    Toast.makeText(getActivity(),"体重输入有误，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (switListener!=null){
                    switListener.onSwitch(df.format(Double.parseDouble(sgValue)),df.format(Double.parseDouble(tzValue)));
                }
            }
        });
        return view;
    }


    private OnSwitListener switListener;
    public InputSgTzFragment setOnSwitListener(OnSwitListener switListener){
        this.switListener = switListener;
        return this;
    }
    public interface OnSwitListener{
        void onSwitch(String str1, String str2);
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

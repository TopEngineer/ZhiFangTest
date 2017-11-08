package com.ipr.zhifangtest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SexAgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SexAgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SexAgeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SexAgeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SexAgeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SexAgeFragment newInstance(String param1, String param2) {
        SexAgeFragment fragment = new SexAgeFragment();
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
    private RadioGroup rg_sex;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private EditText et_age;
    private TextView tv_cancel;
    private TextView tv_ok;
    private String gender;
    private String age;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sex_age, container, false);
        rg_sex = (RadioGroup) view.findViewById(R.id.rg_sex);
        rb_man = (RadioButton) view.findViewById(R.id.rb_man);
        rb_woman = (RadioButton) view.findViewById(R.id.rb_woman);
        et_age = (EditText) view.findViewById(R.id.et_age);
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
                switch (rg_sex.getCheckedRadioButtonId()){
                    case R.id.rb_man:
                        gender = "1";
                        break;
                    case R.id.rb_woman:
                        gender = "0";
                        break;
                    default:
                        Toast.makeText(getActivity(),"请选择性别",Toast.LENGTH_SHORT).show();
                        return;
                }
                String age = et_age.getText().toString().trim();
                if (TextUtils.isEmpty(age)||Integer.parseInt(age)>200){
                    Toast.makeText(getActivity(),"请输入年龄",Toast.LENGTH_SHORT).show();
                    return;
                }
//                age = et_age.getText().toString().trim();
                if (switListener!=null)
                    switListener.onSwitch(gender,age);
            }
        });
        return view;
    }

    private OnSwitListener switListener;
    public SexAgeFragment setOnSwitListener(OnSwitListener switListener){
        this.switListener = switListener;
        return this;
    }
    public interface OnSwitListener{
        void onSwitch(String gender, String age);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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

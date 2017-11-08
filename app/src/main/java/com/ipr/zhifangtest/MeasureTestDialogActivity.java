package com.ipr.zhifangtest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

public class MeasureTestDialogActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_measure_test_dialog);
        int flag = getIntent().getIntExtra("test",0);
        if (flag==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_mea_test,new ZhifangTestFragment()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_mea_test,new SgTzTestFragment()).commit();
        }

    }
}

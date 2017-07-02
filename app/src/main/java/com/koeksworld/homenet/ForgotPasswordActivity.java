package com.koeksworld.homenet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import Fragments.ForgotPasswordFragment;
import Fragments.ForgotPasswordInformationFragment;
import Utilities.DeviceUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private DeviceUtils deviceUtils;
    private FragmentManager manager;
    private TextView toolbarTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        toolbarTextView = (TextView) findViewById(R.id.ForgotPasswordToolbarTextView);
        toolbarTextView.setText("Reset Password");
        deviceUtils = new DeviceUtils(this);
        manager = getFragmentManager();
        loadScreen();
    }

    private void loadScreen() {
        if (deviceUtils.isTablet()) {
            if (deviceUtils.isLandscape()) {
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                FragmentTransaction transactionOne = manager.beginTransaction();
                transactionOne.replace(R.id.ForgotPasswordTabletViewOne, forgotPasswordFragment, null);
                transactionOne.commit();

                ForgotPasswordInformationFragment forgotPasswordInformationFragment = new ForgotPasswordInformationFragment();
                FragmentTransaction transactionTwo = manager.beginTransaction();
                transactionTwo.replace(R.id.ForgotPasswordTableViewTwo, forgotPasswordInformationFragment, null);
                transactionTwo.commit();
            } else {
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                FragmentTransaction transactionThree = manager.beginTransaction();
                transactionThree.replace(R.id.ForgotPasswordTabletPortraitView, forgotPasswordFragment, null);
                transactionThree.commit();
            }
        } else {
                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.ForgotPasswordMobileView, fragment, null);
                transaction.commit();
        }
    }


}

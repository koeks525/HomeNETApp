package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import NotificationFragments.NotificationAnnouncementFragment;

public class NewAnnouncementActivity extends AppCompatActivity {

    private Bundle houseBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_announcement);
        getData();
    }

    private void getData() {
        houseBundle = getIntent().getBundleExtra("notificationBundle");
        if (houseBundle == null) {
            displayMessage("No Data", "No post data was received. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mainIntent = new Intent(NewAnnouncementActivity.this, HomeNetFeedActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            });
        } else {
            NotificationAnnouncementFragment fragment = new NotificationAnnouncementFragment();
            fragment.setArguments(houseBundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.NotificationAnnouncementContentView, fragment, null);
            transaction.commit();

        }
    }
    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton("Ok", listener).setCancelable(false).show();
    }
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, HomeNetFeedActivity.class);
        startActivity(back);
        finish();
    }
}

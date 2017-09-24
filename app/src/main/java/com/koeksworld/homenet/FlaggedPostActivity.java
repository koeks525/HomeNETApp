package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import NotificationFragments.FlaggedPostFragment;

public class FlaggedPostActivity extends AppCompatActivity {

    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_post);
        getData();
    }

    private void getData() {
        bundle = getIntent().getBundleExtra("notificationBundle");
        if (bundle != null) {
            FragmentTransaction transaction  = getFragmentManager().beginTransaction();
            FlaggedPostFragment fragment = new FlaggedPostFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.NotificationFlaggedPostContentView, fragment, null);
            transaction.commit();
        } else {
            displayMessage("No Post Data", "No post data was received. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent main = new Intent(FlaggedPostActivity.this, HomeManagerActivity.class);
                    startActivity(main);
                    finish();

                }
            });
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton("Got it", listener).setCancelable(false).show();
    }
}

package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import NotificationFragments.NotificationNewPostFragment;

public class NewPostActivity extends AppCompatActivity {

    private TextView toolbarTextView;
    private int housePostID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        toolbarTextView = (TextView) findViewById(R.id.NewPostActivityTitleTextView);
        toolbarTextView.setText("Getting Data...");
        getData();
        if (housePostID == 0) {
            displayMessage("No Data", "No partial house post data was found. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent newIntent = new Intent(NewPostActivity.this, HomeNetFeedActivity.class);
                    startActivity(newIntent);
                    finish();
                }
            });
        } else {
            Bundle housePostBundle = new Bundle();
            housePostBundle.putInt("housePostID", housePostID);
            NotificationNewPostFragment fragment = new NotificationNewPostFragment();
            fragment.setArguments(housePostBundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.NewPostActivityContentView, fragment, null);
            transaction.commit();
        }
    }
    public void getData() {
        Bundle extras = getIntent().getBundleExtra("notificationBundle");
        housePostID = extras.getInt("housePostID");
    }
    @Override
    public void onBackPressed() {
        Intent mainFeed = new Intent(this, HomeNetFeedActivity.class);
        startActivity(mainFeed);
        finish();
    }
    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }
}

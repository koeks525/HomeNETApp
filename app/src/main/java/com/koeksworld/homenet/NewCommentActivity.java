package com.koeksworld.homenet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewCommentActivity extends AppCompatActivity {

    private int housePostID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        getData();
    }

    private void getData () {
        Bundle extras = getIntent().getBundleExtra("notificationBundle");
        housePostID = extras.getInt("housePostID");
        if (housePostID <= 0) {
            displayMessage("No Data found", "No house post data has been found. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mainIntent = new Intent(NewCommentActivity.this, HomeNetFeedActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            });
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }
}

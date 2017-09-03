package DialogFragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.Locale;

import Communication.HomeNetService;
import Tasks.SearchHousesTask;
import Utilities.DeviceUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/04/18.
 */

//Source: http://www.androidhive.info/2014/07/android-speech-to-text-tutorial/

public class SearchHousesDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int SPEECH_REQUEST = 400;
    private EditText searchHousesEditText;
    private Button searchHousesButton;
    private RecyclerView searchHousesRecyclerView;
    private DeviceUtils deviceUtils;
    private Retrofit retrofit;
    private HomeNetService service;
    private View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.search_houses_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Search Houses");
        builder.setCancelable(false);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        initializeComponents(dialogView);
        initializeRetrofit();
        return builder.create();
    }

    private void initializeComponents(View currentView) {
        searchHousesButton = (Button) currentView.findViewById(R.id.SearchHousesButton);
        searchHousesEditText = (EditText) currentView.findViewById(R.id.SearchHousesEditText);
        searchHousesEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (searchHousesEditText.getRight() - searchHousesEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        startSpeech();
                        return true;
                    }
                }
                return false;
            }
        });
        searchHousesRecyclerView = (RecyclerView) currentView.findViewById(R.id.SearchHousesRecyclerView);
        searchHousesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchHousesButton.setOnClickListener(this);
        deviceUtils = new DeviceUtils(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SearchHousesButton:
                if (deviceUtils.checkNetworkConnection()) {
                    String searchParams = searchHousesEditText.getText().toString().trim();
                    if (searchParams != "") {
                        SearchHousesTask task = new SearchHousesTask(getActivity(), searchParams, searchHousesRecyclerView);
                        task.execute();
                    } else {
                        displaySnackBar("Please enter search terms", view);
                    }

                } else {
                    displaySnackBar("Please check your network connection", view);
                }



                break;
        }
    }

    private void startSpeech() {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(speechIntent, SPEECH_REQUEST);
        } catch (Exception error){
            Toast.makeText(getActivity(), getString(R.string.speech_not_support), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_REQUEST:
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    ArrayList<String> resultData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchHousesEditText.setText(resultData.get(0));
                    SearchHousesTask task = new SearchHousesTask(getActivity(), resultData.get(0), searchHousesRecyclerView);
                    task.execute();

                }
        }
    }

    private void displaySnackBar(String message, View currentView) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }
}

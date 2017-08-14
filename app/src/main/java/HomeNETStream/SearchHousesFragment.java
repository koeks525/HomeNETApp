package HomeNETStream;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Tasks.SearchHousesTask;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/13.
 */

public class SearchHousesFragment extends Fragment implements View.OnClickListener {

    private static final int SPEECH_REQUEST = 200;
    private TextView toolbarTextView;
    private EditText searchHouseEditText;
    private RecyclerView recyclerView;
    private Button searchButton;
    private OkHttpClient client;
    private Retrofit retrofit;
    private List<Protocol> protocolList;
    private HomeNetService service;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.search_houses_dialog, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Search Houses");
        searchHouseEditText = (EditText) currentView.findViewById(R.id.SearchHousesEditText);
        searchHouseEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (searchHouseEditText.getRight() - searchHouseEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        startSpeech();
                        return true;
                    }
                }
                return false;
            }
        });
        searchButton = (Button) currentView.findViewById(R.id.SearchHousesButton);
        searchButton.setOnClickListener(this);
        recyclerView = (RecyclerView) currentView.findViewById(R.id.SearchHousesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SearchHousesButton:


                break;
        }
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
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
                    searchHouseEditText.setText(resultData.get(0));
                    SearchHousesTask task = new SearchHousesTask(getActivity(), resultData.get(0), recyclerView);
                    task.execute();

                }
        }
    }
}

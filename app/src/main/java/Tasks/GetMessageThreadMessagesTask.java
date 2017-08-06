package Tasks;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.GetMessageThreadMessagesAdapter;
import Communication.HomeNetService;
import Models.MessageThread;
import Models.MessagesViewModel;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class GetMessageThreadMessagesTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private Activity currentActivity;
    private RecyclerView recyclerView;
    private List<MessagesViewModel> messagesList;
    private HomeNetService service;
    private ProgressDialog dialog;
    private String errorInformation = "";
    private MessageThread selectedThread;
    private GetMessageThreadMessagesAdapter adapter;

    public GetMessageThreadMessagesTask(Activity currentActivity, RecyclerView recyclerView, MessageThread selectedThread) {
        this.currentActivity = currentActivity;
        this.selectedThread = selectedThread;
        this.recyclerView = recyclerView;
        messagesList = new ArrayList<>();
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setCancelable(false);
        dialog.setMessage("Fetching Message Feed. Please wait...");
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<MessagesViewModel>> response = service.getMessagesInThread("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedThread.getMessageThreadID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (response.isSuccessful()) {
                if (response.body().getModel() != null) {
                    for(MessagesViewModel model : response.body().getModel()) {
                        messagesList.add(model);
                    }
                }
            } else {
                errorInformation += response.errorBody().string();
            }

        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            errorInformation = error.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorInformation == "") {
            if (messagesList.size() > 0) {
                adapter = new GetMessageThreadMessagesAdapter(messagesList);
                recyclerView.setAdapter(adapter);
            } else {
                displayMessage("No Conversation Found", "No conversaton has been found for the selected message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentActivity.getFragmentManager().popBackStack(); //Go back
                    }
                });
            }
        } else {
            displayMessage("Error Getting Messages", errorInformation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

    }



    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
            }
        });
    }
}

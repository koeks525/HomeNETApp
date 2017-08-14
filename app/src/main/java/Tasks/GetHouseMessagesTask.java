package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.MessagesAdapter;
import Adapters.UserMessagesAdapter;
import Communication.HomeNetService;
import Models.MessageThread;
import Models.MessageThreadMessage;
import Models.MessagesViewModel;
import Models.NewMessageThreadViewModel;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/07/29.
 */

public class GetHouseMessagesTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private OkHttpClient client;
    private Activity currentActivity;
    private ProgressDialog dialog;
    private RecyclerView messagesRecyclerView;
    private UserMessagesAdapter messagesAdapter;
    private List<MessageThread> messageThreadList;
    private List<Protocol> protocolList;
    private String errorInformation = "";
    private boolean noPosts = false;

    public GetHouseMessagesTask(Activity currentActivity, RecyclerView messagesRecyclerView) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        this.currentActivity = currentActivity;
        this.messagesRecyclerView = messagesRecyclerView;
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        messageThreadList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching messages, please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<MessageThread>> response = service.getMessageThreads("Bearer "+sharedPreferences.getString("authorization_token",""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (response.isSuccessful()) {
                if (response.body().getModel() != null) {
                    for (MessageThread current : response.body().getModel()) {
                        messageThreadList.add(current);
                    }
                }
            } else {
                if (response.code() == 404) {
                    noPosts = true;
                } else {
                    errorInformation += response.errorBody().string();
                }
            }
            response = null;
            if (messageThreadList.size() > 0) {
                for (MessageThread current : messageThreadList) {
                    Response<ListResponse<MessagesViewModel>> messages = service.getMessagesInThread("Bearer " + sharedPreferences.getString("authorization_token", ""), current.getMessageThreadID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (messages.isSuccessful()) {

                        if (messages.body().getModel() != null) {
                            for (MessagesViewModel item : messages.body().getModel()) {
                                MessageThread currentMessage = null;
                                for (MessageThread currentThread : messageThreadList) {
                                    if (currentThread.getMessageThreadID() == item.getMessageThreadID()) {
                                        currentMessage = currentThread;
                                        break;
                                    }
                                }
                                if (currentMessage != null) {
                                    currentMessage.setMessageList(messages.body().getModel());
                                }
                            }
                        }
                    }
                }



            }
            return 1;

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
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorInformation == "") {
            if (messageThreadList.size() > 0) {
                messagesAdapter = new UserMessagesAdapter(messageThreadList, currentActivity);
                messagesRecyclerView.setAdapter(messagesAdapter);
            } else {
                displayToast("No Messages Found!");
            }


        } else {
            displayMessage("Error Getting Messages", errorInformation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener);
                messageBox.setCancelable(false);
                messageBox.show();
            }
        });
    }
    private void displayToast(String message) {
        Snackbar.make(currentActivity.getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
    }
}

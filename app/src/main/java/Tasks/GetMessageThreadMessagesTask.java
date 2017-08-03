package Tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import Models.MessagesViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;

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

    public GetMessageThreadMessagesTask(Activity currentActivity, RecyclerView recyclerView) {
        this.currentActivity = currentActivity;
        this.recyclerView = recyclerView;
    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        return null;
    }
}

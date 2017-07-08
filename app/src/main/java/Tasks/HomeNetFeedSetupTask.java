package Tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import Communication.HomeNetService;
import retrofit2.Retrofit;

/**
 * Created by Okuhle on 2017/07/08.
 */

public class HomeNetFeedSetupTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;

    @Override
    protected Integer doInBackground(Integer... integers) {
        return null;
    }
}

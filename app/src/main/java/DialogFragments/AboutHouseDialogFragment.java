package DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.List;

import Communication.HomeNetService;
import Models.House;
import Tasks.GetHouseProfileTask;
import Tasks.JoinHouseTask;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;

/**
 * Created by Okuhle on 2017/08/21.
 */

public class AboutHouseDialogFragment extends DialogFragment {

    private TextView name, description, members, dateCreated;
    private ImageView houseImageView;
    private Retrofit retrofit;
    private OkHttpClient client;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private View dialogView;
    private AlertDialog dialog;
    private House selectedHouse;
    private SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.about_house_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("House Details");
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JoinHouseTask joinHouseTask = new JoinHouseTask(getActivity(), selectedHouse.getHouseID(), sharedPreferences.getString("emailAddress", ""));
                joinHouseTask.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.show();
        initializeComponents(dialogView);
        return dialog;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        selectedHouse = getArguments().getParcelable("SelectedHouse");
        name = (TextView) currentView.findViewById(R.id.AboutHouseNameTextView);
        description = (TextView) currentView.findViewById(R.id.AboutHouseDescriptionTextView);
        members = (TextView) currentView.findViewById(R.id.AboutHouseTotalMembersTextView);
        dateCreated = (TextView) currentView.findViewById(R.id.AboutHouseDateCreatedTextView);
        houseImageView = (ImageView) currentView.findViewById(R.id.AboutHouseImageView);
        GetHouseProfileTask task = new GetHouseProfileTask(name, description, members, dateCreated, houseImageView, getActivity(), selectedHouse);
        task.execute();
    }
}

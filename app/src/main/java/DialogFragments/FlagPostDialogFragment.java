package DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import Models.HousePostViewModel;

/**
 * Created by Okuhle on 2017/09/02.
 */

public class FlagPostDialogFragment extends DialogFragment {

    HousePostViewModel selectedPost;
    private ImageView nameSurnameImageView;
    private TextView nameTextView, descriptionTextView;
    private EditText reasonEditText;
    private View dialogView;
    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.flag_post_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Flag Post");
        builder.setPositiveButton("Flag", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        selectedPost = getArguments().getParcelable("SelectedPost");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameSurnameImageView = (ImageView) currentView.findViewById(R.id.FlagPostImageView);
        nameTextView = (TextView) currentView.findViewById(R.id.FlagPostNameSurnameTextView);
        descriptionTextView = (TextView) currentView.findViewById(R.id.FlagPostDescriptionTextView);
        reasonEditText = (EditText) currentView.findViewById(R.id.FlagPostReasonEditText);
        nameTextView.setText(selectedPost.getName() + " "+selectedPost.getSurname());
        descriptionTextView.setText(selectedPost.getPostText());
        TextDrawable drawable = TextDrawable.builder().buildRect(selectedPost.getName().substring(0, 1).toUpperCase()+selectedPost.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
        nameSurnameImageView.setImageDrawable(drawable);
    }
}

package DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.koeksworld.homenet.R;

import Models.HousePostViewModel;
import Models.Picture;

/**
 * Created by Okuhle on 2017/09/04.
 */

public class PhotoDetailsFragment extends DialogFragment {

    private HousePostViewModel housePost;
    private Picture selectedPicture;

    private ImageView photoImageView, nameSurnameImageView;
    private TextView nameSurnameTextView, postDescriptionTextView;
    private View dialogView;
    private Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.photo_details_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Photo Details");
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.show();
        initializeComponents(dialogView);
        return dialog;
    }

    private void initializeComponents(View currentView) {
        housePost = getArguments().getParcelable("SelectedPost");
        selectedPicture = getArguments().getParcelable("SelectedPhoto");
        photoImageView = (ImageView) currentView.findViewById(R.id.PhotoDetailsImageView);
        nameSurnameImageView = (ImageView) currentView.findViewById(R.id.PhotoDetailsNameSurnameImageView);
        nameSurnameTextView = (TextView) currentView.findViewById(R.id.PhotoDetailsNameSurnameTextView);
        postDescriptionTextView = (TextView) currentView.findViewById(R.id.PhotoDetailsTextView);
        TextDrawable drawable = TextDrawable.builder().buildRect(housePost.getName().substring(0,1).toUpperCase() + housePost.getSurname().toUpperCase().substring(0,1), Color.BLUE);
        nameSurnameImageView.setImageDrawable(drawable);
        Glide.with(getActivity()).load(selectedPicture.getImageFile()).into(photoImageView);
        nameSurnameTextView.setText(housePost.getName() + " "+housePost.getSurname());
        postDescriptionTextView.setText(housePost.getPostText());
    }
}

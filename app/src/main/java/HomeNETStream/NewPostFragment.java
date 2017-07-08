package HomeNETStream;


import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.io.File;
import java.util.ArrayList;

import Models.House;
import Tasks.CreatePostTask;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment implements View.OnClickListener {

    private static final int PHOTO_RESULT = 300;
    private ArrayList<House> userHouseList;
    private MaterialSpinner housesSpinner;
    private EditText postDescriptionEditText, postImageEditText;
    private ImageView postImageView;
    private Button selectImageButton;
    private FloatingActionButton submitPostButton;
    private CheckBox postToFacebook, postToTwitter, locationData;
    private Uri imageUri;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_new_post, container, false);
        initializeComponents(currentView);
        if (savedInstanceState != null) {
            userHouseList = savedInstanceState.getParcelableArrayList("Houses");
        } else {
            getData();
        }
        setUpMaterialSpinner();
        return currentView;

    }

    private void initializeComponents(View currentView){
        housesSpinner = (MaterialSpinner) currentView.findViewById(R.id.NewPostSelectHouseSpinner);
        postDescriptionEditText = (EditText) currentView.findViewById(R.id.NewPostDescriptionTextView);
        postImageView = (ImageView) currentView.findViewById(R.id.NewPostPhotoImageView);
        selectImageButton = (Button) currentView.findViewById(R.id.NewPostSelectPhotoButton);
        selectImageButton.setOnClickListener(this);
        postImageEditText = (EditText) currentView.findViewById(R.id.NewPostPhotoLocationEditText);
        submitPostButton = (FloatingActionButton) currentView.findViewById(R.id.NewPostCreatePostButton);
        submitPostButton.setOnClickListener(this);
        postToFacebook = (CheckBox) currentView.findViewById(R.id.NewPostToFacebookCheckbox);
        postToTwitter = (CheckBox) currentView.findViewById(R.id.NewPostToTwitterCheckbox);
        locationData = (CheckBox) currentView.findViewById(R.id.NewPostAddLocationCheckbox);

    }

    private void setUpMaterialSpinner() {
        if (userHouseList != null) {
            housesSpinner.setItems(userHouseList);
        } else {
            displayMessage("No Houses Found", "No linked houses were found for your profile", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getFragmentManager().popBackStack();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Houses", userHouseList);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }

    private void getData() {
        userHouseList = getArguments().getParcelableArrayList("Houses");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewPostSelectPhotoButton:
                Intent newPhotoIntent = new Intent();
                newPhotoIntent.setType("image/*");
                newPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(newPhotoIntent, "Select Picture"), PHOTO_RESULT);
                break;
            case R.id.NewPostCreatePostButton:
                if (postDescriptionEditText.getText().toString() == "") {
                    displayToast("Please post some description", view);
                    return;
                }
                try {
                    House selectedHouse = (House) housesSpinner.getItems().get(housesSpinner.getSelectedIndex());
                    String fileNameString = getFileName(getActivity().getContentResolver(), imageUri);
                    File compressedFile = new Compressor(getActivity()).compressToFile(new File(fileNameString));
                    RequestBody imageBodyPart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), compressedFile);
                    MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("file", compressedFile.getName(), imageBodyPart);
                    String location = "";
                    CreatePostTask task = new CreatePostTask(getActivity(), selectedHouse, postDescriptionEditText.getText().toString(), location, finalImageFile);
                    task.execute();
                } catch (Exception error) {
                    displayMessage("Error Creating Post", error.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().getFragmentManager().popBackStack();
                        }
                    });
                }
                break;
        }
    }

    private String getFileName(ContentResolver resolver, Uri uri) {
        Cursor cursor = null;
        try {
            String [] proj = {MediaStore.Images.Media.DATA};
            cursor = resolver.query(uri, proj, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(index);
        } catch (Exception error) {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_RESULT && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Glide.with(getActivity()).load(imageUri).into(postImageView);
            } catch (Exception error) {

            }
        }
    }

    private void displayToast(String message, View currentView) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }


}

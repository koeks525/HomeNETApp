package HomeNETStream;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePickerActivity;

import java.io.File;
import java.util.ArrayList;
import Models.House;
import Tasks.CreatePostTask;
import Tasks.NewPostGetSubscribedHousesTask;
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
    private TextView toolbarTextView;
    private Uri imageUri;
    private ArrayList<Image> imageList;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_new_post, container, false);
        initializeComponents(currentView);
        getSubscribedHouses();
        imageList = new ArrayList<>();
        return currentView;

    }

    private void getSubscribedHouses() {
        NewPostGetSubscribedHousesTask task = new NewPostGetSubscribedHousesTask(housesSpinner, getActivity()); //error
        try {task.execute().wait(); } catch (Exception error) {}
    }



    private void initializeComponents(View currentView){
        userHouseList = new ArrayList<>();
        housesSpinner = (MaterialSpinner) currentView.findViewById(R.id.NewPostSelectHouseSpinner);
        postDescriptionEditText = (EditText) currentView.findViewById(R.id.NewPostDescriptionTextView);
        postImageView = (ImageView) currentView.findViewById(R.id.NewPostPhotoImageView);
        selectImageButton = (Button) currentView.findViewById(R.id.NewPostSelectPhotoButton);
        selectImageButton.setOnClickListener(this);
        postImageEditText = (EditText) currentView.findViewById(R.id.NewPostPhotoLocationEditText);
        postImageEditText.setEnabled(false);
        submitPostButton = (FloatingActionButton) currentView.findViewById(R.id.NewPostCreatePostButton);
        submitPostButton.setOnClickListener(this);
        postToFacebook = (CheckBox) currentView.findViewById(R.id.NewPostToFacebookCheckbox);
        postToTwitter = (CheckBox) currentView.findViewById(R.id.NewPostToTwitterCheckbox);
        locationData = (CheckBox) currentView.findViewById(R.id.NewPostAddLocationCheckbox);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Post an Update");

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewPostSelectPhotoButton:
                ImagePicker.Builder builder = ImagePicker.with(getActivity())                         //  Initialize ImagePicker with activity or fragment context
                        .setToolbarColor("#212121")         //  Toolbar color
                        .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                        .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                        .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                        .setProgressBarColor("#4CAF50")     //  ProgressBar color
                        .setBackgroundColor("#212121")      //  Background color
                        .setCameraOnly(false)               //  Camera mode
                        .setMultipleMode(false)              //  Select multiple images or single image
                        .setFolderMode(true)                //  Folder mode
                        .setShowCamera(true)                //  Show camera button
                        .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                        .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                        .setDoneTitle("Done")               //  Done button title
                        .setMaxSize(10)                     //  Max images can be selected
                        .setSavePath("HomeNET")         //  Image capture folder name
                        .setSelectedImages(imageList);
                builder.start();
                /*Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivityForResult(intent, Config.RC_PICK_IMAGES);*/

                /*Intent newPhotoIntent = new Intent();
                newPhotoIntent.setType("image*//*");
                newPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(newPhotoIntent, "Select Picture"), PHOTO_RESULT);*/
                break;
            case R.id.NewPostCreatePostButton:
                if (postDescriptionEditText.getText().toString() == "") {
                    displayToast("Please post some description", view);
                    return;
                }
                try {

                    House selectedHouse = (House) housesSpinner.getItems().get(housesSpinner.getSelectedIndex());
                    if (imageList != null) {
                        String fileNameString = imageList.get(0).getPath();
                        File compressedFile = new Compressor(getActivity()).compressToFile(new File(fileNameString));
                        RequestBody imageBodyPart = RequestBody.create(MediaType.parse("image/*"), compressedFile);
                        MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("file", compressedFile.getName(), imageBodyPart);
                        String location = "";
                        CreatePostTask task = new CreatePostTask(getActivity(), selectedHouse, postDescriptionEditText.getText().toString(), location, finalImageFile);
                        task.execute();
                    } else {
                        CreatePostTask task = new CreatePostTask(getActivity(), selectedHouse, postDescriptionEditText.getText().toString(),"", null);
                        task.execute();
                    }
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
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imageList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            postImageEditText.setText(imageList.get(0).getPath());
            postImageEditText.setEnabled(false);
            File selectedFile = new File(imageList.get(0).getPath());
            Glide.with(getActivity()).load(selectedFile).into(postImageView);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void displayToast(String message, View currentView) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }


}

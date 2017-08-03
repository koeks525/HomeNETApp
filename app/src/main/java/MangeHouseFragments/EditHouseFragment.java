package MangeHouseFragments;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.koeksworld.homenet.R;

import java.io.File;

import Models.House;
import Tasks.EditHouseTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditHouseFragment extends Fragment implements View.OnClickListener {

    private MapView editHouseMapView;
    private EditText nameEditText, descriptionEditText, oneTimePinEditText, houseIDEditText, photoEditText;
    private CheckBox isPrivateCheckBox;
    private EditHouseTask editHouseTask;
    private House selectedHouse;
    private FloatingActionButton saveChangesButton;
    private Button selectImageButton;
    private Uri imageUri;
    private String fileName;

    public EditHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_edit_house, container, false);
        initializeComponents(currentView, savedInstanceState);
        getHouseData();
        runTask();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        editHouseMapView = (MapView) currentView.findViewById(R.id.EditHouseLocationMapView);
        editHouseMapView.onCreate(savedInstanceState);
        nameEditText = (EditText) currentView.findViewById(R.id.EditHouseNameEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.EditHouseDescriptionEditText);
        oneTimePinEditText = (EditText) currentView.findViewById(R.id.EditHouseOneTimePinEditText);
        isPrivateCheckBox = (CheckBox) currentView.findViewById(R.id.EditHouseHousePrivacyCheckbox);
        saveChangesButton = (FloatingActionButton) currentView.findViewById(R.id.EditHouseSaveChangesButton);
        selectImageButton = (Button) currentView.findViewById(R.id.EditHouseSelectImageButton);
        photoEditText = (EditText) currentView.findViewById(R.id.EditHouseProfileImageView);
        photoEditText.setText("(No Image Selected)");
        selectImageButton.setOnClickListener(this);
        saveChangesButton.setOnClickListener(this);
    }

    private void getHouseData() {
        selectedHouse = (House) getArguments().getSerializable("House");
    }

    private void runTask() {
        editHouseTask = new EditHouseTask(getActivity(), selectedHouse.getHouseID(), nameEditText, descriptionEditText, oneTimePinEditText, houseIDEditText, editHouseMapView, isPrivateCheckBox);
        editHouseTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        editHouseMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        editHouseMapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        editHouseMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editHouseMapView.onDestroy(); //There is a problem here
    }

    @Override
    public void onPause() {
        super.onPause();
        editHouseMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        editHouseMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        editHouseMapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditHouseSaveChangesButton:

                try {
                    if (isPrivateCheckBox.isChecked()) {
                        if (oneTimePinEditText.getText().length() == 0 || oneTimePinEditText.getText().toString() == "") {
                            displayMessage("One Time PIN Required", "To make your house private, you must supply a one time pin", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            return;
                        }
                       if (oneTimePinEditText.getText().length() < 5) {
                           displayMessage("OTP Error", "Your one time PIN needs to be at least 5 characters long", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                               }
                           });
                           return;
                       }
                    }


                } catch (Exception error) {
                    displayMessage("Error", error.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }



                break;
            case R.id.EditHouseSelectImageButton:
                selectImage();
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

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener);
        messageBox.show();
    }

    private void selectImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                imageUri = data.getData();
                photoEditText.setText(imageUri.toString());
                fileName = getFileName(getActivity().getContentResolver(), imageUri);
            }
        } catch (Exception error) {

        }
    }



}

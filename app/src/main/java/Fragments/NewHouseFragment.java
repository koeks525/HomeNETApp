package Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koeksworld.homenet.R;

import java.io.File;

import Communication.HomeNetService;
import Models.House;
import Models.LoginViewModel;
import Models.Token;
import ResponseModels.SingleResponse;
import Tasks.CreateHouseTask;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Okuhle on 2017/08/17.
 */

public class NewHouseFragment extends Fragment implements View.OnClickListener {

    private TextView toolbarTextView;
    private EditText nameEditText, descriptionEditText, imageLinkEditText;
    private Button submitButton, selectPhotoButton;
    private ImageView houseImageView;
    private OkHttpClient client;
    private Retrofit retrofit;
    private HomeNetService service;
    private Uri imageUri;
    private String fileName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_create_house, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Create House");
        nameEditText = (EditText) currentView.findViewById(R.id.CreateHouseHouseNameEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.CreateHouseDescriptionEditText);
        imageLinkEditText = (EditText) currentView.findViewById(R.id.CreateHouseImageLinkEditText);
        submitButton = (Button) currentView.findViewById(R.id.CreateHouseCreateHouseButton);
        selectPhotoButton = (Button) currentView.findViewById(R.id.CreateHouseSelectImageButton);
        houseImageView = (ImageView) currentView.findViewById(R.id.CreateHouseImageImageView);
        submitButton.setOnClickListener(this);
        selectPhotoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CreateHouseSelectImageButton:
                selectImage();
                break;
            case R.id.CreateHouseCreateHouseButton:
                if (nameEditText.getText().toString() == "") {
                    displaySnackbar("Please enter a house name");
                    return;
                }
                if (descriptionEditText.getText().toString() == "") {
                    displaySnackbar("Please provide a description");
                    return;
                }
                if (imageLinkEditText.getText().toString() == "") {
                    displaySnackbar("Please select an image");
                    return;
                }

                break;
        }
    }

    private void selectImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, 100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                imageUri = data.getData();
                imageLinkEditText.setText(imageUri.toString());
                fileName = getFileName(getActivity().getContentResolver(), imageUri);
                Glide.with(getActivity()).load(fileName).into(houseImageView);
            }
        } catch (Exception error) {

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
        AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
        box.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public void processHouseCreation(final House newHouse) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Creating House. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        if (sharedPreferences.getString("authorization_token","") != "") {
            try {
                File imageFile = new File(fileName);
                File compressedFile = new Compressor(getActivity()).compressToFile(imageFile);
                RequestBody houseNamePart = RequestBody.create(MultipartBody.FORM, newHouse.getName());
                RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, newHouse.getDescription());
                RequestBody emailAddressPart = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
                RequestBody imageBodyPart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), compressedFile);
                MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("imageFile", compressedFile.getName(), imageBodyPart);
                CreateHouseTask task = new CreateHouseTask(getActivity(), finalImageFile, houseNamePart, descriptionPart, emailAddressPart, getResources().getString(R.string.homenet_client_string));
                task.execute();
            } catch (Exception error) {
                displayMessage("Error Creating House", "An unexpected error occurred while creating the house.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        } else {
            createToken(newHouse);
        }
    }
    private void createToken(final House newHouse) {
        final ProgressDialog tokenBar = new ProgressDialog(getActivity());
        tokenBar.setMessage("Authorizing. Please wait...");
        tokenBar.setCancelable(false);
        tokenBar.show();
        if (sharedPreferences.getString("username", "") != "") {
            LoginViewModel model = new LoginViewModel(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
            Call<SingleResponse<Token>> tokenCall = service.createToken(model, getResources().getString(R.string.homenet_client_string));
            tokenCall.enqueue(new Callback<SingleResponse<Token>>() {
                @Override
                public void onResponse(Call<SingleResponse<Token>> call, Response<SingleResponse<Token>> response) {
                    if(tokenBar.isShowing()) {
                        tokenBar.dismiss();
                    }
                    SingleResponse<Token> token = response.body();
                    if (response.code() == 200 && token != null) {
                        Token newToken = token.getModel();
                        editor.putString("authorization_token", newToken.getTokenHandler());
                        editor.putString("dateExpires", newToken.getDateExpires());
                        editor.commit();
                        processHouseCreation(newHouse);
                    } else {
                        displayMessage("Error Creating Token", "An error occurred while generating the token", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<SingleResponse<Token>> call, Throwable t) {
                    if (tokenBar.isShowing()) {
                        tokenBar.dismiss();
                    }

                }
            });

        }
    }
}

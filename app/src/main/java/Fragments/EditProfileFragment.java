package Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.Country;
import Models.User;
import ResponseModels.SingleResponse;
import Tasks.GetProfilePictureTask;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private EditText nameEditText, surnameEditText, emailEditText, userNameEditText, passwordEditText, dateOfBirthEditText;
    private MaterialSpinner countrySpinner;
    private Button saveChangesButton;
    private FloatingActionButton changePictureButton;
    private List<Protocol> protocolList;
    private Uri imageUri;
    private String fileName;
    private ImageView editProfileImageView;
    private SharedPreferences sharedPreferences;
    private User currentUser;
    private RealmHelper helper;
    private Toolbar appToolbar;
    private Toolbar profileToolbar;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        fetchUserData();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        helper = new RealmHelper();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameEditText = (EditText) currentView.findViewById(R.id.EditProfileNameEditText);
        surnameEditText = (EditText) currentView.findViewById(R.id.EditProfileSurnameEditText);
        dateOfBirthEditText = (EditText) currentView.findViewById(R.id.EditProfileDateOfBirthEditText);
        emailEditText = (EditText) currentView.findViewById(R.id.EditProfileEmailEditText);
        userNameEditText = (EditText) currentView.findViewById(R.id.EditProfileUsernameEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.EditProfilePasswordEditText);
        countrySpinner = (MaterialSpinner) currentView.findViewById(R.id.EditProfileCountrySpinner);
        editProfileImageView = (ImageView) currentView.findViewById(R.id.EditProfileImageView);
        saveChangesButton = (Button) currentView.findViewById(R.id.EditProfileSaveChangesButton);
        changePictureButton = (FloatingActionButton) currentView.findViewById(R.id.EditProfilePictureFloatingActionButton);
        saveChangesButton.setOnClickListener(this);
        changePictureButton.setOnClickListener(this);
        profileToolbar = (Toolbar) currentView.findViewById(R.id.EditProfileToolbar);


    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
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
                fileName = getFileName(getActivity().getContentResolver(), imageUri);
                Glide.with(getActivity()).load(fileName).into(editProfileImageView);
                updateProfilePicture();
            }
        } catch (Exception error) {

        }
    }

    private void updateProfilePicture() {
        try {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Updating profile picture. Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            File imageFile = new File(fileName);
            File compressedFile = new Compressor(getActivity()).compressToFile(imageFile);
            RequestBody imageBodyPart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), compressedFile);
            MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("imageFile", compressedFile.getName(), imageBodyPart);
            Call<SingleResponse<User>> updatePic = service.updateProfilePicture("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress",""), finalImageFile, getResources().getString(R.string.homenet_client_string));
            updatePic.enqueue(new Callback<SingleResponse<User>>() {
                @Override
                public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if (response.isSuccessful()) {
                        displayMessage("Update Successful", "Profile Picture Updated Successfully!", null);
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                    displayMessage("Critical Error", t.getMessage(), null);
                }
            });



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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditProfilePictureFloatingActionButton:
                selectImage();
                break;
            case R.id.EditProfileSaveChangesButton:


                break;
        }
    }

    private void fetchUserData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching user data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<User>> userCall = service.getUser("Bearer "+sharedPreferences.getString("authorization_token",""), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
        userCall.enqueue(new Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                if (response.isSuccessful()) {
                    currentUser = response.body().getModel();
                    if (currentUser != null) {
                        nameEditText.setText(currentUser.getName().toUpperCase());
                        surnameEditText.setText(currentUser.getSurname().toUpperCase());
                        emailEditText.setText(currentUser.getEmail().toUpperCase());
                        userNameEditText.setText(currentUser.getUserName().toUpperCase());
                        passwordEditText.setText(currentUser.getPassword());
                        dateOfBirthEditText.setText(currentUser.getDateOfBirth());
                        Country selected = helper.getCountryById(currentUser.getCountryID());
                        countrySpinner.setItems(helper.getCountries());
                        countrySpinner.setSelectedIndex(selected.getCountryID());
                        profileToolbar.setTitle(currentUser.getName() + " "+currentUser.getSurname());
                        profileToolbar.setTitleTextColor(Color.WHITE);
                        if (currentUser.getProfileImage() != "" && currentUser.getProfileImage() != null) {
                            GetProfilePictureTask task = new GetProfilePictureTask(getActivity(), editProfileImageView, dialog);
                            task.execute();
                        }
                    } else {
                        if (dialog.isShowing()) {
                            dialog.cancel();
                        }
                        displayMessage("Error", "No user data was found. Please try again later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().getFragmentManager().popBackStack();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                displayMessage("Critical Error", t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getFragmentManager().popBackStack();
                    }
                });
            }
        });
    }
}

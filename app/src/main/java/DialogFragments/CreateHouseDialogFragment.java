package DialogFragments;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koeksworld.homenet.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.House;
import Models.LoginViewModel;
import Models.Token;
import ResponseModels.SingleResponse;
import Tasks.CreateHouseTask;
import Utilities.UploadCallbacks;
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
 * Created by Okuhle on 2017/04/16.
 */

public class CreateHouseDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnShowListener {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
    private String fileName;
    private Retrofit retrofit;
    private HomeNetService service;
    private EditText houseNameEditText;
    private EditText houseDescriptionEditText;
    private EditText imageUrlEditText;
    private Button selectImageButton;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LocationRequest locationRequest;
    private View dialogView;
    private FloatingActionButton clearMarkerButton;
    private ImageView houseImageView;
    private Button createHouseButton;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.fragment_create_house, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Create House");
        builder.setCancelable(false);
        builder.setNegativeButton("Close", null);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //initializeGoogleApiClient();
        initializeComponents(dialogView, savedInstanceState);
        //getLocationSettings();
        Dialog resultDialog = builder.create();
        resultDialog.setOnShowListener(this);
        return resultDialog;
    }


    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(200, TimeUnit.SECONDS).readTimeout(200, TimeUnit.SECONDS).protocols(Arrays.asList(Protocol.HTTP_1_1)).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        createHouseButton = (Button) currentView.findViewById(R.id.CreateHouseCreateHouseButton);
        createHouseButton.setVisibility(View.GONE);
        createHouseButton.setEnabled(false);
        houseNameEditText = (EditText) currentView.findViewById(R.id.CreateHouseHouseNameEditText);
        houseDescriptionEditText = (EditText) currentView.findViewById(R.id.CreateHouseDescriptionEditText);
        imageUrlEditText = (EditText) currentView.findViewById(R.id.CreateHouseImageLinkEditText);
        imageUrlEditText.setEnabled(false);
        selectImageButton = (Button) currentView.findViewById(R.id.CreateHouseSelectImageButton);
        selectImageButton.setOnClickListener(this);
        clearMarkerButton.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CreateHouseSelectImageButton:
                selectImage();
                break;

        }
    }
    //Source: http://androidbitmaps.blogspot.co.za/2015/04/loading-images-in-android-part-iii-pick.html

    private void selectImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, 100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                imageUri = data.getData();
                imageUrlEditText.setText(imageUri.toString());
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

    public void processHouseCreation(final House newHouse, View currentView) {
        //dialog.setVisibility(View.VISIBLE);
        final AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle("Message");
        messageBox.setCancelable(false);
        if (sharedPreferences.getString("authorization_token","") != "") {
            try {
                File imageFile = new File(fileName);
                File compressedFile = new Compressor(getActivity()).compressToFile(imageFile);
                RequestBody houseNamePart = RequestBody.create(MultipartBody.FORM, newHouse.getName());
                RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, newHouse.getDescription());
                RequestBody emailAddressPart = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
                //ProgressRequestBody modifiedImagePart = new ProgressRequestBody(imageFile, getActivity().getContentResolver().getType(imageUri), this);
                RequestBody imageBodyPart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), compressedFile);
                MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("imageFile", compressedFile.getName(), imageBodyPart);
                CreateHouseTask task = new CreateHouseTask(getActivity(), finalImageFile, houseNamePart, descriptionPart, emailAddressPart, getResources().getString(R.string.homenet_client_string));
                task.execute();
            } catch (Exception error) {
                //Will crash
            }
        } else {
            createToken(newHouse);
        }

    }
    private void createToken(final House newHouse) {
        final ProgressDialog tokenBar = new ProgressDialog(getActivity());
        tokenBar.setMessage("Authorizing. Please wait...");
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
                        processHouseCreation(newHouse, dialogView);

                    } else {

                        displayMessage("Error Creating Token", "An error occurred while generating the token");
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



    private void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", null).show();
    }

    private Bitmap getImageBitmap(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        Button submit = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri == null) {
                    displayMessage("Image Required", "Please select an image for your house");
                    return;
                }

                if (houseNameEditText.getText().toString().length() <= 2) {
                    displayMessage("House Name Required", "Please enter a house name");
                    return;
                }

                if (houseDescriptionEditText.getText().toString().length() <= 2) {
                    displayMessage("House Description Required", "Please enter a house description");
                    return;
                }


                House newHouse = new House();
                newHouse.setHouseID(sharedPreferences.getInt("userID", 0));
                newHouse.setName(houseNameEditText.getText().toString());
                newHouse.setDescription(houseDescriptionEditText.getText().toString());
                newHouse.setDateCreated(dateFormat.format(new Date()));
                processHouseCreation(newHouse, dialogView);

            }
        });
    }
}

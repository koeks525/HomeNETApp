package Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.koeksworld.homenet.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.User;
import ResponseModels.SingleResponse;
import Tasks.GetUserProfileTask;
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
public class HomeNetProfileFragment extends Fragment implements View.OnClickListener {

    private ImageView profileImageView;
    private FloatingActionButton pickPhotoButton;
    private TextView nameTextView, surnamTextView, emailTextView, countryTextView, dateOfBirthTextView, dateRegisteredTextView, totalHouses;
    private BarChart reportChart;
    private GetUserProfileTask task;
    private Toolbar appToolbar;
    private OkHttpClient client;
    private HomeNetService service;
    private Retrofit retrofit;
    private List<Protocol> protocolList;
    private Uri imageUri;
    private String fileName;
    private SharedPreferences sharedPreferences;

    public HomeNetProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_home_net_profile, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        task = new GetUserProfileTask(nameTextView, surnamTextView, emailTextView, countryTextView, dateOfBirthTextView, dateRegisteredTextView, totalHouses, profileImageView, getActivity(), reportChart, appToolbar);
        task.execute();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        profileImageView = (ImageView) currentView.findViewById(R.id.ProfileImageView);
        pickPhotoButton = (FloatingActionButton) currentView.findViewById(R.id.ProfileEditFloatingActionButton);
        pickPhotoButton.setOnClickListener(this);
        nameTextView = (TextView) currentView.findViewById(R.id.NameDataField);
        surnamTextView = (TextView) currentView.findViewById(R.id.SurnameDataField);
        emailTextView = (TextView) currentView.findViewById(R.id.EmailAddressDataField);
        countryTextView = (TextView) currentView.findViewById(R.id.CountryDataField);
        dateOfBirthTextView = (TextView)currentView.findViewById(R.id.DateOfBirthDataField);
        dateRegisteredTextView = (TextView) currentView.findViewById(R.id.DateRegisteredDataField);
        reportChart = (BarChart) currentView.findViewById(R.id.UserOverviewChart);
        totalHouses = (TextView)currentView.findViewById(R.id.TotalHousesDataField);
        appToolbar = (Toolbar) currentView.findViewById(R.id.ProfileToolbar);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ProfileEditFloatingActionButton:
                selectImage();
                break;
        }
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES).connectTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
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
                Glide.with(getActivity()).load(fileName).into(profileImageView);
                updateProfilePicture();
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

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
        box.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }
}

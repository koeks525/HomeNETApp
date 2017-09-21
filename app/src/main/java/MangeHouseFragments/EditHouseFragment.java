package MangeHouseFragments;


import android.app.Activity;
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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.koeksworld.homenet.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.HouseViewModel;
import ResponseModels.SingleResponse;
import Tasks.EditHouseTask;
import Tasks.GetHouseProfileTask;
import Tasks.UpdateHouseTask;
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
public class EditHouseFragment extends Fragment implements View.OnClickListener {

    private EditText nameEditText, descriptionEditText, dateCreatedEditText, totalMembersEditText, photoEditText;
    private EditHouseTask editHouseTask;
    private GetHouseProfileTask task;
    private House selectedHouse;
    private FloatingActionButton saveChangesButton;
    private Button selectImageButton;
    private Uri imageUri;
    private String fileName;
    private ImageView houseImageView;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private OkHttpClient client;

    public EditHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_edit_house, container, false);
        getHouseData();
        initializeRetrofit();
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        nameEditText = (EditText) currentView.findViewById(R.id.EditHouseNameEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.EditHouseDescriptionEditText);
        dateCreatedEditText = (EditText) currentView.findViewById(R.id.EditHouseHouseDateCreatedEditText);
        dateCreatedEditText.setEnabled(false);
        totalMembersEditText = (EditText) currentView.findViewById(R.id.EditHouseHouseTotalMembersEditText);
        totalMembersEditText.setEnabled(false);
        saveChangesButton = (FloatingActionButton) currentView.findViewById(R.id.EditHouseSaveChangesButton);
        selectImageButton = (Button) currentView.findViewById(R.id.EditHouseSelectImageButton);
        photoEditText = (EditText) currentView.findViewById(R.id.EditHouseProfileImageView);
        photoEditText.setText("(No Image Selected)");
        selectImageButton.setOnClickListener(this);
        saveChangesButton.setOnClickListener(this);
        getHouseProfile();
    }

    private void initializeRetrofit() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void getHouseData() {
        selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
    }

    private void getHouseProfile() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching updated house information. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<House>> houseData = service.getHouse("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        houseData.enqueue(new Callback<SingleResponse<House>>() {
            @Override
            public void onResponse(Call<SingleResponse<House>> call, Response<SingleResponse<House>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    selectedHouse = response.body().getModel();
                    if (selectedHouse != null) {
                        nameEditText.setText(selectedHouse.getName());
                        descriptionEditText.setText(selectedHouse.getDescription());
                        dateCreatedEditText.setText(selectedHouse.getDateCreated());
                        totalMembersEditText.setText("0");
                        photoEditText.setText(selectedHouse.getHouseImage());
                        if (selectedHouse.getHouseImage() != "") {
                            GetHouseProfileTask task = new GetHouseProfileTask(nameEditText, descriptionEditText, totalMembersEditText, dateCreatedEditText, houseImageView, getActivity(), selectedHouse);
                            task.execute();
                        }
                    }

                } else {
                    displayMessage("Error Getting House Data", "Something went wrong with getting house data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().getFragmentManager().popBackStack();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<House>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getFragmentManager().popBackStack();
                    }
                });
            }
        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditHouseSaveChangesButton:
                if (nameEditText.getText().toString() == "") {
                    displayMessage("Empty House Name", "Please fill in a valid house name", null);
                    return;
                }
                if (descriptionEditText.getText().toString() == "") {
                    displayMessage("Empty House Description", "Please fill in a valid house description", null);
                    return;
                }
                House updatedHouse = selectedHouse;
                updatedHouse.setName(nameEditText.getText().toString());
                updatedHouse.setDescription(descriptionEditText.getText().toString());
                if (imageUri == null) {
                    UpdateHouseTask updateTask = new UpdateHouseTask(getActivity(), updatedHouse, null);
                    updateTask.execute();
                } else {
                    try {
                        File imageFile = new File(fileName);
                        File compressedFile = new Compressor(getActivity()).compressToFile(imageFile);
                        RequestBody imageBodyPart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), compressedFile);
                        MultipartBody.Part finalImageFile = MultipartBody.Part.createFormData("imageFile", compressedFile.getName(), imageBodyPart);
                        UpdateHouseTask updateHouseTask = new UpdateHouseTask(getActivity(), updatedHouse, finalImageFile);
                        updateHouseTask.execute();
                    } catch (Exception error) {

                    }
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
                Glide.with(getActivity()).load(imageUri).into(houseImageView);
            }
        } catch (Exception error) {

        }
    }





}

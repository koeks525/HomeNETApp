package DialogFragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.maps.MapView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import Communication.HomeNetService;
import Data.DatabaseHelper;
import Models.Category;
import ResponseModels.ListResponse;
import Utilities.DeviceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/04/17.
 */

public class CreateOrganizationDialogFragment extends DialogFragment implements View.OnClickListener {

    private Retrofit retrofit;
    private HomeNetService service;
    private EditText organizationNameEditText;
    private EditText organizationDescriptionEditText;
    private EditText organizationLinkEditText;
    private Button photoButton, createOrganizationButton;
    private MapView locationMapView;
    private DeviceUtils deviceUtils;
    private MaterialSpinner categorySpinner;
    private DatabaseHelper dbHelper;
    private List<Category> categoryList;
    private View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.create_organization_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Create Organization");
        builder.setCancelable(false);
        initializeComponents(dialogView, savedInstanceState);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        deviceUtils = new DeviceUtils(getActivity());
        if (deviceUtils.checkNetworkConnection()) {
            initializeRetrofit();

        } else {
            if (dbHelper.getCategories().size() > 0) {
                List<String> categoryString = new ArrayList<>();
                for (Category category : dbHelper.getCategories()) {
                    categoryString.add(category.getName());
                }
                categorySpinner.setItems(categoryString);
            } else {
                //No category data on database, and no internet connection, keep an offline store on phone
                //Am i supposed to pull this from shared prefs? If I am using shared prefs, what is the point of file based SQL database?
            }
        }


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    public void initializeComponents(View currentView, Bundle savedInstanceState) {

        categoryList = new ArrayList<>();
        organizationNameEditText = (EditText) currentView.findViewById(R.id.CreateOrganizationNameEditText);
        organizationDescriptionEditText = (EditText) currentView.findViewById(R.id.CreateOrganizationDescriptionEditText);
        organizationLinkEditText = (EditText) currentView.findViewById(R.id.CreateOrganizationPhotoEditText);
        photoButton = (Button) currentView.findViewById(R.id.CreateOrganizationPhotoButton);
        categorySpinner = (MaterialSpinner) currentView.findViewById(R.id.CreateOrganizationCategorySpinner);
        locationMapView = (MapView) currentView.findViewById(R.id.CreateOrganizationMapView);
        locationMapView.onCreate(savedInstanceState);
        photoButton.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        locationMapView.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationMapView.onStart();
    }

    public void initializeRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    public void getCategories(View currentView) {
        final Snackbar bar = Snackbar.make(currentView, "Getting Categories. Please wait", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) bar.getView();
        layout.addView(new ProgressBar(getActivity()));
        bar.show();
        Call<ListResponse<Category>> categoryCall = service.getCategories(getResources().getString(R.string.homenet_client_string));
        categoryCall.enqueue(new Callback<ListResponse<Category>>() {
            @Override
            public void onResponse(Call<ListResponse<Category>> call, Response<ListResponse<Category>> response) {
                bar.dismiss();
                ListResponse<Category> categoryListResponse = new ListResponse<Category>();
                if (response.code() == 200) {
                    categoryListResponse = response.body();
                    categoryList.addAll(categoryListResponse.getModel());
                    dbHelper.insertCategories(categoryList);
                    List<String> categoryStringList = new ArrayList<String>();
                    for (Category category : categoryList) {
                        categoryStringList.add(category.getName());
                    }
                    categorySpinner.setItems(categoryStringList);
                } else {
                    displayMessage("Error Getting Categories", "Error getting categories from server");
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Category>> call, Throwable t) {
                bar.dismiss();
                displayMessage("Error Getting Categories", "A critical error occurred\n"+t.getMessage());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        locationMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMapView.onDestroy();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCategories(view);
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got It", null).show();
    }

    @Override
    public void onClick(View view) {

    }
}

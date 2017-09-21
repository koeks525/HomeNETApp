package HomeNETStream;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Adapters.PhotoGalleryAdapter;
import Communication.HomeNetService;
import Models.HousePostViewModel;
import Models.Picture;
import ResponseModels.ListResponse;
import Tasks.GetMultimediaPostsTask;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView toolbarTextView;

    private Retrofit retrofit;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private HomeNetService service;
    private ProgressDialog dialog;
    private List<HousePostViewModel> postList;
    private SharedPreferences sharedPreferences;
    private List<Picture> pictureList = new ArrayList<>();


    public PhotoGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        initiateTask();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.PhotoGalleryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Your Photo Gallery");
    }

    private void initializeRetrofit()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        protocolList = new ArrayList<>();
        postList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void initiateTask() {
        GetMultimediaPostsTask task = new GetMultimediaPostsTask(getActivity(), recyclerView);
        task.execute();
    }

   /* private void getProfileImages() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching house images. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<HousePostViewModel>> postCall = service.getAllMultimediaPosts("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
        postCall.enqueue(new Callback<ListResponse<HousePostViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<HousePostViewModel>> call, Response<ListResponse<HousePostViewModel>> response) {
                if (response.isSuccessful()) {
                   if (response.body().getModel() != null) {
                       for (HousePostViewModel item : response.body().getModel()) {
                           postList.add(item);
                       }
                   }
                        if (postList.size() > 0) {
                            for (HousePostViewModel model : postList) {
                                final Call<ResponseBody> photoCall = service.getHousePostImage("Bearer " + sharedPreferences.getString("authorization_token",""), model.getHousePostID(), getResources().getString(R.string.homenet_client_string));
                                photoCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                InputStream inputStream = null;
                                                FileOutputStream outputStream = null;
                                                File profileFile = new File(getActivity().getExternalCacheDir() + File.separator + generateRandomString()+"tempImage3.jpg");
                                                inputStream = new BufferedInputStream(response.body().byteStream());
                                                outputStream = new FileOutputStream(profileFile);
                                                int c;
                                                Log.i("START", "Starting to read the image");
                                                long timeInMS = System.currentTimeMillis();
                                                while ((c = inputStream.read()) != -1) {
                                                    outputStream.write(c);
                                                }
                                                Picture picture = new Picture(profileFile);
                                                pictureList.add(picture);
                                                long finish = System.currentTimeMillis();
                                                long finalTime = finish - timeInMS;
                                                Log.i("END", "Finished Reading file in " +finalTime+" ms");
                                                inputStream.close();
                                                outputStream.close();
                                            } catch (Exception error) {

                                            }



                                        } else {
                                            dialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        dialog.cancel();
                                    }
                                });

                            }
                            if (pictureList.size() > 0 && postList.size() > 0) {
                                PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(getActivity(), pictureList, postList);
                                recyclerView.setAdapter(adapter);
                                dialog.cancel();
                            } else {
                                dialog.cancel();
                                Snackbar.make(getView(), "No posts found", Snackbar.LENGTH_SHORT).show();
                            }

                        } else {
                            dialog.cancel();
                            Snackbar.make(getView(), "No Photos found!", Snackbar.LENGTH_LONG).show();
                        }

                } else {
                    dialog.cancel();
                    Snackbar.make(getView(), "No Photos found!", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListResponse<HousePostViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Error", t.getMessage(), null);
            }
        });
    }*/

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Ok", listener).setCancelable(false).show();
    }

    private String generateRandomString() {
        Random random = new Random();
        String finalString = "";
        for (int a = 0; a < 7; a++) {
            finalString = finalString + random.nextInt(9) + 1 ;
        }
        return finalString;
    }



}

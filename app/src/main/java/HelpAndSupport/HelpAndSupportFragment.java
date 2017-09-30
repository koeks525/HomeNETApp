package HelpAndSupport;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.koeksworld.homenet.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpAndSupportFragment extends Fragment implements View.OnClickListener {

    private TextView toolbarTextView;
    private File pdfFile;
    private ProgressDialog dialog;
    private ExpandableLayout aboutHomeNet, whatItDoes, joinHouse, createHouse, usingHomeNet, createPost, postMetrics, addingComments, addingAnnouncements, photoGallery, manageHouse;
    private Button aboutHomeButton, whatItDoesButton, joinHouseButton, createHouseButton, usingHomeNetButton, createPostButton, postMetricsButton, addingCommentsButton, addingAnnouncementsButton, photoGalleryButton, manageHouseButton;

    public HelpAndSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_help_and_support, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        aboutHomeNet = currentView.findViewById(R.id.whatIsHomeNETExpandable);
        whatItDoes = currentView.findViewById(R.id.howHomeNETWorksExpandable);
        joinHouse = currentView.findViewById(R.id.howToJoinHouseExpandable);
        createHouse = currentView.findViewById(R.id.howToCreateHouseExpandable);
        usingHomeNet = currentView.findViewById(R.id.usingTheApplicationExpandable);
        postMetrics = currentView.findViewById(R.id.viewingPostMetricsExpandable);
        createPost = currentView.findViewById(R.id.creatingNewPostExpandable);
        addingComments = currentView.findViewById(R.id.viewingCommentsExpandable);
        addingAnnouncements = currentView.findViewById(R.id.creatingAnnouncementsExpandable);
        photoGallery = currentView.findViewById(R.id.homeNETPhotoGalleryExpandable);
        manageHouse = currentView.findViewById(R.id.myHouseExpandable);
        aboutHomeButton = currentView.findViewById(R.id.whatIsHomeNETButton);
        aboutHomeButton.setOnClickListener(this);
        whatItDoesButton = currentView.findViewById(R.id.howHomeNETWorksButton);
        whatItDoesButton.setOnClickListener(this);
        joinHouseButton = currentView.findViewById(R.id.howToJoinHouseButton);
        joinHouseButton.setOnClickListener(this);
        createHouseButton = currentView.findViewById(R.id.howToCreateHouseButton);
        createHouseButton.setOnClickListener(this);
        usingHomeNetButton = currentView.findViewById(R.id.usingTheApplicationButton);
        usingHomeNetButton.setOnClickListener(this);
        postMetricsButton = currentView.findViewById(R.id.viewingPostMetricsButton);
        postMetricsButton.setOnClickListener(this);
        createPostButton = currentView.findViewById(R.id.creatingNewPostButton);
        createPostButton.setOnClickListener(this);
        addingCommentsButton = currentView.findViewById(R.id.viewingCommentsButton);
        addingCommentsButton.setOnClickListener(this);
        addingAnnouncementsButton = currentView.findViewById(R.id.creatingAnnouncementsButton);
        addingAnnouncementsButton.setOnClickListener(this);
        photoGalleryButton = currentView.findViewById(R.id.homeNETPhotoGalleryButton);
        photoGalleryButton.setOnClickListener(this);
        manageHouseButton = currentView.findViewById(R.id.myHouseButton);
        manageHouseButton.setOnClickListener(this);
        /*aboutHomeNet.toggle();
        aboutHomeNet.expand();
        aboutHomeNet.setOnClickListener(this);
        whatItDoes.setOnClickListener(this);
        joinHouse.setOnClickListener(this);
        createHouse.setOnClickListener(this);
        usingHomeNet.setOnClickListener(this);
        postMetrics.setOnClickListener(this);
        addingComments.setOnClickListener(this);
        addingAnnouncements.setOnClickListener(this);
        photoGallery.setOnClickListener(this);
        manageHouse.setOnClickListener(this);*/
        toolbarTextView.setText("Help and Support");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.whatIsHomeNETButton:
                aboutHomeNet.toggle();
                if (aboutHomeNet.isExpanded()) {
                    aboutHomeNet.collapse();
                } else {
                    aboutHomeNet.expand();
                }
                //aboutHomeNet.toggle();
                break;
            case R.id.howHomeNETWorksButton:
                whatItDoes.toggle();
                if (whatItDoes.isExpanded()) {
                    whatItDoes.collapse();
                } else {
                    whatItDoes.expand();
                }
                //whatItDoes.toggle();
                break;
            case R.id.howToJoinHouseButton:
                joinHouse.toggle();
                if (joinHouse.isExpanded()) {
                    joinHouse.collapse();
                } else {
                    joinHouse.expand();
                }
                //joinHouse.toggle();
                break;
            case R.id.howToCreateHouseButton:
                //createHouse.toggle();
                if (createHouse.isExpanded()) {
                    createHouse.collapse();
                } else {
                    createHouse.expand();
                }
                break;
            case R.id.creatingNewPostButton:
                //createPost.toggle();
                if (createPost.isExpanded()) {
                    createPost.collapse();
                } else {
                    createPost.expand();
                }
                break;
            case R.id.creatingAnnouncementsButton:
                addingAnnouncements.toggle();
                if (addingAnnouncements.isExpanded()) {
                    addingAnnouncements.collapse();
                } else {
                    addingAnnouncements.expand();
                }
                break;
            case R.id.viewingCommentsButton:
                addingComments.toggle();
                if (addingComments.isExpanded()) {
                    addingComments.collapse();
                } else {
                    addingComments.expand();
                }
                break;
            case R.id.homeNETPhotoGalleryButton:
                photoGallery.toggle();
                if (photoGallery.isExpanded()) {
                    photoGallery.collapse();
                } else {
                    photoGallery.expand();
                }
                break;
            case R.id.myHouseButton:
                manageHouse.toggle();
                if (manageHouse.isExpanded()) {
                    manageHouse.collapse();
                } else {
                    manageHouse.expand();
                }
                break;
            case R.id.viewingPostMetricsButton:
                postMetrics.toggle();
                if (postMetrics.isExpanded()) {
                    postMetrics.collapse();
                } else {
                    postMetrics.expand();
                }
                break;
            case R.id.usingTheApplicationButton:
                usingHomeNet.toggle();
                if (usingHomeNet.isExpanded()) {
                    usingHomeNet.collapse();
                } else {
                    usingHomeNet.expand();
                }
                break;

        }
    }
}

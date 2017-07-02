package MangeHouseFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.koeksworld.homenet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditHouseFragment extends Fragment {


    private MapView editHouseMapView;

    public EditHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_edit_house, container, false);
        initializeComponents(currentView, savedInstanceState);
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        editHouseMapView = (MapView) currentView.findViewById(R.id.EditHouseLocationMapView);
        editHouseMapView.onCreate(savedInstanceState);
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
}

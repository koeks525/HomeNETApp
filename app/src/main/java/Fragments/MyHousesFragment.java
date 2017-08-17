package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koeksworld.homenet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHousesFragment extends Fragment {

    private TextView houseNameTextView, descriptionTextView, ownerTextView;
    
    public MyHousesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_houses, container, false);
    }

}

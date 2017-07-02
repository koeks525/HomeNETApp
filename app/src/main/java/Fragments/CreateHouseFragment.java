package Fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.koeksworld.homenet.R;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateHouseFragment extends Fragment {


    private TextInputLayout nameHint, descriptionHint;
    private EditText nameEditText, descriptionEditText;
    private TextView toolbarTextView;

    public CreateHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_create_house, container, false);
        initializeComponents(currentView);
        setupHelp();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityToolbarTextView); //This is subject to change depending on the activity loaded up.
        nameHint = (TextInputLayout) currentView.findViewById(R.id.HouseNameHint);
        descriptionHint = (TextInputLayout) currentView.findViewById(R.id.HouseDescriptionHint);
        nameEditText = (EditText) currentView.findViewById(R.id.CreateHouseHouseNameEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.CreateHouseDescriptionEditText);
    }

    private void setupHelp() {
        //Source: http://stackoverflow.com/questions/33996162/click-event-on-edittexts-drawableright-not-working-properly
        nameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getX() >= nameEditText.getWidth() - nameEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                        Tooltip.make(getActivity(), new Tooltip.Builder(101).anchor(getView(), Tooltip.Gravity.BOTTOM).closePolicy(new Tooltip.ClosePolicy().insidePolicy(true, false).outsidePolicy(true, false), 3000).activateDelay(1000).showDelay(400).text(getResources().getString(R.string.house_help_string)).maxWidth(600).withArrow(true).withOverlay(true).floatingAnimation(Tooltip.AnimationBuilder.DEFAULT).build()).show();
                    }
                }
                return true;
            }
        });
    }

}

package HomeNETStream;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.koeksworld.homenet.R;

import Models.MessageThread;
import Tasks.GetMessageThreadMessagesTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFeedFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText replyEditText;
    private FloatingActionButton replyButton;
    private MessageThread selectedThread;
    private GetMessageThreadMessagesTask task;

    public MessageFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_message_feed, container, false);
        initializeComponents(currentView);
        if (savedInstanceState == null) {
            getData();
        } else {
            selectedThread = savedInstanceState.getParcelable("SelectedThread");
        }

        task = new GetMessageThreadMessagesTask(getActivity(), recyclerView, selectedThread);
        task.execute();
        return currentView;

    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.MessagesFeedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyEditText = (EditText) currentView.findViewById(R.id.MessageFeedEditText);
        replyButton = (FloatingActionButton) currentView.findViewById(R.id.MessageFeedSendReplyButton);
        replyButton.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SelectedThread", selectedThread);
    }

    private void getData() {
        selectedThread = getArguments().getParcelable("SelectedThread");
    }

    @Override
    public void onClick(View view) {
        //Add a new message
    }
}

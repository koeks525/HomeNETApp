package DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import Models.HousePostMetaData;
import Models.HousePostViewModel;
import Tasks.GetHousePostDetailsTask;

/**
 * Created by Okuhle on 2017/09/02.
 */

public class HousePostDialogFragment extends DialogFragment{

    private View dialogView;
    private AlertDialog dialog;
    private TextView totalLikes, totalDislikes, likesNames, dislikesNames;
    private HousePostViewModel selectedPost;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.house_post_dialog_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Post Details");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.show();
        initializeComponents(dialogView);
        return dialog;
    }

    private void initializeComponents(View currentView) {
        selectedPost = getArguments().getParcelable("SelectedPost");
        totalLikes = (TextView) currentView.findViewById(R.id.HousePostDialogTotalLikesTextView);
        totalDislikes = (TextView) currentView.findViewById(R.id.HousePostDialogTotalDislikesTextView);
        likesNames = (TextView) currentView.findViewById(R.id.HousePostDialogLikesTextView);
        dislikesNames = (TextView) currentView.findViewById(R.id.HousePostDialogDislikesTextView);
        GetHousePostDetailsTask task = new GetHousePostDetailsTask(getActivity(), totalLikes, totalDislikes, likesNames, dislikesNames, selectedPost);
        task.execute();
    }


}

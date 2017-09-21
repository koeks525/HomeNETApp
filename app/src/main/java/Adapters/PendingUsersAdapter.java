package Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.List;

import Data.RealmHelper;
import Models.House;
import Models.HouseMemberViewModel;
import Models.User;
import Tasks.ApproveHouseMemberTask;
import Tasks.DeclineHouseMemberTask;


/**
 * Created by Okuhle on 2017/07/10.
 */

public class PendingUsersAdapter extends RecyclerView.Adapter<PendingUsersAdapter.PendingUsersViewHolder> {

    private List<HouseMemberViewModel> userList;
    private RealmHelper realmHelper;
    private Activity currentActivity;
    private House selectedHouse;

    public PendingUsersAdapter(List<HouseMemberViewModel> userList, Activity currentActivity, House selectedHouse) {
        this.userList = userList;
        realmHelper = new RealmHelper();
        this.currentActivity = currentActivity;
        this.selectedHouse = selectedHouse;
    }
    @Override
    public PendingUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.house_manager_user_card, parent, false);
        return new PendingUsersViewHolder(currentView);
    }

    public void removeUserFromList(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, userList.size());
    }

    @Override
    public void onBindViewHolder(final PendingUsersViewHolder holder, final int position) {
        final HouseMemberViewModel selectedUser = userList.get(position);
        holder.nameSurnameTextView.setText(selectedUser.getName()+" "+selectedUser.getSurname());
        holder.countryTextView.setText(realmHelper.getCountryById(selectedUser.getCountryID()).getName());
        holder.emailTextView.setText(selectedUser.getEmailAddress());
        TextDrawable drawable = TextDrawable.builder().buildRound(selectedUser.getName().substring(0,1).toUpperCase()+selectedUser.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
        holder.profileImageView.setImageDrawable(drawable);
        holder.houseOverflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.houseOverflowButton.getContext(), view);
                popupMenu.inflate(R.menu.pending_user_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ApproveMembershipOption:
                                ApproveHouseMemberTask task = new ApproveHouseMemberTask(currentActivity, selectedHouse, userList.get(position).getEmailAddress(), PendingUsersAdapter.this, position);
                                task.execute();
                                break;
                            case R.id.DeclineMembershipOption:
                                DeclineHouseMemberTask declineTask = new DeclineHouseMemberTask(currentActivity, selectedHouse, position, userList.get(position).getEmailAddress(), PendingUsersAdapter.this);
                                declineTask.execute();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class PendingUsersViewHolder extends RecyclerView.ViewHolder {

        private CardView userCardView;
        private TextView nameSurnameTextView;
        private TextView countryTextView;
        private TextView emailTextView;
        private ImageView profileImageView;
        private ImageView houseOverflowButton;
        public PendingUsersViewHolder(View itemView) {
            super(itemView);
            userCardView = (CardView) itemView.findViewById(R.id.HouseManagerUserCardView);
            nameSurnameTextView = (TextView) itemView.findViewById(R.id.HouseManagerUserNameSurnameTextView);
            countryTextView = (TextView) itemView.findViewById(R.id.HouseManagerCountryTextView);
            emailTextView = (TextView) itemView.findViewById(R.id.HouseManagerEmailTextView);
            profileImageView = (ImageView) itemView.findViewById(R.id.HouseManagerUserProfilePhoto);
            houseOverflowButton = (ImageView) itemView.findViewById(R.id.HouseManagerOverflowButton);
        }
    }
}

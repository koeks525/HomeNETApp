package Adapters;

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
import Models.User;


/**
 * Created by Okuhle on 2017/07/10.
 */

public class PendingUsersAdapter extends RecyclerView.Adapter<PendingUsersAdapter.PendingUsersViewHolder> {

    private List<User> userList;
    private RealmHelper realmHelper;

    public PendingUsersAdapter(List<User> userList) {
        this.userList = userList;
        realmHelper = new RealmHelper();
    }
    @Override
    public PendingUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.house_manager_user_card, parent, false);
        return new PendingUsersViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final PendingUsersViewHolder holder, int position) {
        User selectedUser = userList.get(position);
        holder.nameSurnameTextView.setText(selectedUser.getName()+" "+selectedUser.getSurname());
        holder.countryTextView.setText(realmHelper.getCountryById(selectedUser.getCountryID()).getName());
        holder.emailTextView.setText(selectedUser.getEmail());
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


                                break;
                            case R.id.DeclineMembershipOption:


                                break;
                        }
                        return true;
                    }
                });
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

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
import Models.HouseMemberViewModel;
import Models.User;

/**
 * Created by Okuhle on 2017/07/10.
 */

public class BannedUsersAdapter extends RecyclerView.Adapter<BannedUsersAdapter.BannedUserViewHolder> {

    private List<HouseMemberViewModel> bannedUserList;
    private RealmHelper realmHelper;

    public BannedUsersAdapter(List<HouseMemberViewModel> bannedUserList) {
        this.bannedUserList = bannedUserList;
        realmHelper = new RealmHelper();
    }

    @Override
    public BannedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.house_manager_user_card, parent, false);
        return new BannedUserViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final BannedUserViewHolder holder, int position) {
        HouseMemberViewModel currentUser = bannedUserList.get(position);
        holder.nameSurnameTextView.setText(currentUser.getName() + " "+currentUser.getSurname());
        holder.countryTextView.setText(realmHelper.getCountryById(currentUser.getCountryID()).getName());
        holder.emailTextView.setText(currentUser.getEmailAddress());
        TextDrawable drawable = TextDrawable.builder().buildRound(currentUser.getName().substring(0,1).toUpperCase() + currentUser.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
        holder.profileImageView.setImageDrawable(drawable);
        holder.profileImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.houseOverflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(holder.houseOverflowButton.getContext(), view);
                menu.inflate(R.menu.banned_users_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ViewBannedReportOption:
                                //Pull up the receipts!!
                                break;
                            case R.id.UnbanUserOption:

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
        return bannedUserList.size();
    }

    class BannedUserViewHolder extends RecyclerView.ViewHolder {

        private CardView userCardView;
        private TextView nameSurnameTextView;
        private TextView countryTextView;
        private TextView emailTextView;
        private ImageView profileImageView;
        private ImageView houseOverflowButton;
        public BannedUserViewHolder(View itemView) {
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

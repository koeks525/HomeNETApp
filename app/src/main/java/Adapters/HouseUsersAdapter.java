package Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import Models.User;

/**
 * Created by Okuhle on 2017/06/14.
 */

public class HouseUsersAdapter extends RecyclerView.Adapter<HouseUsersAdapter.HouseUserAdapter> {

    private List<User> userList = new ArrayList<>();

    public HouseUsersAdapter(List<User> userList) {
        this.userList = userList;
    }
    @Override
    public HouseUserAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.house_manager_user_card, parent, false);
        return new HouseUserAdapter(currentView);

    }

    @Override
    public void onBindViewHolder(HouseUserAdapter holder, int position) {
        holder.nameSurnameTextView.setText(userList.get(position).getName() + " " + userList.get(position).getSurname());
        holder.emailTextView.setText(userList.get(position).getEmail());
        holder.countryTextView.setText(userList.get(position).getSecurityQuestion()); //Horrible, just disgusting
        holder.profileImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        holder.profileImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HouseUserAdapter extends RecyclerView.ViewHolder {

        private CardView userCardView;
        private TextView nameSurnameTextView;
        private TextView countryTextView;
        private TextView emailTextView;
        private ImageView profileImageView;
        private ImageView houseOverflowButton;

        public HouseUserAdapter(View itemView) {
            super(itemView);
            userCardView = (CardView) itemView.findViewById(R.id.HouseManagerUserCardView);
            nameSurnameTextView = (TextView) itemView.findViewById(R.id.HouseManagerUserNameSurnameTextView);
            countryTextView = (TextView) itemView.findViewById(R.id.HouseManagerCountryTextView);
            emailTextView = (TextView) itemView.findViewById(R.id.HouseManagerEmailTextView);
            profileImageView = (ImageView) itemView.findViewById(R.id.HouseManagerUserProfilePhoto);
            houseOverflowButton = (ImageView) itemView.findViewById(R.id.HouseManagerOverflowButton);
            houseOverflowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Inflate the menu here.

                }
            });

        }
    }


}

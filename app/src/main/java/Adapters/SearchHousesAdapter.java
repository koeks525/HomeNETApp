package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.List;

import DialogFragments.AboutHouseDialogFragment;
import Models.House;
import Tasks.JoinHouseTask;

/**
 * Created by Okuhle on 2017/06/27.
 */

public class SearchHousesAdapter extends RecyclerView.Adapter<SearchHousesAdapter.SearchHousesViewHolder> {

    private List<House> housesList;
    private Activity context;
    private SharedPreferences sharedPreferences;

    public SearchHousesAdapter(Activity context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public SearchHousesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.search_houses_card, parent, false);
        return new SearchHousesViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final SearchHousesViewHolder holder, final int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound(housesList.get(position).getName().substring(0,1).toUpperCase(), Color.BLUE);
        holder.titleTextView.setText(housesList.get(position).getName());
        holder.descriptionTextView.setText(housesList.get(position).getDescription());
        holder.houseImageView.setImageDrawable(drawable);
        holder.houseImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        holder.optionsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.optionsImageView);
                popupMenu.inflate(R.menu.search_houses_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.JoinHouseOption:
                                //Select this option to join house
                                JoinHouseTask joinHouseTask = new JoinHouseTask(context, housesList.get(position).getHouseID(), sharedPreferences.getString("emailAddress", ""));
                                joinHouseTask.execute();
                                break;
                            case R.id.MoreInformationOption:
                                House selectedHouse = housesList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("SelectedHouse", selectedHouse);
                                AboutHouseDialogFragment dialogFragment = new AboutHouseDialogFragment();
                                dialogFragment.setArguments(bundle);
                                dialogFragment.show(context.getFragmentManager(), "AboutHouseDialog");
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return housesList.size();
    }

    public List<House> getHousesList() {
        return housesList;
    }

    public void setHousesList(List<House> housesList) {
        this.housesList = housesList;
    }

    class SearchHousesViewHolder extends RecyclerView.ViewHolder {

        ImageView houseImageView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView optionsImageView;

        public SearchHousesViewHolder(View itemView) {
            super(itemView);
            houseImageView = (ImageView) itemView.findViewById(R.id.SearchHouseImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.SearchHousesTitleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.SearchHouseDescriptionTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.SearchHouseOptionImageView);

        }
    }
}

package Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Okuhle on 2017/09/26.
 */

public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.EmptyRecyclerViewHolder> {


    @Override
    public EmptyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EmptyRecyclerViewHolder extends RecyclerView.ViewHolder {

        public EmptyRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}

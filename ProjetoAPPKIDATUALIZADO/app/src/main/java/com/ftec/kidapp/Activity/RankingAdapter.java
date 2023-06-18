package com.ftec.kidapp.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftec.kidapp.R;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private List<String> rankingList;

    public RankingAdapter(List<String> rankingList) {
        this.rankingList = rankingList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        String time = rankingList.get(position);
        holder.bindData(position, time);
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public void setRankingList(List<String> rankingList) {
        this.rankingList = rankingList;
        notifyDataSetChanged();
    }

    public class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView rankingItemTextView1;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            rankingItemTextView1 = itemView.findViewById(R.id.rankingItemTextView1);
        }

        public void bindData(int position, String time) {
            rankingItemTextView1.setText((position + 1) + ". " + time);
        }
    }
}

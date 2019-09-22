package me.ofir.fitme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;

import me.ofir.fitme.ContentActivity;
import me.ofir.fitme.Entites.News;
import me.ofir.fitme.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> data;
    private Context ctx;


    public NewsAdapter(List<News> data, Context activity) {
        this.ctx = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item,viewGroup,false);

        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
            News news = data.get(i);

            newsViewHolder.tvTitle.setText(news.getTitle());
            newsViewHolder.tvDesc.setText(news.getDescription());

       // Glide.with(ctx).load(news.getUrl()).fitCenter().centerCrop().placeholder(R.drawable.common_full_open_on_phone).into(newsViewHolder.ivImage);

            newsViewHolder.container.setOnClickListener((c)->{
                openNews(news.getUrl());
            });
    }

    private void openNews(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        ctx.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return (data.size() == 0)? 0:data.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDesc;
        ConstraintLayout container;
        ImageView ivImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvDesc = itemView.findViewById(R.id.tvNewsDesc);
            container = itemView.findViewById(R.id.newsContainer);
            ivImage = itemView.findViewById(R.id.ivNewsImage);
        }
    }
}

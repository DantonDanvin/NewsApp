package com.example.news_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdepter extends RecyclerView.Adapter<NewsAdepter.NewsViewHolder>{

    List<Article> articals;
    NewsAdepter(List<Article> articles){
        this.articals=articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_template,parent,false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articals.get(position);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.image);

        holder.itemView.setOnClickListener((v -> {
            Intent intent = new Intent(v.getContext(),NewsDetail.class);
            intent.putExtra("url",article.getUrl());
            v.getContext().startActivity(intent);
        }));
    }

    void updateData(List<Article> data) {
        articals.clear();
        articals.addAll(data);
    }

    @Override
    public int getItemCount() {
        return articals.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView title,source;
        ImageView image;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            source=itemView.findViewById(R.id.source);
            image=itemView.findViewById(R.id.artical_image);
        }
    }

}

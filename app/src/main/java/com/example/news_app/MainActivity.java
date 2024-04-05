package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.news_app.databinding.ActivityMainBinding;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    List<Article> articleList = new ArrayList<>();
    NewsAdepter adepter;
    RecyclerView recyclerView;
    LinearProgressIndicator progressIndicator;
    SearchView searchView;
    Button tech,buss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.newsRecyclerView);
        progressIndicator = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        tech = (Button)findViewById(R.id.techno);
        buss = (Button)findViewById(R.id.buss);

        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews("technology",null);
            }
        });

        buss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews("business",null);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL" , query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0){
                    getNews("technology",null);
                    return true;
                }
                return false;
            }
        });

        setUpRecyclerView();
        getNews("technology",null);


    }

    void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adepter = new NewsAdepter(articleList);
        recyclerView.setAdapter(adepter);
    }

    void changeInProgress(boolean show){
        if(show) {progressIndicator.setVisibility(View.VISIBLE);}
        else {progressIndicator.setVisibility(View.INVISIBLE);}
    }

    void getNews(String category , String query){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("aa130e470f2e40c2ba6ff0704b57ea9a");

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(()->{
                            changeInProgress(false);
                            articleList = response.getArticles();
                            if(articleList.size()==0){
                                Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                            adepter.updateData(articleList);
                            adepter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
      Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }
}
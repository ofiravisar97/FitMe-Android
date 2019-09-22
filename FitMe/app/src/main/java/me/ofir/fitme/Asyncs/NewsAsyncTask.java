package me.ofir.fitme.Asyncs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.ofir.fitme.Adapters.NewsAdapter;
import me.ofir.fitme.ContentActivity;
import me.ofir.fitme.Entites.News;

public class NewsAsyncTask extends AsyncTask<Void,Void, List<News>> {

    private WeakReference<RecyclerView> rvNews;
    private WeakReference<Context> ctx;

    public NewsAsyncTask(RecyclerView rv,Context activity){
        this.rvNews = new WeakReference<>(rv);
        this.ctx = new WeakReference<>(activity);
    }

    @Override
    protected List<News> doInBackground(Void... voids) {
         List<News> news = new ArrayList<>();
         try{
             Document document = Jsoup.connect("https://www.israelbody.org/תזונה").get();
             Elements Pictures = document.getElementsByClass("sonsPic");


             for(Element picture: Pictures){
                 String image = picture.getElementsByTag("img").attr("src");
                 String title = picture.getElementsByTag("a").attr("title");
                 String link = picture.getElementsByTag("a").attr("href");
                 String desc = picture.lastElementSibling().previousElementSibling().previousElementSibling().previousElementSibling().text();

                 news.add(new News(title,desc,link,image));
             }

         } catch (IOException e){
            e.printStackTrace();
         }


        return news;
    }

    @Override
    protected void onPostExecute(List<News> news) {
        RecyclerView recyclerView = rvNews.get();
        Context context = ctx.get();
        if(recyclerView != null && ctx != null){
            recyclerView.setAdapter(new NewsAdapter(news,context));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }
    }
}

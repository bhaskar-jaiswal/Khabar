package com.nytarticlesearch.bhaskarjaiswal.khabar.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.adapters.ArticleArrayAdapter;
import com.nytarticlesearch.bhaskarjaiswal.khabar.constants.Config;
import com.nytarticlesearch.bhaskarjaiswal.khabar.decorators.SpacesItemDecoration;
import com.nytarticlesearch.bhaskarjaiswal.khabar.dialogs.SearchCriteriaDialogFragment;
import com.nytarticlesearch.bhaskarjaiswal.khabar.listener.EndlessRecyclerViewScrollListener;
import com.nytarticlesearch.bhaskarjaiswal.khabar.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.rvArticles)
    RecyclerView rvResults;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private StaggeredGridLayoutManager sGridLayout;
    private SearchView searchView;
    private SearchCriteriaDialogFragment searchCriteriaDialogFragment;
    private StringBuilder searchQuery = new StringBuilder();
    private RequestParams params = new RequestParams();

    ArrayList<Article> articles;
    ArticleArrayAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(!(isNetworkAvailable() && isOnline())){
            Toast.makeText(this,"Network Is Not Available",Toast.LENGTH_LONG).show();
            return;
        }

        setupViews();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }


    public void setupViews(){
//        getSupportActionBar().hide();

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADD8E6")));
        bar.setTitle(Html.fromHtml("<font color='#000066'>Khabar </font>"));

        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articleAdapter = new ArticleArrayAdapter(this, articles);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvResults.addItemDecoration(decoration);
        rvResults.setAdapter(articleAdapter);
        setParams();

        sGridLayout = new  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        sGridLayout.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvResults.setLayoutManager(sGridLayout);

        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(sGridLayout){
            public void onLoadMore(int page, int totalItemsCount){
                onArticleSearch(page);
            }
        });

        articleAdapter.setOnChooseOptionsActionListener(new ArticleArrayAdapter.OnItemClickListener(){

            @Override
            public void onSnippetClick(View itemView, int position) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article",Parcels.wrap(article));
                startActivity(intent);
            }

            @Override
            public void onThumbnailView(View itemView, int position) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article",Parcels.wrap(article));
                startActivity(intent);
            }
        });

        searchCriteriaDialogFragment = new SearchCriteriaDialogFragment();
        searchCriteriaDialogFragment.setCallSearchArticles(new SearchCriteriaDialogFragment.CallSearchArticles() {
            @Override
            public void onChangedSearchCriteria() {
                /*if(searchQuery.length()>0){
                    setParams();
                    articles.clear();
                    articleAdapter.notifyDataSetChanged();
                    onArticleSearch(Config.PAGE_ZERO);
                }*/
                setParams();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                articles.clear();
                articleAdapter.notifyDataSetChanged();
                onArticleSearch(Config.PAGE_ZERO);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void onArticleSearch(int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.NYTIMES_SEARCH_URL;

        params.add("api-key", Config.API_KEY);
        params.put("page",page);
        params.put("q",searchQuery.toString());

        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray jsonArticleResults = null;
                try{
                    jsonArticleResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(jsonArticleResults));

                    articleAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    Log.d("Response 2",articles.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", "Fetch timeline error: " + errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.miSettings);

        searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                articles.clear();
                articleAdapter.notifyDataSetChanged();
                searchQuery.delete(0,searchQuery.length());
                searchQuery.append(searchView.getQuery().toString());

                onArticleSearch(Config.PAGE_ZERO);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragment = getFragmentManager();
                searchCriteriaDialogFragment.show(fragment, "article_search_criteria");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setParams(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);

        String begin_date = pref.getString("date","NoDate");

        if(!begin_date.equalsIgnoreCase("NoDate")){
            String year = begin_date.substring(begin_date.lastIndexOf("-")+1);

            String day = begin_date.substring(begin_date.indexOf("-")+1,begin_date.lastIndexOf("-"));

            String month = begin_date.substring(0,begin_date.indexOf("-"));

            String date = year+month+day;
            Log.d("begin_date",date);
            params.put("begin_date",date);

            Log.d("begin_date",begin_date);

            params.put("sort",pref.getString("sortedBy","newest"));
            Log.d("sort",pref.getString("sortedBy","newest"));

            if(pref.getBoolean("arts",false) || pref.getBoolean("fashion",false) || pref.getBoolean("fashion",false)){
                String news_desk="news_desk:(";
                if(pref.getBoolean("arts",false)){
                    news_desk=news_desk+"arts ";
                }
                if(pref.getBoolean("fashion",false)){
                    news_desk=news_desk+"fashion&style ";
                }
                if(pref.getBoolean("sports",false)){
                    news_desk=news_desk+"sports ";
                }
                news_desk=news_desk.trim()+")";
                params.put("fq",news_desk);
                Log.d("fq",news_desk);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}


package com.nytarticlesearch.bhaskarjaiswal.khabar.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.constants.Config;
import com.nytarticlesearch.bhaskarjaiswal.khabar.models.Article;
import com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders.ViewHolderSimpleRecyclerView;
import com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders.ViewHolderSnippet;
import com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders.ViewHolderThumbnail;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by bhaskarjaiswal on 7/26/2016.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> articles;
    private Context context;
    private OnItemClickListener onItemClick;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public interface OnItemClickListener {
        void onSnippetClick(View itemView, int position);
        void onThumbnailView(View itemView, int position);
    }

    public void setOnChooseOptionsActionListener(OnItemClickListener itemClicked) {
        onItemClick = itemClicked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewholder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case Config.HEADLINE_SNIPPET:
                View snippetView = inflater.inflate(R.layout.item_headline_snippet, parent, false);
                viewholder = new ViewHolderSnippet(snippetView, onItemClick);
                break;
            case Config.HEADLINE_THUMBNAIL:
                View thumbnailView = inflater.inflate(R.layout.item_headline_thumbnail, parent, false);
                viewholder = new ViewHolderThumbnail(thumbnailView, onItemClick);
                break;
            default:
                Log.d("DEBUG","INITIALIZING DEFAULT VIEWHOLDER");
                View simpleViewHolder = inflater.inflate(R.layout.item_default_layout, parent, false);
                viewholder = new ViewHolderSimpleRecyclerView(simpleViewHolder, onItemClick);
        }
        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()){
            case Config.HEADLINE_SNIPPET:
                configureViewHolderHeadline((ViewHolderSnippet)viewHolder, position);
                break;
            case Config.HEADLINE_THUMBNAIL:
                configureViewHolderThumbnail((ViewHolderThumbnail)viewHolder, position);
                break;
        }
    }

    private void configureViewHolderHeadline(ViewHolderSnippet viewHolderSnippet, int position) {
        Article article = (Article) articles.get(position);
        if (article != null) {
            viewHolderSnippet.getBinding().setArticle(article);
            viewHolderSnippet.getBinding().executePendingBindings();
        }
    }

    private void configureViewHolderThumbnail(ViewHolderThumbnail viewHolderThumbnail, int position) {

        Article article = (Article) articles.get(position);
        if (article != null) {
            viewHolderThumbnail.getBinding().setArticle(article);
            viewHolderThumbnail.getBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);

        if (TextUtils.isEmpty(article.getThumbNail())) {
            return Config.HEADLINE_SNIPPET;
        } else {
            return Config.HEADLINE_THUMBNAIL;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url).transform(new RoundedCornersTransformation(10, 10))
                .into(imageView);
    }
}

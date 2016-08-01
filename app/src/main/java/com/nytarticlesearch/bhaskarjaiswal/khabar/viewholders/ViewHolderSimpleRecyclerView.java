package com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.adapters.ArticleArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjaiswal on 7/21/2016.
 */
public class ViewHolderSimpleRecyclerView extends RecyclerView.ViewHolder{

    @BindView(R.id.tvHeadline)
    TextView tvHeadline;

    @BindView(R.id.tvSnippet)
    TextView tvSnippet;

    private ArticleArrayAdapter.OnItemClickListener itemClicked;

    public ViewHolderSimpleRecyclerView(final View itemView, final ArticleArrayAdapter.OnItemClickListener itemClicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.itemClicked = itemClicked;

        /*ivImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onPlayVideo(view, getLayoutPosition());
                }
            }
        });*/
    }

    public TextView getTvHeadline() {
        return tvHeadline;
    }

    public void setTvHeadline(TextView tvHeadline) {
        this.tvHeadline = tvHeadline;
    }

    public TextView getTvSnippet() {
        return tvSnippet;
    }

    public void setTvSnippet(TextView tvSnippet) {
        this.tvSnippet = tvSnippet;
    }
}
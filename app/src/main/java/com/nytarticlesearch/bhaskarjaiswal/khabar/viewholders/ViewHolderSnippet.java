package com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.adapters.ArticleArrayAdapter;
import com.nytarticlesearch.bhaskarjaiswal.khabar.databinding.ItemHeadlineSnippetBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjaiswal on 7/27/2016.
 */
public class ViewHolderSnippet extends RecyclerView.ViewHolder {

    @BindView(R.id.tvHeadline)
    TextView tvHeadline;

    @BindView(R.id.tvSnippet)
    TextView tvSnippet;

    private ArticleArrayAdapter.OnItemClickListener itemClicked;
    private ItemHeadlineSnippetBinding binding;

    public ViewHolderSnippet(final View itemView, final ArticleArrayAdapter.OnItemClickListener itemClicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        binding = DataBindingUtil.bind(itemView);

        this.itemClicked = itemClicked;

        tvHeadline.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onSnippetClick(itemView, getLayoutPosition());
                }
            }
        });

        tvSnippet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Clicked","clicked");
                if (itemClicked != null){
                    itemClicked.onSnippetClick(itemView, getLayoutPosition());
                }
            }
        });
    }

    public ItemHeadlineSnippetBinding getBinding(){
        return binding;
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



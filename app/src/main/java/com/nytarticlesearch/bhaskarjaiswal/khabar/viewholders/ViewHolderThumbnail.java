package com.nytarticlesearch.bhaskarjaiswal.khabar.viewholders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.adapters.ArticleArrayAdapter;
import com.nytarticlesearch.bhaskarjaiswal.khabar.databinding.ItemHeadlineThumbnailBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjaiswal on 7/27/2016.
 */
public class ViewHolderThumbnail extends RecyclerView.ViewHolder {

    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;

    @BindView(R.id.tvHeadline)
    TextView tvHeadline;

    private ArticleArrayAdapter.OnItemClickListener itemClicked;
    private ItemHeadlineThumbnailBinding itemHeadlineThumbnailBinding;

    public ViewHolderThumbnail(final View itemView, final ArticleArrayAdapter.OnItemClickListener itemClicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemHeadlineThumbnailBinding = DataBindingUtil.bind(itemView);

        this.itemClicked = itemClicked;

        ivThumbnail.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onThumbnailView(itemView, getLayoutPosition());
                }
            }
        });

        tvHeadline.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onThumbnailView(itemView, getLayoutPosition());
                }
            }
        });
    }

    public ItemHeadlineThumbnailBinding getBinding() {
        return itemHeadlineThumbnailBinding;
    }

    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    public void setIvThumbnail(ImageView ivThumbnail) {
        this.ivThumbnail = ivThumbnail;
    }

    public TextView getTvHeadline() {
        return tvHeadline;
    }

    public void setTvHeadline(TextView tvHeadline) {
        this.tvHeadline = tvHeadline;
    }
}

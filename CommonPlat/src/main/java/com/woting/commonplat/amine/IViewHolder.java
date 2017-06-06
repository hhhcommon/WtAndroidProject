package com.woting.commonplat.amine;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by amine on 16/3/12.
 */
public abstract class IViewHolder extends RecyclerView.ViewHolder {

    public IViewHolder(View itemView) {
        super(itemView);
    }

    @Deprecated
    public final int getIPosition() {
        return getPosition() - 2;
    }

    public final int getILayoutPosition() {
        return getLayoutPosition() - 2;
    }

    public final int getIAdapterPosition() {
        return getAdapterPosition() - 2;
    }

    public final int getIOldPosition() {
        return getIOldPosition() - 2;
    }

    public final long getIItemId() {
        return getItemId();
    }

    public final int getIItemViewType() {
        return getItemViewType();
    }
}

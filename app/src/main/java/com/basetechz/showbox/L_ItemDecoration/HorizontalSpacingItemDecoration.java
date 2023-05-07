package com.basetechz.showbox.L_ItemDecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpacingItemDecoration extends RecyclerView.ItemDecoration {
    public int spanCount;
    public int spacing;
    public boolean includeEdge;

    public HorizontalSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.top = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.bottom = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // left edge
                outRect.left = spacing;
            }
            outRect.right = spacing; // item right
        } else {
            outRect.top = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.bottom = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f / spanCount) * spacing)
            if (position >= spanCount) {
                outRect.left = spacing; // item left
            }
        }
    }
}

package com.example.genzgpt.View;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adds blank space between RecyclerViews and their children.
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    /**
     * The constructor for the spacingItemDecoration.
     * @param spacing
     * The amount of spacing.
     */
    public SpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    /**
     * A getter for the offset of an item in a RecyclerView.
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = spacing;
    }
}

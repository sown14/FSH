package com.example.shoe.models;

import android.view.View;

public class ItemActionProfile {
    private final int icon;
    private final String label;
    private final View.OnClickListener onClickListener;

    public ItemActionProfile(int icon, String label, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.label = label;
        this.onClickListener = onClickListener;
    }


    public int getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}

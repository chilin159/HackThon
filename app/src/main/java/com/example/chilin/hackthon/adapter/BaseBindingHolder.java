package com.example.chilin.hackthon.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by evanhou on 2016/11/23.
 */

public class BaseBindingHolder extends RecyclerView.ViewHolder {

    public ViewDataBinding binding;

    public static BaseBindingHolder newInstance(ViewDataBinding binding) {
        BaseBindingHolder holder = new BaseBindingHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    public BaseBindingHolder(View itemView) {
        super(itemView);
    }

    public Context getContext() {
        return binding != null ? binding.getRoot().getContext() : null;
    }
}
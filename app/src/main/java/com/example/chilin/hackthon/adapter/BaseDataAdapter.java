package com.example.chilin.hackthon.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseDataAdapter<V extends RecyclerView.ViewHolder, E extends List> extends RecyclerView.Adapter<V> {
    public abstract void setData(E dataList);
}
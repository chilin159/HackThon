package com.example.chilin.hackthon.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chilin.hackthon.listener.RecyclerViewListListener;
import com.example.chilin.hackthon.model.SelectableModel;

import java.util.ArrayList;
import java.util.List;

public class BaseSelectableAdapter<T> extends BaseDataAdapter<BaseBindingHolder, List<T>> {
    public static final int NO_SELECT = 0;
    public static final int SINGLE_SELECT = 1;
    public static final int MULTI_SELECT = 2;
    public static final int TYPE_DEFAULT_HEADER = -1;
    public static final int TYPE_MAIN_CONTENT = 100;
    public static final int TYPE_FOOTER = 200;
    private final String PAYLAOD_SELECTED = "PAYLOAD_SELECTED";

    private List<T> mData = new ArrayList<>();
    private @IdRes
    int mBindingVariable = 0;
    private @LayoutRes
    int mRes = 0;

    private List<T> mHeaderData = new ArrayList<>();
    private @IdRes
    List<Integer> mHeaderBindingVariable = new ArrayList<>();
    private @LayoutRes
    List<Integer> mHeaderRes = new ArrayList<>();
    List<Integer> mViewTypeForOutside = new ArrayList<>();

    private boolean mHasFooter = false;
    private @IdRes
    int mFooterBindingVariable = 0;
    private @LayoutRes
    int mFooterRes = 0;
    private T mFooterData = null;

    private @IdRes
    int mSelectableBindingVariable = 0;
    private int mType = NO_SELECT;
    private List<Boolean> mSelectableList = new ArrayList<>();
    private int mSelectedPosition = -1;

    private RecyclerViewListListener mRecyclerViewListClickListener;
    private boolean mRootViewClickable = true;
    private boolean mRootViewSelectable = true;
    private List<Integer> mClickableItemIdList = new ArrayList<>();
    private List<Integer> mSelectableItemIdList = new ArrayList<>();

    /***********************************************************************************************
     * Below are necessary methods
     * ********************************************************************************************/
    /**
     * mBindingVariable  is  binding  variable in resourse
     * mRes is xml resource of item
     * for example:  BaseSelectableAdapter(BR.data, R.layout.item_school_grade_level);
     */
    public BaseSelectableAdapter(@IdRes int bindingVariable, @LayoutRes int res) {
        mBindingVariable = bindingVariable;
        mRes = res;
    }

    /**
     * You can use BindingUtils setDataSource to call this method
     */
    @Override
    public void setData(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        mData.clear();
        mData.addAll(dataList);

        /**
         * Setting selected states:
         * SINGLE_SELECT: need to update last position,  see setSingleSelectedPosition(int position)
         * MULTI_SELECT: dataList need to extends SelectableModel
         */
        switch (mType) {
            case SINGLE_SELECT:
                mSelectableList.clear();
                for (int position = 0; position < mData.size(); position++) {
                    if (mSelectedPosition == position) {
                        mSelectableList.add(true);
                    } else {
                        mSelectableList.add(false);
                    }
                }
                break;
            case MULTI_SELECT:
                mSelectableList.clear();
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i) instanceof SelectableModel) {
                        SelectableModel data = (SelectableModel) mData.get(i);
                        mSelectableList.add(data.getIsSelected());
                    }
                }
                break;
        }

        notifyDataSetChanged();
    }
    /***********************************************************************************************
     * Below are optional methods
     * ********************************************************************************************/
    /**
     * Set header resources , data and viewType by below methods
     * mViewTypeForOutside : you can customized header viewType in outside, just remember not to define same value as TYPE_MAIN_CONTENT for header viewtype
     * for example:
     * adapter.addHeaderRes(BR.viewModel, R.layout.holder_header_favourite_tutors, mViewModel);
     * adapter.addHeaderRes(mVBR.viewModel, R.layout.holder_header_favourite_tutors, mViewModel, HEADER_TYPE);
     */
    public void addHeaderRes(@IdRes int bindingVariable, @LayoutRes int res, @Nullable T data) {
        mHeaderBindingVariable.add(bindingVariable);
        mHeaderRes.add(res);
        mHeaderData.add(data);
        mViewTypeForOutside.add(TYPE_DEFAULT_HEADER);
    }

    /**
     * mViewTypeForOutside : you can customized header viewType in outside, just remember not to define the same value as TYPE_MAIN_CONTENT and TYPE_FOOTER for header viewType
     */
    public void addHeaderRes(@IdRes int bindingVariable, @LayoutRes int res, @Nullable T data, int viewType) {
        mHeaderBindingVariable.add(bindingVariable);
        mHeaderRes.add(res);
        mHeaderData.add(data);
        mViewTypeForOutside.add(viewType);
    }

    public void clearHeader() {
        mHeaderBindingVariable.clear();
        mHeaderRes.clear();
        mHeaderData.clear();
        mViewTypeForOutside.clear();
    }

    public void setHasFooter(@IdRes int bindingVariable, @LayoutRes int res, @Nullable T data) {
        mHasFooter = true;
        mFooterBindingVariable = bindingVariable;
        mFooterRes = res;
        mFooterData = data;
    }

    /**
     * You can call this function to set view selected states
     * type : SINGLE_SELECT and MULTI_SELECT
     * bindingVariable: selected status in xml
     * If you set SINGLE_SELECT, you need to set the last selected position, see setSingleSelectedPosition(int position)
     * If you set MULTI_SELECT, your data model need to extends SelectableModel and init selected states
     */
    public void setSelectableRes(int type, @IdRes int bindingVariable) {
        mType = type;
        mSelectableBindingVariable = bindingVariable;
    }

    /**
     * For single select, you just need to send the last selected position to init state.
     * You can use BindingUtils setSingleSelectedPosition to call this method
     */
    public void setSingleSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    /**
     * You can call this function to listen view item click event
     */
    public void setAdapterItemClickListener(RecyclerViewListListener adapterItemClickListener) {
        this.mRecyclerViewListClickListener = adapterItemClickListener;
    }

    /**
     * Set false to make root view unClickable and unSelectable
     * To make root view clickable but unSelectable, you can just listen the click event and will not change view's selected state
     */
    public void setRootViewStatus(boolean clickable, boolean selectable) {
        mRootViewClickable = clickable;
        mRootViewSelectable = selectable;
    }

    /**
     * You can set an item into clickListener, it will just return view and position but won't change selected state
     */
    public void addClickableItemId(@IdRes int id) {
        mClickableItemIdList.add(id);
    }

    /**
     * You can set multiple items into clickListener, it will just return view and position but won't change selected state
     */
    public void setClickableItemIdList(List<Integer> idList) {
        if (idList == null) {
            return;
        }
        mClickableItemIdList.clear();
        mClickableItemIdList.addAll(idList);
    }

    /**
     * You can set an item into clickListener, it will  return view and position and change selected state
     */
    public void addSelectableItemId(@IdRes int id) {
        mSelectableItemIdList.add(id);
    }

    /**
     * You can set multiple items into clickListener, it will  return view and position and change selected state
     */
    public void setSelectableItemIdList(List<Integer> idList) {
        if (idList == null) {
            return;
        }
        mSelectableItemIdList.clear();
        mSelectableItemIdList.addAll(idList);
    }

    /***********************************************************************************************
     * Below are Override and private methods
     * ********************************************************************************************/
    private int getHeaderCount() {
        return mHeaderBindingVariable.size();
    }

    private void onClick(View v, int adapterPos) {
        if (mRecyclerViewListClickListener != null) {
            int viewType = TYPE_MAIN_CONTENT;
            if (adapterPos < getHeaderCount()) {
                viewType = mViewTypeForOutside.get(adapterPos);
            }
            mRecyclerViewListClickListener.onItemClick(viewType, v, adapterPos - getHeaderCount());
        }
    }

    private void onSelectedClick(View v, int adapterPos) {
        int selectPos = adapterPos - getHeaderCount();
        if (!mSelectableList.isEmpty()) {
            switch (mType) {
                case SINGLE_SELECT:
                    if (mSelectedPosition >= 0 && mSelectedPosition != selectPos) {
                        mSelectableList.set(mSelectedPosition, false);
                    }
                    mSelectableList.set(selectPos, true);
                    notifyItemChanged(adapterPos, PAYLAOD_SELECTED);
                    notifyItemChanged(mSelectedPosition + getHeaderCount(), PAYLAOD_SELECTED);
                    mSelectedPosition = selectPos;
                    break;
                case MULTI_SELECT:
                    if (mSelectableList.get(selectPos)) {
                        mSelectableList.set(selectPos, false);
                    } else {
                        mSelectableList.set(selectPos, true);
                    }
                    notifyItemChanged(adapterPos, PAYLAOD_SELECTED);
                    break;
            }
        }

        if (mRecyclerViewListClickListener != null) {
            int viewType = TYPE_MAIN_CONTENT;
            if (adapterPos < getHeaderCount()) {
                viewType = mViewTypeForOutside.get(adapterPos);
            }
            mRecyclerViewListClickListener.onItemClick(viewType, v, selectPos);
        }
    }


    private void initClickListener(BaseBindingHolder holder) {
        if (mRootViewClickable) {
            holder.binding.getRoot().setOnClickListener(v -> {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (mRootViewSelectable) {
                        onSelectedClick(v, adapterPos);
                    } else {
                        onClick(v, adapterPos);
                    }
                }
            });
        }

        for (int i = 0; i < mClickableItemIdList.size(); i++) {
            if (holder.binding.getRoot().findViewById(mClickableItemIdList.get(i)) != null) {
                holder.binding.getRoot().findViewById(mClickableItemIdList.get(i)).setOnClickListener(v -> {
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                        onClick(v, holder.getAdapterPosition());
                    }
                });
            }
        }

        for (int i = 0; i < mSelectableItemIdList.size(); i++) {
            if (holder.binding.getRoot().findViewById(mSelectableItemIdList.get(i)) != null) {
                holder.binding.getRoot().findViewById(mSelectableItemIdList.get(i)).setOnClickListener(v -> {
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                        onSelectedClick(v, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public BaseBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType != TYPE_MAIN_CONTENT) {
            if (viewType != TYPE_FOOTER) {
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, mHeaderRes.get(viewType), parent, false);
                return BaseBindingHolder.newInstance(binding);
            } else {
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, mFooterRes, parent, false);
                return BaseBindingHolder.newInstance(binding);
            }
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, mRes, parent, false);
            BaseBindingHolder holder = BaseBindingHolder.newInstance(binding);
            initClickListener(holder);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(BaseBindingHolder holder, int position) {
        if (getItemViewType(position) != TYPE_MAIN_CONTENT) {
            if (getItemViewType(position) != TYPE_FOOTER) {
                holder.binding.setVariable(mHeaderBindingVariable.get(position), mHeaderData.get(position));
            } else {
                holder.binding.setVariable(mFooterBindingVariable, mFooterData);
            }
        } else {
            final T data = mData.get(position - getHeaderCount());
            holder.binding.setVariable(mBindingVariable, data);
            if (mType != NO_SELECT && !mSelectableList.isEmpty()) {
                holder.binding.setVariable(mSelectableBindingVariable, mSelectableList.get(position - getHeaderCount()));
            }
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public void onBindViewHolder(BaseBindingHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.contains(PAYLAOD_SELECTED)) {
                if (mType != NO_SELECT && !mSelectableList.isEmpty()) {
                    holder.binding.setVariable(mSelectableBindingVariable, mSelectableList.get(position - getHeaderCount()));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mHasFooter ? mData.size() + getHeaderCount() + 1 : mData.size() + getHeaderCount();
        }
        return mHasFooter ? getHeaderCount() + 1 : getHeaderCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            //return position because in onCreateViewHolder, it need correct position to call mHeaderRes.get(viewType)
            return position;
        } else if (mHasFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_MAIN_CONTENT;
        }
    }
}

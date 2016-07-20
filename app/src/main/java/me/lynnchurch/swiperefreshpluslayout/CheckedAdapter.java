package me.lynnchurch.swiperefreshpluslayout;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynn on 2016-7-9.
 */

public class CheckedAdapter extends BaseAdapter
{
    private List<String> mDataSet;
    private List<Boolean> mCheckedStates;
    private boolean mIsShowCheckedIcons;
    private int mIndex;

    public CheckedAdapter(List<String> dataSet)
    {
        mDataSet = dataSet;
        mIndex=mDataSet.size();
        initCheckedStates();
    }

    private void initCheckedStates()
    {
        if (null != mDataSet && 0 != mDataSet.size())
        {
            mCheckedStates = new ArrayList<>();
            for (int i = 0; i < mDataSet.size(); i++)
            {
                mCheckedStates.add(false);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.i("TAG", "onCreateViewHolder: ");
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checked_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        final ViewHolder vh = (ViewHolder) holder;
        vh.tv_feature_name.setText(mDataSet.get(position));
        vh.cb_select.setVisibility(mIsShowCheckedIcons ? View.VISIBLE : View.GONE);
        vh.cb_select.setChecked(mCheckedStates.get(position));
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }

    class ViewHolder extends BaseAdapter.ViewHolder
    {
        AppCompatTextView tv_feature_name;
        AppCompatCheckBox cb_select;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tv_feature_name = (AppCompatTextView) itemView.findViewById(R.id.tv_feature_name);
            cb_select = (AppCompatCheckBox) itemView.findViewById(R.id.cb_select);
            cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    mCheckedStates.set(getLayoutPosition(), b);
                }
            });
        }
    }

    public void showCheckedIcons(AppCompatCheckBox cb, int position)
    {
        mIsShowCheckedIcons = true;
        cb.setChecked(true);
        notifyDataSetChanged();
    }

    public void hideCheckedIcons()
    {
        mIsShowCheckedIcons = false;
        initCheckedStates();
        notifyDataSetChanged();
    }

    public void deleteCheckedItems()
    {
        List<String> checkedItems = new ArrayList<>();
        for (int i = 0; i < mCheckedStates.size(); i++)
        {
            if (mCheckedStates.get(i))
            {
                checkedItems.add(mDataSet.get(i));
            }
        }
        mDataSet.removeAll(checkedItems);
        hideCheckedIcons();
    }

    public void addItem()
    {
        mDataSet.add("item" + mIndex++);
        initCheckedStates();
        notifyDataSetChanged();
    }
}

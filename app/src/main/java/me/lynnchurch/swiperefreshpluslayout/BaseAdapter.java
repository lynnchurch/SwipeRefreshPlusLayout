package me.lynnchurch.swiperefreshpluslayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Lynn on 2016-7-11.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter
{
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListner(OnItemClickListener listner)
    {
        mOnItemClickListener=listner;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        mOnItemLongClickListener=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(final View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(null != mOnItemClickListener)
                    {
                        mOnItemClickListener.onItemClick(itemView,getLayoutPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if(null != mOnItemLongClickListener)
                    {
                        mOnItemLongClickListener.onItemLongClick(itemView, getLayoutPosition());
                        return true;
                    }
                   return false;
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int position);
    }
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v, int position);
    }
}

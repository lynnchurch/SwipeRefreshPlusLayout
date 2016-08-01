package me.lynnchurch.swiperefreshpluslayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.lynnchurch.swiperefreshplus.SwipeRefreshPlusLayout;

public class MainActivity extends AppCompatActivity
{
    private static final int REFRESH = 0;
    private static final int LOADMORE = 1;
    private List<String> mItems = new ArrayList<>();
    private MenuItem menu_delete;
    private MenuItem menu_add;
    private CheckedAdapter mAdapter;
    private int mIndex;
    private SwipeRefreshPlusLayout swipe_refresh;
    private RefreshHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new RefreshHandler(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for (int i = 0; i < 5; i++)
        {
            mItems.add("item" + i);
            mIndex++;
        }
        mAdapter = new CheckedAdapter(mItems);
        mAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener()
        {
            @Override
            public void onItemLongClick(View v, int position)
            {
                if (!menu_delete.isVisible())
                {
                    mAdapter.showCheckedIcons((AppCompatCheckBox) v.findViewById(R.id.cb_select));
                    toggleMenuItems();
                }
            }
        });
        RecyclerView rv_items = (RecyclerView) findViewById(R.id.rv_items);
        rv_items.setAdapter(mAdapter);
        rv_items.setLayoutManager(new LinearLayoutManager(this));

        swipe_refresh = (SwipeRefreshPlusLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
        swipe_refresh.setEnableLoadMore(true);
        swipe_refresh.setLoadMoreColorSchemeResources(R.color.swipe_color_1);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshPlusLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                simulateRefreshData();
            }
        });
        swipe_refresh.setOnLoadMoreListener(new SwipeRefreshPlusLayout.OnLoadMoreListener()
        {
            @Override
            public void onLoadMore()
            {
                simulateLoadMoreData();
            }
        });
    }

    /**
     * 模拟刷新数据
     */
    public void simulateRefreshData()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(4 * 1000);
                    refreshData();
                    mHandler.sendEmptyMessage(REFRESH);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 模拟加载更多数据
     */
    public void simulateLoadMoreData()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2 * 1000);
                    loadMoreData();
                    mHandler.sendEmptyMessage(LOADMORE);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu_add = menu.getItem(0);
        menu_delete = menu.getItem(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_add:
                mItems.add("item" + mIndex++);
                mAdapter.notifyCheckedDataSetChanged();
                return true;
            case R.id.menu_delete:
                mAdapter.deleteCheckedItems();
                toggleMenuItems();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleMenuItems()
    {
        menu_add.setVisible(!menu_add.isVisible());
        menu_delete.setVisible(!menu_delete.isVisible());
    }

    @Override
    public void onBackPressed()
    {
        if (menu_delete.isVisible())
        {
            toggleMenuItems();
            mAdapter.hideCheckedIcons();
        } else
        {
            super.onBackPressed();
        }
    }

    class RefreshHandler extends Handler
    {
        private WeakReference<MainActivity> mActivity;

        public RefreshHandler(MainActivity activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity activity = mActivity.get();
            if (null != activity)
            {
                switch (msg.what)
                {
                    case REFRESH:
                        mAdapter.notifyCheckedDataSetChanged();
                        activity.swipe_refresh.setRefreshing(false);
                        break;
                    case LOADMORE:
                        mAdapter.notifyCheckedDataSetChanged();
                        activity.swipe_refresh.setLoadingMore(false);
                        break;
                    default:
                }
            }
        }
    }

    /**
     * 刷新数据
     */
    private void refreshData()
    {
        mItems.clear();
        for (int i = 0; i < 5; i++)
        {
            mItems.add("item" + mIndex++);
        }
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData()
    {
        for (int i = 0; i < 10; i++)
        {
            mItems.add("item" + mIndex++);
        }
    }
}

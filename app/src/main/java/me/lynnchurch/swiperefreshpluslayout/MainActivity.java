package me.lynnchurch.swiperefreshpluslayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private List<String> mItems = new ArrayList<>();
    private MenuItem menu_delete;
    private MenuItem menu_add;
    private CheckedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for (int i = 0; i < 5; i++)
        {
            mItems.add("item" + i);
        }
        mAdapter = new CheckedAdapter(mItems);
        mAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener()
        {
            @Override
            public void onItemLongClick(View v, int position)
            {
                if (!menu_delete.isVisible())
                {
                    mAdapter.showCheckedIcons((AppCompatCheckBox) v.findViewById(R.id.cb_select), position);
                    initMenuItems();
                }
            }
        });
        RecyclerView rv_items = (RecyclerView) findViewById(R.id.rv_items);
        rv_items.setAdapter(mAdapter);
        rv_items.setLayoutManager(new LinearLayoutManager(this));
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
                mAdapter.addItem();
                return true;
            case R.id.menu_delete:
                mAdapter.deleteCheckedItems();
                initMenuItems();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuItems()
    {
        menu_add.setVisible(!menu_add.isVisible());
        menu_delete.setVisible(!menu_delete.isVisible());
    }

    @Override
    public void onBackPressed()
    {
        if (menu_delete.isVisible())
        {
            initMenuItems();
            mAdapter.hideCheckedIcons();
        } else
        {
            super.onBackPressed();
        }
    }
}

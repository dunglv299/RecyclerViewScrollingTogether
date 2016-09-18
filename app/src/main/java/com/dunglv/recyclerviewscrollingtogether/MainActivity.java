package com.dunglv.recyclerviewscrollingtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_list)
    RecyclerView mainRecyclerView;
    @BindView(R.id.indexRecyclerView)
    RecyclerView indexRecyclerView;
    @BindView(R.id.headerRowRecyclerView)
    RecyclerView headerRowRecyclerView;
    private boolean isVerticalScrolling;
    private boolean isHorizontalScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int items[][];
        items = new int[50][10];

        int count = 0;
        for (int i = count; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                //                items[i][j] = (int) (Math.random() * 200);
                items[i][j] = count;
                count++;
            }
        }

        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainRecyclerView.setAdapter(new HorizontalListAdapter(items, new HorizontalListAdapter.HorizontalScrollListener() {
            @Override
            public void setScrollHorizontal(int dx, int dy) {
                if (!isHorizontalScrolling) {
                    //                    headerRowRecyclerView.scrollBy(dx, dy);
                    scrollHorizontalTogether(mainRecyclerView, dx, dy);
                }
            }
        }));
        mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isVerticalScrolling) {
                    scrollVerticalTogether(recyclerView, dx, dy);
                }
            }
        });
        // index column
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            indexList.add(i);
        }
        SimpleTextAdapter columnAdapter = new SimpleTextAdapter(indexList);
        indexRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        indexRecyclerView.setAdapter(columnAdapter);
        indexRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isVerticalScrolling) {
                    scrollVerticalTogether(recyclerView, dx, dy);
                }
            }
        });

        // Header mainRecyclerView
        List<Integer> headerList = new ArrayList<>();
        for (int i = 0; i < items[0].length; i++) {
            headerList.add(i);
        }
        SimpleTextAdapter headerAdapter = new SimpleTextAdapter(headerList);
        headerRowRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        headerRowRecyclerView.setAdapter(headerAdapter);
        headerRowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isHorizontalScrolling) {
                    scrollHorizontalTogether(recyclerView, dx, dy);
                }
            }
        });
    }

    private void scrollVerticalTogether(RecyclerView recyclerView, int dx, int dy) {
        isVerticalScrolling = true;
        if (indexRecyclerView != recyclerView) {
            indexRecyclerView.scrollBy(dx, dy);
        }
        if (mainRecyclerView != recyclerView) {
            mainRecyclerView.scrollBy(dx, dy);
        }
        isVerticalScrolling = false;
    }

    private void scrollHorizontalTogether(RecyclerView recyclerView, int dx, int dy) {
        isHorizontalScrolling = true;
        if (headerRowRecyclerView != recyclerView) {
            headerRowRecyclerView.scrollBy(dx, dy);
        }
        if (mainRecyclerView != recyclerView) {
            ((HorizontalListAdapter) mainRecyclerView.getAdapter()).scroll(dx, -1);
        }
        isHorizontalScrolling = false;
    }
}

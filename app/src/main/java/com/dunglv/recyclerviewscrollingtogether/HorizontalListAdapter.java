package com.dunglv.recyclerviewscrollingtogether;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ListViewHolder> {
    int channels[][];
    int offset;
    RecyclerView mainList;
    boolean scrolling = false;
    private HorizontalScrollListener listener;

    public HorizontalListAdapter(int channels[][], HorizontalScrollListener listener) {
        this.channels = channels;
        this.listener = listener;
    }

    public void scroll(int dx, int position) {
        scrolling = true;
        offset += dx;

        int count = mainList.getLayoutManager().getChildCount();

        for (int i = 0; i < count; i++) {
            ListViewHolder viewHolder = (ListViewHolder) mainList
                    .getLayoutManager().getChildAt(i).getTag();
            if (viewHolder.position != position) {
                viewHolder.programsHorizontalList.scrollBy(dx, 0);
            }
        }
        scrolling = false;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_row, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        int items[] = channels[position];
        holder.bind(items, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mainList = recyclerView;
    }

    @Override
    public int getItemCount() {
        return channels.length;
    }

    @Override
    public void onViewAttachedToWindow(final ListViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        scrolling = true;
        LinearLayoutManager layoutManager = (LinearLayoutManager) holder.programsHorizontalList.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, -offset);
        scrolling = false;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.horizontal_list)
        RecyclerView programsHorizontalList;

        int position = 0;

        LinearLayoutManager layoutManager;

        public ListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int items[], final int position) {
            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            this.position = position;
            programsHorizontalList.setLayoutManager(layoutManager);
            programsHorizontalList.clearOnScrollListeners();
            programsHorizontalList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!scrolling) {
                        listener.setScrollHorizontal(dx, dy);
                        scroll(dx, position);
                    }
                }
            });

            ProgramAdapter programAdapter = new ProgramAdapter(items);
            programsHorizontalList.setAdapter(programAdapter);
            itemView.setTag(this);
        }

        public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
            int items[];

            ProgramAdapter(int items[]) {
                this.items = items;
            }

            @Override
            public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false);
                return new ProgramViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(ProgramViewHolder holder, int position) {
                holder.bind(items[position]);
            }

            @Override
            public int getItemCount() {
                return items.length;
            }

            public class ProgramViewHolder extends RecyclerView.ViewHolder {
                @BindView(R.id.title)
                TextView title;

                public ProgramViewHolder(View itemView) {
                    super(itemView);
                    ButterKnife.bind(this, itemView);
                }

                public void bind(int i) {
                    String s = i + "";
                    title.setText(s);
                }

            }
        }
    }

    public interface HorizontalScrollListener {
        void setScrollHorizontal(int dx, int dy);
    }

}

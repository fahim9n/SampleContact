package com.fahim9n.sampleContact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {


    public static final int NORMAL = 1;
    public static final int FOOTER = 2;
    int i=1;

    private List<Contact> data;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<Contact> data) {
        this.data = data;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? FOOTER : NORMAL;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NORMAL:
                return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
            case FOOTER:
                return new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.footer_item, parent, false));
            default:
                throw new IllegalStateException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).mTitle.setText(data.get(holder.getAdapterPosition()).getName());
            ((ItemViewHolder) holder).mNumber.setText(data.get(holder.getAdapterPosition()).getPhoneNumber());
        }else{
            ((FooterViewHolder)holder).loadTv.setOnClickListener(getClickListener(((FooterViewHolder)holder).loadTv));
        }
    }

    @NonNull
    private View.OnClickListener getClickListener(final TextView loadTv) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Contact> contactList=((MainActivity)mContext).db.getNextTenContacts(i);

                if(contactList!=null){
                    data.addAll(contactList);
                    i++;
                    notifyDataSetChanged();
                }else{
                    loadTv.setText("No More Results to show");
                }


            }
        };
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle,mNumber;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvName);
            mNumber = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView loadTv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            loadTv = (TextView) itemView.findViewById(R.id.tvByWhom);

        }
    }


}

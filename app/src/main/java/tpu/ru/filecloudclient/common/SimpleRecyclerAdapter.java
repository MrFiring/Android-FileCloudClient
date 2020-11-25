package tpu.ru.filecloudclient.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tpu.ru.filecloudclient.PageFragment;
import tpu.ru.filecloudclient.R;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder> {

    private List<String> mItems;
    private PageFragment.OnListItemClickListener mItemClickListener;

    public SimpleRecyclerAdapter(List<String> items){
        if(items == null)
            mItems = new ArrayList<String>();
        else
            mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String itemTitle = mItems.get(position);
        holder.title.setText(itemTitle);
    }

    @Override
    public int getItemCount(){
        return mItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;

        public ViewHolder(View v){
            super(v);

            title= (TextView)v.findViewById(R.id.rc_item_title);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view){
            if(mItemClickListener != null)
                mItemClickListener.onListItemClick(mItems.get(getAdapterPosition()));
        }
    }

    public void setOnItemClickListener(PageFragment.OnListItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }
}

package com.matrixdeveloper.aivita.Discover.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.model.general.SearchData;

import java.util.ArrayList;


public class listAdapter extends RecyclerView.Adapter<listAdapter.viewHolder> {

    private ArrayList<SearchData> items;
    public Context context;


    public interface OnItemClickListener {
        void onItemClick(View view,int postion, SearchData item);
    }

    public listAdapter.OnItemClickListener listener;

    public listAdapter(Context context, ArrayList<SearchData> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_custom,parent,false);
        //rootView.setPadding(8,8,8,8);
        context = rootView.getContext();
        return new viewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder view, int position) {


        view.bind(position,items.get(position),listener);
        view.main_text.setText(items.get(position).getTag_name());
        view.sub_text.setText("");
        view.sub_text.setHint(items.get(position).getVideo_count()+" posts");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        private TextView main_text, sub_text;

        viewHolder(@NonNull View itemView) {
            super(itemView);

            main_text = itemView.findViewById(R.id.text_main);
            sub_text = itemView.findViewById(R.id.text_sub);

        }


        public void bind(final int pos , final SearchData item, final listAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });
        }
    }

}



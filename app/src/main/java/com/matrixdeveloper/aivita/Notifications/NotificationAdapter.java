package com.matrixdeveloper.aivita.Notifications;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;


        import com.matrixdeveloper.aivita.model.NotificationModel;

        import com.matrixdeveloper.aivita.R;


        import java.util.ArrayList;

        import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context myContext;
    private List<NotificationModel> mdata;
    private List<NotificationModel> leadListModelsFilter;

    public NotificationAdapter(Context myContext, List<NotificationModel> mdata) {
        this.myContext = myContext;
        this.mdata = mdata;
        leadListModelsFilter = new ArrayList<>();
        leadListModelsFilter.addAll(mdata);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.notification_listlayout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        Log.e("LeadListAdapter", mdata.get(i).toString());
        myViewHolder.title.setText(mdata.get(i).getNotificationTitle());
        myViewHolder.sender_id.setText(mdata.get(i).getDescription());
        myViewHolder.reciver_id.setText(mdata.get(i).getView());
        myViewHolder.datetxt.setText(mdata.get(i).getCreated());
//        myViewHolder.notification_image.setText(mdata.get(i).getGif());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, sender_id, reciver_id,notification_image,datetxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            sender_id = itemView.findViewById(R.id.sender_id);
            reciver_id = itemView.findViewById(R.id.receiver_id);
            datetxt=itemView.findViewById(R.id.datetxt);
//            notification_image=itemView.findViewById(R.id.notification_image);


        }
    }
}

package adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anamika.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ICS on 14/03/2018.
 */

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

    private static final String TAG = "AnnounceAdapter";
    private ArrayList<HashMap<String,String>> annlist;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ann_title,ann_des;
        CardView card_view;
        int pos;

        public ViewHolder(View view) {
            super(view);

            ann_title = (TextView) view.findViewById(R.id.ann_title);
            ann_des = (TextView)view.findViewById(R.id.ann_des);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;
    public AnnounceAdapter(Context mContext, ArrayList<HashMap<String, String>> ann_list) {
        context = mContext;
        annlist = ann_list;

    }

    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_announce_list, parent, false);

        return new AnnounceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AnnounceAdapter.ViewHolder viewHolder, int position) {
        final Map<String, String> mMap = annlist.get(position);
        viewHolder.ann_title.setText(mMap.get("notice_type"));
        viewHolder.ann_des.setText(mMap.get("notice_description"));
        viewHolder.card_view.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context,DineActivity.class);
                // intent.putExtra("followlist",mMap.get("enqTime"));
                context.startActivity(intent);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return annlist.size();
    }

}

package android.example.com.newsolutionbodymanager.history;

import android.content.Context;
import android.example.com.newsolutionbodymanager.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HistorySportAdapter extends FirestoreRecyclerAdapter<SportHistory, HistorySportAdapter.HistoryHolder> {
    private HistorySportAdapter.OnItemClickListener listener;
    private Context context;
    private static final String TAG = "DetailSportAct";
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public HistorySportAdapter(@NonNull FirestoreRecyclerOptions<SportHistory> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistorySportAdapter.HistoryHolder holder, int position, @NonNull SportHistory model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText(String.valueOf(model.getCalorie()));
    }

    @NonNull
    @Override
    public HistorySportAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_sport,
                parent, false);
        return new HistorySportAdapter.HistoryHolder(v);
    }


    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        public HistoryHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_food_name);
            textViewDescription = itemView.findViewById(R.id.tv_calorie_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(HistorySportAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
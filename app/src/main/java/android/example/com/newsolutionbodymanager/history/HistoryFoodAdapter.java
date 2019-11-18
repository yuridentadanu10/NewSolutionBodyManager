package android.example.com.newsolutionbodymanager.history;

import android.content.Context;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.recyclerView.Food;
import android.example.com.newsolutionbodymanager.recyclerView.FoodAdapter;
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

public class HistoryFoodAdapter extends FirestoreRecyclerAdapter<FoodHistory, HistoryFoodAdapter.HistoryHolder> {
    private HistoryFoodAdapter.OnItemClickListener listener;
    private Context context;
    private static final String TAG = "DetailFoodAct";
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public HistoryFoodAdapter(@NonNull FirestoreRecyclerOptions<FoodHistory> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryFoodAdapter.HistoryHolder holder, int position, @NonNull FoodHistory model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText("+ "+String.valueOf(model.getCalorie())+" Cal");
        holder.textViewWaktuMakan.setText(model.getWaktuMakan());
        Picasso.get()
                .load(model.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgFood);
    }

    @NonNull
    @Override
    public HistoryFoodAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,
                parent, false);
        return new HistoryFoodAdapter.HistoryHolder(v);
    }


    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription,textViewWaktuMakan;
        ImageView imgFood;

        public HistoryHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_food_name);
            textViewDescription = itemView.findViewById(R.id.tv_calorie_info);
            imgFood = itemView.findViewById(R.id.img_food);
            textViewWaktuMakan=itemView.findViewById(R.id.tv_waktu_makan);

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

    public void setOnItemClickListener(HistoryFoodAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
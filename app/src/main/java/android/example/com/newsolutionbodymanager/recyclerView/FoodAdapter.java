package android.example.com.newsolutionbodymanager.recyclerView;

import android.content.Context;
import android.example.com.newsolutionbodymanager.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FoodAdapter extends FirestoreRecyclerAdapter<Food, FoodAdapter.FoodHolder> {
    private OnItemClickListener listener;
    private Context context;
    private static final String TAG = "DetailFoodAct";
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public FoodAdapter(@NonNull FirestoreRecyclerOptions<Food> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodHolder holder, int position, @NonNull Food model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText(String.valueOf(model.getCalorie()));
        Picasso.get()
                .load(model.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgFood);


    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent, false);
        return new FoodHolder(v);
    }


    class FoodHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imgFood;

        public FoodHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_food_name);
            textViewDescription = itemView.findViewById(R.id.tv_calorie_info);
            imgFood = itemView.findViewById(R.id.img_food);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
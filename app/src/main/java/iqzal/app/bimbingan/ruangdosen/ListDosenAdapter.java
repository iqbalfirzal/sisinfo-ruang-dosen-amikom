package iqzal.app.bimbingan.ruangdosen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListDosenAdapter extends RecyclerView.Adapter<ListDosenAdapter.MyViewHolder> {
    ArrayList<ListDosen> list;
    public ListDosenAdapter(ArrayList<ListDosen> list){
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ppurl = list.get(position).getPpurl();
        Glide.with(holder.itemView.getContext()).setDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.default_user).error(R.drawable.default_user)).load(ppurl).into(holder.foto);
        holder.nama.setText(list.get(position).getFullname());
        holder.diruangan.setText(list.get(position).getDiruangan());
        holder.ruang.setText(list.get(position).getRuang());
        holder.waktu.setText("diupdate : "+list.get(position).getWaktu());
        holder.catatan.setText(list.get(position).getCatatan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama,diruangan,ruang,waktu, catatan;
        CircleImageView foto;
        LinearLayout hidden_layout, trigger;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            foto = itemView.findViewById(R.id.cardFotoDosen);
            nama = itemView.findViewById(R.id.cardNamaDosen);
            diruangan = itemView.findViewById(R.id.cardPosisiDosen);
            ruang = itemView.findViewById(R.id.cardRuangDosen);
            waktu = itemView.findViewById(R.id.cardWaktu);
            catatan = itemView.findViewById(R.id.cardCatatan);
            hidden_layout = itemView.findViewById(R.id.hidden_layout);
            trigger = itemView.findViewById(R.id.cardMain);
            trigger.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (hidden_layout.getVisibility() == View.GONE) {
                hidden_layout.animate()
                        .translationY(hidden_layout.getBaseline()).alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                hidden_layout.setVisibility(View.VISIBLE);
                                hidden_layout.setAlpha(0.0f);
                            }
                        });
            } else {
                hidden_layout.setVisibility(View.GONE);
                TranslateAnimation animate = new TranslateAnimation(0,0, hidden_layout.getHeight(), 0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                hidden_layout.startAnimation(animate);
            }
        }
    }


}
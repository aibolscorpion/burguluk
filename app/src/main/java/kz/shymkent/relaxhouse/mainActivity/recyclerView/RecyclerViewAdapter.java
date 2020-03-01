package kz.shymkent.relaxhouse.mainActivity.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import kz.shymkent.relaxhouse.R;
import kz.shymkent.relaxhouse.databinding.ItemClientBinding;
import kz.shymkent.relaxhouse.models.Client;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    private final static int FADE_DURATION = 500;
    DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("EE'\n'd MMM");
    List<Client> clientList  = new ArrayList<>();
    public RecyclerViewAdapter(Context context){
        this.context = context;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList.clear();
        this.clientList.addAll(clientList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientBinding clientItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_client,parent,false);
        return new ViewHolder(clientItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        setFadeAnimation(holder.itemView);
        Client client = Client.copy(clientList.get(position));

        LocalDate checkInDate = LocalDate.parse(client.getCheckInDate(),dateTimeFormatter1);
        String checkInDateString = dateTimeFormatter2.format(checkInDate)+"\n"+client.getCheckInTime();
        client.setCheckInTime(checkInDateString);

        LocalDate checkOutDate = LocalDate.parse(client.getCheckOutDate(),dateTimeFormatter1);
        String checkOutDateString = dateTimeFormatter2.format(checkOutDate)+"\n"+client.getCheckOutTime();
        client.setCheckOutTime(checkOutDateString);

        holder.clientItemBinding.setClient(client);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount() {
        return (clientList == null) ? 0 : clientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ItemClientBinding clientItemBinding;
        ViewHolder(@NonNull ItemClientBinding clientItemBinding) {
            super(clientItemBinding.getRoot());
            this.clientItemBinding = clientItemBinding;
        }
    }
}

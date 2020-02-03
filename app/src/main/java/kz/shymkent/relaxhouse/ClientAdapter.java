package kz.shymkent.relaxhouse;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import kz.shymkent.relaxhouse.databinding.ClientItemBinding;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    Context context;
    DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("EEE'\n'd MMM");
    List<Client> clientList  = new ArrayList<>();
    public ClientAdapter(Context context){
        this.context = context;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList.clear();
        this.clientList.addAll(clientList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClientItemBinding clientItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.client_item,parent,false);
        return new ViewHolder(clientItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.ViewHolder holder, int position) {
        Client client = Client.copy(clientList.get(position));

        LocalDate checkInDate = LocalDate.parse(client.getCheckInDate(),dateTimeFormatter1);
        String checkInDateString = dateTimeFormatter2.format(checkInDate)+"\n"+client.getCheckInTime();
        client.setCheckInTime(checkInDateString);

        LocalDate checkOutDate = LocalDate.parse(client.getCheckOutDate(),dateTimeFormatter1);
        String checkOutDateString = dateTimeFormatter2.format(checkOutDate)+"\n"+client.getCheckOutTime();
        client.setCheckOutTime(checkOutDateString);

        holder.clientItemBinding.setClient(client);
    }

    @Override
    public int getItemCount() {
        return (clientList == null) ? 0 : clientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ClientItemBinding clientItemBinding;
        ViewHolder(@NonNull ClientItemBinding clientItemBinding) {
            super(clientItemBinding.getRoot());
            this.clientItemBinding = clientItemBinding;
        }
    }
}

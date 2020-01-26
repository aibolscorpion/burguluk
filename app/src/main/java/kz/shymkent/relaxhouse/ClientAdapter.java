package kz.shymkent.relaxhouse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.shymkent.relaxhouse.databinding.ClientItemBinding;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    Context context;
    List<Client> clientList;
    public ClientAdapter(Context context){
        this.context = context;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
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
        Client client = clientList.get(position);
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

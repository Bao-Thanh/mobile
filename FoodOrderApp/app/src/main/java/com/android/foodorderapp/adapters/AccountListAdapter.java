package com.android.foodorderapp.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.model.Menu;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder>{

    private List<Menu> menuList;
    private AccountListAdapter.AccountListClickListener clickListener;

    public AccountListAdapter(List<Menu> menuList, AccountListAdapter.AccountListClickListener clickListener) {
        this.menuList = menuList;
        this.clickListener = clickListener;
    }

    public void updateData(List<Menu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface AccountListClickListener {
    }
}

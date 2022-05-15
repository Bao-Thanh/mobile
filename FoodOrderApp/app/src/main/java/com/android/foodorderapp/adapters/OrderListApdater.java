package com.android.foodorderapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.MainActivity;
import com.android.foodorderapp.R;
import com.android.foodorderapp.ViewOrderDetail;
import com.android.foodorderapp.model.Orders;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.profile.OrderHistory;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class OrderListApdater extends RecyclerView.Adapter<OrderListApdater.MyViewHolder> {
    Context context;
    ArrayList<Orders> list;
    private List<Menu> listExtra = new ArrayList<Menu>();

    public OrderListApdater(Context context, ArrayList<Orders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.recycle_row_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Orders order = list.get(position);
        //status
        listExtra = order.getMenu();
        String isFinish = String.valueOf(order.getIsFinish());
        String orderid = order.getOrderid();
        String userid = order.getCustomerid();
        String sstatus = order.getStatus();
        String sfname = order.getFname();
        String sstk = order.getSstk();
        String sphno = order.getPhno();
        String semail = order.getEmail();
        String stotalPrice = String.valueOf(order.getTotalPrice());
        String ssubtotalPrice = String.valueOf(order.getSubTotal());
        String sdeliveryPrice = String.valueOf(order.getFdelivery());
        String sotp = order.getOtp();
        //When delivery
        String stp = order.getThanhpho();
        String sqh = order.getQuanhuyen();
        String ssn = order.getSonha();
        holder.restaurantName.setText(order.getOrderat());
        holder.dateorder.setText("Ngày: " + order.getDateorder());
        holder.totalAmount.setText("Giá: " + String.valueOf(toVND(order.getTotalPrice())) + " VND");
        holder.statusOrder.setText("Hình thức: " + order.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click
                Intent i = new Intent(context, ViewOrderDetail.class);
                i.putExtra("isFinish", isFinish);
                i.putExtra("orderid",orderid);
                i.putExtra("userid", userid);
                i.putExtra("fname", sfname);
                i.putExtra("sstk", sstk);
                i.putExtra("phno", sphno);
                i.putExtra("email", semail);
                i.putExtra("thanhpho", stp);
                i.putExtra("quanhuyen", sqh);
                i.putExtra("sonha", ssn);
                i.putExtra("status", sstatus);
                i.putExtra("subTotal", ssubtotalPrice);
                i.putExtra("totalPrice", stotalPrice);
                i.putExtra("fdelivery", sdeliveryPrice);
                i.putExtra("otp", sotp);
                context.startActivity(i);
            }
        });
        Glide.with(holder.thumbImage)
                .load(list.get(position).getImage())
                .into(holder.thumbImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Convert to VND format
    public String toVND(float value) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(value);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView restaurantName;
        TextView  dateorder;
        TextView  totalAmount;
        TextView statusOrder;
        ImageView thumbImage;
        public MyViewHolder(@NonNull View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantNameOrder);
            dateorder = view.findViewById(R.id.timeOrder);
            totalAmount = view.findViewById(R.id.totalamountOrder);
            statusOrder = view.findViewById(R.id.statusOrder);
            thumbImage = view.findViewById(R.id.thumbImageOrder);

        }
    }
}

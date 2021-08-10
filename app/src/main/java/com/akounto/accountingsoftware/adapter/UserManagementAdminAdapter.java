package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.Users;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

public class UserManagementAdminAdapter extends RecyclerView.Adapter<UserManagementAdminAdapter.MyViewHolder> {

    private Context context;
    private UserManagementAdminItemClick onClickListener;
    private final List<Users> usersList;

    public UserManagementAdminAdapter(Context context2, List<Users> users, UserManagementAdminItemClick onClickListener2) {
        this.context = context2;
        this.usersList = users;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_mgmt_admin_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder vh = holder;
        Users user = this.usersList.get(position);
        TextView textView = vh.name;
        textView.setText(user.getFirstName() + " " + user.getLastName());
        vh.email.setText(user.getEmail());
        vh.role.setText(user.getRole());
        Log.e("User Email ::", UiUtil.getUserName(context));
        if (user.isOwner() || user.getEmail().equalsIgnoreCase(AppSingle.getInstance().getEmail())) {
            vh.edit.setEnabled(false);
            vh.edit.setAlpha(128);
            vh.delete.setEnabled(false);
            vh.delete.setAlpha(.5f);
        } else {
            vh.edit.setEnabled(true);
            vh.edit.setAlpha(255);
            vh.delete.setEnabled(true);
            vh.delete.setAlpha(255);
        }
        vh.edit.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onClickListener.editAdmin(user);;
            }
        });

        vh.delete.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onClickListener.deleteAdmin(user);;
            }
        });
    }

    public int getItemCount() {
        return this.usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView edit;
        ImageView delete;
        TextView email;
        TextView name;
        TextView role;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
            this.role = itemView.findViewById(R.id.role);
            this.delete = itemView.findViewById(R.id.iv_delete);
            this.edit = itemView.findViewById(R.id.editButton);
        }
    }
}

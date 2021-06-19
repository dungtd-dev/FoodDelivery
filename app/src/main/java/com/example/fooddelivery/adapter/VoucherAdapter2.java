package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import es.dmoral.toasty.Toasty;


public class VoucherAdapter2 extends RecyclerView.Adapter<VoucherAdapter2.VoucherViewHolder2> {
    Context context;

    public VoucherAdapter2() {
    }

    public VoucherAdapter2(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_template_2, parent, false);
        return new VoucherViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder2 holder, int position) {
        Voucher voucher = LoginActivity.firebase.availableVoucherList.get(position);
        setData(holder, voucher);
        setClickDetails(holder, voucher);
    }

    private void setClickDetails(VoucherViewHolder2 holder, Voucher voucher) {
        holder.ib_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialog(voucher);
            }
        });

        holder.ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = holder.ib_add.getText().toString();
                if (status.equals(context.getResources().getString(R.string.already_got))) {
                    Toasty.warning(context, R.string.exist_voucher).show();
                } else {
                    addToVoucherList(voucher);
                    holder.ib_add.setText(R.string.already_got);
                }
            }
        });
    }

    private void addToVoucherList(Voucher v) {
        LoginActivity.firebase.addVoucherToList(context, v.getId());
        v.setStatus("Hiện có");
        LoginActivity.firebase.voucherList.add(v);
    }

    private void setData(VoucherViewHolder2 holder, Voucher v) {
        holder.imgVoucher.setImageResource(R.drawable.voucher);
        holder.tv_title.setText(v.getTitle());
        holder.tv_date.setText(String.format("%s %s", context.getResources().getString(R.string.HSD), v.getDate()));
        for (Voucher voucher : LoginActivity.firebase.voucherList) {
            if (v.getCode().equals(voucher.getCode())) {
                holder.ib_add.setText(R.string.already_got);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return LoginActivity.firebase.availableVoucherList != null ?
                LoginActivity.firebase.availableVoucherList.size() : 0;
    }

    public final class VoucherViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imgVoucher;
        TextView tv_title, tv_date, ib_add;
        ImageButton ib_details;

        public VoucherViewHolder2(@NonNull View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.vc2_iv_image);
            tv_title = itemView.findViewById(R.id.vc2_tv_title);
            tv_date = itemView.findViewById(R.id.vc2_tv_date);
            ib_details = itemView.findViewById(R.id.vc2_ib_details);
            ib_add = itemView.findViewById(R.id.vc2_ib_add);

            ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    private void clickOpenBottomSheetDialog(Voucher v) {
        View dialog = LayoutInflater.from(context).inflate(R.layout.voucher_bottom_sheet, null);
        BottomSheetDialog voucherDialog = new BottomSheetDialog(context);
        voucherDialog.setContentView(dialog);
        voucherDialog.show();
        InitDialog(dialog, v);
    }

    private void InitDialog(View dialog, Voucher v) {
        TextView tv_title, tv_code, tv_date, tv_details;
        tv_title = dialog.findViewById(R.id.vc_bs_tv_title);
        tv_code = dialog.findViewById(R.id.vc_bs_tv_code);
        tv_date = dialog.findViewById(R.id.vc_bs_tv_date);
        tv_details = dialog.findViewById(R.id.vc_bs_tv_details);

        tv_title.setText(v.getTitle());
        tv_code.setText(v.getCode());
        tv_date.setText(v.getDate());

        String details = "";
        for (String s : v.getDetails()) {
            details += "•  " + s + "\n\n";
        }
        tv_details.setText(details);
    }
}

package com.akounto.accountingsoftware.Activity.Bill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.TaxtAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.response.Taxs.TaxsResponse;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EditBillItem extends AppCompatActivity {

    private EditText name, description, qty, price;
    private TextView adds_item_price, adds_item_name, spinner_taxtes;
    private Context mContext;
    public List<TaxResponse> taxReceivedList = new ArrayList<>();
    private List<ProductServiceTaxesItem> taxs;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        mContext = this;
        name = findViewById(R.id.item_name);
        UiUtil.disableEditText(name);
        description = findViewById(R.id.item_description);
        adds_item_price = findViewById(R.id.edit_item_price);
        qty = findViewById(R.id.ed_qty);
        price = findViewById(R.id.ed_price);
        spinner_taxtes = findViewById(R.id.spinner_taxtes);
        adds_item_name = findViewById(R.id.edit_item_name);
        try {
            spinner_taxtes.setText("Taxs + " + CreateBill.edit_item.getProductServiceTaxes().size());
            name.setText(CreateBill.edit_item.getName());
            adds_item_name.setText(CreateBill.edit_item.getName());
            description.setText(CreateBill.edit_item.getDescription());

            try {
                qty.setText(CreateBill.edit_item.getQuantity() + "");
            } catch (Exception e) {
            }

            price.setText(CreateBill.edit_item.getPrice() + "");
            adds_item_price.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) * Double.parseDouble(price.getText().toString())));
        } catch (Exception e) {
        }

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "");
                setResult(16, intent);
                finish();//finishing activity
            }
        });
        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equalsIgnoreCase(""))
                    CreateBill.edit_item.setName(name.getText().toString());
                if (!description.getText().toString().equalsIgnoreCase(""))
                    CreateBill.edit_item.setDescription(description.getText().toString());
                if (!qty.getText().toString().equalsIgnoreCase(""))
                    CreateBill.edit_item.setQuantity(Integer.parseInt(qty.getText().toString()));
                else
                    CreateBill.edit_item.setQuantity(1);
                if (!price.getText().toString().equalsIgnoreCase(""))
                    CreateBill.edit_item.setPrice(Double.parseDouble(price.getText().toString()));
                if (CreateBill.edit_item.getProductId() == 0)
                    CreateBill.edit_item.setProductId(CreateBill.edit_item.getId());
                lunch();
            }
        });
        spinner_taxtes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtaxs(UiUtil.makeCompatible(UiUtil.changeTaxResponse(taxReceivedList), CreateBill.edit_item.getProductServiceTaxes()));
            }
        });
        watcher();
        getTaxList();
    }

    private void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        setResult(15, intent);
        finish();//finishing activity
    }

    private void calPriceWithPrice() {
        try {
            String q, p;
            if (!qty.getText().toString().equalsIgnoreCase(""))
                q = qty.getText().toString();
            else
                q = "0";
            if (!price.getText().toString().equalsIgnoreCase(""))
                p = price.getText().toString();
            else
                p = "0";
            adds_item_price.setText(String.valueOf(Integer.parseInt(q) * Double.parseDouble(p)));
        } catch (Exception e) {
        }
    }

    private void calPriceWithQty() {
        try {
            String q, p;
            if (!qty.getText().toString().equalsIgnoreCase(""))
                q = qty.getText().toString();
            else
                q = "0";
            if (!price.getText().toString().equalsIgnoreCase(""))
                p = price.getText().toString();
            else
                p = "0";
            adds_item_price.setText(String.valueOf(Integer.parseInt(q) * Double.parseDouble(p)));
        } catch (Exception e) {
        }
    }

    private void watcher() {
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calPriceWithQty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calPriceWithPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adds_item_name.setText(name.getText().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getTaxList() {
        RestClient.getInstance((Activity) this).getTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, (String) null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                try {
                    Log.d("getTaxList----", new Gson().toJson((Object) response.body().getData()));
                    if (response.isSuccessful()) {
                        EditBillItem.this.taxReceivedList = response.body().getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
    public void showtaxs(List<ProductServiceTaxesItem> list) {
        try {
            Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.dilog_tax_display);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RecyclerView rc = (RecyclerView) dialog.findViewById(R.id.rc_tax);
            rc.setHasFixedSize(true);
            rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            TaxtAdapter ta = new TaxtAdapter(mContext, list);
            rc.setAdapter(ta);
            Button btn_create_new = (Button) dialog.findViewById(R.id.btn_create_new);
            ImageView btn_cancel = (ImageView) dialog.findViewById(R.id.btn_cancel);
            Button btn_apply_tax = (Button) dialog.findViewById(R.id.btn_apply_tax);
            btn_create_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUpWindow(new TaxSelected() {
                        @Override
                        public void onTaxSelected(ProductServiceTaxesItem tax) {
                            list.add(tax);
                            rc.removeAllViews();
                            TaxtAdapter taz = new TaxtAdapter(mContext, list);
                            rc.setAdapter(taz);
                        }
                    });
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_apply_tax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        taxs=ta.getItemList();
                        CreateBill.edit_item.setProductServiceTaxes(taxs);
                        spinner_taxtes.setText("Taxts + " + CreateBill.edit_item.getProductServiceTaxes().size());
                    } catch (Exception e) {
                        spinner_taxtes.setText("Taxts + " + taxs.size());
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
        }
    }
    /*public void showtaxs(List<ProductServiceTaxesItem> list) {
        try {
            Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.dilog_tax_display);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RecyclerView rc = (RecyclerView) dialog.findViewById(R.id.rc_tax);
            rc.setHasFixedSize(true);
            rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            TaxtAdapter ta = new TaxtAdapter(mContext, list);
            rc.setAdapter(ta);
            Button btn_create_new = (Button) dialog.findViewById(R.id.btn_create_new);
            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
            Button btn_apply_tax = (Button) dialog.findViewById(R.id.btn_apply_tax);
            btn_create_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_apply_tax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    CreateBill.edit_item.setProductServiceTaxes(ta.getItemList());
                    spinner_taxtes.setText("Taxts + " + CreateBill.edit_item.getProductServiceTaxes().size());
                }
            });
            dialog.show();
        } catch (Exception e) {
        }
    }*/

    public void showPopUpWindow(TaxSelected tax) {
        Dialog dialog2 = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog2.requestWindowFeature(1);
        dialog2.setContentView(R.layout.add_salestax_popup_layout);
        dialog2.setCancelable(true);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog2.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.findViewById(R.id.isCompoundTax).setVisibility(View.GONE);
        ((RadioButton) dialog2.findViewById(R.id.isRecoverableTax)).setChecked(true);
        dialog2.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                String taxName = ((EditText) dialog2.findViewById(R.id.et_taxName)).getText().toString().trim();
                String taxDesc = ((EditText) dialog2.findViewById(R.id.et_taxDesc)).getText().toString().trim();
                String taxNumber = ((EditText) dialog2.findViewById(R.id.et_taxNumber)).getText().toString().trim();
                String taxRate = ((EditText) dialog2.findViewById(R.id.et_taxRate)).getText().toString().trim();
                ArrayList<EffectiveTaxesItem> effectiveTaxesItems = new ArrayList<>();
                effectiveTaxesItems.add(new EffectiveTaxesItem(Double.parseDouble(taxRate)));
                ArrayList<EffectiveTaxesItem> arrayList = effectiveTaxesItems;
                RestClient.getInstance(mContext).addSaleTaxDilog(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), new AddSaleTaxRequest(taxName, taxDesc, false, true, taxNumber, effectiveTaxesItems)).enqueue(new CustomCallBack<TaxsResponse>(mContext, "Adding Sale tax...") {
                    public void onResponse(Call<TaxsResponse> call, Response<TaxsResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(mContext, "Sale Tax added!");
                            dialog2.dismiss();
                            ProductServiceTaxesItem taxesItem = new ProductServiceTaxesItem();
                            taxesItem.setTaxName(response.body().getData().getName());
                            taxesItem.setName(response.body().getData().getName());
                            taxesItem.setRate(response.body().getData().getEffectiveTaxes().get(0).getRate());
                            taxesItem.setTaxId(response.body().getData().getId());
                            taxesItem.setSelected(true);
                            tax.onTaxSelected(taxesItem);
                            TaxResponse taxesm = new TaxResponse();
                            taxesm.setName(response.body().getData().getName());
                            taxesm.setEffectiveTaxes(response.body().getData().getEffectiveTaxes());
                            taxesm.setId(response.body().getData().getId());
                            taxReceivedList.add(taxesm);
                            //SaleTaxesFragment.this.getSalesTaxList();
                        }
                    }

                    public void onFailure(Call<TaxsResponse> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }
        });
        dialog2.show();
    }

    public interface TaxSelected {
        void onTaxSelected(ProductServiceTaxesItem tax);
    }
}

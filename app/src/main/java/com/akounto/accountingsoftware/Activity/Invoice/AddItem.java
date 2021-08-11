package com.akounto.accountingsoftware.Activity.Invoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.util.DecimalDigitsInputFilter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class AddItem extends AppCompatActivity {

    private EditText name, description, qty, price, discount;
    private TextView adds_item_price, adds_item_name, spinner_taxtes,discount_error;
    private Context mContext;
    public List<TaxResponse> taxReceivedList = new ArrayList<>();
    List<ProductServiceTaxesItem> taxs;
    Spinner discount_spi;
    int discount_index = 0;
    String[] discount_type_data = {"Please select type", "Percent discount", "Flat discount"};

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mContext = this;
        name = findViewById(R.id.item_name);
        UiUtil.disableEditText(name);
        EditInvoice.start=true;
        discount_error= findViewById(R.id.discount_error);
        description = findViewById(R.id.item_description);
        adds_item_price = findViewById(R.id.edit_item_price);
        discount = findViewById(R.id.discount);
        discount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(25, 2)});
        discount_spi = findViewById(R.id.discount_type);
        qty = findViewById(R.id.ed_qty);
        price = findViewById(R.id.ed_price);
        spinner_taxtes = findViewById(R.id.spinner_taxtes);
        adds_item_name = findViewById(R.id.edit_item_name);
        spinner_taxtes.setText("Taxs + " + ItemList.adds_item.getProductServiceTaxes().size());
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, discount_type_data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        discount_spi.setAdapter(aa);
        discount_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discount_index = position;
                discount.setText("");
                discount_error.setVisibility(View.GONE);
                if (position == 1) {
                    discount.setHint("Percent");
                    ItemList.adds_item.setDiscountType(2);
                } else if (position == 2) {
                    discount.setHint("Flat");
                    ItemList.adds_item.setDiscountType(1);
                } else {
                    discount.setHint("discount");
                    ItemList.adds_item.setDiscountType(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            name.setText(ItemList.adds_item.getName());
            adds_item_name.setText(ItemList.adds_item.getName());
            description.setText(ItemList.adds_item.getDescription());
            if (!qty.getText().toString().equalsIgnoreCase(""))
                qty.setText(ItemList.adds_item.getQty());
            else
                qty.setText("1");

            price.setText(ItemList.adds_item.getPrice() + "");
            adds_item_price.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) * Double.parseDouble(price.getText().toString())));
        } catch (Exception e) {
        }

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equalsIgnoreCase(""))
                    ItemList.adds_item.setName(name.getText().toString());
                if (!description.getText().toString().equalsIgnoreCase(""))
                    ItemList.adds_item.setDescription(description.getText().toString());
                if (!qty.getText().toString().equalsIgnoreCase(""))
                    ItemList.adds_item.setQty(qty.getText().toString());
                else
                    ItemList.adds_item.setQty("1");
                if (!price.getText().toString().equalsIgnoreCase(""))
                    ItemList.adds_item.setPrice(Double.parseDouble(price.getText().toString()));
                if (ItemList.adds_item.getProductId() == 0) {
                    ItemList.adds_item.setProductId(ItemList.adds_item.getId());
                }
                if (ItemList.adds_item.getDiscountType() != 0) {
                    if (!discount.getText().toString().isEmpty()) {
                        try {
                            ItemList.adds_item.setDiscount(Integer.parseInt(discount.getText().toString()));
                            //setDiscountedPrice(ItemList.adds_item.getDiscountType(), discount.getText().toString(), price.getText().toString());
                            lunch();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Discount value is empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    ItemList.adds_item.setPriceAfterDiscount(Float.parseFloat(price.getText().toString()));
                    lunch();
                }
            }
        });
        spinner_taxtes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtaxs(UiUtil.makeCompatible(UiUtil.changeTaxResponse(taxReceivedList), ItemList.adds_item.getProductServiceTaxes()));
            }
        });
        watcher();
        getTaxList();
    }

    private void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "AddCustomers.result_add.getData().getName()");
        setResult(4, intent);
        finish();//finishing activity
    }

    private void setDiscountedPrice(int type, String value, String amount) {
        if (type == 2) {
            try {
                float am = Float.parseFloat(amount);
                float val = Float.parseFloat(value);
                ItemList.adds_item.setPriceAfterDiscount(am - ((am * val) / 100f));
            } catch (Exception e) {
            }
        } else if(type==1){
            float am = Float.parseFloat(amount);
            float val = Float.parseFloat(value);
            ItemList.adds_item.setPriceAfterDiscount(am - val);
        }else{
            float am = Float.parseFloat(amount);
            ItemList.adds_item.setPriceAfterDiscount(am);
        }
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
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                        s.clear();
                    }
                    if (discount.getHint().toString().equalsIgnoreCase("Percent")) {
                        if (Double.parseDouble(discount.getText().toString()) > 100) {
                            discount.setText("");
                            discount_error.setVisibility(View.VISIBLE);
                            //Toast.makeText(AddItem.this, " Item discount cannot be applied more than amount!", Toast.LENGTH_SHORT).show();
                        }else{
                            discount_error.setVisibility(View.GONE);
                        }
                    } else if (discount.getHint().toString().equalsIgnoreCase("Flat")) {
                        if (Double.parseDouble(discount.getText().toString()) > Double.parseDouble(price.getText().toString())) {
                            discount.setText("");
                            discount_error.setVisibility(View.VISIBLE);
                            //Toast.makeText(AddItem.this, " Item discount cannot be applied more than amount!", Toast.LENGTH_SHORT).show();
                        }else{
                            discount_error.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                }
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
                        AddItem.this.taxReceivedList = response.body().getData();
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
                        taxs = ta.getItemList();
                        ItemList.adds_item.setProductServiceTaxes(taxs);
                        spinner_taxtes.setText("Taxts + " + ItemList.adds_item.getProductServiceTaxes().size());
                    } catch (Exception e) {
                        spinner_taxtes.setText("Taxts + " + taxs.size());
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
        }
    }

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
                try {
                    taxName = String.valueOf(taxName.charAt(0)).toUpperCase() + taxName.substring(1, taxName.length()).toLowerCase();
                } catch (Exception e) {
                }
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
                            try {
                                UiUtil.showToast(mContext, "Sale Tax added!");
                                dialog2.dismiss();
                                ProductServiceTaxesItem taxesItem = new ProductServiceTaxesItem();
                                taxesItem.setTaxName(response.body().getData().getName());
                                taxesItem.setName(response.body().getData().getName());
                                taxesItem.setRate(response.body().getData().getEffectiveTaxes().get(0).getRate());
                                taxesItem.setTaxId(response.body().getData().getId());
                                tax.onTaxSelected(taxesItem);
                                TaxResponse taxesm = new TaxResponse();
                                taxesm.setName(response.body().getData().getName());
                                taxesm.setEffectiveTaxes(response.body().getData().getEffectiveTaxes());
                                taxesm.setId(response.body().getData().getId());
                                taxReceivedList.add(taxesm);
                                //SaleTaxesFragment.this.getSalesTaxList();
                            } catch (Exception e) {
                            }
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

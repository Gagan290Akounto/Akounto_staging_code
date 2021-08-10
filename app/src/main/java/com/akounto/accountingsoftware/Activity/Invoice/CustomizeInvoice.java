package com.akounto.accountingsoftware.Activity.Invoice;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.RadioData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.databinding.LayoutCustomizeInvoiceBinding;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ReportSettingRequest;
import com.akounto.accountingsoftware.response.ReportSetting.ReportSettings;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class CustomizeInvoice extends Fragment {

    private LayoutCustomizeInvoiceBinding binding;
    private int type = 1;
    private int templeat = 0;
    private int colr = 0;
    private ReportSettings settings = null;
    public ArrayList<String> array_due_time = new ArrayList<>();
    public static ImageView logo;
    public String image_base64 = "";
    int selectedDuePayment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_customize_invoice, container, false);
        getReportSetting();

        logo = binding.ivLogo;
        itemlistner();

        binding.logoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
                //Intent i = new Intent(CustomizeInvoice.this,ActivityImagePicker.class);
                //startActivity(new Intent(getContext(), ActivityImagePicker.class));
              /*  ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();*/
            }
        });
        binding.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePreview();
            }
        });
        binding.layoutBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colr = 0;
                setColor();
            }
        });
        binding.layoutGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colr = 1;
                setColor();
            }
        });
        binding.layoutPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colr = 2;
                setColor();
            }
        });
        binding.layoutRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colr = 3;
                setColor();
            }
        });
        binding.layoutYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colr = 4;
                setColor();
            }
        });

        binding.layoutOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templeat = 0;
                setTempleat();
            }
        });
        binding.layoutTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templeat = 1;
                setTempleat();
            }
        });
        binding.layoutThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templeat = 2;
                setTempleat();
            }
        });
        binding.btnStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                setButton();
            }
        });

        binding.btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                setButton();
            }
        });

        binding.btnColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 3;
                setButton();
            }
        });
        setarray();
        binding.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateReportSetting();
                } catch (Exception e) {
                    Log.e("Error :: ", e.getMessage());
                }
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, SettingMenu.class);
            }
        });
        return binding.getRoot();
    }

    private void setImages() {
        switch (colr) {
            case 0:
                binding.ivOne.setImageResource(R.drawable.classical_1);
                binding.ivTwo.setImageResource(R.drawable.default_1);
                binding.ivThree.setImageResource(R.drawable.minimal_1);
                break;
            case 1:
                binding.ivOne.setImageResource(R.drawable.classical_2);
                binding.ivTwo.setImageResource(R.drawable.default_2);
                binding.ivThree.setImageResource(R.drawable.minimal_2);
                break;
            case 2:
                binding.ivOne.setImageResource(R.drawable.classical_3);
                binding.ivTwo.setImageResource(R.drawable.default_3);
                binding.ivThree.setImageResource(R.drawable.minimal_3);
                break;
            case 3:
                binding.ivOne.setImageResource(R.drawable.classical_4);
                binding.ivTwo.setImageResource(R.drawable.default_4);
                binding.ivThree.setImageResource(R.drawable.minimal_4);
                break;
            case 4:
                binding.ivOne.setImageResource(R.drawable.classical_5);
                binding.ivTwo.setImageResource(R.drawable.default_5);
                binding.ivThree.setImageResource(R.drawable.minimal_5);
        }
    }

    private void setColor() {
        switch (colr) {
            case 0:
                binding.layoutBlue.setBackgroundResource(R.drawable.circle_blue);
                binding.layoutGreen.setBackgroundResource(R.drawable.circle);
                binding.layoutPurple.setBackgroundResource(R.drawable.circle);
                binding.layoutRed.setBackgroundResource(R.drawable.circle);
                binding.layoutYellow.setBackgroundResource(R.drawable.circle);
                setImages();
                break;
            case 1:
                binding.layoutBlue.setBackgroundResource(R.drawable.circle);
                binding.layoutGreen.setBackgroundResource(R.drawable.circle_blue);
                binding.layoutPurple.setBackgroundResource(R.drawable.circle);
                binding.layoutRed.setBackgroundResource(R.drawable.circle);
                binding.layoutYellow.setBackgroundResource(R.drawable.circle);
                setImages();
                break;
            case 2:
                binding.layoutBlue.setBackgroundResource(R.drawable.circle);
                binding.layoutGreen.setBackgroundResource(R.drawable.circle);
                binding.layoutPurple.setBackgroundResource(R.drawable.circle_blue);
                binding.layoutRed.setBackgroundResource(R.drawable.circle);
                binding.layoutYellow.setBackgroundResource(R.drawable.circle);
                setImages();
                break;
            case 3:
                binding.layoutBlue.setBackgroundResource(R.drawable.circle);
                binding.layoutGreen.setBackgroundResource(R.drawable.circle);
                binding.layoutPurple.setBackgroundResource(R.drawable.circle);
                binding.layoutRed.setBackgroundResource(R.drawable.circle_blue);
                binding.layoutYellow.setBackgroundResource(R.drawable.circle);
                setImages();
                break;
            case 4:
                binding.layoutBlue.setBackgroundResource(R.drawable.circle);
                binding.layoutGreen.setBackgroundResource(R.drawable.circle);
                binding.layoutPurple.setBackgroundResource(R.drawable.circle);
                binding.layoutRed.setBackgroundResource(R.drawable.circle);
                binding.layoutYellow.setBackgroundResource(R.drawable.circle_blue);
                setImages();
                break;
        }
    }

    public void setButton() {
        switch (type) {
            case 1:
                binding.btnStyle.setBackgroundResource(R.drawable.rounded_corner_button);
                binding.btnStyle.setTextColor(Color.parseColor("#ffffff"));
                binding.btnInformation.setBackgroundResource(R.color.white);
                binding.btnInformation.setTextColor(Color.parseColor("#333333"));
                binding.btnColumns.setBackgroundResource(R.color.white);
                binding.btnColumns.setTextColor(Color.parseColor("#333333"));
                binding.layoutStyle.setVisibility(View.VISIBLE);
                binding.layoutInfo.setVisibility(View.GONE);
                binding.layoutColoum.setVisibility(View.GONE);
                break;
            case 2:
                binding.btnStyle.setBackgroundResource(R.color.white);
                binding.btnStyle.setTextColor(Color.parseColor("#333333"));
                binding.btnInformation.setBackgroundResource(R.drawable.rounded_corner_button);
                binding.btnInformation.setTextColor(Color.parseColor("#ffffff"));
                binding.btnColumns.setBackgroundResource(R.color.white);
                binding.btnColumns.setTextColor(Color.parseColor("#333333"));
                binding.layoutStyle.setVisibility(View.GONE);
                binding.layoutInfo.setVisibility(View.VISIBLE);
                binding.layoutColoum.setVisibility(View.GONE);
                break;
            case 3:
                binding.btnStyle.setBackgroundResource(R.color.white);
                binding.btnStyle.setTextColor(Color.parseColor("#333333"));
                binding.btnInformation.setBackgroundResource(R.color.white);
                binding.btnInformation.setTextColor(Color.parseColor("#333333"));
                binding.btnColumns.setBackgroundResource(R.drawable.rounded_corner_button);
                binding.btnColumns.setTextColor(Color.parseColor("#ffffff"));
                binding.layoutStyle.setVisibility(View.GONE);
                binding.layoutInfo.setVisibility(View.GONE);
                binding.layoutColoum.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void imagePreview() {
        Dialog settingsDialog = new Dialog(getContext());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.layout_customize_invoice_image_preview
                , null));
        ImageView iv = settingsDialog.findViewById(R.id.iv_preview);
        settingsDialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
            }
        });
        iv.setImageResource(Constant.images[colr][templeat]);
        settingsDialog.show();
    }

    public void setTempleat() {
        switch (templeat) {
            case 0:
                binding.layoutOne.setBackgroundResource(R.drawable.rounded_corner_button);
                binding.layoutTwo.setBackgroundResource(R.color.white);
                binding.layoutThree.setBackgroundResource(R.color.white);
                break;
            case 1:
                binding.layoutOne.setBackgroundResource(R.color.white);
                binding.layoutTwo.setBackgroundResource(R.drawable.rounded_corner_button);
                binding.layoutThree.setBackgroundResource(R.color.white);
                break;
            case 2:
                binding.layoutOne.setBackgroundResource(R.color.white);
                binding.layoutTwo.setBackgroundResource(R.color.white);
                binding.layoutThree.setBackgroundResource(R.drawable.rounded_corner_button);
                break;
        }
    }

    private void getReportSetting() {
        RestClient.getInstance(getContext()).getReportSetting(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "").enqueue(new CustomCallBack<ReportSettings>(getContext(), null) {
            public void onResponse(Call<ReportSettings> call, Response<ReportSettings> response) {
                super.onResponse(call, response);
                try {
                    settings = response.body();
                    if (settings.getTransactionStatus().getIsSuccess())

                        if (settings != null) {
                            if (settings.getData().getLogoURL() != null)
                                Picasso.with(getContext()).load(settings.getData().getLogoURL()).into(binding.ivLogo);
                            binding.etInvoiceTitle.setText(settings.getData().getDefaultTitle());
                            binding.etInvoicePrefix.setText(settings.getData().getInvoiceNoPrefix());
                            binding.etInvoiceSuffix.setText(settings.getData().getInvoiceNoSuffix());
                            colr = getIndexCorol(settings.getData().getAcentColor());
                            templeat = settings.getData().getInvoiceLayout() - 1;
                            setColor();
                            setTempleat();
                            setcheckboxs();
                            binding.dueDays.setSelection(getIndexDue(settings.getData().getPaymentTermType()));
                            binding.etInvoiceSubheading.setText(String.valueOf(settings.getData().getSubHeading()));
                            binding.etInvoiceNo.setText(String.valueOf(settings.getData().getInvoiceInitialNo()));
                            getData();
                        }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<ReportSettings> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void updateReportSetting() {
        RadioData radioData = getData();
        ReportSettingRequest reportSettings = null;
        try {
            int iin = settings.getData().getInvoiceInitialNo();
            try {
                iin = Integer.parseInt(binding.etInvoiceNo.getText().toString());
            } catch (Exception e) {
                Log.e("Error :: ", e.getMessage());
            }
            reportSettings = new ReportSettingRequest(settings.getData().getId(), settings.getData().getCompanyId(), binding.cbLogo.isChecked(), templeat + 1, Constant.due_val[selectedDuePayment], binding.etInvoiceTitle.getText().toString(), Constant.color[colr], radioData.getItemtype(), radioData.getItemtypeDescription(), radioData.getUnittype(), radioData.getUnittypeDescription(), radioData.getPricetype(), radioData.getPricetypeDescription(), settings.getData().getLogoURL(), null, iin, binding.etInvoicePrefix.getText().toString(), binding.etInvoiceSuffix.getText().toString());
        } catch (Exception e) {

        }

        RestClient.getInstance(getContext()).updateReportSetting2(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), reportSettings).enqueue(new CustomCallBack<ReportSettings>(getContext(), null) {
            public void onResponse(Call<ReportSettings> call, Response<ReportSettings> response) {
                super.onResponse(call, response);
                try {
                    settings = response.body();
                    String e = new Gson().toJson(settings.getData());
                    if (settings.getTransactionStatus().getIsSuccess()) {
                        Toast.makeText(getContext(), "Setting Updated", Toast.LENGTH_LONG).show();
                        AddFragments.addFragmentToDrawerActivity(getContext(), null, SettingMenu.class);
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"setting");
                        b.putString(Constant.ACTION,"customize_invoice_updated");
                        SplashScreenActivity.sendEvent("setting_customize_invoice",b);
                    }else{
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"setting");
                        b.putString(Constant.ACTION,"customize_invoice_fail");
                        SplashScreenActivity.sendEvent("setting_customize_invoice",b);
                    }
                } catch (Exception e) {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"setting");
                    b.putString(Constant.ACTION,"customize_invoice_fail");
                    SplashScreenActivity.sendEvent("setting_customize_invoice",b);
                }
            }

            public void onFailure(Call<ReportSettings> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"setting");
                b.putString(Constant.ACTION,"customize_invoice_fail");
                SplashScreenActivity.sendEvent("setting_customize_invoice",b);
            }
        });
    }

    public void setarray() {
        array_due_time.add("Due within 15 days");
        array_due_time.add("Due within 30 days");
        array_due_time.add("Due within 45 days");
        array_due_time.add("Due within 60 days");
        array_due_time.add("Due within 90 days");
        ArrayAdapter dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, array_due_time);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.dueDays.setAdapter(dataAdapter);
        binding.dueDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDuePayment = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //Toast.makeText(getActivity(), data.getAction(), Toast.LENGTH_LONG).show();
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                CustomizeInvoice.logo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                changeintobase64(BitmapFactory.decodeFile(picturePath));
                cursor.close();
            } else {
                Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                        .show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            Uri fileUri = data != null ? data.getData() : null;
            binding.ivLogo.setImageURI(fileUri);
            Intrinsics.checkNotNull(ImagePicker.Companion.getFile(data));
            Intrinsics.checkNotNull(ImagePicker.Companion.getFilePath(data));
        } else if (resultCode == 64) {
            Toast.makeText(getContext(), (CharSequence) ImagePicker.Companion.getError(data), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), (CharSequence) "Task Cancelled", Toast.LENGTH_LONG).show();
        }

    }*/

    public int getIndexCorol(String color) {
        int r = 0;
        for (int i = 0; i < Constant.color.length; i++) {
            if (color.equalsIgnoreCase(Constant.color[i])) {
                r = i;
                break;
            }
        }
        return r;
    }

    public void changeintobase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        image_base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public String getSelectedRadioText(RadioGroup radiogroup) {
        int selectedId = radiogroup.getCheckedRadioButtonId();
        RadioButton radioButton = binding.getRoot().findViewById(selectedId);
        return radioButton.getText().toString();
    }

    public void itemlistner() {
        binding.radioGroupItemtype.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("chk", "id" + checkedId);
            if (checkedId == R.id.radioItems) {
                UiUtil.disableEditText2(binding.etItemType, getActivity());
            } else if (checkedId == R.id.radioProducts) {
                UiUtil.disableEditText2(binding.etItemType, getActivity());
            } else if (checkedId == R.id.radioServices) {
                UiUtil.disableEditText2(binding.etItemType, getActivity());
            } else if (checkedId == R.id.radioItemtypeCustom) {
                UiUtil.enableEditText2(binding.etItemType);
            }
        });
        binding.radioGroupUnittype.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("chk", "id" + checkedId);
            if (checkedId == R.id.radioQuantity) {
                UiUtil.disableEditText2(binding.etUnitType, getActivity());
            } else if (checkedId == R.id.radioHours) {
                UiUtil.disableEditText2(binding.etUnitType, getActivity());
            } else if (checkedId == R.id.radioUnitCustome) {
                UiUtil.enableEditText2(binding.etUnitType);
            }
        });
        binding.radioGroupPricetype.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("chk", "id" + checkedId);
            if (checkedId == R.id.radioPrice) {
                UiUtil.disableEditText2(binding.etPricetype, getActivity());
            } else if (checkedId == R.id.radioRate) {
                UiUtil.disableEditText2(binding.etPricetype, getActivity());
            } else if (checkedId == R.id.radioPricetypeCustome) {
                UiUtil.enableEditText2(binding.etPricetype);
            }
        });
    }

    public void setcheckboxs() {

        int item = settings.getData().getItemsType();
        int unit = settings.getData().getUnitsType();
        int price = settings.getData().getPriceType();

        binding.etPricetype.setText(settings.getData().getPriceName());
        binding.etUnitType.setText(settings.getData().getUnitName());
        binding.etItemType.setText(settings.getData().getItemName());

        if (item == 0) {
            binding.radioItems.setChecked(true);
        } else if (item == 1) {
            binding.radioProducts.setChecked(true);
        } else if (item == 2) {
            binding.radioServices.setChecked(true);
        } else if (item == 10) {
            binding.radioItemtypeCustom.setChecked(true);
        }

        if (unit == 0) {
            binding.radioQuantity.setChecked(true);
        } else if (unit == 1) {
            binding.radioHours.setChecked(true);
        } else if (unit == 10) {
            binding.radioUnitCustome.setChecked(true);
        }

        if (price == 0) {
            binding.radioPrice.setChecked(true);
        } else if (price == 1) {
            binding.radioRate.setChecked(true);
        } else if (price == 10) {
            binding.radioPricetypeCustome.setChecked(true);
        }
    }

    public int getIndexDue(int val) {
        int result = 0;
        for (int i = 0; i < Constant.due_val.length; i++) {
            if (Constant.due_val[i] == val) {
                result = i;
                break;
            }
        }
        return result;
    }

    public RadioData getData() {
        RadioData radioData = new RadioData();
        String item = getSelectedRadioText(binding.radioGroupItemtype);
        String unit = getSelectedRadioText(binding.radioGroupUnittype);
        String price = getSelectedRadioText(binding.radioGroupPricetype);
        try {
            if (item.equalsIgnoreCase("Items")) {
                radioData.setItemtypeDescription("Items");
                radioData.setItemtype(0);
            } else if (item.equalsIgnoreCase("Products")) {
                radioData.setItemtypeDescription("Products");
                radioData.setItemtype(1);
            } else if (item.equalsIgnoreCase("Services")) {
                radioData.setItemtypeDescription("Services");
                radioData.setItemtype(2);
            } else if (item.equalsIgnoreCase("Custom")) {
                radioData.setItemtypeDescription(binding.etItemType.getText().toString());
                radioData.setItemtype(10);
            }
        } catch (Exception e) {
        }
        try {
            if (unit.equalsIgnoreCase("Quantity")) {
                radioData.setUnittypeDescription("Quantity");
                radioData.setUnittype(0);
            } else if (unit.equalsIgnoreCase("Hours")) {
                radioData.setUnittypeDescription("Hours");
                radioData.setUnittype(1);
            } else if (unit.equalsIgnoreCase("Custom")) {
                radioData.setUnittypeDescription(binding.etUnitType.getText().toString());
                radioData.setUnittype(10);
            }
        } catch (Exception e) {
        }
        try {
            if (price.equalsIgnoreCase("Price")) {
                radioData.setPricetypeDescription("Price");
                radioData.setPricetype(0);
            } else if (price.equalsIgnoreCase("Rate")) {
                radioData.setPricetypeDescription("Rate");
                radioData.setPricetype(1);
            } else if (price.equalsIgnoreCase("Custom")) {
                radioData.setPricetypeDescription(binding.etPricetype.getText().toString());
                radioData.setPricetype(10);
            }
        } catch (Exception e) {
        }
        return radioData;
    }
}

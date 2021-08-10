package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Invoice.ViewInvoice;
import com.akounto.accountingsoftware.databinding.SendEstimateHomeFragmentBinding;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetMailRequest;
import com.akounto.accountingsoftware.request.SendMailRequest;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.mailreq.MailReceipentData;
import com.akounto.accountingsoftware.response.mailreq.MailReqDetails;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Response;

public class SendEstimateHomeFragment extends Fragment {

    EditText toEstimateEDTTV, from, sub, message;
    View view;
    SendEstimateHomeFragmentBinding binding;
    MailReceipentData data;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View inflate = inflater.inflate(R.layout.send_estimate_home_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.send_estimate_home_fragment, container, false);
        View inflate = binding.getRoot();
        this.view = binding.getRoot();
        this.toEstimateEDTTV = inflate.findViewById(R.id.toEstimateEDTTV);
        from = inflate.findViewById(R.id.from);
        getMailReceipant();
        message = inflate.findViewById(R.id.messageEditText);
        sub = inflate.findViewById(R.id.subjectEditText);
        this.view.findViewById(R.id.sendTV).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                send_mail();
            }
        });
        this.view.findViewById(R.id.cancelTV).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SendEstimateHomeFragment.this.lambda$onCreateView$1$SendEstimateHomeFragment(view);
            }
        });
        return this.view;
    }

    private void getMailReceipant() {
        GetMailRequest req = new GetMailRequest();
        req.setId(ViewInvoice.receivedData.getId());
        req.setHeadTrnsactionId(ViewInvoice.receivedData.getHeadTransactionCustomerId());
        req.setInvoiceNo(ViewInvoice.receivedData.getInvoiceNo());
        req.setMailType(1);
        RestClient.getInstance(getContext()).getMailReceipant(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), req).enqueue(new CustomCallBack<MailReqDetails>(getContext(), null) {
            public void onResponse(Call<MailReqDetails> call, Response<MailReqDetails> response) {
                super.onResponse(call, response);
                try {
                    data = response.body().getData();
                    from.setText(data.getFrom().get(0));
                    toEstimateEDTTV.setText(data.getRecipient().get(0));
                    sub.setText(data.getSubject());
                    binding.sendCopyCb.setText("Send a copy to myself at " + UiUtil.getEmail(getContext()));
                } catch (Exception e) {
                    Log.e("Error :: ",e.getMessage());
                }
            }

            public void onFailure(Call<MailReqDetails> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    public void send_mail() {
        SendMailRequest mailRequest = new SendMailRequest(data.getId(), data.getFrom(), binding.messageEditText.getText().toString(), binding.sendCopyCb.isChecked(), binding.attachPdf.isChecked(), data.getSubject(), data.getMailType(), data.getDownloadLink(), data.getRecipient());
        RestClient.getInstance(getContext()).sendMail(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), mailRequest).enqueue(new CustomCallBack<CustomeResponse>(getContext(), "sending mail...") {
            public void onResponse(Call<CustomeResponse> call, Response<CustomeResponse> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"invoicing");
                        b.putString(Constant.ACTION,"sending");
                        SplashScreenActivity.sendEvent("invoice_send",b);

                        SendEstimateHomeFragment.this.lambda$onCreateView$0$SendEstimateHomeFragment(view);
                    } else {
                        UiUtil.showToast(getContext(), response.body().getTransactionStatus().getError().getDescription());
                    }
                } else {
                    UiUtil.showToast(getContext(), "Fail to add customer");
                }
            }

            public void onFailure(Call<CustomeResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void lambda$onCreateView$0$SendEstimateHomeFragment(View v) {
        UiUtil.showToast(getActivity(), "Sent");
        getActivity().finish();
    }

    public void lambda$onCreateView$1$SendEstimateHomeFragment(View v) {
        getActivity().finish();
    }

    public void update(MailReceipentData data) {
        //this.data = data;
    }
}

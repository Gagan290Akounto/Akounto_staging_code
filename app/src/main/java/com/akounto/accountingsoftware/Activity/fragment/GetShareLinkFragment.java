package com.akounto.accountingsoftware.Activity.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.mailreq.MailReceipentData;
import com.akounto.accountingsoftware.util.UiUtil;

public class GetShareLinkFragment extends Fragment {

    EditText shareLinkEDTV;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.get_share_link_fragment, container, false);
        this.view = inflate;
        this.shareLinkEDTV = inflate.findViewById(R.id.shareLinkEDTV);
        this.view.findViewById(R.id.copyLink).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                GetShareLinkFragment.this.lambda$onCreateView$0$GetShareLinkFragment(view);
            }
        });
        this.view.findViewById(R.id.cancelEstimateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return this.view;
    }

    public void lambda$onCreateView$0$GetShareLinkFragment(View v) {
        UiUtil.showToast(getActivity(), "Link Copied");
        ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("SHARED LINK", this.shareLinkEDTV.getText().toString()));
    }

    public void update(MailReceipentData data) {
        this.shareLinkEDTV.setText(data.getDownloadLink());
    }
}

package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.UploadSecondActivity;

public class ReceiptsFragment extends Fragment implements View.OnClickListener {
    TextView uploadReceipt;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.receipts_fragment_layout, container, false);
        inItUi();
        return this.view;
    }

    private void inItUi() {
        TextView textView = this.view.findViewById(R.id.uploadReceipt);
        this.uploadReceipt = textView;
        textView.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.uploadReceipt) {
            startActivity(new Intent(getActivity(), UploadSecondActivity.class));
        }
    }
}

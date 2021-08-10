package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.akounto.accountingsoftware.R;

public class ViewPdfActivity extends AppCompatActivity {

    String base64;
    byte[] decodedString;
    //PDFView pdfView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
       /* this.base64 = LocalManager.getInstance().getBase64();
        this.pdfView = findViewById(R.id.pdfView);
        this.decodedString = Base64.decode(this.base64, 0);
        PDFView pDFView = findViewById(R.id.pdfView);
        this.pdfView = pDFView;
        pDFView.fromBytes(this.decodedString).load();*/
    }
}

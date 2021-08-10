package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    Context context;
    Uri fileName;
    List<List<String>> rows = new ArrayList();

    public CSVReader(Context context2, Uri fileName2) {
        this.context = context2;
        this.fileName = fileName2;
    }

    public List<List<String>> readCSV() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.context.getContentResolver().openInputStream(this.fileName)));
        br.readLine();
        while (true) {
            String readLine = br.readLine();
            String line = readLine;
            if (readLine == null) {
                return this.rows;
            }
            List<String> duplicateList = new ArrayList<>(Arrays.asList(line.split(",")));
            if (duplicateList.size() == 3) {
                duplicateList.add("0");
            }
            this.rows.add(duplicateList);
        }
    }
}

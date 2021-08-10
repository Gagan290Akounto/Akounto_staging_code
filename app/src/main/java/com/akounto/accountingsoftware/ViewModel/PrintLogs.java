package com.akounto.accountingsoftware.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.akounto.accountingsoftware.Repository.LoginRepo;

public class PrintLogs  extends AndroidViewModel {
    private LoginRepo loginRepo;
    public PrintLogs(@NonNull Application application) {
        super(application);
        loginRepo = new LoginRepo();
    }
}

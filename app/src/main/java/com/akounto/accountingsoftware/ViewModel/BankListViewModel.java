package com.akounto.accountingsoftware.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.akounto.accountingsoftware.Data.BankLinkData;
import com.akounto.accountingsoftware.Data.RegisterBank.AutoSynData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData2;
import com.akounto.accountingsoftware.Data.RegisterBank.Data;
import com.akounto.accountingsoftware.Data.RegisterBank.TransactionStatus;
import com.akounto.accountingsoftware.Repository.LoadBankRepo;
import com.akounto.accountingsoftware.request.RegisterBankRequest;
import com.akounto.accountingsoftware.util.UiUtil;

public class BankListViewModel extends AndroidViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<BankAccountData2> bankDataOption;
    private final MutableLiveData<BankAccountData> bankData;
    private final MutableLiveData<BankAccountData> impbankData;
    private final MutableLiveData<AutoSynData> autoSyncData;
    private final MutableLiveData<BankLinkData> bankLinkData;
    private final LoadBankRepo loadRepo;
    private final MutableLiveData<BankAccountData> afterRegisterbankData;

    public BankListViewModel(@NonNull Application application) {
        super(application);
        bankData = new MutableLiveData<>();
        afterRegisterbankData = new MutableLiveData<>();
        impbankData = new MutableLiveData<>();
        bankDataOption = new MutableLiveData<>();
        autoSyncData = new MutableLiveData<>();
        bankLinkData = new MutableLiveData<>();
        loadRepo = new LoadBankRepo();
    }

    //we will call this method to get the data
    public LiveData<BankAccountData> loadBank(Context mContext,String x_company, String access_code) {
        //we will load it asynchronously from server in this method
        loadRepo.loadBank(mContext,x_company, access_code);
        bankData.setValue(loadRepo.getBankData().getValue());
        return loadRepo.getBankData();
    }
    //we will call this method to get the data
    public LiveData<BankAccountData> loadregisterBank(Context mContext, String x_company, String access_code, RegisterBankRequest req) {
        //we will load it asynchronously from server in this method
        loadRepo.loadRegisterBank(mContext,x_company, access_code,req);
        afterRegisterbankData.setValue(loadRepo.getBankData().getValue());
        return afterRegisterbankData;
    }
    //we will call this method to get the data
    public LiveData<BankAccountData2> registerBank(Context mContext,String pt, String iid, String in) {
        //we will load it asynchronously from server in this method
        loadRepo.loadRegister(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext), pt, iid, in);
        bankDataOption.setValue(loadRepo.getBankDataOption().getValue());
        return bankDataOption;
    }

    //we will call this method to get the data
    public LiveData<AutoSynData> autoSync(Context mContext,String x_company, String access_code, String bank_id, String account_id, boolean state) {
        //we will load it asynchronously from server in this method
        loadRepo.autoSyncChange(mContext,x_company, access_code, bank_id, account_id, state);
        return loadRepo.getAutoSynData();
    }
    public LiveData<BankAccountData> import_trans(Context mContext,String x_company, String access_code, String bank_id) {
        //we will load it asynchronously from server in this method
        loadRepo.importBankTrans(mContext,x_company, access_code, bank_id);
        impbankData.setValue(loadRepo.getBankData().getValue());
        return impbankData;
    }
    public LiveData<BankLinkData> linkBank(Context mContext,String x_company, String access_code) {
        //we will load it asynchronously from server in this method
        loadRepo.linkBank(mContext,x_company, access_code);
        return loadRepo.getBankLinkData();
    }

    public MutableLiveData<BankAccountData> getBankData() {
        return bankData;
    }

    public BankAccountData setData(AutoSynData old) {
        BankAccountData bankAccountData=new BankAccountData();
        TransactionStatus status=old.getTransactionStatus();
        Data data=new Data();
        data.setBanks(old.getBanks());
        bankAccountData.setTransactionStatus(status);
        bankAccountData.setData(data);
        bankAccountData.setStatus(old.getStatus());
        bankAccountData.setStatusMessage(old.getStatusMessage());
        return bankAccountData;
    }

    public MutableLiveData<BankAccountData> getImpbankData() {
        return impbankData;
    }
}

package com.akounto.accountingsoftware.util;

import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.response.chartaccount.HeadTransactions;
import java.util.ArrayList;
import java.util.List;

public class LocalManager {
    private static LocalManager localManager;
    private String base64;
    List<AddWithdrawalCategorySpinnerItem> categoryList = new ArrayList();
    List<HeadTransactions> headTransactionsList = new ArrayList();
    private SignInResponse logedinUserDetails;
    private RegisterBusiness registerBusiness;

    public static LocalManager getLocalManager() {
        return localManager;
    }

    public static synchronized LocalManager getInstance() {
        LocalManager localManager2;
        synchronized (LocalManager.class) {
            if (localManager == null) {
                localManager = new LocalManager();
            }
            localManager2 = localManager;
        }
        return localManager2;
    }

    public RegisterBusiness getRegisterBusiness() {
        return this.registerBusiness;
    }

    public void setRegisterBusiness(RegisterBusiness registerBusiness2) {
        this.registerBusiness = registerBusiness2;
    }

    public SignInResponse getLogedinUserDetails() {
        return this.logedinUserDetails;
    }

    public void setLogedinUserDetails(SignInResponse logedinUserDetails2) {
        this.logedinUserDetails = logedinUserDetails2;
    }

    public List<HeadTransactions> getHeadTransactionsList() {
        return this.headTransactionsList;
    }

    public void setHeadTransactionsList(List<HeadTransactions> headTransactionsList2) {
        this.headTransactionsList = headTransactionsList2;
    }

    public String getBase64() {
        return this.base64;
    }

    public void setBase64(String base642) {
        this.base64 = base642;
    }

    public void setCategoriesList(List<AddWithdrawalCategorySpinnerItem> list) {
        this.categoryList = list;
    }

    public List<AddWithdrawalCategorySpinnerItem> getCategoryList() {
        return this.categoryList;
    }
}

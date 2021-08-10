package com.akounto.accountingsoftware.network;

import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.request.AddAccountRequest;
import com.akounto.accountingsoftware.request.AddBankStatementRequest;
import com.akounto.accountingsoftware.request.AddBill;
import com.akounto.accountingsoftware.request.AddCompanyRequest;
import com.akounto.accountingsoftware.request.AddCustomerRequest;
import com.akounto.accountingsoftware.request.AddJournalTransactionRequest;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.request.AddSchedulerReq;
import com.akounto.accountingsoftware.request.AddTransactionRequest;
import com.akounto.accountingsoftware.request.AddUserRequest;
import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.akounto.accountingsoftware.request.ApproveBillReq;
import com.akounto.accountingsoftware.request.ApproveRecurringInvoice;
import com.akounto.accountingsoftware.request.BillUpdate.RequestBill;
import com.akounto.accountingsoftware.request.CashFlowRequest;
import com.akounto.accountingsoftware.request.ConvertToInvoice;
import com.akounto.accountingsoftware.request.CreateRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.EditCompanyRequest;
import com.akounto.accountingsoftware.request.GetBalanceSheetRequest;
import com.akounto.accountingsoftware.request.GetDashboardRequest;
import com.akounto.accountingsoftware.request.GetMailRequest;
import com.akounto.accountingsoftware.request.GetRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.InvoiceRequest;
import com.akounto.accountingsoftware.request.ReportSettingRequest;
import com.akounto.accountingsoftware.request.SaleTaxRequest;
import com.akounto.accountingsoftware.request.SendBill;
import com.akounto.accountingsoftware.request.SendMailRequest;
import com.akounto.accountingsoftware.request.Setting;
import com.akounto.accountingsoftware.request.TransectionRequest;
import com.akounto.accountingsoftware.request.TrialBalanceRequest;
import com.akounto.accountingsoftware.request.UpdateStatus;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.request.UserDelete;
import com.akounto.accountingsoftware.request.ViewReportRequest;
import com.akounto.accountingsoftware.response.AddBusinessResponse;
import com.akounto.accountingsoftware.response.AddSchedulerResponse;
import com.akounto.accountingsoftware.response.BillsByIdResponse;
import com.akounto.accountingsoftware.response.BussinessData;
import com.akounto.accountingsoftware.response.CustomerOnlyResponse;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.CustomerUpdateResponse;
import com.akounto.accountingsoftware.response.DashboardResponse;
import com.akounto.accountingsoftware.response.DeleteCustomerResponse;
import com.akounto.accountingsoftware.response.DownloadResponse;
import com.akounto.accountingsoftware.response.GetBillsResponse;
import com.akounto.accountingsoftware.response.GetCompanyResponse;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.response.IncomeAccountsResponse;
import com.akounto.accountingsoftware.response.InvoiceNumberResponse;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.PurchaseItemResponse;
import com.akounto.accountingsoftware.response.RecurringInvoicesResponse;
import com.akounto.accountingsoftware.response.ReportSetting.Data;
import com.akounto.accountingsoftware.response.ReportSetting.ReportSettings;
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.SignUpResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.response.Taxs.TaxsResponse;
import com.akounto.accountingsoftware.response.Transaction;
import com.akounto.accountingsoftware.response.TransectionDetails;
import com.akounto.accountingsoftware.response.UpdateResponse;
import com.akounto.accountingsoftware.response.UpdateStatusResponse;
import com.akounto.accountingsoftware.response.UserInfoResponse;
import com.akounto.accountingsoftware.response.UserManagementResponse;
import com.akounto.accountingsoftware.response.VendorDetailsResponse;
import com.akounto.accountingsoftware.response.VendorResponse;
import com.akounto.accountingsoftware.response.accounting.AccountDetailByIdResponse;
import com.akounto.accountingsoftware.response.accounting.GetBankResponse;
import com.akounto.accountingsoftware.response.accounting.JournalTransactionDetailByIdResponse;
import com.akounto.accountingsoftware.response.accounting.TransactionDetailByIdResponse;
import com.akounto.accountingsoftware.response.cashflow.CashFLowData;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.response.getBalanceSheet.GetBalanceSheet;
import com.akounto.accountingsoftware.response.getusercompany.GetUserCompany;
import com.akounto.accountingsoftware.response.mailreq.MailReqDetails;
import com.akounto.accountingsoftware.response.saletax.EditSaleTaxResponse;
import com.akounto.accountingsoftware.response.saletax.SaleTaxDetails;
import com.akounto.accountingsoftware.response.trialbalance.TrialBalanceDetails;
import com.akounto.accountingsoftware.response.viewbill.ViewBillResponse;
import com.akounto.accountingsoftware.response.viewreport.ViewReportDetails;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {

    @POST("api/head-trans/{mode}")
    Call<ResponseBody> addAccount(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddAccountRequest addAccountRequest, @Path("mode") String str);

    @POST("api/profile/add-company")
    Call<AddBusinessResponse> addCompany(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddCompanyRequest addCompanyRequest);

    @POST("api/journal/transaction-add")
    Call<ResponseBody> addJournalTransaction(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddJournalTransactionRequest addJournalTransactionRequest);

    @GET("api/report/search-dashboard")
    Call<SearchDashboardData> searchDashboard(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String x_company);

    @POST("api/customer/add")
    Call<CustomerUpdateResponse> addCustomer(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddCustomerRequest addCustomerRequest);

    @POST("api/send-mail")
    Call<CustomeResponse> sendMail(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body SendMailRequest sendMailRequest);

    @POST("api/product/add")
    Call<ResponseBody> addProduct(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body Product product);

    @POST("api/tax/add")
    Call<ResponseBody> addSaleTax(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddSaleTaxRequest addSaleTaxRequest);
    @POST("api/tax/add")
    Call<CustomeResponse> addSaleTaxB(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddSaleTaxRequest addSaleTaxRequest);

    @POST("api/tax/add")
    Call<TaxsResponse> addSaleTaxDilog(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddSaleTaxRequest addSaleTaxRequest);

    @POST("api/recurring/add-schedule")
    Call<AddSchedulerResponse> addScheduler(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddSchedulerReq addSchedulerReq);

    @POST("api/vendor/add")
    Call<ResponseBody> addVendor(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddVendorRequest addVendorRequest);

    @POST("api/profile/change-user-password")
    Call<SignUpResponse> changeUserPassword(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body Map<String, String> map);

    @GET("/api/accounting/get-user-companies")
    Call<BussinessData> getBusinessList(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company);

    @POST("api/bill/create")
    Call<CustomeResponse> createBill(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddBill addBill);

    @POST("api/{invoice-type}/create")
    Call<CustomeResponse> createInvoice(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path(encoded = true, value = "invoice-type") String str, @Body CreateRecurringInvoiceRequest createRecurringInvoiceRequest);

    @POST(" api/{invoice-type}/update")
    Call<GetChartResponse> UpdateInvoice(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path(encoded = true, value = "invoice-type") String str, @Body InvoiceRequest createRecurringInvoiceRequest);

    @POST("api/bank/transaction-add")
    Call<CustomeResponse> addTransaction(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddTransactionRequest addTransactionRequest);

    @GET("api/accounting/get-chart")
    Call<GetChartResponse> getChart(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("cc") String f);

    @GET("api/invoice/get-report-settings")
    Call<ReportSettings> getReportSetting(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("cc") String f);

    @POST("api/invoice/update-report-settings")
    Call<ReportSettings> updateReportSetting(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body Data data);

    @POST("api/invoice/update-report-settings")
    Call<ReportSettings> updateReportSetting2(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body ReportSettingRequest data);

    @GET("api/journal/transactions-get/{id}")
    Call<JournalTransactionDetailByIdResponse> getJournalTransactionDetailsById(@Header("Authorization") String signature, @Header("X-Company") String company, @Path("id") int i);

    @POST("api/profile/{mode}")
    Call<ResponseBody> createUser(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body AddUserRequest addUserRequest, @Path("mode") String str);

    @GET("api/bank/transactions-get/{id}")
    Call<TransactionDetailByIdResponse> getTransactionDetailsById(@Header("Authorization") String signature, @Header("X-Company") String company, @Path("id") int i);

    @POST("api/bank/transactions-get")
    Call<TransectionDetails> getTransectionList(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body TransectionRequest transectionRequest);

    @GET("api/customer/delete/{id}")
    Call<DeleteCustomerResponse> deleteCustomer(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path("id") String str);

    @GET("api/vendor/delete/{id}")
    Call<DeleteCustomerResponse> deleteVendor(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path("id") String str);

    @POST("api/estimate/download")
    Call<DownloadResponse> downloadPdf(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body UpdateStatus updateStatus);

    @POST("api/report/download-profit-loss-report-summary-pdf")
    Call<DownloadResponse> downloadSummeryPdf(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ViewReportRequest viewReportRequest);

    @POST("api/profile/edit-company")
    Call<ResponseBody> editCompany(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body EditCompanyRequest editCompanyRequest);

    @POST("api/profile/edit-company-financial-year")
    Call<ResponseBody> editCompanyFinancialYear(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body Map<String, Integer> map);

    @POST("api/product/edit")
    Call<ResponseBody> editProduct(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body Product product);

    @POST("api/tax/edit")
    Call<ResponseBody> editSaleTax(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body SaleTax saleTax);

    @POST("api/profile/update-accounting-basis-type")
    Call<ResponseBody> updateAccountingBasisType(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body Setting saleTax);


    @POST("api/vendor/edit")
    Call<ResponseBody> editVendor(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body AddVendorRequest addVendorRequest);

    @POST("api/estimate/estimate-to-invoice/")
    Call<UpdateStatusResponse> estimateToInvoice(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ConvertToInvoice convertToInvoice);

    @GET("api/head-trans/edit/{id}")
    Call<AccountDetailByIdResponse> getAccountDetailsById(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") int i);

    @POST("api/report/get-balance-sheet-report")
    Call<GetBalanceSheet> getBalanceSHeet(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body GetBalanceSheetRequest getBalanceSheetRequest);

    @GET("api/bank/get")
    Call<GetBankResponse> getBanks(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @GET("api/bill/get/{id}")
    Call<BillsByIdResponse> getBillById(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") String str);

    @GET("api/bill/get/{id}")
    Call<ViewBillResponse> getBillForEdit(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") String str);

    @POST("api/bill/get")
    Call<GetBillsResponse> getBillsList(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body SendBill sendBill);

    @GET("api/profile/get-company/{id}")
    Call<GetCompanyResponse> getBusinessById(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") int i);

    @POST("api/report/get-cash-flow-report")
    Call<CashFLowData> getCashFlow(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body CashFlowRequest cashFlowRequest);

    @POST("api/invoice/get-cash-flow-report")
    Call<DashboardResponse> getCashFlowReport(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ViewReportRequest viewReportRequest);

    @GET("api/currency/exchange-get/{id}")
    Call<CurrencyResponse> getCurrencyConverter(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") String str);

    @GET("api/customer/get")
    Call<CustomerResponse> getCustomerList(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Query("F") String s);

    @GET("api/customer/get/{HeadTransactionId}")
    Call<CustomerOnlyResponse> getCustomerListById(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("HeadTransactionId") int i);

    @POST("api/accounting/dashboad")
    Call<DashboardData> getDashboard(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body GetDashboardRequest getDashboardRequest);

    @GET("api/head-trans/get/4000")
    Call<IncomeAccountsResponse> getIncomeAccounts(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @GET("api/head-trans/get")
    Call<IncomeAccountsResponse> getIncomeAccountsPandS(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @GET("api/head-trans/get/5000")
    Call<IncomeAccountsResponse> getIncomeAccountsPurchase(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @POST("api/{invoice-type}/get")
    Call<RecurringInvoicesResponse> getInvoice(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path(encoded = true, value = "invoice-type") String str, @Body GetRecurringInvoiceRequest getRecurringInvoiceRequest);

    @GET("api/{invoice-type}/get/{id}")
    Call<GetInvoiceByIdResponse> getInvoiceById(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path(encoded = true, value = "invoice-type") String str, @Path("id") String str2);

    @GET("api/invoice/get-invoice-number")
    Call<InvoiceNumberResponse> getInvoiceNo(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("E") String s);

    @GET("api/estimate/get-estimate-number")
    Call<InvoiceNumberResponse> getEstimateNo(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("E") String s);

    @POST("api/get-mail-recipient")
    Call<MailReqDetails> getMailReceipant(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body GetMailRequest getMailRequest);

    @POST("api/invoice/get-profit-loss-report")
    Call<ViewReportDetails> getProfitLossReport(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body ViewReportRequest viewReportRequest);

    @GET("api/product/purchase/get")
    Call<PurchaseItemResponse> getPurchaseItem(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @POST("api/report/sales-tax-get")
    Call<SaleTaxDetails> getSaleTax(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body SaleTaxRequest saleTaxRequest);

    @GET("api/tax/edit/{id}")
    Call<EditSaleTaxResponse> getSaleTaxById(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path("id") int i);

    @GET("api/product/sales/get")
    Call<SalesProductResponse> getSalesProductList(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("S") String d);

    @GET("api/tax/get")
    Call<SalesTaxResponse> getSalesTaxList(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Query("S") String d);

    @GET("api/country/states-get/{state_id}")
    Call<GetStatesResponse> getStatesByCountryId(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Path("state_id") int i);

    @GET("api/tax/get")
    Call<TaxResponseList> getTaxList(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company);

    @POST("api/report/get-trial-balance")
    Call<TrialBalanceDetails> getTrailBalance(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body TrialBalanceRequest trialBalanceRequest);

    @GET("api/accounting/get-user-companies")
    Call<GetUserCompany> getUserCompany(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company);

    @GET("api/profile/get-user-info")
    Call<UserInfoResponse> getUserInfo(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @GET("api/profile/get-users")
    Call<UserManagementResponse> getUsers(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company);

    @GET("api/vendor/edit/{id}")
    Call<VendorDetailsResponse> getVendorDetails(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path("id") int i);

    @GET("api/vendor/get")
    Call<VendorResponse> getVendorList(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Query("F") String s);

    @POST("api/invoice/update-status")
    Call<GetInvoiceByIdResponse> invoiceCacel(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ApproveRecurringInvoice approveRecurringInvoice);


    @POST("api/bill/update")
    Call<AddBill> updateBill(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body AddBill addBill);

    @POST("api/bill/create")
    Call<CustomeResponse> addBill(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body AddBill addBill);

    @POST("api/bill/update")
    Call<AddBill> updateBill(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body RequestBill addBill);

    @POST("api/bill/update-status")
    Call<BillsByIdResponse> updateBillStatus(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ApproveBillReq approveBillReq);

    @POST("api/bank/transaction-review")
    Call<Transaction> updateCatAction(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String company, @Body Transaction transaction);

    @POST("api/customer/edit")
    Call<CustomerUpdateResponse> updateCustomer(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body AddCustomerRequest addCustomerRequest);

    @POST("api/{invoice-type}/update-status")
    Call<UpdateStatusResponse> updateEstimateStatus(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Path(encoded = true, value = "invoice-type") String str, @Body UpdateStatus updateStatus);

    @POST("api/recurring/update-status")
    Call<GetInvoiceByIdResponse> updateRecurring(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body ApproveRecurringInvoice approveRecurringInvoice);

    @POST("api/bank/transaction-csv-import")
    Call<ResponseBody> uploadBankStatement(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body AddBankStatementRequest addBankStatementRequest);

    @Multipart
    @POST("api/bank/transaction-csv-import-new")
    Call<BankAccountData> uploadBankCSV(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Part MultipartBody.Part file1, @Part("BankAccountId") RequestBody bbank_account_id, @Part("OpeningBallance") RequestBody opening_ballance);

    @GET("api/general/android-app-version")
    Call<UpdateResponse> getUpadte();

    @POST("api/profile/adnroid-log-error")
    Call<ResponseBody> addErrorLog(@Header("X-Signature") String signature, @Field("message") String message, @Field("level") int level, @Field("filename") String filename);

    @POST("api/profile/disassociate-user")
    Call<CustomeResponse> disassociateUser(@Header("X-Signature") String signature, @Header("Authorization") String authHeader, @Header("X-Company") String company, @Body UserDelete user_delete);

}

package com.mangoman.sms.models;

import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;

import java.text.SimpleDateFormat;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 12:51 PM.
 */
public class Expense {

    private Bank bank;
    private double amountSpent;
    private long time;
    private String formattedTime;
    private String formattedDate;
    private Category cardCategory;
    private String merchantName;

    public Expense(Bank bank, double amountSpent, String merchantName, long time, Category cardCategory) {
        this.bank = bank;
        this.merchantName = merchantName;
        this.amountSpent = amountSpent;
        this.time = time;
        this.cardCategory = cardCategory;
        SimpleDateFormat st = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("EEE, d MMM yyyy");
        this.formattedTime = st.format(time);
        this.formattedDate = sd.format(time);
    }

    public Bank getBank() {
        return bank;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public long getTime() {
        return time;
    }

    public Category getCardCategory() {
        return cardCategory;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bank: " + bank.getBankName()).append("\n");
        sb.append("Time: " + formattedTime).append("\n");
        sb.append("Expense Category: " + cardCategory.getName()).append("\n");
        sb.append("Merchant : " + merchantName).append("\n");
        sb.append("Amount spent: " + amountSpent).append("\n").append("\n");
        return sb.toString();
    }
}

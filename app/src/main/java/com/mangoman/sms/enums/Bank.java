package com.mangoman.sms.enums;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 12:47 PM.
 */
public enum Bank implements Serializable {

    ICICI("ICICI Bank", "ICICIB"),
    HDFC("HDFC Bank", "HDFCBK"),
    SBI("SBI Bank", "SBI"),
    CITIBANK("CitiBank", "Citibk"),
    KOTAK("Kotak Mahindra", "KOTAKB");


    String bankName;
    String smsSenderName;

    private Bank(String bankName, String smsSenderName) {
        this.bankName = bankName;
        this.smsSenderName = smsSenderName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getSmsSenderName() {
        return smsSenderName;
    }

    public static CopyOnWriteArrayList<String> getBankNameList() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();

        for (Bank bank : Bank.values()) {
            list.add(bank.getBankName());
        }
        return list;
    }

    public static Bank getByName(String name) {

        for (Bank b : Bank.values()) {
            if (b.getBankName().equals(name))
                return b;
        }
        return null;
    }
}

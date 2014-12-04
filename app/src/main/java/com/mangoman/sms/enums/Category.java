package com.mangoman.sms.enums;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 12:20 PM.
 */
public enum Category implements Serializable {

    DEBIT_CARD_PURCHASE("Debit Card Purchases"),
    CREDIT_CARD_PURCHASE("Credit Card Purchases"),
    ATM_WITHDRAWAL("ATM Withdrawals");
//    OTHER("Other");

    String categoryName;

    private Category(String name) {
        this.categoryName = name;
    }

    public String getName() {
        return categoryName;
    }

    public static ArrayList<Category> getCategoryList() {
        ArrayList<Category> list = new ArrayList<Category>();

        for (Category c : Category.values()) {
            list.add(c);
        }
        return list;
    }


}

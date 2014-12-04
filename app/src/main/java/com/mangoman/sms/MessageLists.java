package com.mangoman.sms;

import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;
import com.mangoman.sms.models.Expense;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 12:50 PM.
 */
public class MessageLists {

    //******************************* Global Constants & Properties ********************************//
    public static ConcurrentHashMap<Bank, ArrayList<Expense>> messageList = new ConcurrentHashMap<Bank, ArrayList<Expense>>();


    public static ArrayList<Expense> getAllList(Bank bank, Category category) {
        ArrayList<Expense> finalList = new ArrayList<Expense>();

        if (messageList.get(bank) != null) {

            for (Expense e : messageList.get(bank)) {
                if (e.getCardCategory() == category) {
                    finalList.add(e);
                }
            }

        }

        return finalList;
    }

    public static ArrayList<Expense> getListBetweenDates(Bank bank, Category category) {

        ArrayList<Expense> allList = getAllList(bank, category);
        ArrayList<Expense> newList = new ArrayList<Expense>();

        for (Expense ex : allList) {
            if (ex.getTime() > DateRange.fromDate && ex.getTime() < DateRange.toDate) {
                newList.add(ex);
            }
        }
        return newList;
    }

    public static double getTotalAmountBetweenDates(Bank bank, Category category) {

        ArrayList<Expense> list = getListBetweenDates(bank, category);

        double total = 0;

        for (Expense ex : list) {
            total = total + ex.getAmountSpent();
        }
        return total;
    }

    public static double getBankTotalAllCategoriesBetweenDates(Bank bank) {
        double total = 0;
        for (Category category : Category.values()) {
            total = total + getTotalAmountBetweenDates(bank, category);
        }

        return total;
    }

    public static double getAllBanksTotalBetweenDates() {
        double total = 0;

        for (Bank bank : Bank.values()) {
            for (Category category : Category.values()) {

                total = total + getTotalAmountBetweenDates(bank, category);
            }
        }

        return total;
    }


}

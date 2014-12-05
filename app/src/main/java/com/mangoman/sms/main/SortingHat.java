package com.mangoman.sms.main;

import android.util.Log;

import com.mangoman.sms.SmsTemplates;
import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;

import java.util.regex.Pattern;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 1:06 PM.
 */
public class SortingHat {


    // Working on converting these into a proper regex format. See SmsTemplates.class.

    private static final String icici_debit_merchant = "You have made a Debit Card purchase of";
    private static final String icici_debit_atm = "is debited with";

    private static final String hdfc_debit_merchant = "Thank you for using your HDFC bank DEBIT/ATM card ending ";
    private static final String hdfc_debit_atm = "Thank you for using your HDFC Bank DEBIT/ATM Card ending ";
    private static final String hdfc_credit_merchant = "was spent on ur HDFCBank CREDIT Card ending";

    private static final String sbi_debit_merchant = "Thank you for using your SBI Debit Card";
    private static final String sbi_debit_atm = "Your SBI Debit Card ";

    private static final String citi_debit_merchant = "Your debit card ";
    private static final String citi_credit_merchant = "was spent on your Credit Card ";
    private static final String citi_debit_atm = "was withdrawn at an ATM from a/c";

    private static final String kotak_debit_merchant = "made on Kotak Debit card";
    private static final String kotak_credit_merchant = " has been made on Kotak credit card";
    private static final String kotak_debit_atm = "Cash withdrawal of Rs";

    private static final String LOGTAG = "SortingHat";


    public static Category getCategory(Bank bank, String messageBody) {

        switch (bank) {

            case ICICI:

                if (SmsTemplates.getTemplatePattern(bank, Category.DEBIT_CARD_PURCHASE).matcher(messageBody).find()) {
                    return Category.DEBIT_CARD_PURCHASE;
                } else if (SmsTemplates.getTemplatePattern(bank, Category.ATM_WITHDRAWAL).matcher(messageBody).find()) {
                    return Category.ATM_WITHDRAWAL;
                }

//                if (messageBody.contains(icici_debit_atm)) {
//                    return Category.ATM_WITHDRAWAL;
//                } else if (messageBody.contains(icici_debit_merchant)) {
//                    return Category.DEBIT_CARD_PURCHASE;
//                }
                break;
            case HDFC:
                if (messageBody.contains(hdfc_debit_atm) && messageBody.contains("ATM WDL")) {
                    return Category.ATM_WITHDRAWAL;
                } else if (messageBody.contains(hdfc_debit_merchant) && !messageBody.contains("ATM WDL")) {
                    return Category.DEBIT_CARD_PURCHASE;
                } else if (messageBody.contains(hdfc_credit_merchant)) {
                    return Category.CREDIT_CARD_PURCHASE;
                }
                break;
            case SBI:
                if (messageBody.contains(sbi_debit_atm)) {
                    return Category.ATM_WITHDRAWAL;
                } else if (messageBody.contains(sbi_debit_merchant)) {
                    return Category.DEBIT_CARD_PURCHASE;
                }
                break;


            case CITIBANK:
                if (messageBody.contains(citi_credit_merchant)) {
                    return Category.CREDIT_CARD_PURCHASE;
                } else if (messageBody.contains(citi_debit_atm)) {
                    return Category.ATM_WITHDRAWAL;
                } else if (messageBody.contains(citi_debit_merchant)) {
                    return Category.DEBIT_CARD_PURCHASE;
                }
                break;
            case KOTAK:
                if (messageBody.contains(kotak_credit_merchant)) {
                    return Category.CREDIT_CARD_PURCHASE;
                } else if (messageBody.contains(kotak_debit_atm)) {
                    return Category.ATM_WITHDRAWAL;
                } else if (messageBody.contains(kotak_debit_merchant)) {
                    return Category.DEBIT_CARD_PURCHASE;
                }
                break;
        }

        return null;
    }

    static int start, end;
    static double amount;
    static String subs = "", newStr = "";

    public static double getAmountSpent(Bank bank, Category expenseCategory, String message) {


        switch (bank) {

            case ICICI:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        return getAmountFromMessage("purchase of", " on ", message);
                    case CREDIT_CARD_PURCHASE:
                        break;
                    case ATM_WITHDRAWAL:

                        Pattern p = SmsTemplates.getTemplatePattern(bank, expenseCategory);
                        String[] items = p.split(message);

                        for (String s : items) {
                            if (!s.isEmpty()) {
                                String am = s.replaceAll("[^\\d.]+", "");
                                try {
                                    return Double.parseDouble(am);
                                } catch (Exception e) {
                                    return 0;
                                }
                            }
                        }

                        return 0;

//                        return getAmountFromMessage("debited with", "NFS", message);
                }
                break;

            case HDFC:

                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        return getAmountFromMessage("for Rs.", " in ", message);
                    case CREDIT_CARD_PURCHASE:
                        return getAmountFromMessage("Rs.", "was spent on", message);
                    case ATM_WITHDRAWAL:
                        return getAmountFromMessage(" for Rs. ", " towards ATM WDL ", message);
                }

                break;
            case SBI:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        return getAmountFromMessage("purchase worth", " on POS ", message);
                    case CREDIT_CARD_PURCHASE:
                        break;
                    case ATM_WITHDRAWAL:
                        return getAmountFromMessage("to draw", ".", message);
                }
                break;


            case CITIBANK:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        return getAmountFromMessage("Rs", "on", message);
                    case CREDIT_CARD_PURCHASE:
                        double amount = getAmountFromMessage("Rs ", "was spent on", message);
                        if (amount == 0) {
                            amount = getAmountFromMessage("", " INR was spent", message);
                            return amount;
                        } else return amount;

                    case ATM_WITHDRAWAL:
                        amount = getAmountFromMessage("Rs.", " was withdrawn", message);
                        if (amount == 0) {
                            amount = getAmountFromMessage("Rs ", " was withdrawn", message);
                            return amount;
                        } else return amount;
                }
                break;
            case KOTAK:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        return getAmountFromMessage("Rs.", " made on Kotak", message);
                    case CREDIT_CARD_PURCHASE:
                        return getAmountFromMessage("Rs", " has been", message);
                    case ATM_WITHDRAWAL:
                        return getAmountFromMessage("of Rs.", " made on Kotak", message);
                }
                break;
        }

        return 0;
    }


    public static String getMechantName(Bank bank, Category expenseCategory, String message) {

        switch (bank) {
            case ICICI:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
//                        start = message.indexOf("*") + 1;
//                        end = message.lastIndexOf("Your") - 1;
//                        if (start >= 0 && end > 0 && end > start) {
//                            return message.substring(start, end);
//                        } else return "";

                        Pattern p = SmsTemplates.getTemplatePattern(bank, expenseCategory);
                        String[] items = p.split(message);

                        if (items.length == 3) {
                            return items[2];
                        } else return "N/A";

                    case CREDIT_CARD_PURCHASE:
                        break;
                    case ATM_WITHDRAWAL:
                        return "ATM";
                }
                break;
            case HDFC:
                switch (expenseCategory) {
                    case DEBIT_CARD_PURCHASE:
                        start = message.indexOf("at");
                        end = message.indexOf("on");
                        if (start >= 0 && end > 0 && end > start)
                            return message.substring(start, end);
                        else return "";
                    case CREDIT_CARD_PURCHASE:
                        start = message.indexOf("at");
                        end = message.indexOf("Avl bal");
                        if (start >= 0 && end > 0 && end > start)
                            return message.substring(start, end);
                        else return "";
                    case ATM_WITHDRAWAL:
                        return "ATM";
                }
                break;
            case SBI:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        start = message.indexOf(" at ") + 3;
                        end = message.lastIndexOf("txn#");
                        if (start >= 0 && end > 0 && end > start) {
                            return message.substring(start, end);
                        } else return "";
                    case CREDIT_CARD_PURCHASE:
                        break;
                    case ATM_WITHDRAWAL:
                        return "ATM";
                }
                break;


            case CITIBANK:
                switch (expenseCategory) {

                    case DEBIT_CARD_PURCHASE:
                        start = message.lastIndexOf(" at ") + 4;
                        end = message.lastIndexOf(".");
                        if (start >= 0 && end > 0 && end > start) {
                            return message.substring(start, end);
                        } else return "";
                    case CREDIT_CARD_PURCHASE:
                        start = message.lastIndexOf(" at ") + 4;
                        end = message.lastIndexOf(".");
                        if (start >= 0 && end > 0 && end > start) {
                            return message.substring(start, end);
                        } else return "";
                    case ATM_WITHDRAWAL:
                        return "ATM";
                }
                break;
            case KOTAK:
                switch (expenseCategory) {
                    case DEBIT_CARD_PURCHASE:
                        start = message.indexOf(" at ") + 2;
                        end = message.lastIndexOf(".Combined");
                        if (start >= 0 && end > 0 && end > start) {
                            return message.substring(start, end);
                        } else return "";
                    case CREDIT_CARD_PURCHASE:
                        start = message.indexOf(" at ") + 2;
                        end = message.lastIndexOf(".Available");
                        if (start >= 0 && end > 0 && end > start) {
                            return message.substring(start, end);
                        } else return "";
                    case ATM_WITHDRAWAL:
                        return "ATM";
                }
                break;
        }

        return "";
    }


    private static double getAmountFromMessage(String firstString, String secondString, String message) {
        String subs = "";
        String amountString = "";
        start = message.indexOf(firstString);
        end = message.lastIndexOf(secondString);
        if (start >= 0 && end > 0 && end > start) {
            subs = message.substring(start, end);
            amountString = subs.replaceAll("[^\\d.]+", "");

            if (amountString.startsWith("."))
                amountString = amountString.replaceFirst(".", "");
            if (amountString.isEmpty())
                return 0;

            double d = 0;

            try {
                d = Double.parseDouble(amountString);
            } catch (Exception e) {
                Log.w(LOGTAG, "Double not parsed : " + message);
            }

            return d;
        } else {
            Log.w(LOGTAG, start + " " + end + " " + message);
        }

        return 0;
    }


}


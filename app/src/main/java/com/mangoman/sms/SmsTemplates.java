package com.mangoman.sms;

import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;

import java.util.regex.Pattern;

/**
 * Created by Harshit Rastogi on 9/23/2014 at 12:23 AM.
 */
public class SmsTemplates {

//******************************* Private Constants & Properties ********************************//

    private static final String icici_debit = "((Dear Customer, You have made a Debit Card purchase of INR)|( on \\d* \\w*. Info.[A-Z]*\\*)|(. Your Net Available Balance is INR [\\d,.]*))";
    private static final String icici_atm = "((Your Ac [\\w]* is debited with INR)|( (\\w+)\\*CASH WDL\\*[\\w\\W]*. Avbl Bal [\\w,.]* To bank on phone with iMobile, click [\\w.\\/]*))";
    private static final String icici_credit = "";

    // TO-DO : convert the following templates into the regex format

    private static final String hdfc_debit_merchant = "Thank you for using your HDFC bank DEBIT/ATM card ending ";
    private static final String hdfc_debit_atm = "((Thank you for using your HDFC Bank DEBIT/ATM Card ending (\\d+) for Rs.[\\s])|([\\s]towards ATM WDL in [\\w]* at (.+)))";
    private static final String hdfc_credit_merchant = "was spent on ur HDFCBank CREDIT Card ending";

    private static final String sbi_debit_merchant = "Thank you for using your SBI Debit Card";
    private static final String sbi_debit_atm = "Your SBI Debit Card ";

    private static final String citi_debit_merchant = "Your debit card ";
    private static final String citi_credit_merchant = "was spent on your Credit Card ";
    private static final String citi_debit_atm = "was withdrawn at an ATM from a/c";

    private static final String kotak_debit_merchant = "made on Kotak Debit card";
    private static final String kotak_credit_merchant = " has been made on Kotak credit card";
    private static final String kotak_debit_atm = "Cash withdrawal of Rs";

    public static Pattern getTemplatePattern(Bank bank, Category category) {
        switch (bank) {

            case ICICI:
                switch (category) {

                    case DEBIT_CARD_PURCHASE:
                        return Pattern.compile(icici_debit);
                    case CREDIT_CARD_PURCHASE:
                        return Pattern.compile(icici_credit);
                    case ATM_WITHDRAWAL:
                        return Pattern.compile(icici_atm);
                }
                break;
            case HDFC:
                switch (category) {
                    case DEBIT_CARD_PURCHASE:
                        return Pattern.compile(hdfc_debit_merchant);
                    case CREDIT_CARD_PURCHASE:
                        return Pattern.compile(hdfc_credit_merchant);
                    case ATM_WITHDRAWAL:
                        return Pattern.compile(hdfc_debit_atm);
                }
                break;
            case SBI:
                switch (category) {

                    case DEBIT_CARD_PURCHASE:
                        return Pattern.compile(sbi_debit_merchant);
                    case CREDIT_CARD_PURCHASE:
                        return Pattern.compile("");
                    case ATM_WITHDRAWAL:
                        return Pattern.compile(sbi_debit_atm);
                }
                break;
            case CITIBANK:
                switch (category) {
                    case DEBIT_CARD_PURCHASE:
                        return Pattern.compile(citi_debit_merchant);
                    case CREDIT_CARD_PURCHASE:
                        return Pattern.compile(citi_credit_merchant);
                    case ATM_WITHDRAWAL:
                        return Pattern.compile(citi_debit_atm);
                }
                break;
            case KOTAK:
                switch (category) {
                    case DEBIT_CARD_PURCHASE:
                        return Pattern.compile(kotak_debit_merchant);
                    case CREDIT_CARD_PURCHASE:
                        return Pattern.compile(kotak_credit_merchant);
                    case ATM_WITHDRAWAL:
                        return Pattern.compile(kotak_debit_atm);
                }
                break;
        }

        return Pattern.compile("");
    }


}

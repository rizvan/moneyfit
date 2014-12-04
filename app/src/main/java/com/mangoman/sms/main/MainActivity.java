package com.mangoman.sms.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mangoman.sms.DateRange;
import com.mangoman.sms.MessageLists;
import com.mangoman.sms.R;
import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;
import com.mangoman.sms.models.Expense;
import com.mangoman.sms.models.Sms;
import com.mangoman.sms.ui.CategoriesFragment;
import com.mangoman.sms.ui.HomeFragment;
import com.mangoman.sms.ui.SMSFragmentManager;
import com.mangoman.sms.ui.TransactionsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends Activity implements Observer, View.OnClickListener, DatePickerFragment.DateSelectedListener {


    private static String LOGTAG = "MainActivitySms";
    private FragmentManager manager = getFragmentManager();

    Button editDate1, editDate2;
    TextView date1, date2;

    public static Button editMaxAmount;
    public static TextView totalAmount;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        editDate1 = (Button) findViewById(R.id.edit_date1);
        editDate2 = (Button) findViewById(R.id.edit_date2);
        editMaxAmount = (Button) findViewById(R.id.edit_max_amount);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        totalAmount = (TextView) findViewById(R.id.total_spent_amount);
        editDate1.setOnClickListener(this);
        editDate2.setOnClickListener(this);
        editMaxAmount.setOnClickListener(this);
        readMessages();
        setInitialDates();

        SMSFragmentManager.getInstance().addObserver(this);

        try {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, SMSFragmentManager.getInstance().getActiveFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            totalAmount.setText("Rs. " + MessageLists.getAllBanksTotalBetweenDates() + " of " + MainActivity.getSavedAmount());
        } catch (Exception e) {
            Log.w(LOGTAG, "Home already loaded");
            e.printStackTrace();
        }

        showNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (SMSFragmentManager.getInstance().getActiveFragment() instanceof HomeFragment) {
            // showAppExitDialog();

            finish();

            // Toast.makeText(
            // this,
            // "You cannot exit like this. You are trapped in this forever",
            // Toast.LENGTH_LONG).show();
        } else {
            SMSFragmentManager.getInstance().setPreviousFragment();
            // super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readMessages() {

        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        c.moveToFirst();
        ArrayList<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        c.close();
        createBankList(Bank.ICICI, lstSms);
        createBankList(Bank.HDFC, lstSms);
        createBankList(Bank.SBI, lstSms);
        createBankList(Bank.CITIBANK, lstSms);
        createBankList(Bank.KOTAK, lstSms);
    }


    private void createBankList(Bank bank, ArrayList<Sms> completeList) {

        ArrayList<Expense> bankList = new ArrayList<Expense>();
        for (Sms m : completeList) {
            if (m.getAddress().contains(bank.getSmsSenderName())) {
                Category cat = SortingHat.getCategory(bank, m.getMsg());
                if (cat != null) {
                    double amountSpent = SortingHat.getAmountSpent(bank, cat, m.getMsg());
                    String merchant = SortingHat.getMechantName(bank, cat, m.getMsg());
                    Expense exObj = new Expense(bank, amountSpent, merchant, m.getTime(), cat);
                    if (amountSpent != 0) {
                        bankList.add(exObj);
                        Log.d(LOGTAG, exObj.toString());
                    }
                } else {
//                    Log.w(LOGTAG, "Category was null \n " + m.getMsg());
                }
            }
        }
        Log.d(LOGTAG, "Total Message Count for " + bank.getBankName() + " " + bankList.size());
        MessageLists.messageList.put(bank, bankList);
    }

    @Override
    public void update(Observable observable, Object data) {
        Log.i(LOGTAG, "Observer notified");
        if (data instanceof Fragment) {
            Fragment fragment = (Fragment) data;
            setActiveFragment(fragment);
        }
    }

    private void setActiveFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        SMSFragmentManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        DatePickerFragment dpf = new DatePickerFragment();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.edit_date1:
                bundle.putInt("type", 1);
                bundle.putString("initialdate", date1.getText().toString());
                dpf.setArguments(bundle);
                dpf.setListener(this);
                dpf.show(getFragmentManager(), "datePicker");
                break;

            case R.id.edit_date2:
                bundle.putInt("type", 2);
                bundle.putString("initialdate", date2.getText().toString());
                dpf.setArguments(bundle);
                dpf.setListener(this);
                dpf.show(getFragmentManager(), "datePicker");
                break;

            case R.id.edit_max_amount:
                showEnterAmountDialog();
                break;
        }
    }

    @Override
    public void onDateSelected(final long milliseconds, final int beginOrEndDate) {

        SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy");
        final String formattedDate = sd.format(milliseconds);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (beginOrEndDate) {
                    case 1:
                        date1.setText(formattedDate);
                        DateRange.fromDate = milliseconds;
                        break;

                    case 2:
                        date2.setText(formattedDate);
                        DateRange.toDate = milliseconds;
                        break;
                }

            }
        });

        refresh();
    }

    private void refresh() {
        if (SMSFragmentManager.getInstance().getActiveFragment() instanceof TransactionsFragment) {
            TransactionsFragment tf = (TransactionsFragment) SMSFragmentManager.getInstance().getActiveFragment();
            tf.refresh();
        } else if (SMSFragmentManager.getInstance().getActiveFragment() instanceof CategoriesFragment) {
            CategoriesFragment cf = (CategoriesFragment) SMSFragmentManager.getInstance().getActiveFragment();
            cf.refresh();
        }
    }

    private void setInitialDates() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sd = new SimpleDateFormat("d MM yyyy");

        try {
            Date d1 = sd.parse("1 " + (month + 1) + " " + year);
            DateRange.fromDate = d1.getTime();
            date1.setText(new SimpleDateFormat("d/MM/yyyy").format(d1.getTime()));
            Date d2 = sd.parse(day + " " + (month + 1) + " " + year);
            DateRange.toDate = d2.getTime();
            date2.setText(new SimpleDateFormat("d/MM/yyyy").format(d2.getTime()));
        } catch (Exception e) {
            Log.w(LOGTAG, "Couldn't parse date");
        }
    }


    private void showEnterAmountDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter Amount");
        alert.setMessage("Enter your spending limit");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(getSavedAmount() + "");
//        alert.setView(LayoutInflater.from(this).inflate(R.layout.dialog_layout, null));
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                saveToSharedPref(value);
                refresh();
                totalAmount.setText("Rs. " + MessageLists.getAllBanksTotalBetweenDates() + " of " + MainActivity.getSavedAmount());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void saveToSharedPref(String value) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        sp.edit().putString("amount", String.valueOf(value)).commit();
        showNotification();


    }

    public static double getSavedAmount() {
        SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String amount = sp.getString("amount", "0");
        try {
            return Double.parseDouble(amount);
        } catch (Exception e) {
            return 0;
        }
    }

    private void showNotification() {

        if ((MessageLists.getAllBanksTotalBetweenDates() + 1000) > getSavedAmount()) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    1, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.dollar_icon)
                    .setTicker("You have exceeded your spending limit")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Money Fit")
                    .setContentText("You have exceeded your spending limit");
            Notification n = builder.build();
            nm.notify(1, n);
        }
    }

}

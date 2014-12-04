package com.mangoman.sms.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mangoman.sms.MessageLists;
import com.mangoman.sms.R;
import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;
import com.mangoman.sms.main.MainActivity;
import com.mangoman.sms.models.Expense;

import java.util.ArrayList;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 8:55 PM.
 */
public class TransactionsFragment extends Fragment {

    ListView homeList;
    HomeListAdapter adapter;
    Category category;
    ArrayList<Expense> expenses = new ArrayList<Expense>();
    Bank bank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeList = (ListView) getView().findViewById(R.id.home_list);
        category = (Category) getArguments().getSerializable("category");
        bank = (Bank) getArguments().getSerializable("bank");
        expenses.addAll(MessageLists.getListBetweenDates(bank, category));
        if (expenses.isEmpty()) {
            homeList.setVisibility(View.GONE);
        } else {
            homeList.setVisibility(View.VISIBLE);
        }
        adapter = new HomeListAdapter();
        homeList.setAdapter(adapter);
        getActivity().getActionBar().setTitle(bank.getBankName() + " - " + category.getName());
        MainActivity.totalAmount.setText("Rs. " + MessageLists.getTotalAmountBetweenDates(bank, category) + " of " + MainActivity.getSavedAmount());
    }

    public void refresh() {
        expenses.clear();
        expenses.addAll(MessageLists.getListBetweenDates(bank, category));
        if (expenses.isEmpty()) {
            homeList.setVisibility(View.GONE);
        } else {
            homeList.setVisibility(View.VISIBLE);
        }

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    double expenses = MessageLists.getTotalAmountBetweenDates(bank, category);
                    double max = MainActivity.getSavedAmount();
                    MainActivity.totalAmount.setText("Rs. " + expenses + " of " + max);
                    adapter.notifyDataSetChanged();
                }
            });
    }

    public class HomeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return expenses.size();
        }

        @Override
        public Object getItem(int i) {
            return expenses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, null);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            TextView date = (TextView) convertView.findViewById(R.id.date);

            title.setText(expenses.get(i).getMerchantName());
            subtitle.setText(expenses.get(i).getFormattedDate());
            icon.setImageResource(R.drawable.dollar_icon);
            date.setText("Rs " + expenses.get(i).getAmountSpent());

            if (expenses.get(i).getAmountSpent() >= 1000) {
                date.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_dark));
            }

            return convertView;
        }
    }


}

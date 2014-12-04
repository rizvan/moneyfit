package com.mangoman.sms.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mangoman.sms.MessageLists;
import com.mangoman.sms.R;
import com.mangoman.sms.enums.Bank;
import com.mangoman.sms.enums.Category;
import com.mangoman.sms.main.MainActivity;

import java.util.ArrayList;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 8:55 PM.
 */
public class CategoriesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView homeList;
    ArrayList<Category> categories;
    HomeListAdapter adapter;
    Bank bank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeList = (ListView) getView().findViewById(R.id.home_list);
        categories = Category.getCategoryList();
        adapter = new HomeListAdapter();
        homeList.setAdapter(adapter);
        homeList.setOnItemClickListener(this);
        bank = Bank.getByName(getArguments().getString("bank"));
        getActivity().getActionBar().setTitle(bank.getBankName());
        MainActivity.totalAmount.setText("Rs. " + MessageLists.getBankTotalAllCategoriesBetweenDates(bank)+" of "+MainActivity.getSavedAmount());
    }

    public void refresh() {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double expenses = MessageLists.getBankTotalAllCategoriesBetweenDates(bank);
                    double max = MainActivity.getSavedAmount();
                    MainActivity.totalAmount.setText("Rs. " + expenses + " of " + max);

                    if (expenses >= max) {
                        MainActivity.totalAmount.setBackgroundColor(getActivity().getResources().getColor(android.R.color.holo_red_dark));
                    }
                }
            });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", categories.get(i));
        bundle.putSerializable("bank", bank);
        TransactionsFragment tf = new TransactionsFragment();
        tf.setArguments(bundle);
        SMSFragmentManager.getInstance().setNextFragment(tf);
    }


    public class HomeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int i) {
            return categories.get(i);
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

            title.setText(categories.get(i).getName());
            subtitle.setVisibility(View.GONE);

            switch (categories.get(i)) {

                case DEBIT_CARD_PURCHASE:
                    icon.setImageResource(R.drawable.debit_card_icon);
                    break;
                case CREDIT_CARD_PURCHASE:
                    icon.setImageResource(R.drawable.credit_card_icon);
                    break;
                case ATM_WITHDRAWAL:
                    icon.setImageResource(R.drawable.atm_withdrawal_icon);
                    break;
            }

            date.setVisibility(View.GONE);
            return convertView;
        }
    }


}

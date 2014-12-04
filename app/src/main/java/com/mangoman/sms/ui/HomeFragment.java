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

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 8:55 PM.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView homeList;
    CopyOnWriteArrayList<String> bankNames;
    HomeListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle("Home");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeList = (ListView) getView().findViewById(R.id.home_list);
        bankNames = Bank.getBankNameList();

        for (String bankName : bankNames) {
            if (MessageLists.messageList.get(Bank.getByName(bankName)).isEmpty()) {
                bankNames.remove(bankName);
            }
        }

        adapter = new HomeListAdapter();
        homeList.setAdapter(adapter);
        homeList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putString("bank", bankNames.get(i));
        CategoriesFragment cf = new CategoriesFragment();
        cf.setArguments(bundle);
        SMSFragmentManager.getInstance().setNextFragment(cf);
    }


    public class HomeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bankNames.size();
        }

        @Override
        public Object getItem(int i) {
            return bankNames.get(i);
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

            title.setText(bankNames.get(i));
            subtitle.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.dollar_icon);
            date.setVisibility(View.GONE);
            return convertView;
        }
    }


}

package com.mangoman.sms.ui;

import android.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;

public class SMSFragmentManager extends Observable {
    private static SMSFragmentManager instance;

    private ArrayList<Fragment> stack;
    private static final String LOGTAG = "SMSFragmentManager";

    private SMSFragmentManager() {
        stack = new ArrayList<Fragment>();
        stack.add(new HomeFragment());
    }

    public static SMSFragmentManager getInstance() {
        if (instance == null)
            instance = new SMSFragmentManager();
        return instance;
    }

    public void setNextFragment(Fragment fragment) {

        Fragment f = isFragmentInStack(fragment);

        if (f == null) {
            stack.add(0, fragment);
            notifyAllObservers(fragment);
            Log.i(LOGTAG,
                    "Setting next fragment to "
                            + fragment.getClass().getSimpleName() + " size : "
                            + stack.size()
            );
        } else {
            if (getActiveFragment().equals(f)) {
                Log.w(LOGTAG, "This fragment is already open, doing nothing");
            } else {

                stack.remove(f);
                stack.add(0, f);
                notifyAllObservers(f);
                Log.i(LOGTAG,
                        "Fragment found in stack, returning it from stack");
            }
        }
    }

    public Fragment getActiveFragment() {
        return stack.get(0);
    }

    public void setPreviousFragment() {

        Log.i(LOGTAG, "Removing fragment from activity: "
                + getActiveFragment().getClass().getSimpleName());
        // manager.beginTransaction().remove(getActiveFragment()).commit();

        Fragment fragment = getPreviousFragment();
        notifyAllObservers(fragment);
        Log.i(LOGTAG,
                "Setting previous fragment to "
                        + fragment.getClass().getSimpleName() + " size : "
                        + stack.size()
        );
    }

    public Fragment getPreviousFragment() {
        if (stack.size() <= 1)
            return stack.get(0);

        Fragment f = stack.get(1);
        if (stack.size() > 1) {
            stack.remove(0);
            Log.i(LOGTAG, "Item removed: New on top is "
                    + stack.get(0).getClass().getSimpleName() + " size : "
                    + stack.size());
        }

        return f;
    }

    public void clearBackStack() {
        stack.clear();
    }

    private void notifyAllObservers(Fragment fragment) {
        instance.setChanged();
        instance.notifyObservers(fragment);
    }

    private Fragment isFragmentInStack(Fragment fragment) {

        for (Fragment frag : stack) {
            if (fragment.getClass().getSimpleName()
                    .equals(frag.getClass().getSimpleName())) {
                return frag;
            }
        }

        return null;
    }

    public void destroy() {
        stack.clear();
        instance = null;
    }

}

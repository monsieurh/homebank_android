package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.activities.AccountListFragment;
import budget.homebank.monsieur_h.homebudget.activities.BudgetSummaryFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private AccountListFragment accountListFragment;
    private BudgetSummaryFragment budgetSummaryFragment;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        initFragments();
    }

    public void initFragments() {
        budgetSummaryFragment = new BudgetSummaryFragment();
        accountListFragment = new AccountListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return accountListFragment;
            default:
            case 1:
                return budgetSummaryFragment;
        }
    }

    @Override
    public int getCount() {
        // Show 2 pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.account_tab);
            case 1:
                return context.getResources().getString(R.string.budget_tab);
        }
        return null;
    }


}

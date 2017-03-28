package budget.homebank.monsieur_h.homebudget.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import budget.homebank.monsieur_h.homebudget.activities.AccountListFragment;
import budget.homebank.monsieur_h.homebudget.activities.BudgetSummaryFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private AccountListFragment accountListFragment;
    private BudgetSummaryFragment budgetSummaryFragment;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
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
                return "Comptes";
            case 1:
                return "Budget";//todo : translate
        }
        return null;
    }


}

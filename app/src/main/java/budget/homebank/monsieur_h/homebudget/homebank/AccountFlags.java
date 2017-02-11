package budget.homebank.monsieur_h.homebudget.homebank;

/**
 * Created by ahub on 2/11/17 for project ${PROJECT}.
 */
public class AccountFlags {
    public static int AF_CLOSED = (1 << 1);
    public static int AF_ADDED = (1 << 2);
    public static int AF_CHANGED = (1 << 3);
    public static int AF_NOSUMMARY = (1 << 4);
    public static int AF_NOBUDGET = (1 << 5);
    public static int AF_NOREPORT = (1 << 6);
    public static int AF_OLDBUDGET = (1 << 0);

}

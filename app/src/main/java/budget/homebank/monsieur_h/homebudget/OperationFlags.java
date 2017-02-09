package budget.homebank.monsieur_h.homebudget;

public class OperationFlags {

    public static int OF_OLDVALID = 1 << 0;//deprecated
    public static int OF_INCOME = 1 << 1;
    public static int OF_AUTO = 1 << 2;//scheduled
    public static int OF_ADDED = 1 << 3;//tmp flag
    public static int OF_CHANGED = 1 << 4;//tmp flag
    public static int OF_OLDREMIND = 1 << 5;//deprecated since 5.x
    public static int OF_CHEQ2 = 1 << 6;
    public static int OF_LIMIT = 1 << 7;//scheduled
    public static int OF_SPLIT = 1 << 8;

    /*
    from transaction.h
    OF_OLDVALID 1<<0 //deprecated
    OF_INCOME 1<<1
    OF_AUTO 1<<2 //scheduled
    OF_ADDED 1<<3 //tmp flag
    OF_CHANGED 1<<4 //tmp flag
    OF_OLDREMIND 1<<5 //deprecated since 5.x
    OF_CHEQ2 1<<6
    OF_LIMIT 1<<7 //scheduled
    OF_SPLIT 1<<8

    typedef enum{
    TXN_STATUS_NONE
    TXN_STATUS_CLEARED
    TXN_STATUS_RECONCILED
    TXN_STATUS_REMIND
    } HbTxnStatus
     */
}

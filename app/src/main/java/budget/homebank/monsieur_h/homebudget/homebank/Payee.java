package budget.homebank.monsieur_h.homebudget.homebank;

public class Payee {
    private final int key;
    private final String name;
    private XHB xhb;

    public Payee(int key, String name) {

        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Payee{" +
                "key=" + key +
                ", name='" + name + '\'' +
                '}';
    }

    public void setXhb(XHB xhb) {
        this.xhb = xhb;
    }
}

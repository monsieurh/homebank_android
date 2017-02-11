package budget.homebank.monsieur_h.homebudget.homebank;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final int key;
    private final String name;
    private List<Operation> operations = new ArrayList<Operation>();
    private int flags;

    public Account(int key, String name) {

        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(int flag) {
        return (this.flags & flag) != 0;//todo extends flaggable !
    }

    public void addOperation(Operation op) {
        operations.add(op);
    }
}

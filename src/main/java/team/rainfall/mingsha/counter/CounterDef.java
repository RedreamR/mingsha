package team.rainfall.mingsha.counter;

import java.io.Serializable;

public class CounterDef implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name = "";
    public int initialValue = 0;
    public String displayName = "";

    public CounterDef() {
    }

    public CounterDef(String name, int initialValue, String displayName) {
        this.name = name == null ? "" : name;
        this.initialValue = initialValue;
        this.displayName = displayName == null ? "" : displayName;
    }
}

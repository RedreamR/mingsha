package team.rainfall.mingsha.counter;

import java.io.Serializable;

public class Counter implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String CODENAME = "MSCTR";
    public static final String PREFIX = "$$" + CODENAME + "_";

    public int iCivID = -1;
    public String name = "";
    public int value = 0;

    public Counter() {
    }

    public Counter(String rawEntry) {
        if (rawEntry != null && rawEntry.startsWith(PREFIX)) {
            String body = rawEntry.substring(PREFIX.length());
            int eq = body.lastIndexOf('=');
            if (eq > 0) {
                name = body.substring(0, eq);
                try {
                    value = Integer.parseInt(body.substring(eq + 1));
                } catch (NumberFormatException e) {
                    value = 0;
                }
            }
        }
    }

    public String getRaw() {
        return PREFIX + name + "=" + value;
    }

    public static String entryPrefixOf(String counterName) {
        return PREFIX + counterName + "=";
    }
}

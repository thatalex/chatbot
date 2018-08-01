package by.bgtu.model;

import java.util.Set;

public enum Util {
    ;
    public static final String SPLIT_EXPRESION = "[.,\"\"«»:\\s]+";
    public static final String SPLIT_SENTENCE = "[;!?s]+";

    public static String toString(Set<?> objects) {
        if (objects != null && !objects.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Object o : objects) {
                builder.append(o).append(", ");
            }

            builder.setLength(builder.length() - 2);
            return builder.toString();
        }
        return "";
    }
}

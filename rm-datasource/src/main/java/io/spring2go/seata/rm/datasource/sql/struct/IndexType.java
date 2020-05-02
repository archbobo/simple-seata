package io.spring2go.seata.rm.datasource.sql.struct;

/**
 * Created by william on May, 2020
 */
public enum IndexType {
    /** */
    PRIMARY(0),
    /** */
    Normal(1),
    /** */
    Unique(2),
    /** */
    FullText(3);

    private int i;

    IndexType(int i) {
        this.i = i;
    }

    public int value() {
        return this.i;
    }

    public static IndexType valueOf(int i) {
        for (IndexType t : values()) {
            if (t.value() == i) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid IndexType:" + i);
    }
}

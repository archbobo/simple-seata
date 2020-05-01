package io.spring2go.seata.core.model;

/**
 * Created by william on May, 2020
 */
public enum BranchType {
    // AT Branch
    AT,

    // MT Branch
    MT;

    public static BranchType get(byte ordinal) {
        return get((int) ordinal);
    }
    public static BranchType get(int ordinal) {
        for (BranchType branchType : BranchType.values()) {
            if (branchType.ordinal() == ordinal) {
                return branchType;
            }
        }
        throw new IllegalArgumentException("Unknown BranchType[" + ordinal + "]");
    }
}

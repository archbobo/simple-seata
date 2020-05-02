package io.spring2go.seata.rm.datasource.sql.struct;

/**
 * Created by william on May, 2020
 */
public class Null {
    private static Null instance = new Null();

    public static Null get() {
        return instance;
    }

    private Null() {
    }

    @Override
    public String toString() {
        return "NULL";
    }
}

package io.spring2go.seata.rm.datasource.sql.struct;

/**
 * Created by william on May, 2020
 */
public class Field {
    public String name;

    private KeyType keyType = KeyType.NULL;

    public int type;

    public Object value;

    public Field() {
    }

    public Field(String name, int type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String attrName) {
        this.name = attrName;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public int getType() {
        return type;
    }

    public void setType(int attrType) {
        this.type = attrType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isKey(String pkname) {
        return name.equalsIgnoreCase(pkname);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", name, String.valueOf(value));
    }
}

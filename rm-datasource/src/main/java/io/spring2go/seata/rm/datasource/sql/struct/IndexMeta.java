package io.spring2go.seata.rm.datasource.sql.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class IndexMeta {
    private List<ColumnMeta> values = new ArrayList<ColumnMeta>();

    private boolean nonUnique;
    private String indexQualifier;
    private String indexName;
    private short type;
    private IndexType indextype;
    private String ascOrDesc;
    private int cardinality;
    private int ordinalPosition;

    public IndexMeta() {
    }

    public List<ColumnMeta> getValues() {
        return values;
    }

    public void setValues(List<ColumnMeta> values) {
        this.values = values;
    }

    public boolean isNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    public String getIndexQualifier() {
        return indexQualifier;
    }

    public void setIndexQualifier(String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public IndexType getIndextype() {
        return indextype;
    }

    public void setIndextype(IndexType indextype) {
        this.indextype = indextype;
    }

    public List<ColumnMeta> getIndexvalue() {
        return values;
    }

    @Override
    public String toString() {
        return "indexName:" + indexName + "->" + "type:" + type + "->" + "values:" + values;
    }
}

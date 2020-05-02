package io.spring2go.seata.rm.datasource.sql.struct;

import io.spring2go.seata.common.exception.NotSupportYetException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class Row {
    private List<Field> fields = new ArrayList<Field>();

    public Row() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void add(Field field) {
        fields.add(field);
    }

    public List<Field> primaryKeys() {
        List<Field> pkFields = new ArrayList<>();
        for (Field field : fields) {
            if (KeyType.PrimaryKey == field.getKeyType()) {
                pkFields.add(field);
            }
        }
        if (pkFields.size() > 1) {
            throw new NotSupportYetException("Multi-PK");
        }
        return pkFields;
    }
}

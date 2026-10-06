package org.digijava.kernel.ampapi.endpoints.activity.field;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Nadejda Mandrescu
 */
public enum FieldType {
    STRING("string"),
    LONG("long"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    DATE("date"),
    TIMESTAMP("timestamp"),
    LIST("list"),
    OBJECT("object");

    @JsonValue
    private String name;

    FieldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSimpleType() {
        return !(this.isList() || this.isObject());
    }

    public boolean isList() {
        return FieldType.LIST.equals(this);
    }

    public boolean isObject() {
        return FieldType.OBJECT.equals(this);
    }
    
    public boolean isTimestampType() {
        return FieldType.TIMESTAMP.equals(this);
    }
    
    public boolean isDateType() {
        return FieldType.DATE.equals(this);
    }
    
    public boolean isStringType() {
        return FieldType.STRING.equals(this);
    }

    @Override
    public String toString() {
        return this.name;
    }

}

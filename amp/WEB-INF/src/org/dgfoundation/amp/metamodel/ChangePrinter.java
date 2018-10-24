package org.dgfoundation.amp.metamodel;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.metamodel.diff.AttributeChange;
import org.dgfoundation.amp.metamodel.diff.Change;
import org.dgfoundation.amp.metamodel.diff.CollectionChange;
import org.dgfoundation.amp.metamodel.diff.ObjectChange;
import org.dgfoundation.amp.metamodel.diff.ValueChange;
import org.dgfoundation.amp.metamodel.type.Attribute;
import org.dgfoundation.amp.metamodel.type.ValueType;

/**
 * Given change print it in such a way that it's readable in console. Akin to unified diff format.
 *
 * FIXME remove inefficient str concatenation
 *
 * @author Octavian Ciubotaru
 */
public class ChangePrinter {

    private static final int FIELD_COL_WIDTH = 30;
    private static final int VALUE_COL_WIDTH = 35;
    private static final int INDENT_STEP = 2;

    public String print(Change change) {
        return print(change, 0);
    }

    public String print(Change change, int indent) {
        if (change instanceof ObjectChange) {
            return printObject((ObjectChange) change, indent);
        } else if (change instanceof ValueChange) {
            return printSimpleChange((ValueChange) change);
        } else if (change instanceof CollectionChange) {
            return printCollectionChange((CollectionChange) change, indent);
        } else {
            throw new RuntimeException();
        }
    }

    private String printCollectionChange(CollectionChange change, int indent) {
        String out = "";
        for (int i = 0; i < change.size(); i++) {
            Change itemChange = change.get(i);
            out += itemChange.getChangeType().getTextualMarker()
                    + StringUtils.repeat(' ', indent + INDENT_STEP) + "#" + (i + 1) + "\n";
            out += print(itemChange, indent + 2 * INDENT_STEP);
        }
        return out;
    }

    private String printSimpleChange(ValueChange change) {
        return rightPadAndAbbreviate(StringUtils.defaultString(change.getOldValue()), VALUE_COL_WIDTH)
                + rightPadAndAbbreviate(StringUtils.defaultString(change.getNewValue()), VALUE_COL_WIDTH);
    }

    private String rightPadAndAbbreviate(String str, int len) {
        return StringUtils.rightPad(StringUtils.abbreviate(str, len), len);
    }

    public String printObject(ObjectChange change, int indent) {
        String out = "";
        String prefix = StringUtils.repeat(' ', indent);

        for (Attribute attribute : change.getObjectType()) {

            AttributeChange attributeChange = change.getChange(attribute.getName());
            if (attributeChange != null) {
                out += attributeChange.getChange().getChangeType().getTextualMarker();
                out += rightPadAndAbbreviate(prefix + attributeChange.getAttribute().getName(), FIELD_COL_WIDTH);
                if (attributeChange.getChange() instanceof ValueChange) {
                    out += printSimpleChange((ValueChange) attributeChange.getChange()) + "\n";
                } else {
                    out += "\n" + print(attributeChange.getChange(), indent);
                }
            } else if (attribute.getType() instanceof ValueType) {
                // TODO display additionally only some of the fields, now it's too verbose
                Object value = attribute.get(Optional.ofNullable(change.getOldObject()).orElse(change.getNewObject()));
                if (value != null) {
                    String strValue = value.toString();
                    out += " " + rightPadAndAbbreviate(prefix + attribute.getName(), FIELD_COL_WIDTH)
                            + rightPadAndAbbreviate(strValue, VALUE_COL_WIDTH)
                            + rightPadAndAbbreviate(strValue, VALUE_COL_WIDTH)
                            + "\n";
                }
            }
        }

        return out;
    }
}

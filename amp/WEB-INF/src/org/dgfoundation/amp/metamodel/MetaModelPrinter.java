package org.dgfoundation.amp.metamodel;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.metamodel.type.CollectionType;
import org.dgfoundation.amp.metamodel.type.ObjectType;
import org.dgfoundation.amp.metamodel.type.Type;
import org.dgfoundation.amp.metamodel.type.ValueType;

/**
 * @author Octavian Ciubotaru
 */
public class MetaModelPrinter {

    private int indentStep = 2;

    public void print(Object obj, Type type) {
        print(obj, type, 0);
    }

    private void print(Object obj, Type type, int indent) {
        if (type instanceof ObjectType) {
            printObj(obj, (ObjectType) type, indent);
        } else if (type instanceof CollectionType) {
            printCol((Collection) obj, (CollectionType) type, indent);
        } else if (type instanceof ValueType) {
            System.out.println(StringUtils.repeat(' ', indent) + obj);
        } else {
            throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private void printCol(Collection obj, CollectionType type, int indent) {
        if (obj != null) {
            ((Collection<?>) obj).forEach(el -> print(el, type.getElementType(), indent));
        }
    }

    private void printObj(Object obj, ObjectType type, int indent) {
        String prefix = StringUtils.repeat(' ', indent);
        type.forEach(a -> {
            Object value = a.get(obj);
            if (value != null && !(value instanceof Collection && ((Collection) value).isEmpty())) {
                System.out.print(prefix + a.getName() + ": ");
                if (a.getType() instanceof ValueType) {
                    print(value, a.getType(), 0);
                } else {
                    System.out.println();
                    print(value, a.getType(), indent + indentStep);
                }
            }
        });
    }
}

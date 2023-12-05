package org.digijava.kernel.ampapi.endpoints.common.field;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpContact;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public final class FieldMap {

    private static Map<String, String> titleToUnderscoreMap = new HashMap<String, String>();

    /**
     * map from discriminated field name (i.e. "orgroles") to actual field title (i.e. "Donor Organization")
     */
    private static Map<String, List<String>> discriminatedFieldTitlesByFieldName = new HashMap<>();

    static {
        FieldMap.addUnderscoredTitlesToMap(AmpActivityFields.class);
        FieldMap.addUnderscoredTitlesToMap(AmpContact.class);
        FieldMap.addUnderscoredTitlesToMap(AmpResource.class);
    }

    private FieldMap() {
    }

    public static Map<String, List<String>> getDiscriminatedFieldTitlesByFieldName() {
        return discriminatedFieldTitlesByFieldName;
    }

    /**
     * Maps every title in the AF fields model to an underscorified version
     * TODO: this section needs heavy rewrite -- we hope to remove the need to underscorify at all
     * and use the Field Label separately from the Field Title
     * @param clazz
     */
    static void addUnderscoredTitlesToMap(Class<?> clazz) {
        for (Field field : InterchangeUtils.getFieldsAnnotatedWith(clazz, Interchangeable.class,
                InterchangeableDiscriminator.class)) {

            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            if (ant != null) {
                titleToUnderscoreMap.put(ant.fieldTitle(), underscorify(ant.fieldTitle()));
                if (!InterchangeUtils.isSimpleType(InterchangeUtils.getClassOfField(field)) && !ant.pickIdOnly()) {
                    addUnderscoredTitlesToMap(InterchangeUtils.getClassOfField(field));
                }
            }

            InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
            if (antd != null) {
                Interchangeable[] settings = antd.settings();
                for (Interchangeable ants : settings) {
                    titleToUnderscoreMap.put(ants.fieldTitle(), underscorify(ants.fieldTitle()));
                    discriminatedFieldTitlesByFieldName
                            .computeIfAbsent(field.getName(), z -> new ArrayList<>())
                            .add(underscorify(ants.fieldTitle()));
                }
                if (!InterchangeUtils.isSimpleType(InterchangeUtils.getClassOfField(field))) {
                    addUnderscoredTitlesToMap(InterchangeUtils.getClassOfField(field));
                }
            }
        }
    }

    /**
     * converts the uppercase letters of a string to underscore + lowercase (except for first one)
     *
     * @param input String to be converted
     * @return converted string
     */
    public static String underscorify(String input) {
        if (titleToUnderscoreMap.containsKey(input)) {
            return titleToUnderscoreMap.get(input);
        }
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                bld.append('_');
            } else {
                bld.append(Character.toLowerCase(input.charAt(i)));
            }
        }
        return bld.toString();
    }
}

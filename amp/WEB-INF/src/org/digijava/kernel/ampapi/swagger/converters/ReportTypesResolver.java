package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.ImmutableMap;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.digijava.kernel.ampapi.swagger.types.ErrorOrWarningPH;
import org.digijava.kernel.ampapi.swagger.types.FiltersPH;
import org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH;
import org.digijava.kernel.ampapi.swagger.types.PublicHeadersPH;
import org.digijava.kernel.ampapi.swagger.types.PublicTopDataPH;
import org.digijava.kernel.ampapi.swagger.types.PublicTopTotalsPH;
import org.digijava.kernel.ampapi.swagger.types.SettingsPH;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.Json;

/**
 * @author Octavian Ciubotaru
 */
public class ReportTypesResolver extends AbstractModelConverter {

    private static final int EXAMPLE_STATUS_AWAITING_IMPLEMENTATION = 2172;
    private static final int EXAMPLE_STATUS_AWAITING_INTERRUPTED = 2186;
    private static final int EXAMPLE_STATUS_AWAITING_ONGOING = 2123;

    public ReportTypesResolver() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = _mapper.constructType(type);

        if (javaType.getRawClass().isAssignableFrom(ReportFilters.class)) {
            ModelImpl model = new ModelImpl();
            model.name("ReportFilters");
            model.additionalProperties(new RefProperty("FilterRule"));

            context.defineModel("ReportFilters", model);

            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(FiltersPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("FiltersPH");
            model.additionalProperties(new ObjectProperty());
            model.description("Report filters.");
            HashMap<Object, Object> example = new HashMap<>();
            example.put("status", Arrays.asList(
                    EXAMPLE_STATUS_AWAITING_IMPLEMENTATION,
                    EXAMPLE_STATUS_AWAITING_INTERRUPTED,
                    EXAMPLE_STATUS_AWAITING_ONGOING));
            example.put("date", ImmutableMap.of("start", "2016-07-01", "end", "2018-07-01"));
            model.example(example);
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(SettingsPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("SettingsPH");
            model.description("Report settings.");
            model.additionalProperties(new ObjectProperty());
            HashMap<Object, Object> example = new HashMap<>();
            example.put("currency-code", "USD");
            example.put("calendar-id", "4");
            model.example(example);
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(MultilingualLabelPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("MultilingualLabelPH");
            model.description("Multilingual label.");
            model.additionalProperties(new ObjectProperty());
            HashMap<Object, Object> example = new HashMap<>();
            example.put("en", "English label");
            example.put("fr", "French label");
            model.example(example);
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(PublicHeadersPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("HeadersPH");
            model.description("Headers of { key: displayValue } pairs.");
            model.additionalProperties(new ObjectProperty());
            HashMap<Object, Object> example = new HashMap<>();
            example.put("donor-agency", "Donor Agency");
            example.put("actual-commitments", "Actual Commitments");
            model.example(example);
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(PublicTopTotalsPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("PublicTopTotalsPH");
            model.description("Totals relevant for the current top results.");
            model.additionalProperties(new ObjectProperty());
            HashMap<Object, Object> example = new HashMap<>();
            double magicNumberWarnWorkaround = 12717179650.207333;
            example.put("Total Actual Commitments", magicNumberWarnWorkaround);
            model.example(example);
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(PublicTopDataPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("PublicTopDataPH");
            model.description("Top result data.");
            model.additionalProperties(new ObjectProperty());
            HashMap<Object, Object> example = new HashMap<>();
            example.put("actual-commitments", "2 176 576 698");
            example.put("donor-agency", "Agence des États-Unis pour le développement international");
            model.example(Arrays.asList(example));
            return model;
        }

        if (javaType.getRawClass().isAssignableFrom(ErrorOrWarningPH.class)) {
            ModelImpl model = new ModelImpl();
            model.name("ErrorOrWarningPH");
            model.description("The list of messages coded by AABB format, where AA is the package id and BB is"
                    + " the error code inside that package. 00 is the global/generic package.");
            model.additionalProperties(new ObjectProperty());
            model.example(Collections.singletonMap(
                    "0101",
                    Arrays.asList(Collections.singletonMap("Required field", Arrays.asList("activity_status")))));
            return model;
        }

        return super.resolve(type, context, chain);
    }

}

package org.dgfoundation.amp.onepager.converters;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractNumberConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by esoliani on 24/03/16.
 */
public final class CustomDoubleConverter extends AbstractNumberConverter<Double> {

    public static final CustomDoubleConverter INSTANCE = new CustomDoubleConverter();

    @Override
    public NumberFormat getNumberFormat(Locale locale) {
        return FormatHelper.getDecimalFormat(true);
    }

    @Override
    protected Class<Double> getTargetType() {
        return Double.class;
    }

    @Override
    public Double convertToObject(String value, Locale locale) throws ConversionException {
        try {
            DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
            NumberFormat formatter = FormatHelper.getDecimalFormat(true);
            converter.setNumberFormat(locale, formatter);
            return converter.convertToObject(value, locale);
        } catch (ConversionException e) {
            throw newCustomMessageException(e);
        }
    }

    private static ConversionException newCustomMessageException(final ConversionException e) {
        final ConversionException conv = new ConversionException("DoubleFormatConverter", e);
        conv.setResourceKey("DoubleFormatConverter");
        conv.setVariable("format", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT));
        return conv;
    }
}

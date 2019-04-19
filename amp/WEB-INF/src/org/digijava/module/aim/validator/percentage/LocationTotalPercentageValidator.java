package org.digijava.module.aim.validator.percentage;

import javax.validation.ConstraintValidatorContext;

import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityLocation;

/**
 * @author Octavian Ciubotaru
 */
public class LocationTotalPercentageValidator
        extends AbstractTotalPercentageValidator<LocationTotalPercentage, AmpActivityFields, Boolean> {


    @Override
    public Iterable getCollection(AmpActivityFields value) {
        return value.getLocations();
    }

    @Override
    public Boolean getItemGroup(Object collectionItem) {
        return Boolean.TRUE;
    }

    @Override
    public Float getPercentage(Object collectionItem) {
        return ((AmpActivityLocation) collectionItem).getLocationPercentage();
    }

    @Override
    public void addConstraintViolationForGroup(ConstraintValidatorContext context, Boolean group) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode("locations")
                .addNode("locationPercentage")
                        .inIterable()
                .addConstraintViolation();
    }
}

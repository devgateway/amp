package org.dgfoundation.amp.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;
import org.dgfoundation.amp.metamodel.type.Attribute;
import org.dgfoundation.amp.metamodel.type.CollectionType;
import org.dgfoundation.amp.metamodel.type.ObjectType;
import org.dgfoundation.amp.metamodel.type.ValueType;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;

/**
 * Defines the AMP metamodel.
 *
 * Improvements:
 *  - make it follow FM rules, however this is not required for {@link MetaModelDiffer}
 *
 * @author Octavian Ciubotaru
 */
public class AmpMetaModel {

    public ObjectType activity() {
        List<Attribute> attrs = new ArrayList<>();
        attrs.add(new Attribute(beanProperty("name"), ActivityFieldsConstants.PROJECT_TITLE, new ValueType()));
        attrs.add(new Attribute(beanProperty("projectCode"), ActivityFieldsConstants.PROJECT_CODE, new ValueType()));
        attrs.add(new Attribute(beanProperty("funding"), "Fundings", new CollectionType(funding())));
        return new ObjectType(attrs);
    }

    public ObjectType funding() {
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new Attribute(
                beanProperty("ampDonorOrgId"),
                "From",
                new ValueType()));

        attributes.add(new Attribute(
                beanProperty("sourceRole"),
                "As",
                new ValueType()));

        attributes.add(new Attribute(
                beanProperty("fundingDetails").andThen(transactionTypeFilter(Constants.COMMITMENT)),
                "Commitments",
                new CollectionType(commitment())));

        attributes.add(new Attribute(
                beanProperty("fundingDetails").andThen(transactionTypeFilter(Constants.DISBURSEMENT)),
                "Disbursements",
                new CollectionType(disbursement())));

        return new ObjectType(attributes);
    }

    private ObjectType commitment() {
        List<Attribute> attributes = new ArrayList<>();
        addAmountTrio(attributes);
        return new ObjectType(attributes);
    }

    private ObjectType disbursement() {
        List<Attribute> attributes = new ArrayList<>();
        addAmountTrio(attributes);
        attributes.add(new Attribute(beanProperty("disbursementOrderRejected"),
                "Disbursement Order Rejected", new ValueType()));
        return new ObjectType(attributes);
    }

    private void addAmountTrio(List<Attribute> attributes) {
        attributes.add(new Attribute(beanProperty("transactionAmount"), "Amount", new ValueType()));
        attributes.add(new Attribute(beanProperty("ampCurrencyId"), "Currency", new ValueType()));
        attributes.add(new Attribute(beanProperty("transactionDate"), "Transaction Date", new ValueType()));
    }

    private Function<Object, Object> beanProperty(String name) {
        return o -> BeanUtils.getBeanProperty(o, name);
    }

    private Function<Object, List<Object>> transactionTypeFilter(Integer transactionType) {
        return o -> {
            if (o == null) {
                return null;
            } else {
                return ((Collection<?>) o).stream()
                        .filter(fundingDetail ->
                                ((AmpFundingDetail) fundingDetail).getTransactionType().equals(transactionType))
                        .collect(Collectors.toList());
            }
        };
    }
}

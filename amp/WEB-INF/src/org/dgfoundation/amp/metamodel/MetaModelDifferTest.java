package org.dgfoundation.amp.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Date;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.metamodel.diff.AttributeChange;
import org.dgfoundation.amp.metamodel.diff.CollectionChange;
import org.dgfoundation.amp.metamodel.diff.ObjectChange;
import org.dgfoundation.amp.metamodel.diff.ValueChange;
import org.dgfoundation.amp.metamodel.type.Attribute;
import org.dgfoundation.amp.metamodel.type.CollectionType;
import org.dgfoundation.amp.metamodel.type.ObjectType;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class MetaModelDifferTest {

    private ObjectType activityType = new AmpMetaModel().activity();
    private ObjectType fundingType = new AmpMetaModel().funding();

    @Test
    public void nothingChanged() {
        AmpActivityVersion oldAct = new AmpActivityVersion();
        oldAct.setName("a title");

        AmpActivityVersion newAct = new AmpActivityVersion();
        newAct.setName("a title");

        ObjectChange diff = new MetaModelDiffer().diff(oldAct, newAct, activityType);

        assertNull(diff);
    }

    @Test
    public void titleChanged() {
        AmpActivityVersion oldAct = new AmpActivityVersion();
        oldAct.setName("old activity");

        AmpActivityVersion newAct = new AmpActivityVersion();
        newAct.setName("new activity");

        ObjectChange actualChange = new MetaModelDiffer().diff(oldAct, newAct, activityType);

        Attribute projectTitle = activityType.getAttribute("Project Title");
        ObjectChange expectedChange = new ObjectChange(oldAct, newAct, activityType,
                new AttributeChange(projectTitle, new ValueChange("old activity", "new activity")));

        assertEquals(expectedChange, actualChange);
    }

    @Test
    public void objectListDiff_SameId() {
        AmpOrganisation worldBank = org("World Bank");
        AmpOrganisation eu = org("European Union");

        AmpFunding oldFunding = new AmpFunding();
        oldFunding.setAmpFundingId(1L);
        oldFunding.setAmpDonorOrgId(worldBank);

        AmpFunding newFunding = new AmpFunding();
        newFunding.setAmpFundingId(2L);
        newFunding.setOriginalObjectId(1L);
        newFunding.setAmpDonorOrgId(eu);

        CollectionChange diff = new MetaModelDiffer().diff(
                Collections.singletonList(oldFunding),
                Collections.singletonList(newFunding), new CollectionType(fundingType));

        Attribute fromAttr = fundingType.getAttribute("From");

        CollectionChange expectedDiff = new CollectionChange(
                new ObjectChange(oldFunding, newFunding, fundingType,
                        new AttributeChange(fromAttr,
                                new ValueChange("World Bank", "European Union"))));

        assertEquals(expectedDiff, diff);
    }

    @Test
    public void objectListDiff_DifferentIds() {
        AmpOrganisation worldBank = org("World Bank");
        AmpOrganisation eu = org("European Union");

        AmpFunding oldFunding = new AmpFunding();
        oldFunding.setAmpFundingId(1L);
        oldFunding.setAmpDonorOrgId(worldBank);

        AmpFunding newFunding = new AmpFunding();
        newFunding.setAmpFundingId(2L);
        newFunding.setAmpDonorOrgId(eu);

        CollectionChange diff = new MetaModelDiffer().diff(
                Collections.singletonList(oldFunding),
                Collections.singletonList(newFunding),
                new CollectionType(fundingType));

        Attribute fromAttr = fundingType.getAttribute("From");

        CollectionChange expectedDiff = new CollectionChange(
                new ObjectChange(oldFunding, null, fundingType,
                        new AttributeChange(fromAttr, new ValueChange("World Bank", null))),
                new ObjectChange(null, newFunding, fundingType,
                        new AttributeChange(fromAttr, new ValueChange(null, "European Union"))));

        assertEquals(expectedDiff, diff);
    }

    @Test
    public void nestedChange() {
        AmpOrganisation worldBank = org("World Bank");
        AmpOrganisation eu = org("European Union");

        AmpFunding oldFunding = new AmpFunding();
        oldFunding.setAmpFundingId(1L);
        oldFunding.setAmpDonorOrgId(worldBank);

        AmpActivity oldActivity = new AmpActivity();
        oldActivity.setFunding(Collections.singleton(oldFunding));

        AmpFunding newFunding = new AmpFunding();
        newFunding.setAmpFundingId(2L);
        newFunding.setOriginalObjectId(1L);
        newFunding.setAmpDonorOrgId(eu);

        AmpActivity newActivity = new AmpActivity();
        newActivity.setFunding(Collections.singleton(newFunding));

        ObjectChange diff = new MetaModelDiffer().diff(oldActivity, newActivity, activityType);

        Attribute fundingAttr = activityType.getAttribute("Fundings");
        ObjectType fundingType = (ObjectType) ((CollectionType) fundingAttr.getType()).getElementType();
        Attribute fromAttr = fundingType.getAttribute("From");

        ObjectChange expectedChange = new ObjectChange(oldActivity, newActivity, activityType,
                new AttributeChange(fundingAttr,
                        new CollectionChange(
                                new ObjectChange(oldFunding, newFunding, fundingType,
                                        new AttributeChange(fromAttr,
                                                new ValueChange("World Bank", "European Union"))))));

        assertEquals(expectedChange, diff);
    }

    @Test
    public void printComplexDiff() {
        AmpOrganisation worldBank = org("World Bank");

        AmpFunding oldFunding = new AmpFunding();
        oldFunding.setAmpFundingId(1L);
        oldFunding.setAmpDonorOrgId(worldBank);
        oldFunding.setSourceRole(role("Donor"));

        AmpFunding oldFunding3 = new AmpFunding();
        oldFunding3.setAmpFundingId(4L);
        oldFunding3.setAmpDonorOrgId(worldBank);
        oldFunding3.setSourceRole(role("Implementing Agency"));
        oldFunding3.setFundingDetails(ImmutableSet.of(
                commitment(1L, null, 1d, "USD", new Date()),
                commitment(2L, null, 2d, "USD", new Date()),
                commitment(3L, null, 3d, "USD", new Date())));

        AmpActivity oldActivity = new AmpActivity();
        oldActivity.setName("test versioning 1");
        oldActivity.setProjectCode("007");
        oldActivity.setFunding(ImmutableSet.of(oldFunding, oldFunding3));

        AmpFunding newFunding = new AmpFunding();
        newFunding.setAmpFundingId(2L);
        newFunding.setOriginalObjectId(1L);
        newFunding.setAmpDonorOrgId(org("European Union"));
        newFunding.setSourceRole(role("Donor"));

        AmpFunding newFunding2 = new AmpFunding();
        newFunding2.setAmpFundingId(3L);
        newFunding2.setAmpDonorOrgId(org("USAID"));
        newFunding2.setSourceRole(role("Donor"));
        newFunding2.setFundingDetails(ImmutableSet.of(
                commitment(4L, null,50000d, "USD", new Date()),
                disbursement(5L, null, 10000d, "XOF", new Date())));

        AmpFunding newFunding3 = new AmpFunding();
        newFunding3.setAmpFundingId(5L);
        newFunding3.setOriginalObjectId(4L);
        newFunding3.setAmpDonorOrgId(worldBank);
        newFunding3.setSourceRole(role("Implementing Agency"));
        newFunding3.setFundingDetails(ImmutableSet.of(
                commitment(6L, 1L,1d, "USD", new Date()),
                commitment(7L, 2L, 20d, "USD", new Date())));

        AmpActivity newActivity = new AmpActivity();
        newActivity.setName("test versioning 2");
        newActivity.setProjectCode("007");
        newActivity.setFunding(ImmutableSet.of(newFunding, newFunding2, newFunding3));

        ObjectChange diff = new MetaModelDiffer().diff(oldActivity, newActivity, activityType);

        System.out.println(new ChangePrinter().print(diff));

        MetaModelPrinter printer = new MetaModelPrinter();

        printer.print(oldActivity, activityType);

        printer.print(newActivity, activityType);
    }

    private AmpFundingDetail commitment(Long id, Long originalObjectId, double amount, String currCode, Date date) {
        AmpFundingDetail fundDetail = fundingDetail(id, originalObjectId, amount, currCode, date);
        fundDetail.setTransactionType(Constants.COMMITMENT);
        return fundDetail;
    }

    private AmpFundingDetail disbursement(Long id, Long originalObjectId, double amount, String currCode, Date date) {
        AmpFundingDetail fundDetail = fundingDetail(id, originalObjectId, amount, currCode, date);
        fundDetail.setTransactionType(Constants.DISBURSEMENT);
        return fundDetail;
    }

    private AmpFundingDetail fundingDetail(Long id, Long originalObjectId, double amount, String currCode, Date date) {
        AmpFundingDetail fundDetail = new AmpFundingDetail();
        fundDetail.setAmpFundDetailId(id);
        fundDetail.setOriginalObjectId(originalObjectId);
        fundDetail.setAmpCurrencyId(currency(currCode));
        fundDetail.setTransactionAmount(amount);
        fundDetail.setTransactionDate(date);
        return fundDetail;
    }

    private AmpCurrency currency(String code) {
        AmpCurrency currency = new AmpCurrency();
        currency.setCurrencyCode(code);
        return currency;
    }

    private AmpRole role(String name) {
        AmpRole role = new AmpRole();
        role.setName(name);
        return role;
    }

    private AmpOrganisation org(String name) {
        AmpOrganisation worldBank = new AmpOrganisation();
        worldBank.setName(name);
        return worldBank;
    }
}

package org.digijava.module.dataExchange.webservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.dataExchange.util.ExportBuilder;
import org.digijava.module.dataExchange.util.ExportUtil;

public class WsHelper {

	public static Activities GenerateWsExport() {
		Activities activities = (new ObjectFactory()).createActivities();
		List<AmpActivity> ampActivities = (List<AmpActivity>) ExportUtil
				.getWsActivities();
		for (Iterator iterator = ampActivities.iterator(); iterator.hasNext();) {
			ExportBuilder eBuilder = null;
			AmpActivity ampActivity = (AmpActivity) iterator.next();

			eBuilder = new ExportBuilder(ampActivity, "3");
			AmpColumnEntry columns = new AmpColumnEntry();
			columns.setKey("activityTree.select");
			columns.setName("activity");
			columns.setSelect(true);
			
			AmpColumnEntry colid = new AmpColumnEntry("activityTree.elements[0].select","id","activity.id");
			colid.setSelect(true);
			colid.setMandatory(true);
			
			AmpColumnEntry coltitle = new AmpColumnEntry("activityTree.elements[1].select","title","activity.title");
			coltitle.setSelect(true);
			coltitle.setMandatory(true);
			
			AmpColumnEntry colobjetive = new AmpColumnEntry("activityTree.elements[2].select","objective","activity.objective");
			colobjetive.setSelect(true);
			colobjetive.setMandatory(true);

			AmpColumnEntry coldescription = new AmpColumnEntry("activityTree.elements[3].select","description","activity.description");
			coldescription.setSelect(true);
			coldescription.setMandatory(true);
			
			//location elemenet
			AmpColumnEntry collocation = new AmpColumnEntry("activityTree.elements[4].select","location","activity.location");
			collocation.setSelect(true);
			collocation.setMandatory(true);
			
			ArrayList<AmpColumnEntry>locationitems = new ArrayList<AmpColumnEntry>();
			AmpColumnEntry locationname = new AmpColumnEntry("activityTree.elements[4].elements[0].select","locationname","activity.location.locationName");
			locationname.setName("locationname");
			locationname.setSelect(true);
			locationname.setMandatory(false);
			
			AmpColumnEntry locationFunding = new AmpColumnEntry("activityTree.elements[4].elements[1].select","locationFunding","activity.location.locationFunding");
			locationFunding.setName("locationFunding");
			locationFunding.setSelect(true);
			locationFunding.setMandatory(false);
			
			ArrayList<AmpColumnEntry>locationfundingitems = new ArrayList<AmpColumnEntry>();
			AmpColumnEntry loccommitments = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[0].select","commitments","activity.location.locationFunding.commitments");
			loccommitments.setSelect(true);
			loccommitments.setName("commitments");
			AmpColumnEntry locdisbursements = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[1].select","disbursements","activity.location.locationFunding.disbursements");
			locdisbursements.setSelect(true);
			locdisbursements.setName("disbursements");
			AmpColumnEntry locexpenditures = new AmpColumnEntry("activityTree.elements[4].elements[1].elements[2].select","expenditures","activity.location.locationFunding.expenditures");
			locexpenditures.setSelect(true);
			locexpenditures.setName("expenditures");
			locationfundingitems.add(loccommitments);
			locationfundingitems.add(locdisbursements);
			locationfundingitems.add(locexpenditures);
			locationFunding.setElements(locationfundingitems);
			
			locationitems.add(locationname);
			locationitems.add(locationFunding);
			collocation.setElements(locationitems);
			AmpColumnEntry colproposedStartDate = new AmpColumnEntry("activity.proposedStartDate","proposedStartDate","activity.proposedStartDate");
			colproposedStartDate.setSelect(true);
			
			AmpColumnEntry colactualStartDate = new AmpColumnEntry("activity.actualStartDate","actualStartDate","activity.actualStartDate");
			colactualStartDate.setSelect(true);
			
			
			
			//sectors
			AmpColumnEntry colsectors = new AmpColumnEntry("activityTree.elements[6].select","sectors","activity.sectors");
			colsectors.setSelect(true);
			colsectors.setMandatory(false);
			
			AmpColumnEntry colfunding = new AmpColumnEntry("activityTree.elements[5].select","funding","activity.funding");
			colfunding.setSelect(true);
			colfunding.setMandatory(true);
			ArrayList<AmpColumnEntry>fundingitems = new ArrayList<AmpColumnEntry>();
			AmpColumnEntry  fundingOrg = new AmpColumnEntry("activityTree.elements[5].elements[0].select","fundingOrg","activity.funding.fundingorg");
			fundingOrg.setMandatory(true);
			fundingOrg.setSelect(true);
			AmpColumnEntry  assistanceType = new AmpColumnEntry("activityTree.elements[5].elements[1].select","assistanceType","activity.funding.assistanceType");
			assistanceType.setMandatory(false);
			assistanceType.setSelect(true);
			AmpColumnEntry  financingInstrument = new AmpColumnEntry("activityTree.elements[5].elements[2].select","financingInstrument","activity.funding.financingInstrument");
			financingInstrument.setMandatory(false);
			financingInstrument.setSelect(true);
			AmpColumnEntry  commitments = new AmpColumnEntry("activityTree.elements[5].elements[3].select","commitments","activity.funding.commitments");
			commitments.setMandatory(true);
			commitments.setSelect(true);
			AmpColumnEntry  disbursements = new AmpColumnEntry("activityTree.elements[5].elements[4].select","disbursements","activity.funding.disbursements");
			disbursements.setMandatory(true);
			disbursements.setSelect(true);
			AmpColumnEntry  expenditures = new AmpColumnEntry("activityTree.elements[5].elements[5].select","expenditures","activity.funding.expenditures");
			expenditures.setMandatory(true);
			expenditures.setSelect(true);
			fundingitems.add(fundingOrg);
			fundingitems.add(assistanceType);
			fundingitems.add(financingInstrument);
			fundingitems.add(commitments);
			fundingitems.add(disbursements);
			fundingitems.add(expenditures);
			
			colfunding.setElements(fundingitems);
			
			AmpColumnEntry coldocuments= new AmpColumnEntry("activityTree.elements[6].select","documents","activity.documents");
			coldocuments.setSelect(true);
			coldocuments.setMandatory(true);
			
			AmpColumnEntry colrelatedOrgs= new AmpColumnEntry("activityTree.elements[7].select","relatedOrgs","activity.relatedOrgs");
			colrelatedOrgs.setSelect(true);
			colrelatedOrgs.setMandatory(true);
			
			
			columns.setElements(new ArrayList<AmpColumnEntry>());
			columns.getElements().add(colid);
			columns.getElements().add(coltitle);
			columns.getElements().add(colobjetive);
			columns.getElements().add(colactualStartDate);
			columns.getElements().add(colproposedStartDate);
			columns.getElements().add(coldescription);
			columns.getElements().add(collocation);
			columns.getElements().add(colsectors);
			columns.getElements().add(colfunding);
			columns.getElements().add(coldocuments);
			columns.getElements().add(colrelatedOrgs);
			try {
				activities.getActivity().add(eBuilder.getActivityType(columns));
				
			} catch (AmpExportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return activities;
	}

	public static StringBuffer TransformToIiati(StringBuffer xml){
		

		
		return xml;
		
	}
}


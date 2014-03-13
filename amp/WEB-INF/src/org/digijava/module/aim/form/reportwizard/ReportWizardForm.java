/**
 * 
 */
package org.digijava.module.aim.form.reportwizard;

import java.util.*;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpTeamMember;

/**
 * @author alex
 *
 */
@Data
public class ReportWizardForm extends ActionForm {
	
	private Long reportId				= null;
	private AmpTeamMember ampTeamMember	= null;
	
	private Map<String, List<AmpColumns>> ampTreeColumns;
	private Collection<AmpMeasures> ampMeasures		= null;
	private String reportType 			= "donor";
	private Boolean desktopTab   		= false;
	
	private Boolean hideActivities 		= false;
	private String 	reportPeriod		= "A";
	private String reportTitle 			= "";
	private String reportDescription 	= "";
	private String originalTitle		= "";
	
	private Long[] selectedColumns		= null;
	private Long[] selectedHierarchies	= null;
	private Long[] selectedMeasures		= null;

	private Boolean duplicateName		= false;
	private Boolean overwritingForeignReport = false;
	
	private Boolean publicReport 		= false;
	private Boolean workspaceLinked = false;
	private Boolean useFilters			= false;
	
	private Boolean allowEmptyFundingColumns	= false;
	private Boolean onePager			= false;
	
	private String projecttitle= "Project Title";
	
	private Boolean budgetExporter = false;
	
	private Long reportCategory			= new Long(0);
}

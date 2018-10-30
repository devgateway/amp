<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<div class="right_menu">
	<div class="right_menu_header">
		<div class="right_menu_header_cont"><digi:trn>Updated Activities</digi:trn></div>
	</div>
	<div class="right_menu_box">
		<div class="right_menu_cont">
        <ul>
			<logic:notEmpty name="lastVersions" scope="session">
				<logic:iterate id="iter" scope="session" name="lastVersions">
			        	<li class="tri tri-desktop"><a
								href='/aim/viewActivityPreview.do~activityId=<bean:write name="iter" property="ampActivityId"/>' class="triText"><bean:write name='iter' property='name'/></a></li>
				</logic:iterate>
			</logic:notEmpty>
            </ul>
			<logic:empty name="lastVersions" scope="session">
				<p class="right_menu_empty"><digi:trn>No results</digi:trn></p>
			</logic:empty>
		</div>
	</div>
</div>
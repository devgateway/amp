<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/category.tld" prefix="category" %>
<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module" %>

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
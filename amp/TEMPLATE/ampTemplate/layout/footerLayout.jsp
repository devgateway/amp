<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<!-- FOOTER START -->

	<div class="footer">
		
			AMP <b><tiles:getAsString name="version"/></b> build <b><tiles:getAsString name="build_version"/></b> - <digi:trn>Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DGF</digi:trn>
			<logic:notEmpty name="currentMember" scope="session">
				<digi:secure actions="ADMIN">
            		<a href='<digi:site property="url"/>/admin/'>Admin</a>
            		<a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
       			</digi:secure>
			</logic:notEmpty>
	
	 </div>
<div class="dgf_footer">
<img src="/TEMPLATE/ampTemplate/img_2/dgf_logo_bottom.gif" class="dgf_logo_footer" /><br />
1889 F Street, NW, Second Floor, Washington, D.C. 20006, USA<br>
info@developmentgateway.org, Tel: +1.202.572.9200, Fax: +1 202.572.9290
</div>
<!-- FOOTER END  -->
	


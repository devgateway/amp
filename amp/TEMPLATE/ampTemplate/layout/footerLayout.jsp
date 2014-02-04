<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.helper.GlobalSettingsConstants" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>



<!-- FOOTER START -->
	<div class="footer">
			AMP <b><tiles:getAsString name="version"/></b> build <b><tiles:getAsString name="build_version"/></b> - <digi:trn>Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DGF</digi:trn>
			<logic:notEmpty name="currentMember" scope="session">
				<digi:secure globalAdmin="true">
            		<a href='<digi:site property="url"/>/admin/'>Admin</a>
            		<a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
       			</digi:secure>
			</logic:notEmpty>
	
	 </div>
<div class="dgf_footer">
<img src="/TEMPLATE/ampTemplate/img_2/dgf_logo_bottom.gif" class="dgf_logo_footer" /><br />
1889 F Street, NW, Second Floor, Washington, D.C. 20006, USA<br>
info@developmentgateway.org, Tel: +1.202.572.9200, Fax: +1 202.572.9290</div>
<c:if test="<%=FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ENABLE_SITE_TRACKING) == true%>">
	<!-- Piwik 
	Site id can be checked here: http://stats.ampsite.net/index.php?module=SitesManager&action=index&idSite=1&period=range&date=last30
	Also,the wiki for piwik: https://wiki.dgfoundation.org/display/AMPDOC/Integrating+AMP+with+Piwik 
	-->
	<script type="text/javascript">
	  var _paq = _paq || [];
	  _paq.push(["trackPageView"]);
	  _paq.push(["enableLinkTracking"]);
	
	  (function() {
	    var u="<%=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_URL)%>";
	    _paq.push(["setTrackerUrl", u+"piwik.php"]);
	    _paq.push(["setSiteId", "<%=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_ID)%>"]);
	    var d=document, g=d.createElement("script"), s=d.getElementsByTagName("script")[0]; g.type="text/javascript";
	    g.defer=true; g.async=true; g.src=u+"piwik.js"; s.parentNode.insertBefore(g,s);
	  })();
	</script>
	<!-- End Piwik Code -->	
</c:if>
<!-- FOOTER END  -->
	


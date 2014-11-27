<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.helper.GlobalSettingsConstants" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>


<div class="dgf_footer">
<img src="/TEMPLATE/ampTemplate/img_2/dgf_logo_bottom.gif" class="dgf_logo_footer" />
<br/>
Development Gateway
<br/>
1110 Vermont Ave, NW, Suite 500
<br/>
Washington, DC 20005 USA
<br/>
info@developmentgateway.org, Tel: +1.202.572.9200, Fax: +1 202.572.9290
</div>
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

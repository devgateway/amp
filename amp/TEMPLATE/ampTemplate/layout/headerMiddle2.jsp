<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
        <!-- Dependencies --> 
		
<%@page import="org.digijava.module.aim.helper.Constants"%>

		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
		<script type="text/javascript" src="<digi:file src="script/yui/dragdrop-min.js"/>"></script>
 	 	<script type="text/javascript" src="<digi:file src="script/yui/event-min.js"/>"></script>
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <jsp:include page="/repository/aim/view/ar/aboutScripts.jsp"/>
 		<div id="customAbout" class="invisible-item" class="content">
 			<jsp:include page="/repository/aim/view/helpAbout.jsp" />
 		</div>

<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:notPresent name="currentMember">
<% 
String publicView=FeaturesUtil.getGlobalSettingValue("Public View");
if("On".equals(publicView)) { 
%>
<style>
.yuiampmenuitemlabel
{
border-right:1px solid white;
}
</style>
<div class="yuiamp-skin-amp" style="clear:both;">
    <div id="mainmenuHeader" class="yuiampmenu">
      <div class="bd">
          <ul class="first-of-typeamp">
            <li class="yuiampmenuitem">
                <a class="yuiampmenuitemlabel" href="/" module="aim" title="${trn3}">
                <digi:trn key="aim:homePage">
                Home Page
                </digi:trn>
	            </a>
            </li>
            <li class="yuiampmenuitem">
                <c:set var="message">
                <digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                </c:set>
                <a class="yuiampmenuitemlabel" href="/reportsPublicView.do" module="aim" onclick="return quitRnot1('${message}')">
                <digi:trn key='aim:PublicSite'>PUBLIC SITE</digi:trn>
                </a>
            </li>
<!--           <module:display name="Content Repository" parentModule="Resources">
	            <li class="yuiampmenuitem">
	                <digi:link styleClass="yuiampmenuitemlabel" href="/documentManager.do" module="contentrepository" onclick="return quitRnot()">
	                <digi:trn key="contentrepository:publicDocuments">Public Documents</digi:trn></digi:link>
	            </li>
	            </module:display>
-->
            <feature:display name="Language Option" module="Tools">
		            <li>
		                <span class="yuiampmenuitemlabel" style="float:left;position:relative;top:0px;_top:1px;border-right:0px;">
		                <digi:trn key="aim:deflanguage">Language</digi:trn>
		                </span>
		                 <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
		                   <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
		                </a>               
		                <div id="reports2" class="yuiampmenu" style="width:120px;">
		                    <div class="bd">                    
		                        <ul>
		                        <digi:insert flush="false" attribute="dropdownLangSwitch" />
		                        </ul>
		                    </div>
		                </div>                              
		            </li>
			</feature:display>
          </ul>            
      </div>
  </div>
</div> 

<% } 
else  //In case the public view aren't activated
{
%>
<style>
.yuiampmenuitemlabel
{
border-right:1px solid white;
}
</style>
<div class="yuiamp-skin-amp" style="clear:both;">
    <div id="mainmenuHeader" class="yuiampmenu">
      <div class="bd">
          <ul class="first-of-type">
			<%-- 
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/showRegisterUser.do" module="aim" title="${trn3}">
                <digi:trn key="aim:newUserRegistration">
                New user registration
                </digi:trn>
	            </digi:link>
            </li>
			<li class="yuiampmenuitem">
	 	 		<a class="yuiampmenuitemlabel"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px" href="/viewTeamreports.do?tabs=false">
	 	 			<digi:trn key="">reports</digi:trn>
	 	 		</a>
	 	 	</li>
	 	 	<li class="yuiampmenuitem">
	 	 		<a class="yuiampmenuitemlabel"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px" href="#">
	 	 			<digi:trn key="">resources</digi:trn>
	 	 		</a>
	 	 	</li>
	 	 	<li class="yuiampmenuitem">
	 	 		<a class="yuiampmenuitemlabel"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px" href="#">
	 	 			<digi:trn key="">projects</digi:trn>
	 	 		</a>
	 	 	</li>
 	 	--%>
 	 	<li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="" module="aim" title="${trn3}">
                <digi:trn key="aim:homePage">
                Home Page
                </digi:trn>
	            </digi:link>
            </li>
            <module:display name="HELP">
 	 		<li class="yuiampmenuitem">
 	 			<span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px; border-right:0px">
 	 				<digi:trn key="help:help">HELP</digi:trn>
 	 			</span>
 	 			<a  onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
 	 				<img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
 	 			</a>
 	 			<div id="help" class="yuiampmenu" style="opacity:0.9;">
 	 				<div class="bd">
 	 					<ul>
 	 						<feature:display name="About AMP" module="HELP">
 	 							<li>
 	 								<%siteDomain = (org.digijava.kernel.request.SiteDomain) request.getAttribute(org.digijava.kernel.Constants.CURRENT_SITE); 
 	 								session.setAttribute("site", siteDomain);%>
 	 								<a class="yuiampmenuitemlabel" href="" target="name"       onClick="showAbout(); return false;">
 	 	                               <digi:trn key="aim:aboutamp">About AMP</digi:trn>
 	 	                            </a>
 	 					       </li>
 	 	                   </feature:display>                                      
 	                   </ul>
 	 				</div>
 	 			</div>              
 	 		</li>
 	 	</module:display> 

            <feature:display name="Language Option" module="Tools">
		            <li>
		                <a href="#" class="yuiampmenuitemlabel" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px;border-right:0px;">
		                <digi:trn key="aim:deflanguage">Language</digi:trn>
		                </a>
		                 <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
		                   <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
		                </a>               
		                <div id="reports2" class="yuiampmenu" style="width:120px;">
		                    <div class="bd">                    
		                        <ul>
		                        <digi:insert flush="false" attribute="dropdownLangSwitch" />
		                        </ul>
		                    </div>
		                </div>                              
		            </li>
		 </feature:display>
          </ul>            
      </div>
  </div>
</div> 
<%
}
%>
</logic:notPresent>
<script language="javascript">

var arrowClicked = false;
//Run initialization for menu

var oMenuBar = new YAHOOAmp.widget.MenuBar("mainmenuHeader", { 
constraintoviewport:false
 });

for(var i = 0; i < oMenuBar.getItems().length; i++){
	oMenuBar.getItem(i).cfg.setProperty("onclick", { fn: onMenuItemClick });
}

oMenuBar.render();

function onMenuItemClick(p_sType, p_aArgs, p_oValue) { 
	if(arrowClicked){
		oSubmenu = this.cfg.getProperty("submenu");
		if(oSubmenu.cfg.getProperty("visible"));
		if(oSubmenu)
		{
			if(oSubmenu.cfg.getProperty("visible"))
			{
				if (oSubmenu)
					oSubmenu.hide();
			}
			else
			{
				if (oSubmenu)
					oSubmenu.show();
			}
				
		}
		arrowClicked = false;
	}
	else
	{
	if (oSubmenu)
		oSubmenu.hide();		
	}
}



</script>

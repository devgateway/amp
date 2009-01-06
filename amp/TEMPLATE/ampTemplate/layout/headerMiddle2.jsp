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
		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />

<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

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
                <c:set var="message">
                <digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                </c:set>
                <digi:link styleClass="yuiampmenuitemlabel" href="/reportsPublicView.do" module="aim" onclick="return quitRnot1('${message}')">
                <digi:trn key='aim:PublicView'>PUBLIC VIEW</digi:trn>
                </digi:link>
            </li>
<!--            <feature:display name="yuiampmenuitemlabel" module="Document Management">-->
<!--            <li class="yuiampmenuitem">-->
<!--                <digi:link styleClass="yuiampmenuitemlabel" href="/documentManager.do" module="contentrepository" onclick="return quitRnot()">			-->
<!--                <digi:trn key="contentrepository:publicDocuments">Public Documents</digi:trn></digi:link>-->
<!--            </li>-->
<!--            </feature:display>-->
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/showRegisterUser.do" module="aim" title="${trn3}">
                <digi:trn key="aim:newUserRegistration">
                New user registration
                </digi:trn>
	            </digi:link>
            </li>
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
                        <digi:insert attribute="dropdownLangSwitch" />
                        </ul>
                    </div>
                </div>                              
            </li>
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
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/showRegisterUser.do" module="aim" title="${trn3}">
                <digi:trn key="aim:newUserRegistration">
                New user registration
                </digi:trn>
	            </digi:link>
            </li>
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
                        <digi:insert attribute="dropdownLangSwitch" />
                        </ul>
                    </div>
                </div>                              
            </li>
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

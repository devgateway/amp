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
                <a class="yuiampmenuitemlabel" href="#">
                <digi:trn key="aim:deflanguage">Language</digi:trn>
                </a>
                <div id="reports2" class="yuiampmenu">
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
                <a class="yuiampmenuitemlabel" href="#">
                <digi:trn key="aim:deflanguage">Language</digi:trn>
                </a>
                <div id="reports2" class="yuiampmenu">
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
//Run initialization for menu
var oMenuBar = new YAHOOAmp.widget.MenuBar("mainmenuHeader", { 
autosubmenudisplay: true
 });
oMenuBar.render();

</script>      

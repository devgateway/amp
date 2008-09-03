<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@page import="org.digijava.module.help.util.HelpUtil"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="javascript" type="text/javascript">
	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}

	 function expandProgram(progId){
	 	
	 	var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}       
	
	function collapseProgram(progId){
	
		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	} 
	
</script>
<digi:instance property="helpForm" />
<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=viewSelectedHelpTopic" />
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               <ul class="yui-nav">&nbsp;
                          <li class="selected" style="width: 100%">
                          <a title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div style="border-left-width:1px">
                          	<digi:trn key="aim:helpTopic">Help Topics</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:700px;overflow: auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                        <bean:define id="topic" name="helpForm" property="topicTree" type="java.util.Collection"/>
                        <%= HelpUtil.renderTopicsTree(topic) %>
             		</div>



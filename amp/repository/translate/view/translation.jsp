<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import = "org.digijava.kernel.translator.ValueBean"%>
<%@ page import = "org.digijava.kernel.entity.Message"%>
<%@ page import="org.digijava.kernel.util.DgUtil"%>
<%@ page import="org.digijava.kernel.request.SiteDomain"%>
<%@ page import="org.digijava.kernel.request.Site"%>

<logic:notEmpty name="Message" scope="request">
<bean:define id="message" name="Message" type="java.util.Collection" scope="request"/>
</logic:notEmpty>

<logic:notEmpty name="list" scope="request">
<bean:define id="ls" name="list" type="java.util.Set" scope="request"/>
</logic:notEmpty>

<logic:notEmpty name="languages" scope="request">
<bean:define id="lang" name="languages" type="java.util.Set" scope="request"/>
</logic:notEmpty>
<%
	DgUtil dg = new DgUtil();
	SiteDomain siteDomain = new SiteDomain();
	siteDomain = dg.getSiteDomain(request);
	Site site = siteDomain.getSite();
	Site rootSite = dg.getRootSite(site);
	String rootSiteId=rootSite.getId().toString();
	String siteid=site.getId().toString();
%>

<html>
  <head>
    <script language="JavaScript">

function myfunc(myString)
{
	window.document.form1.rownum.value=myString;
	window.document.form1.method.value='update';
	window.document.form1.submit();
}
function myfunction(myString)
{
	window.document.form1.SaveAll.value=myString;
	window.document.form1.submit();
}
function myfunction1(myString)
{
	window.document.form1.rownum.value=myString;
	
	document.form1.multi.value='0';
	window.document.form1.submit();
}

function myfunction2(myString)
{
	window.document.form1.rownum.value=myString;
	document.form1.multi.value='1';
	window.document.form1.submit();
}

function check(myString)
{
	window.document.form1.expired.value=myString;
	window.document.form1.submit();
}
function addkey()
{
	window.document.form1.action="<%=request.getContextPath()%>/translate/complete/AddKey.do";
	document.form1.method.value = 'newMessage';
	window.document.form1.submit();
}
//-->
    </script>
  </head>
<%
int expired = 0;
int site_admin = 0;
int pagenum=0;
int rownum=0;
int rownum1=0;
String source = "";
String target = "";
String source_locale = "";
String target_locale = "";
String search = request.getParameter("search");

if(request.getParameter("pagenum")!=null){
	String s = (String) request.getParameter("pagenum");
	pagenum=Integer.parseInt(s);
}
else
 pagenum=1;

if(request.getAttribute("radio_locale1")!=null)
	source_locale = (String) request.getAttribute("radio_locale1");

if(request.getAttribute("radio_locale2")!=null)
	target_locale = (String) request.getAttribute("radio_locale2");

if(request.getParameter("source")!=null)
	source = request.getParameter("source");

if(request.getParameter("target")!=null)
	target=request.getParameter("target");

if(request.getAttribute("No_rows")!=null){
	String s =(String)request.getAttribute("No_rows");
	rownum=Integer.parseInt(s);
}
%>
<body bgcolor="white">
	<table width="900" border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td>
	<form name="form1" method="POST" action="<%=request.getContextPath()%>/translate/complete/trans.do">
	<input type="hidden" name="src_locale" value="en">
	  <input type="hidden" name="target_locale" value="en">
	  <input type="hidden" name="anc" value="">
	  <input type="hidden" name="mykey" value="">
	  <input type="hidden" name="new_text" value="">
	  <input type="hidden" name="multi" value="">
	  <input type="hidden" name="rownum" value="">
	  <input type="hidden" name="SaveAll" value="">
	  <input type="hidden" name="buttontype" value="0">
      <input type="hidden" name="choice" value="">
       
      <h3>Complete translation interface for site <%=DgUtil.getSiteDomain(request).getSite().getName()%></h3>	
      
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
     
      
      <tr>
          <td class="headerNav" width="50%" bgcolor="#D8D8D8" align="left">
            <digi:trn key="ep:selectSrcLang"> </digi:trn>:&nbsp;
			<select name="radio_locale1" style="FONT-SIZE: 7pt; FONT-FAMILY: Verdana, Tahoma, 'MS Sans Serif', Arial" onChange="document.form1.method.value='keySearch';document.form1.mykey.value='';document.form1.new_text.value='';document.form1.choice.value='';document.form1.src_locale.value=this.options[this.selectedIndex].value;document.form1.multi.value='0';document.form1.buttontype.value='0';document.form1.submit()">
            <logic:iterate id="lan" name="lang" type="org.digijava.kernel.entity.Locale">
			<option value="<bean:write name='lan' property='code'/>"
			<%	if(source_locale != null){%>
				<logic:equal name="lan" property="code" value="<%=source_locale%>">
					 selected
				 </logic:equal>
				 <%}%>><bean:write name="lan" property="name"/></option>
			</logic:iterate>
		  </select>
          </td>
          
          
                    
          <td class="headerNav" width="50%" bgcolor="#D8D8D8" align="right">
            <digi:trn key="ep:selectTargetLang"> </digi:trn>:&nbsp;
			<select name="radio_locale2" style="FONT-SIZE: 7pt; FONT-FAMILY: Verdana, Tahoma, 'MS Sans Serif', Arial" onChange="document.form1.method.value='keySearch';document.form1.mykey.value='';document.form1.new_text.value='';document.form1.choice.value='';document.form1.target_locale.value=this.options[this.selectedIndex].value;document.form1.multi.value='0';document.form1.buttontype.value='0';document.form1.submit()">
          	<logic:iterate id="lan" name="lang" type="org.digijava.kernel.entity.Locale">
			<option value="<bean:write name='lan' property='code'/>"
			<%if(target_locale != null){%>
				<logic:equal name="lan" property="code" value="<%=target_locale%>">
					 selected
				</logic:equal>
				 <%}%>><bean:write name="lan" property="name"/></option>
			</logic:iterate>
		  </select>
         </td>
        </tr>
      </table>
      <br>
			<table class="headerNav" width="98%" align="center" border="0" cellpadding="0" cellspacing="0">
			  <tr align="right">
				  <%
				  if(request.getParameter("search")!=null)
				  {
					 if((pagenum == 1)&&(rownum==50))
					 {
					%>
						<th width="49%" align="right">
						<a href="trans.do?siteId=dgMarket&method=keySearch&startFrom=<%=(pagenum*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum+1%>">Next Page</a>
						</th>
				  	 <%}
					if(pagenum > 1 && rownum < 50)
					{
					%>
						<th width="49%" align="right">
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=((pagenum-2)*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum-1%>">Previous Page</a>
						
						</th>
				  	<%}
				  	
				  	if(pagenum > 1 && rownum == 50)
										{
										%>
						<th width="49%" align="right">
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=((pagenum-2)*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum-1%>">Previous Page</a>
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=(pagenum*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum+1%>">Next Page</a>
						</th>
				  	<%}
				  	
				}%>
			  </tr>
			</table>
			<br>
           <digi:trn key="ep:SelectPackageToTranslate"> </digi:trn>:&nbsp;
			  <select name="search" onChange="document.form1.method.value='keySearch';document.form1.mykey.value='';document.form1.new_text.value='';document.form1.choice.value='';document.form1.multi.value='0';document.form1.buttontype.value='0';document.form1.submit()">
	         		  <logic:iterate id="lst" name="list" type="java.lang.String">
					<option value="<bean:write name='lst'/>"
						<%if(search != null){%>
						<logic:equal name="lst" value="<%=search%>">
						 selected
						 </logic:equal>
				 		<%}%>><bean:write name="lst"/>
				 	</option>
             	</logic:iterate>
	           </select>
					
					<digi:secure actions="ADMIN">
					
					&nbsp;Expired:&nbsp;
					
					<input type="checkbox" name="expired"									
					<logic:present parameter="expired" scope="request">				
					<logic:equal parameter="expired" scope="request" value="on">
					checked
					</logic:equal>
					</logic:present>
					>
					
					</digi:secure>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="Add" value="Add New Key" onClick="addkey();">
					<input type="hidden" name="method" value="getAll">
			       <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
			         <tr>
			           <th width="24%">Key</th><th width="24%">Source text</th><th width="34%">Target text</th><th width="14%">&nbsp;</th>
			         </tr>
			         <tr>
			           <td width="30%">
			          	 <input type="text" name="keyword" size="10"  onChange="document.form1.choice.value=0;document.form1.submit();">
			           
			          	 <input type="button" name="butt1" value="Search" onClick="document.form1.method.value='keywordSearch';document.form1.choice.value=0;document.form1.submit()">
			           </td>
			           <td width="30%">
			           	<input type="text" name="source_message" size="10" onChange="document.form1.method.value='search';document.form1.choice.value='1';document.form1.submit();">
			          	 <input type="button" name="butt2" value="Search" onClick="document.form1.choice.value='1';document.form1.submit()">
			           </td>
			         
			           <td width="30%">
			             &nbsp;&nbsp;&nbsp;&nbsp;    &nbsp;&nbsp;&nbsp;&nbsp;
			           	<input type="text" name="target_message"  size="10" onChange="document.form1.choice.value=2;">
			           	<input type="button" name="butt3" value="Search" onClick="document.form1.method.value='search';document.form1.choice.value=2;document.form1.submit()">
			           </td>
			           <td width="14%" valign="bottom">
 				<input type="button" value="Save All" onClick="document.form1.method.value='saveAll';myfunction('SaveAll');"
				<logic:present parameter="expired" scope="request">				
				<logic:equal parameter="expired" scope="request" value="on">
				disabled="true" 
				</logic:equal>
				</logic:present>			           
			          >
			           </td>
			         </tr>
			         
			         <tr><td colspan="2">&nbsp;</td></tr>
			         <logic:notEmpty name="Message" scope="request">
						<logic:iterate id="smessage" name="message" type="org.digijava.kernel.translator.ValueBean" >
						<logic:empty name = "smessage" property="target">
						<tr bgcolor="red">
      			       </logic:empty>
      			       <%rownum1=rownum1+1;
      			        int row_tr = rownum1%2;
      			       if(row_tr!=0)
      			       {
      			       %>
      			       	<tr bgcolor="lightgrey">
      			     <%}
      			       else
      			       {%>
      			       	<tr bgcolor="white">
      			     <%}%>
			             <a name="anc<%=rownum1%>"></a>
			             <td width="24%" valign="top" height="50">
			             <input type="hidden" name="mykeys<%=rownum1%>" value="<bean:write name='smessage' property='keyId'/>">
		                 <logic:empty name = "smessage" property="target">
							 <font color="red"><bean:write name="smessage" property="keyId"/></font>
						 </logic:empty>

      			         <logic:notEmpty name = "smessage" property="target">
			                 <font color="black"><bean:write name="smessage" property="keyId"/></font>
 						 </logic:notEmpty>
				    </td>	
				    <td valign="top">
			             <input type="button" name="exp_button<%=rownum1%>" style="font-size: 8pt"
			             <% if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){ %>
			             value="Unexpire" onClick="document.form1.method.value='expireKey';myfunction2('<%=rownum1%>');">
			             <%}
			             else
			             {%>
			             value="Expire" onClick="document.form1.method.value='expireKey';myfunction1('<%=rownum1%>');">
			             
			             <%}%>
			             </td>
			             <td width="24%" valign="top">

			             	<logic:equal name="smessage" property="sourceSiteId" value='<%=DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId().toString()%>'>
			             	
			             		<%if(DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId() != DgUtil.getSiteDomain(request).getSite().getId()){%>
							<font color="blue"><bean:write name="smessage" property="source"/></font>
						<%}else{%>
							<bean:write name="smessage" property="source"/>
						<%}%>
					</logic:equal>
					<logic:notEqual name="smessage" property="sourceSiteId" value='<%=DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId().toString()%>'>
						
						<bean:write name="smessage" property="source"/>
						
					</logic:notEqual>
					
			             </td>
			             <td width="34%" align="center" valign="top">
			             <input type="hidden" name="changed.line<%=rownum1%>" value="0">
			             <input type="hidden" name="target<%=rownum1%>" value="<bean:write name='smessage' property='target'/>">
			             
			            
			             			
					<logic:equal name="smessage" property="targetSiteId" value='<%=DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId().toString()%>'>
						<%if(DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId() != DgUtil.getSiteDomain(request).getSite().getId()){%>
							<textarea name="txt<%=rownum1%>" style="color:blue;background-color: white; width:260px; height:100%"><bean:write name="smessage" property="target"/></textarea>
						<%}else{%>
							<%if(smessage.isNeedsUpdate()){%>
								<textarea name="txt<%=rownum1%>" style="color=red;background-color: white; width:260px; height:100%"><bean:write name="smessage" property="target"/></textarea>
							<%}else{%>
								<textarea name="txt<%=rownum1%>" style="background-color: white; width:260px; height:100%"><bean:write name="smessage" property="target"/></textarea>
							<%}%>
						<%}%>
					</logic:equal>

					<logic:notEqual name="smessage" property="targetSiteId" value='<%=DgUtil.getRootSite(DgUtil.getSiteDomain(request).getSite()).getId().toString()%>'>
						
						<%if(smessage.isNeedsUpdate()){%>
							<textarea name="txt<%=rownum1%>" cols="30" rows="3" style="color=red;background-color: white"><bean:write name="smessage" property="target"/></textarea>
						<%}else{%>
							<textarea name="txt<%=rownum1%>" cols="30" rows="3" style="background-color: white"><bean:write name="smessage" property="target"/></textarea>
						<%}%>

					</logic:notEqual>


			               	</td>
			               	<td width="14%" align="center" valign="top">
			               	<input type="button" name="button<%=rownum1%>" value="Update" onClick="document.form1.method.value='update';myfunc('<%=rownum1%>');"
			               	<logic:present parameter="expired" scope="request">				
					<logic:equal parameter="expired" scope="request" value="on">
					disabled="true" 
					</logic:equal>
					</logic:present>			           
			               	
			               	>
			               	</td>
						</tr>
						</logic:iterate>
			           </logic:notEmpty>
 						<input type="hidden" name="total_rows" value=<%=rownum1%>>
				  </table>
      <br><br>
			<table class="headerNav" width="98%" align="center" border="0" cellpadding="0" cellspacing="0">
			  <tr align="right">
				  <%
				  if(request.getParameter("search")!=null)
				  {
					 if((pagenum == 1)&&(rownum==50))
					 {
					%>
						<th width="49%" align="right">
						<a href="trans.do?siteId=dgMarket&method=keySearch&startFrom=<%=(pagenum*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum+1%>">Next Page</a>
						</th>
					 <%}
					if(pagenum > 1 && rownum < 50)
					{
					%>
						<th width="49%" align="right">
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=((pagenum-2)*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum-1%>">Previous Page</a>

						</th>
					<%}

					if(pagenum > 1 && rownum == 50)
										{
										%>
						<th width="49%" align="right">
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=((pagenum-2)*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum-1%>">Previous Page</a>
						<a href="trans.do?method=keySearch&siteId=dgMarket&startFrom=<%=(pagenum*50)+1%>&search=<%=search%>&radio_locale1=<%=source_locale%>&radio_locale2=<%=target_locale%>&pagenum=<%=pagenum+1%>">Next Page</a>
						</th>
					<%}

				}%>
			  </tr>
			</table>

		<input type="submit" name="butt" value=" " style="background-color:white; border:none;">
	 </form>
	 </td>
	 </tr>
	 </table>
  </body>
</html>

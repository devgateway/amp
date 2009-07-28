<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:context name="digiContext" property="context"/>
<digi:instance property="addressbookForm"/>

<!-- tabs styles -->
<style type="text/css">


#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}



#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:742px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>

<script type="text/javascript">

	function checkNumber(phoneOrFaxId){
		var phoneOrFax=document.getElementById(phoneOrFaxId);
	 	var number=phoneOrFax.value;
	 	var validChars= "0123456789()+ ";
	 	for (var i = 0;  i < number.length;  i++) {
	  		var ch = number.charAt(i);
	  		if (validChars.indexOf(ch)==-1){
	   			alert('enter correct number');
	   			phoneOrFax.value=number.substring(0,i);
	   			return false;	   
	  		}
	 	}	 
	 return true;
	}

	function saveContact(){
		if(validateInfo()){
		    <digi:context name="addCont" property="context/addressBook.do?actionType=saveContact"/>
		    document.addressbookForm.action = "<%= addCont %>";
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
		}
	}

	function validateInfo(){
		if(document.getElementById('name').value==null || document.getElementById('name').value==''){
			alert('Please Enter Name');
			return false;
		}
		if(document.getElementById('lastname').value==null || document.getElementById('lastname').value==''){
			alert('Please Enter lastname');
			return false;
		}
		if(document.getElementById('email').value==null || document.getElementById('email').value==''){
			alert('Please Enter email');
			return false;
		}else if(document.getElementById('email').value.indexOf('@')==-1){
			alert('Please Enter Correct Email');
			return false;
		}
		if (checkNumber('phone')==false){
			return false;
		}
		if(checkNumber('fax')==false){
			return false;
		}		
		return true;
	}
</script>

<digi:form action="/addressBook.do?actionType=saveContact" method="post">	
	<table bgColor="#ffffff" cellPadding="5" cellSpacing="1" >
		<tr>
			<td width="14">&nbsp;</td>
			<td align="left" vAlign="top" width="752">
				<table bgcolor="#ffffff" cellPadding="0" cellSpacing="0" width="100%">
					<tr>
						<!-- Start Navigation -->
						<td height="33">
							<span class="crumb">
					           <c:set var="translation">
									<digi:trn>Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/showDesktop.do" styleClass="comment" title="${translation}">
									<digi:trn>Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:link href="/addressBook.do?actionType=viewAddressBook" styleClass="comment" title="${translation}">
									<digi:trn>Address Book</digi:trn>
								</digi:link>							
								&nbsp;&gt;&nbsp;
								<digi:trn>Add/Edit Contact</digi:trn>
				              </span>
						</td>
						<!-- End navigation -->
					</tr>
					<tr>
						<td height="100%">
							<DIV id="tabs">
								<UL>
							      	<LI>
							           	<span>
						                	<a href="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&reset=true&tabIndex=1">
						                    	<div title='<digi:trn>Existing Contacts</digi:trn>'>
							                 		<digi:trn>Existing Contacts</digi:trn>
						                        </div>
							                </a>
						                </span>
							        </LI>
									<LI>
							           	<a name="node">
					                	<div>
											<digi:trn>Add New Contact</digi:trn>							
					                    </div>
					                </a>
							        </LI>
								</UL>					
							</DIV>
						</td>
					</tr>
					<tr>
						<td height="100%" width="100%">
							<table bgcolor="#ffffff" cellPadding="1" cellSpacing="0" width="100%" border="1">
								<tr height="100%">
									<td height="100%" width="100%">
										<table bgcolor="#ffffff" cellPadding="5" cellSpacing="0" width="100%" border="0">
											<tr>
												<td><digi:trn>All fields marked with <font size="2" color="#FF0000">*</font> are required.</digi:trn></td>
											</tr>
											<tr>
												<td noWrap width=100% vAlign="top">
													<table bgcolor="#ffffff" border="0" width="100%">
														<tr>
															<td><digi:errors /></td>
														</tr>
														<tr>
															<td>
																<table>
																	<tr>
																		<td noWrap width=690 vAlign="top">
																			<table bgColor="#ffffff" cellPadding="0" cellSpacing="0"class="box-border-nopadding" width="100%">
																				<tr bgColor="#f4f4f2">
																					<td vAlign="top" width="100%">&nbsp;</td>
																				</tr>
																				<tr bgColor="#ffffff">
																					<td valign="top">
																						<table align="center" bgColor="#f4f4f2" cellPadding="0"	cellSpacing="0" border="0">
																							<tr>
																								<td bgColor="#ffffff" class="box-border" width="680">
																									<table border="0" class="box-border" width="100%">
																										<tr bgColor="#dddddb">
																											<td bgColor="#dddddb" height="20" align="center"colspan="5">
																												<digi:trn>Add/Edit Contact</digi:trn>
																											</td>
																										</tr>
																										<!-- Page Logic -->
																										<tr>
																											<td width="100%">
																												<table border="0" bgColor="#f4f4f2" width="100%">
																													<tr height="5"><td>&nbsp;</td></tr>
																													<tr>
																														<td rowspan="9" width="10%"/>
																														<td align="right"><strong><digi:trn>First Name</digi:trn></strong><font color="red">*</font></td>
																														<td align="left"><html:text property="name" styleId="name"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Last Name</digi:trn></strong><font color="red">*</font></td>
																														<td align="left"><html:text property="lastname" styleId="lastname"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Email</digi:trn></strong><font color="red">*</font></td>
																														<td align="left"><html:text property="email" styleId="email"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Title</digi:trn></strong> </td>
																														<td align="left"><html:text property="title"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
																														<td align="left"><html:text property="organisationName"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Phone Number</digi:trn></strong></td>
																														<td align="left"><html:text property="phone" styleId="phone" onkeyup="checkNumber('phone')"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Fax</digi:trn></strong></td>
																														<td align="left"><html:text property="fax" styleId="fax" onkeyup="checkNumber('fax')"/></td>
																													</tr>		
																													<tr height="5px"><td colspan="2"/></tr>
																													<tr>
																														<td colspan="4" align="center"><html:button property="" styleClass="dr-menu" onclick="saveContact()">Save</html:button> </td>			
																													</tr>																
																												</table>
																											</td>
																										</tr>
																										<!-- end page logic -->
																									</table>
																								</td>
																							</tr>
																							<tr>
																								<td bgColor=#f4f4f2>&nbsp;</td>
																							</tr>
																						</table>
																					</td>
																					<td noWrap width=10 vAlign="top"></td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>	
</digi:form>
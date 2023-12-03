<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
  
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

	function validate() {
		if (document.aimFlagUploaderForm.countryId.value == "-1") {
			alert("Please select a country");
			document.aimFlagUploaderForm.countryId.focus();
			return false;
		}
		
		/*
		if (trim(document.aimFlagUploaderForm.flagFile.value).length == 0) {
			alert("Please select a flag");
			document.aimFlagUploaderForm.flagFile.focus();
			return false;				  
		}*/
	}

	function upload() {
		ret = validate();
		if (ret == true) {
			document.aimFlagUploaderForm.submit();
		}
		return ret;
	}

	function load() {
		if (document.aimFlagUploaderForm.winFlag.value == "close") {
			<digi:context name="flagUploader" property="context/ampModule/moduleinstance/flagUploader.do"/>
		   document.aimFlagUploaderForm.action = "<%= flagUploader %>";
			document.aimFlagUploaderForm.target = window.opener.name;
		   document.aimFlagUploaderForm.submit();
			window.close();				  
		}
	}

	function unload() {
	}

</script>

<digi:form action="/uploadFlag.do" method="post" enctype="multipart/form-data">

<html:hidden name="aimFlagUploaderForm" property="winFlag" />
<html:hidden name="aimFlagUploaderForm" property="event" />

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:uploadFlag">
								Upload Flag</digi:trn>
							</td>
						</tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table width="100%" cellpadding="2" cellspacing="1">
									<tr>
										<td colspan="2" align="center">
											<digi:errors/>
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:country">Administrative Level 0</digi:trn>
										</td>
										<td>
											<html:select property="countryId" styleClass="inp-text">
												<html:option value="-1">-- Select Country --</html:option>
												<html:optionsCollection name="aimFlagUploaderForm" property="countries" 
													value="id" label="name" />
											</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:flag">Flag</digi:trn>	
										</td>
										<td>
											<html:file name="aimFlagUploaderForm" property="flagFile" size="30"
											styleClass="inp-text"/>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="center">
											<table cellPadding=3 cellSpacing=3>
												<tr>
													<td>
														<input type="submit" value="Upload" class="dr-menu"
															onclick="return upload()">
													</td>
													<td>
														<input type="reset" value="Clear" class="dr-menu">
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu" onclick="window.close()">
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
	</td></tr>
</table>
</digi:form>

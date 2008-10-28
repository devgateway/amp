<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<digi:form action="/createPhysicalProgress.do" method="post">
<table width="100%" align="center">
<tr><td><h2 align=center><digi:trn key="aim:physicalProgressDetails">Physical Progress Details</digi:trn></h2></td></tr>
<tr><td>
<table width=80% align=center>
<tr><td><digi:trn key="aim:title">Title : </digi:trn></td>
<td> <html:text property="title"/></td></tr>
<tr><td><digi:trn key="aim:reportingDate">Reporting Date : </digi:trn></td><td>
<html:select property="dd">
	<html:option value="01">01</html:option>
	<html:option value="02">02</html:option>
	<html:option value="03">03</html:option>
	<html:option value="04">04</html:option>
	<html:option value="05">05</html:option>
	<html:option value="06">06</html:option>
	<html:option value="07">07</html:option>
	<html:option value="08">08</html:option>
	<html:option value="09">09</html:option>
	<html:option value="10">10</html:option>
	<html:option value="11">11</html:option>
	<html:option value="12">12</html:option>
	<html:option value="13">13</html:option>
	<html:option value="14">14</html:option>
	<html:option value="15">15</html:option>
	<html:option value="16">16</html:option>
	<html:option value="17">17</html:option>
	<html:option value="18">18</html:option>
	<html:option value="19">19</html:option>
	<html:option value="20">20</html:option>
	<html:option value="21">21</html:option>
	<html:option value="22">22</html:option>
	<html:option value="23">23</html:option>
	<html:option value="24">24</html:option>
	<html:option value="25">25</html:option>
	<html:option value="26">26</html:option>
	<html:option value="27">27</html:option>
	<html:option value="28">28</html:option>
	<html:option value="29">29</html:option>
	<html:option value="30">30</html:option>
	<html:option value="31">31</html:option>
</html:select>
<html:select property="mm">
	<html:option value="01">Jan</html:option>
	<html:option value="02">Feb</html:option>
	<html:option value="03">Mar</html:option>
	<html:option value="04">Apr</html:option>
	<html:option value="05">May</html:option>
	<html:option value="06">Jun</html:option>
	<html:option value="07">Jul</html:option>
	<html:option value="08">Aug</html:option>
	<html:option value="09">Sep</html:option>
	<html:option value="10">Oct</html:option>
	<html:option value="11">Nov</html:option>
	<html:option value="12">Dec</html:option>
</html:select>
<html:select property="yyyy">
	<html:option value="2000">2000</html:option>
	<html:option value="2001">2001</html:option>
	<html:option value="2002">2002</html:option>
	<html:option value="2003">2003</html:option>
	<html:option value="2004">2004</html:option>
	<html:option value="2005">2005</html:option>
	<html:option value="2006">2006</html:option>
	<html:option value="2007">2007</html:option>
	<html:option value="2008">2008</html:option>
	<html:option value="2009">2009</html:option>
</html:select>
</td></tr>
<tr><td><digi:trn key="aim:description">Description : </digi:trn></td>
<td> <html:textarea property="pdescription"/></td></tr>

<tr>
	<td align="center" height="21"> <html:reset> Reset </html:reset> </td>
	<td align="center" height="21"> <html:submit> Save </html:submit> </td> 
</tr>
</table>
</digi:form>

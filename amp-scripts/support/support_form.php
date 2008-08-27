<?php
session_start();
if (isset($_SESSION['bla']) && isset($_POST['username'])) {
$_SESSION['bla2']=100;
$username="amp";
$server_address="localhost";
$password="";
$link = mysql_connect($server_address, $username, $password);
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('amp_support');

$myusername=$_POST['username'];
$mypassword=$_POST['password'];
$mycountry=$_POST['countrycode'];
$mylanguage=$_POST['rlocale'];

$sql="SELECT * FROM user_logon WHERE user_name='$myusername' and user_password='$mypassword' and country_code='$mycountry'";
$result=mysql_query($sql);

$count=mysql_num_rows($result);
if ($count == 1) {
	$row=mysql_fetch_array($result);
	$_SESSION['loginid']=$row['loginid'];
	$_SESSION['mycountry']=$mycountry;
}
else {
	header("location:login_failed.php");
}
}
else {
    header("location:index.php");
}
?>

<html>
<head>
<title>AMP Support Site </title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body>

<form name="SupportForm" id="ShowRequestForm" action="request.php" method="post">
	<table width="780" border="0" cellpadding="0" cellspacing="0" class="table">
      <tr>
        <td height="655">

        <table width="500" border="0" align="center" cellpadding="2" cellspacing="1">
          <tr>
            <td height="23" colspan="3" align="left" class="textError">            
          </td>
          </tr>
          <tr>
            <td width="8" align="left" valign="top" class="textError">*</td>
            <td width="194" align="right" class="Text">
            	Full User Name&nbsp;            
            </td>

            <td width="234"><input type="text" name="fullusername" value="" id="ShowRequestForm_fullusername"/></td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	E-mail &nbsp;            
            </td>
            <td><input type="text" name="email" value="" id="ShowRequestForm_email"/></td>

          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	AMP Login&nbsp;</td>
            <td><input type="text" name="amplogin" value="" id="ShowRequestForm_amplogin"/></td>
          </tr>
          <tr>

            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">AMP Password
              &nbsp;</td>
            <td><input type="password" name="amppassword" id="ShowRequestForm_amppassword"/></td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">Browser
              &nbsp;</td>
              <?

              $link = mysql_connect($server_address, $username, $password);
              if (!$link) {
               die('Could not connect: ' . mysql_error());
				  }
				  mysql_select_db('amp_support'); 
              $query="SELECT DISTINCT browser FROM request"; 
              $result=mysql_query($query);  
              ?>

            <td><select name="browser" id="ShowRequestForm_browser">
    			<option value="Specify a Browser">Specify a Browser</option>
    			<option value="Internet Explorer">Internet Explorer</option>
			<option value="Firefox">Firefox</option>
    			<option value="Other">Other</option>
				</select></td>

          </tr>
          <tr>
            <td align="left" valign="top" class="textError">&nbsp;</td>
            <td align="right" class="Text">
            	Browser Version&nbsp;            
            </td>
            <td>
            	<input type="text" name="browserversion" value="" id="ShowRequestForm_browserversion"/>            
            </td>

          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	Operating System&nbsp;            
            </td>
            <td><select name="operatingsystem" id="ShowRequestForm_operatingsystem">
    <option value="Windows XP">Windows XP</option>

    <option value="Windows Vista">Windows Vista</option>
    <option value="Windows Server">Windows Server</option>
    <option value="Windows 98">Windows 98</option>
    <option value="Windows 95">Windows 95</option>
    <option value="Linux">Linux</option>
    <option value="Unix">Unix</option>

    <option value="Other">Other</option>


</select></td>
          </tr>
          
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">Module AMP&nbsp;             
			</td>
             <td align="left">
					<?
					 $link = mysql_connect($server_address, $username, $password);
           	   if (!$link) {
               die('Could not connect: ' . mysql_error());
				  }
				  mysql_select_db('amp_support');
				  $column='name_'.$mylanguage;
				  if ($mylanguage == 'en')
				  		$query="SELECT name_en FROM amp_modules";
				  if ($mylanguage == 'es')
				  		$query="SELECT name_es FROM amp_modules";
				  if ($mylanguage == 'fr')
				  		$query="SELECT name_fr FROM amp_modules"; 
              $result=mysql_query($query); 		
					?>
              	<select name="modules" size="5" id="ShowRequestForm_modules" multiple="multiple">
              	<?              	 
              	while($row=mysql_fetch_array($result)) {
              		if ($mylanguage == 'en') { 
              	?>
					<option value="<?=$row['name_en'] ?>"><?=$row['name_en'] ?></option>
              	<? }
              		if ($mylanguage == 'es') { 
              	?>
              	<option value="<?=$row['name_es'] ?>"><?=$row['name_es'] ?></option>
              	<?
              		}
              		if ($mylanguage == 'fr') {
              	?>
              	<option value="<?=$row['name_fr'] ?>"><?=$row['name_fr'] ?></option>
					<?
						}
					}
					?>             	
					</select>            
            	</td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">
            	AMP Version  &nbsp;           	
            </td>
            <td>
            	<select name="version" id="ShowRequestForm_version">

    <option value="Choose your version of AMP">Choose your version of AMP</option>
    <option value="1.09RC1">1.09RC1</option>
    <option value="1.09RC1.10">1.09RC1.10</option>


</select>            
            </td>
          </tr>
          <tr>

            <td align="left" valign="top" class="textError">&nbsp;</td>
            <td align="right" class="Text">
            	Priority Level&nbsp;
            </td>
            <td>
            	<select name="level" id="ShowRequestForm_level">
    <option value="Low">Low</option>
    <option value="Medium">Medium</option>

    <option value="High">High</option>


</select>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">

            	Subject&nbsp;</td>
            <td>
            	<input type="text" name="subject" value="" id="ShowRequestForm_subject"/>
            </td>
          </tr>
          <tr>
            <td height="136" align="left" valign="top" class="textError">*</td>
            <td height="136" align="right" valign="top" class="Text">

            	Description&nbsp;            
            </td>
            <td>
            	<textarea name="details" cols="30" rows="10" id="ShowRequestForm_details"></textarea>            
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">

            	CC&nbsp;            
            </td>
            <td>
            	<input type="text" name="mailcc" value="" id="ShowRequestForm_mailcc"/>            
            </td>
          </tr>
          <tr>
            <td height="23" colspan="3" align="right" valign="middle" class="Textk">
            	Separate all email addresses with a semicolon (;)&nbsp;            
            </td>

          </tr>
          <tr>
           <td height="35" colspan="3" align="center" valign="middle"><input type="submit" id="ShowRequestForm_saveRequest" name="action:saveRequest" value="Send Request"/>
			</td>
          </tr>
          <tr>
            <td colspan="3" align="center">&nbsp;</td>
          </tr>
          <tr>

            <td colspan="3" align="center"><span class="textError">*</span> 
            	<span class="Text">
              		Required Field
            	</span>            
            </td>
          </tr>
          <tr>
            <td colspan="3" align="center">&nbsp;</td>
          </tr>
          <tr>

            <td colspan="3" align="center">
            	<img src="Images/logo_mini.gif" width="91" height="32" longdesc="Development Gateway Foundation ">            </td>
          </tr>
        </table>
		<br>
		</td>
      </tr>
    </table>

</form>
</body>
</html>
<?php
srand((double)microtime()*1000000);
$rand=rand(1,9);
$rand2=12345;
$session_id=$rand.substr(md5($rand2), 0, 11+$rand);
$session_id.=substr(md5(rand(1,1000000)), rand(1,32-$rand), 21-$rand);
session_id($session_id);
session_start();
$_SESSION['bla']=99;
$username="amp";
$server_address="localhost";
$password="";
$link = mysql_connect($server_address, $username, $password);
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('amp_support');
$query="select code,name from country";
$result=mysql_query($query);
?>

<html>
<head>
<title>AMP Support Site </title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><style type="text/css">
<!--
body {
	margin-top: 0px;
}
-->
</style></head>
<body>
<form namespace="" id="login" action="support_form.php" method="post">
	<br>
	<br>
	<br>

	<table width="343" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td width="285">
        	<img src="Images/logo.gif" alt="AMP Support" width="229" height="85" hspace="0" vspace="0"></td>
      </tr>
    </table>
    <br>
    <table width="343" border="0" align="center" cellpadding="1" cellspacing="1">
	<tr>

        <td colspan="3" align="left" class="textError">
        	        
        </td>
      </tr>
      <tr>
        <td width="39" align="right" class="textError"> * </td>
        <td width="126" align="right" class="Text">User
        &nbsp; </td>
        <td width="174">

        	<input type="text" name="username" value="" id="login_username" style=".fields"/>        
        </td>
      </tr>
      <tr>
        <td align="right" class="textError"> * </td>
        <td align="right" class="Text">Password
          &nbsp; </td>
        <td>

        	<input type="password" name="password" id="login_password" class="Styles/common.css" style="FieldStyle"/>        
        </td>
      </tr>
      <tr>
        <td align="right" class="textError"> * </td>
        <td align="right" class="Text">Country
          &nbsp; </td>
        <td>

        	<select name="countrycode" id="login_countrycode" class="Styles/common.css" style="FieldStyle">
        	<? while($row=mysql_fetch_array($result)) { ?>
        	<option value="<?=$row['code'] ?>"><?=$row['name'] ?></option>
        	<? } ?>
			</select>        
        </td>
      </tr>
      <tr>
        <td colspan="2" align="right" class="Text">Language
          &nbsp;			
        <td>

        	<select name="rlocale" id="login_rlocale" class="Styles/common.css" style="FieldStyle">
    <option value="en">English</option>
    <option value="fr">French</option>
    <option value="es">Spanish</option>


</select>        
        </td>
      </tr>

      <tr>
        <td height="42" colspan="3" align="center">
        	<input type="submit" id="login_loging" name="action:postloging" value="Login" class="Styles/common.css" style="FieldStyle"/>
        
        </td>
      </tr>
      <tr>
        <td colspan="3" align="center">
			<span class="textError">*</span>

			<span class="Text">
				Required Field
			</span>		</td>
      </tr>
  </table>
</form>
</body>
</html>

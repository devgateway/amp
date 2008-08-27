<?php 
session_start();
if (isset($_SESSION['bla']) && isset($_SESSION['bla2']) && isset($_POST['fullusername']) && isset($_POST['email']) && isset($_POST['amplogin']) && isset($_POST['amppassword']) && isset($_POST['browser']) && isset($_POST['operatingsystem']) && isset($_POST['subject']) && isset($_POST['details'])) {
$username="amp";
$server_address="localhost";
$password="";
$fullusername=$_POST['fullusername'];
$email=$_POST['email'];
$amplogin=$_POST['amplogin'];
$amppassword=$_POST['amppassword']; 
$browser=$_POST['browser'];
$browserversion=$_POST['browserversion'];
$operatingsystem=$_POST['operatingsystem'];
$modules=$_POST['modules'];
$version=$_POST['version']; 
$countrycode=$_SESSION['mycountry'];
$level=$_POST['level'];
$subject=$_POST['subject'];
$description=$_POST['details'];
$mailcc=$_POST['mailcc'];

$loginid=$_SESSION['loginid'];

$link = mysql_connect($server_address, $username, $password);
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('amp_support') or die('Cannot select db');

$query="INSERT INTO request (browser, browserversion, date, details, email, fullusername, module, operatingsystem, subject, version, country_code, login_id, amp_login, amp_password, mail_cc, level) VALUES ('$browser', '$browserversion', NOW(), '$description', '$email', '$fullusername', '$modules', '$operatingsystem', 'test support form', '$version', '$countrycode', $loginid, '$amplogin', '$amppassword', '$mailcc', '$level')";
$result=mysql_query($query);
$query2="SELECT max(request_id) FROM request";
$result2=mysql_query($query2);
$row2=mysql_fetch_array($result2);
$supportid=$row2[0]; 
$query3="SELECT name FROM country WHERE code='$countrycode'";
$result3=mysql_query($query3);
$row3=mysql_fetch_array($result3);
$countryname=$row3['name'];

$time=gmstrftime("It is %a on %b %d, %Y, %X",time());

$subject="New Support Request";
$to="amp@code.ro";
$message="Support Request ID: $supportid\nSubmitted: $time\nCountry: $countryname\nUsername: $fullusername\nAMP User: $amplogin\nAMP Password: $amppassword\nEmail: $email\nBrowser: $browser\nBrowser version: $browserversion\nOperating system: $operatingsystem\nModule AMP: $modules\nAMP Version: $version\nPriority: $level\nSubject: $subject\nDescription: $description";

if (mail($to, $subject, $message, "From: ampsupport@ampdev.net")) {
}

$subject2="AMP Support Request";
$message2="Dear AMP $countryname customer\n\nThank you for your continued work with the AMP software.  We have received your email request for AMP support. The next steps in the process are:\n\n\n1. This issue will be entered in our bug tracking software for Ethiopia\n2. The AMP Ethiopia Support Team will analyze and prioritize the issue in jira.\n3. The AMP Ethiopia Support Team will respond with an email to provide an update on the issue and the next steps.\n\n\nNOTE: The response time for AMP Startup Support is the next business day. The response time for Basic Support is two business days.\nThanks again\nAMP Support Team";

if (mail($email, $subject2, $message2, "From: ampsupport@ampdev.net")) {
}

if (isset($_POST['mailcc'])) {
$tok=strtok($mailcc,";\n\t");
while($tok !== false) {
	mail($tok, $subject2, $message2, "From: ampsupport@ampdev.net");
	$tok=strtok($mailcc,";\n\t");
}
}

}
else {
    header("location:support_form.php");
}
?>

<html>
<head>
<title>AMP Support Site </title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
<form namespace="" id="RequestEnd" action="RequestEnd.action" method="post">
<br>
<br>
<br>
<br>

<table width="780" border="0" align="center" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" align="center" class="Text">
			Thank you for submitting your request to the AMP Technical Support Team. <br>
			We have been notified of your issue and a member of our technical team will respond to your request. <br>

			Thank you for using AMP.	  
		</td>
	</tr>
	<tr>
		<td width="259" height="87">&nbsp;</td>
	    <td width="326" align="right" valign="bottom" class="Textk">
	    	<a href="logout.php" style="text-decoration: none">
	    	LogOut&nbsp;</a>
	  </td>

	    <td width="193" align="right" valign="bottom" class="Textk">
	    	<a href="support_form.php" style="text-decoration: none">
	    	Submit Another Request&nbsp;</a>
	    	&nbsp;
      </td>
	</tr>
</table>
</form>

</body>
</html>

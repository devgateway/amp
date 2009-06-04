var decimalSymbol=null;
var groupSymbol=null;

function trim(s) {
	return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}



function checkAmountUsingSymbol(amount){
	var validChars= "-0123456789"+this.decimalSymbol+this.groupSymbol;

	for (i = 0;  i < amount.length;  i++) {
		var ch = amount.charAt(i);
		if (validChars.indexOf(ch)==-1){
			return false;
			break
		}
	}
}

function chkNumericForProjection(objName)

{



	var checkOK = "-0123456789"+this.decimalSymbol+this.groupSymbol;

	var checkStr = objName;

	var allValid = true;

	var decPoints = 0;

	var periodFlag = 0;

	var allNum = "";



	for (i = 0;  i < checkStr.value.length;  i++) {

		ch = checkStr.value.charAt(i);

		if (ch == ".") {

			if (periodFlag == 1) {

				allValid = false;

				break;

			}

			periodFlag = 1;

		}

		for (j = 0;  j < checkOK.length;  j++)

			if (ch == checkOK.charAt(j))

				break;



		if (j == checkOK.length) {

			allValid = false;

			break;

		}

	}



	return allValid;

}



function validateProjection (errorMsg) {

	var numProjections	= document.aimEditActivityForm.numProjections.value;

	var j				= 0;

	var i				= 0;

	for (i=0; i<numProjections; i++) {

		var name		= "mtefProjection[" + i + "].amount";

		//alert(name);

		var elements	= document.aimEditActivityForm.elements;

		for (j=0; j<elements.length; j++) {

			//alert(j);

			if (elements[j].name != null && elements[j].name == name ) {

				//alert("Found " + name + ": " + elements[j].value);

				var x	= chkNumericForProjection(elements[j]);

				if( elements[j].value.length == 0 || x==false) {alert(errorMsg + " " +  i); return false;}

			}

		}

	}

	return true;

}



function validateFunding() {

	var fundId = trim(document.getElementById("orgFundingId").value);

	if (fundId.length == 0) {

		alert ("Funding Id not entered");

		document.getElementById("orgFundingId").focus();

		return false;

	}


	var numComm = document.getElementById("numComm").value;

	var numDisb = document.getElementById("numDisb").value;

	var numExp = document.getElementById("numExp").value;



	if (numComm == 0) {

		var noCommitsWarn = confirm("No commitments have been added. Continue?");
		
		if (noCommitsWarn==false) return false;
	}

	

	return validateFundingDetails(numComm,numDisb,numExp);

}



function validateFundingTrn(errmsg1,errmsg2,errmsg3,msgEnterAmount,msgInvalidAmount,msgEnterDate, decimalSymbol,groupSymbol,msgConfirmFunding) {
	
	this.decimalSymbol=decimalSymbol;
	
	this.groupSymbol=groupSymbol;

	
	var fundId = trim(document.getElementById("orgFundingId").value);

	var assistType = trim(document.getElementsByName("funding.assistanceType")[0].value);

	var mod=trim(document.getElementsByName("funding.modality")[0].value);

	var errmsg='';

	if (assistType == 0) {

		errmsg+=errmsg1;

	}

	if (fundId.length == 0) {

		//errmsg+=errmsg2;//tanzania

	}

	if (mod == 0) {

		errmsg+=errmsg3;

	}

	if (errmsg!=''){

		alert (errmsg);

		document.getElementById("orgFundingId").focus();

		return false;

	}

	/*

	var sigDate = trim(document.aimEditActivityForm.signatureDate.value);

	if (sigDate.length == 0) {

		alert ("Signature date not entered");

		document.aimEditActivityForm.signatureDate.focus();

		return false;

	}*/

	var numComm = document.aimEditActivityForm.numComm.value;

	var numDisb = document.aimEditActivityForm.numDisb.value;

	var numExp = document.aimEditActivityForm.numExp.value;



	if (numComm == 0) {

		var noCommitsWarn = confirm("No commitments have been added. Continue?");
		
		if (noCommitsWarn==false) return false;
	}

	

	return validateFundingDetails(numComm,numDisb,numExp,msgEnterAmount,msgInvalidAmount,msgEnterDate,msgConfirmFunding);

}



function validateFundingExchangeRate() {

	var fundId = trim(document.getElementById("orgFundingId").value);



	var numComm = document.getElementById("numComm").value;

	var numDisb = document.getElementById("numDisb").value;

	var numExp = document.getElementById("numExp").value;





	return validateFundingDetailsExchangeRate(numComm,numDisb,numExp);

}









	function chkNumeric(objName,comma,period,hyphen)

	{

// only allow 0-9 be entered, plus any values passed

// (can be in any order, and don't have to be comma, period, or hyphen)

// if all numbers allow commas, periods, hyphens or whatever,

// just hard code it here and take out the passed parameterscheckNumeric

		var checkOK = "0123456789" + comma + period + hyphen;

		var checkStr = objName;

		var allValid = true;

		var decPoints = 0;

		//var contor;

		var allNum = "";

		var j 		= 0;

		var i		= 0;



		contor=0;

		for (i = 0;  i < checkStr.value.length;  i++)

		{

			ch = checkStr.value.charAt(i);

			if (ch==".")

			{

				if (i==0) {allValid=false; break;}

				contor++;

				if (contor>1) {allValid=false; break;}



			}



			for (j = 0;  j < checkOK.length;  j++)

			 {

			 	if (ch == checkOK.charAt(j)) break;

			 }

			if (j == checkOK.length)

			{

				allValid = false;

				break;

			}

			if (ch != ",") allNum += ch;

		}

		if (!allValid)

		{

			alertsay = "Please enter only numbers in the \"Exchange rate\" field or a valid decimal number using \".\" "

			alert(alertsay);

			return false;

		}

		return true;

	}





function validateFundingDetailsExchangeRate(comm,disb,exp)

{

	var itr = comm + disb + exp;

	var commAmt = 0, disbAmt = 0, expAmt = 0;

	var disbIndex = -1, expIndex = -1;

	for (var j = 0;j < itr;j ++) {

		var amtField = "fundingDetail[" + j + "].transactionAmount";

		var dateField = "fundingDetail[" + j + "].transactionDate";

		var transType = "fundingDetail[" + j + "].transactionType";

		var fixedExchangeRate="fundingDetail[" + j + "].fixedExchangeRate";

		var temp = new Array();

		temp = document.aimEditActivityForm.elements;



		for (var i = 0;i < temp.length;i ++) {



			if(temp[i].name != null && temp[i].name == fixedExchangeRate)

			{
				if(chkNumeric(temp[i],this.groupSymbol,this.decimalSymbol,'')==false) {return false;}

			}





		}

	}

	return true;



}





function validateFundingDetails(comm,disb,exp,msgEnterAmount,msgInvalidAmount,msgEnterDate,msgConfirmFunding) {

	var itr = comm + disb + exp;

	var commAmt = 0, disbAmt = 0, expAmt = 0;

	var disbIndex = -1, expIndex = -1;

	for (var j = 0;j < itr;j ++) {

		var amtField = "fundingDetail[" + j + "].transactionAmount";

		var dateField = "fundingDetail[" + j + "].transactionDate";

		var transType = "fundingDetail[" + j + "].transactionType";

		var fixedExchangeRate="fundingDetail[" + j + "].fixedExchangeRate";

		var temp = new Array();

		temp = document.aimEditActivityForm.elements;



		for (var i = 0;i < temp.length;i ++) {



			if(temp[i].name != null && temp[i].name == fixedExchangeRate)

			{

				if(chkNumeric(temp[i],this.groupSymbol,this.decimalSymbol,'')==false) {return false;}

			}



			if (temp[i].name != null && temp[i].name == amtField) {

				if (trim(temp[i].value) == "") {



					alert(msgEnterAmount);

					temp[i].focus();

					return false;

				}



				if (checkAmountUsingSymbol(temp[i].value) == false) {



					alert(msgInvalidAmount);

					temp[i].focus();

					return false;

				}
				
				if (msgConfirmFunding != "" && checkAmountLen(temp[i].value,msgConfirmFunding) == false) {

					temp[i].focus();

					return false;

				}

				var type = document.getElementsByName(transType);

				value = parseFloat(temp[i].value);

				if (isNaN(value)) {

					alert(msgInvalidAmount);

					temp[i].focus();

					return false;

				}

				else {

					if (type.item(0).value == 0)

						commAmt = commAmt + value;

					if (type.item(0).value == 1) {

						disbAmt = disbAmt + value;

						if (disbIndex == -1)

							disbIndex = j;

					}

					if (type.item(0).value == 2) {

						expAmt  = expAmt  + value;

						if (expIndex == -1)

							expIndex = j;

					}

				}

			}

			if (temp[i].name != null && temp[i].name == dateField) {

				if (trim(temp[i].value) == "") {

					alert(msgEnterDate);

					temp[i].focus();

					return false;

				}

			}

		}

	}

	return true;

}



function setTranscationType(type) {

	if (type == "addCommitments") {

		document.aimEditActivityForm.numComm.value++;

	} else if (type == "delCommitments") {

		document.aimEditActivityForm.numComm.value--;

	} else if (type == "addExpenditures") {

		document.aimEditActivityForm.numExp.value++;

	} else if (type == "delExpenditures") {

		document.aimEditActivityForm.numExp.value--;

	} else if (type == "addDisbursements") {

		document.aimEditActivityForm.numDisb.value++;

	} else if (type == "delDisbursements") {

		document.aimEditActivityForm.numDisb.value--;

	}

}


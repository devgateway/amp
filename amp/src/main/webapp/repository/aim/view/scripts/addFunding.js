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
		var projection=document.getElementsByName(name)[0];
			if (projection != null && projection.value != null && projection.value.length > 0 ) {
				var x	= chkNumericForProjection(projection);
				if( projection.value.length == 0 || x==false) {alert(errorMsg + " " +  i); return false;}
			}
	}
	return true;
}



function validateFunding() {

	var fundId = trim(document.getElementById("orgFundingId").value);

	if (fundId.length == 0) {

		alert (fundingNotEntered());

		document.getElementById("orgFundingId").focus();

		return false;

	}


	var numComm = document.getElementById("numComm").value;

	var numDisb = document.getElementById("numDisb").value;

	var numExp = document.getElementById("numExp").value;



	if (numComm == 0) {

		var messageconf=noCommitmentsAdded();
		
		var noCommitsWarn = confirm(messageconf);
		
		if (noCommitsWarn==false) return false;
	}

	

	return validateFundingDetails(numComm,numDisb,numExp);

}



function validateFundingTrn(errmsg1,errmsg2,errmsg3, errmsg4,msgEnterAmount,msgInvalidAmount,msgEnterDate, msgEnterRate, decimalSymbol,groupSymbol,msgConfirmFunding) {
	
	this.decimalSymbol=decimalSymbol;
	
	this.groupSymbol=groupSymbol;

	
	var fundId = trim(document.getElementById("orgFundingId").value);

	var assistType = trim(document.getElementsByName("funding.assistanceType")[0].value);

	var mod=trim(document.getElementsByName("funding.modality")[0].value);
	
	var fundStatus		= -1;
	
	var fundStatusTemp	= document.getElementsByName("funding.fundingStatus");
	
	if ( fundStatusTemp != null && fundStatusTemp.length != null && fundStatusTemp.length > 0 ) {
		fundStatus	= fundStatusTemp[0].value;
	}

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
	
	if (fundStatus == 0) 
		errmsg+=errmsg4;

	if (errmsg!=''){

		alert (errmsg);

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

		var messageconf= noCommitmentsAdded();
		
		var noCommitsWarn = confirm(messageconf);
		
		if (noCommitsWarn==false) return false;
	}

	

	return validateFundingDetails(numComm,numDisb,numExp,msgEnterAmount,msgInvalidAmount,msgEnterDate, msgEnterRate, msgConfirmFunding,decimalSymbol,groupSymbol);

}



function validateFundingExchangeRate() {

	var fundId = trim(document.getElementById("orgFundingId").value);



	var numComm = document.getElementById("numComm").value;

	var numDisb = document.getElementById("numDisb").value;

	var numExp = document.getElementById("numExp").value;





	return validateFundingDetailsExchangeRate(numComm,numDisb,numExp);

}









function chkNumeric(objName,comma,period,hyphen) {
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



		contor=0; //counts the number of decimal separators (period paramater) 

		for (i = 0;  i < checkStr.value.length;  i++)

		{

			ch = checkStr.value.charAt(i);

			if (ch==period)

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

			if (ch != comma) allNum += ch;

		}

		if (!allValid)

		{

			//alertsay = "Please enter only numbers in the \"Exchange rate\" field or a valid decimal number using \".\" "

			//alert(alertsay);

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
		var fixedExchangeRate="fundingDetail[" + j + "].fixedExchangeRate";
		if(forValidation[fixedExchangeRate]==null) continue;
		var currExchange=document.getElementsByName(fixedExchangeRate)[0];
			if(currExchange != null)
				if(chkNumeric(currExchange,this.groupSymbol,this.decimalSymbol,'')==false) {return false;}
	}
	return true;
}





function validateFundingDetails(comm,disb,exp,msgEnterAmount, msgInvalidAmount,msgEnterDate, msgEnterRate,msgConfirmFunding,decimalSymbol,groupSymbol) {
	
	var itr = comm + disb + exp;

	var commAmt = 0, disbAmt = 0, expAmt = 0;

	var disbIndex = -1, expIndex = -1;
	
	this.decimalSymbol=decimalSymbol;
	
	this.groupSymbol=groupSymbol;


	for (var j = 0;j < itr;j ++) {
		var amtField = "fundingDetail[" + j + "].transactionAmount";
		var dateField = "fundingDetail[" + j + "].transactionDate";
		var transType = "fundingDetail[" + j + "].transactionType";
		var fixedExchangeRate="fundingDetail[" + j + "].fixedExchangeRate";	
			
		if(forValidation[fixedExchangeRate]!=null) {
				var currExchange=document.getElementsByName(fixedExchangeRate)[0];
				if(currExchange!=null && chkNumeric(currExchange,this.groupSymbol,this.decimalSymbol,'-')==false) { 
							alert(msgEnterRate+"'"+ this.decimalSymbol +"'");
							currExchange.focus();
							return false;
				}
		}
		
		if(forValidation[amtField]!=null) {
				var currAmt=document.getElementsByName(amtField)[0];
				if (currAmt!=null) {
					if(trim(currAmt.value) == "") {
					alert(msgEnterAmount);
							currAmt.focus();
							return false;
					}
	
					if (checkAmountUsingSymbol(currAmt.value) == false) {
						alert(msgInvalidAmount);
						currAmt.focus();
						return false;
					}
					
					if (msgConfirmFunding != "" && checkAmountLen(currAmt.value,msgConfirmFunding, this.groupSymbol,this.decimalSymbol) == false) {
						currAmt.focus();
						return false;
					}
					
					value = parseFloat(currAmt.value);
					if (isNaN(value)) {
						alert(msgInvalidAmount);
						currAmt.focus();
						return false;
					} else {
						var type = document.getElementsByName(transType);
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
		}
		
		if(forValidation[dateField]!=null) {
				var currDate=document.getElementsByName(dateField)[0];
					if (currDate!=null && trim(currDate.value) == "") {
						alert(msgEnterDate);
						currDate.focus();
						return false;
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


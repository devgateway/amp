function trim(s) {
	return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}

function validateFunding() {
	var fundId = trim(document.aimEditActivityForm.orgFundingId.value);
	if (fundId.length == 0) {
		alert ("Funding Id not entered");
		document.aimEditActivityForm.orgFundingId.focus();
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
		alert ("Please enter a commitment");
		return false;	
	}
	if (numExp > 0 && numDisb == 0) {
		alert ("Expenditure entered without entering Disbursement");
		return false;		
	}
	return validateFundingDetails(numComm,numDisb,numExp);
}

function validateFundingDetails(comm,disb,exp) {
	var itr = comm + disb + exp;
	var commAmt = 0, disbAmt = 0, expAmt = 0;
	var disbIndex = -1, expIndex = -1; 
	
	for (var j = 0;j < itr;j ++) {
		var amtField = "fundingDetail[" + j + "].transactionAmount";
		var dateField = "fundingDetail[" + j + "].transactionDate";
		var transType = "fundingDetail[" + j + "].transactionType";
		var temp = new Array();
		temp = document.aimEditActivityForm.elements;		

		for (var i = 0;i < temp.length;i ++) {
			if (temp[i].name != null && temp[i].name == amtField) {
				if (trim(temp[i].value) == "") {
					msg = "Please enter the amount for the transaction";
					alert(msg);
					temp[i].focus();
					return false;
				}
						
				if (checkAmount(temp[i].value) == false) {
					msg = "Invalid amount entered for the transaction";
					alert(msg);
					temp[i].focus();
					return false;
				}
				if (checkAmountLen(temp[i].value) == false) {
					temp[i].focus();
					return false;
				}
				var type = document.getElementsByName(transType);
				value = parseFloat(temp[i].value);
				if (isNaN(value)) {
					alert("Invalid amount!");
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
					msg = "Please enter the transaction date for the transaction";
					alert(msg);
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

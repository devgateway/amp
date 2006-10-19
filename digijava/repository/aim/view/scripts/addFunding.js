function trim(s) {
	return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
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

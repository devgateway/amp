<script type="text/javascript">
	var EMAIL_REGEX = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

	function isInvalid(field) {
		if (field == "" || field == null || field.charAt(0) == ' ') {
	    	return 1;
	    }		
		if (!isNaN(field)) {
	    	return 2;
	    }		
		return 0;
	}

	function validateEmails(email, confirmationEmail) {
		if(! EMAIL_REGEX.test(email) || ! EMAIL_REGEX.test(confirmationEmail)) {
			<c:set var="translation">
			<digi:trn key="error.registration.noemail">you must enter Valid email please check in</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		if(email != confirmationEmail){
			<c:set var="translation">
			<digi:trn key="error.registration.noemailmatch">Emails in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		return true;
	}

	function validateEmail(email) {
		if(isInvalid(email) == 1) {
			errorMsg='<digi:trn jsFriendly="true">Email Is Blank or starts with an space</digi:trn>';
			alert(errorMsg);
			return false;
		} 
		if (! EMAIL_REGEX.test(email)) {
			var errorMsg='<digi:trn jsFriendly="true" >Please enter valid email</digi:trn>';
			alert(errorMsg);
			return false;
		}
		return true;
	}	
</script>	
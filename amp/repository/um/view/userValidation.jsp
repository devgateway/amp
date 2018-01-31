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
			<digi:trn key="error.registration.noemail">Please enter a valid email address.</digi:trn>
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
		blankMessage = '<digi:trn jsFriendly="true">Email is blank or starts with an space</digi:trn>';
		invalidMessage ='<digi:trn jsFriendly="true">Please enter valid email</digi:trn>';
		
		return validateMail(email, blankMessage, invalidMessage);
	}
	
	function validateNotificationEmail(email) {
		blankMessage = '<digi:trn jsFriendly="true">Notification Email is blank or starts with an space</digi:trn>';
        invalidMessage ='<digi:trn jsFriendly="true">Please enter a valid Notification Email</digi:trn>';
        
        return validateMail(email, blankMessage, invalidMessage);
    }
	
	function validateMailWithNotificationMail(email, notificationEmail) {
		if (email == notificationEmail) {
			alert('<digi:trn jsFriendly="true">Email address and notification email address must be different</digi:trn>');
			return false;
		}
		
        return true;
	}
	
	function validateMail(email, blankMessage, invalidMessage) {
        if (isInvalid(email) == 1) {
            alert(blankMessage);
            return false;
        } 
        if (!EMAIL_REGEX.test(email)) {
            alert(invalidMessage);
            return false;
        }
        
        return true;
    }
</script>	
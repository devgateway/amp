/*
 * A JavaScript implementation of the Digest Authentication
 * Digest Authentication, as defined in RFC 2617.
 * Version 1.0 Copyright (C) Maricn Michalski (http://marcin-michalski.pl) 
 * Distributed under the BSD License
 * 
 * site: http://arrowgroup.eu
 */

function ajaxLogin() {
		$(".error_text_login").remove();
		$('#result').before("<div class='error_text_login'><img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif' style='vertical-align:middle;'></div>");

		var digestAuth = new pl.arrowgroup.DigestAuthentication(
	   			{
					onSuccess : function(data) {
						var error = jQuery.trim(data);
						$(".error_text_login").remove();
						switch (error) {
						case 'noTeamMember':
							$('#result')
									.before(
											"<div class='error_text_login'><img src='/TEMPLATE/ampTemplate/img_2/login_error.gif' style='vertical-align:middle;'>&nbsp;&nbsp;&nbsp;<digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</div>");
							break;
						case 'invalidUser':
							$('#result')
									.before(
											"<div class='error_text_login'><img src='/TEMPLATE/ampTemplate/img_2/login_error.gif' style='vertical-align:middle;'>&nbsp;&nbsp;&nbsp;<digi:trn>Invalid User</digi:trn>.</div>");
							break;
						case 'noError':
							location.href = '/index.do';
							break;
						}
					},
	   				onFailure : function(response){
	   					$(".error_text_login").remove();
						$('#result')
						.before(
								"<div class='error_text_login'><img src='/TEMPLATE/ampTemplate/img_2/login_error.gif' style='vertical-align:middle;'>&nbsp;&nbsp;&nbsp;<digi:trn>Invalid username or password</digi:trn>.</div>");	   			
	   				},
	   				cnonce : 'testCnonce'
	   			}
	   		);	   		

  			digestAuth.setCredentials($('#j_username').val(),$('#j_password').val());
   			digestAuth.call('/aim/postLogin.do');
	}

$.Class("pl.arrowgroup.DigestAuthentication", {
	MAX_ATTEMPTS : 1,
	AUTHORIZATION_HEADER : "Authorization",
	WWW_AUTHENTICATE_HEADER : 'WWW-Authenticate',
	NC : "00000001", //currently nc value is fixed it is not incremented
	HTTP_METHOD : "GET",
	/**
	 * settings json:
	 *  - onSuccess - on success callback
	 *  - onFailure - on failure callback
	 *  - username - user name
	 *  - password - user password
	 *  - cnonce - client nonce
	 */
	init : function(settings) {
		this.settings = settings;
	},
	setCredentials: function(username, password){
		this.settings.username = username;
		this.settings.password = password;
	},
	call : function(uri){
		this.attempts = 0;
		this.invokeCall(uri);
	},
	invokeCall: function(uri,authorizationHeader){
		var digestAuth = this;
		$.ajax({
		        url: uri,
		        type: this.HTTP_METHOD,
		        beforeSend: function(request){
		        	if(typeof authorizationHeader != 'undefined'){
		        		request.setRequestHeader(digestAuth.AUTHORIZATION_HEADER, authorizationHeader); 		        		
		        	}
		        },
		        success: function(response) {
		        	digestAuth.settings.onSuccess(response);		        	
		        },
		        error: function(response) { 
		        	if(digestAuth.attempts == digestAuth.MAX_ATTEMPTS){
			    		digestAuth.settings.onFailure(response);
			    		return;
			    	}
		        	var paramParser = new pl.arrowgroup.HeaderParamsParser(response.getResponseHeader(digestAuth.WWW_AUTHENTICATE_HEADER));
		        	var nonce = paramParser.getParam("nonce");
		        	var realm = paramParser.getParam("realm");
		        	var qop = paramParser.getParam("qop");
		        	var response = digestAuth.calculateResponse(uri, nonce, realm, qop);
		        	var authorizationHeaderValue = digestAuth.generateAuthorizationHeader(paramParser.headerValue, response, uri); 
		        	digestAuth.attempts++;
		        	digestAuth.invokeCall(uri, authorizationHeaderValue);
		        }, 
		        cache: false
		    });
	},
	calculateResponse : function(uri, nonce, realm, qop){
		var a2 = this.HTTP_METHOD + ":" + uri;
		var a2Md5 = hex_md5(a2);
		var sha1Password= CryptoJS.SHA1(this.settings.password);
		var a1Md5 = hex_md5(this.settings.username + ":" + realm + ":" + sha1Password);
		var digest = a1Md5 + ":" + nonce + ":" + this.NC + ":" + this.settings.cnonce + ":" + qop + ":" +a2Md5;
//		console.log("a2="+a2);
//		console.log("a2Md5="+a2Md5);
//		console.log("sha1Password="+sha1Password);
//		console.log("a1Md5="+a1Md5);
//		console.log("digest="+digest);
		return hex_md5(digest);
	},
	generateAuthorizationHeader : function(wwwAuthenticationHeader, response, uri){
		return wwwAuthenticationHeader+', username="'+this.settings.username+'", uri="'+
			   uri+'", response="'+response+'", nc='+
			   this.NC+', cnonce="'+this.settings.cnonce+'"';
	}
});
$.Class("pl.arrowgroup.HeaderParamsParser",{
	init : function(headerValue) {
		this.headerValue = headerValue;
		this.headerParams = this.headerValue.split(",");
	},
	getParam: function(paramName){
		var paramVal = null;
		$.each(this.headerParams, function(index, value){
    		if(value.indexOf(paramName)>0){
    			paramVal = value.split(paramName+"=")[1];
    			paramVal = paramVal.substring(1, paramVal.length-1);
    		}
    	});
		return paramVal;
	}
});


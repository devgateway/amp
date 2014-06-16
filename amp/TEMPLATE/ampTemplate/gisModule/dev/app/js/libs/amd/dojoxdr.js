require(["dojo/request/registry", "dojo/request/xhr", 'dojo/Deferred'], function(request, xhr, Deferred) {
	
	// Alternative CORS implementation to XHR for IE8-9 
	function xdr(url, options, returnDeferred) {
		var def = new Deferred();
		var xdr = new XDomainRequest();
		
		if (xdr) {
			xdr.onload = function(e) {
				def.resolve(xdr.responseText);
			}
			//The following handlers need to be defined even if empty (IE8 bug)
			xdr.onerror = function() {};
			xdr.onprogress = function() {};
			xdr.ontimeout = function() {};

			xdr.open(options.method || 'GET', url);
			xdr.send();
			return def;
		}
		def.reject(new Error('XDomainRequest not supported.'));
		return def;
	}
	
	// Load external web resources via custom XHR handling
	function corsProvider(url, options, returnDeferred) {
		if (window.XDomainRequest) {
			return xdr(url, options, returnDeferred);
		}
		return xhr(url, options, returnDeferred);
	}

	request.register(/^https?:\/\//i, corsProvider);
});

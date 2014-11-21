RESTClient
==========

Known Issues
------------

1. PUT requests require additional testing
2. OPTION requests have not been implemented

Overview
--------

Generic API to make REST calls. Includes methods for GET, POST, PUT and DELETE.

The module allows you to make REST based requests to web services. It uses drupal_http_request by default but also supports cURL for more advanced configurations, authentication and improved performance. It also includes a debug mode to monitor REST requests and responses using devel's dpm function. As well there is a basic caching system in the works to reduce duplicate requests. This can be disabled globally or on a per-request basis.

Advantages when using cURL
<ul>
  <li>Supports NTLM authentication</li>
  <li>Support for all cURL library options</li>
  <li>Support for HTTP 1.1</li>
  <li>Improved header data parsing</li>
</ul>

This module doesn't really provide anything out of the box. You should install this if another module depends on it or you wish to build against it.

At the moment, GET and POST are fully implemented with DEL and PUT coming soon. Once those are ready, we'll promote this to a full project. Also, full API documentation is in the works and will be posted once complete. We're still working out some of the architecture which may affect the API docs.

If you'd like to see any additional features or support for other cURL options please post those requests in the issue queue!

Installation
------------

1. Download and install RESTClient module
2. (Optional) To enable cURL support, download and install cURL HTTP REQUEST.
    - At the moment, you need the 7.x-restclient-merge branch of cURL HTTP Request. Once the features stabilize the normal release copy will work just fine.

Configuration
-------------

1. Go to admin/config/services/restclient
2. Select your request library (defaults to Drupal)
3. Set your common REST endpoint (this can be overridden per request. This is only the default value.)

Additional Headers
------------------

RESTClient supports adding additional header data to all requests made. You can set this using variable_set() in your code or by adding to your $conf array in settings.php
No additional processing or validation is done on these header values so ensure the data is safe and correct.
Ex:

    $conf['restclient_additional_headers'] = array(
      'MyCustomHeaderName' => 'MyCustomHeaderValue',
    );
    
    
    variable_set('restclient_additional_headers', array('MyCustomHeaderName' => 'MyCustomHeaderValue'));

Troubleshooting
===============

RESTClient integrates with Devel to allow you to see the request/response object for any request make through RESTClient. To enable this, go to the configuration for RESTClient and enable debug mode.

Common Issues
-------------

1. No results from requests.

It's possible SELinux is preventing Apache from connecting to the remote host.  To fix this, allow the following:

     allow httpd_t port_t:tcp_socket name_connect;



Sponsored by <a href="http://coldfrontlabs.ca">Coldfront Labs Inc.</a>

Maintained by Mathew Winstone (<a href="http://drupal.org/user/129088">minorOffense</a>) and David Pascoe-Deslauriers (<a href="http://drupal.org/user/657848">spotzero</a>).
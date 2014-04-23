
=======
SUMMARY
=======

This module allows shortening URLs via almost any service. This is accomplished
either via the API or with an included block or page. Over 25 services are
available by default and additional services are easily added. If the shorturl
module or the shurly module is installed, they can be used to shorten URLs
on your own domain as well.

=======
DETAILS
=======

This module includes a number of features that will ensure the best experience
for you and your users. When retrieving a URL, Shorten URLs will time out if the
shortening service you chose does not respond after 3 seconds.  It will then use
a backup service of your choice to make sure you still have a shortened URL. If
retrieving a shortened URL fails, an explanation will be logged.

If you have cURL enabled, you can choose whether to retrieve shortened URLs via
cURL or PHP's native functions. cURL is usually faster. If you don't have cURL
compiled in your PHP package, you will not be able to choose the cURL option.

You can also choose whether to allow users to choose which shortening service to
use on the block and page. Note however that since shortened URLs are cached,
requesting the same shortened URL from different services may return results
from the first service in both instances.


==========
SUBMODULES
==========

This package contains two submodules: Record Shortened URLs and Shorten URLs
Custom Services. The first keeps track of which URLs have been shortened and
displays them to administrators at admin/reports/shorten; the second allows
administrators to add custom URL shortening services through the user interface
(instead of using the API).

===
API
===

Up-to-date API documentation is maintained at http://drupal.org/node/805174.

=====
NOTES
=====

IceCreamYou (http://drupal.org/user/201425) wrote and maintains the module.
Please report issues in the module's queue at
http://drupal.org/project/issues/shorten
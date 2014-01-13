// $Id: README.TXT,v 1.1 2012/01/20 19:32:46 vamirbekyan Exp $

Feeds Image Grabber (FIG), an addon module for Feeds module, grabs (downloads) images for each feed-item from their respective webpages and maps to an image field in the node created by Feeds (Node Processor).

Visit:
Project page: http://drupal.org/project/feeds_imagegrabber
Release page: http://publicmind.in/blog/drupal-feeds-image-grabber
Tutorial: http://publicmind.in/blog/tutorial-for-feeds-image-grabber


Dependencies:
-------------
Feeds
Image

Installation
------------

1) Place this module directory in your modules folder (this will usually be "sites/all/modules/").
3) Enable the module.

Usage
------

Whenever you create a new feeds content type, you will see extra options for Feeds Image Grabber:

1) Enable Feeds Image Grabber: Check this box, if you want to enable FIG for the feed in concern.
2) and 3) Id/Class: select the identifying tag and enter its id/class (see tutorial[http://publicmind.in/blog/tutorial-for-feedapi-imagegrabber] for details.)
4) Feeling Lucky
5) Execution time: Percentage of Maximum PHP execution time to take while grabbing images for each feed-item.

After these settings, go to mappings setting for FeedsNodeProcessor and map Item URL(Link) to the image field of your choice. Image fields will be appended with "(FIG)" which are processed by Feeds Image Grabber.

Maximum size, Minimum resolution, Maximum resolution settings for FIG can be set for each image field on their "Manage Fields" page. FIG considers the settings of individual fields while grabbing images for each feed-item.

For detailed explanation: http://publicmind.in/blog/tutorial-for-feeds-image-grabber/

Author
------

publicmind wrote and maintains this module. The author can be contacted for paid customizations of this module as well as
Drupal consulting and development at http://drupal.org/user/472412
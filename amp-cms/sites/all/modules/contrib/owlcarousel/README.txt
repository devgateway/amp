CONTENTS OF THIS FILE
---------------------
 * Introduction
 * Branches
 * Sub modules
 * Requirements
 * Installation
 * Configuration
 * Troubleshooting
 * FAQ

 
INTRODUCTION
------------
This module integrates the wonderful Owl Carousel slider built by OwlFonk. The primary module is comprised of three sub modules providing
a views style, field formatter & administration UI.

BRANCHES
--------
 *  1.x
    For Owl Carousel library 1.x
 *  2.x
    For Owl Carousel library 2.x

SUB MODULES
-----------
 *  Owl Carousel Views
    Provides Drupal Views integration.
 *  Owl Carousel Fields
    Provides a Drupal field formatter.
 *  Owl Carousel UI
    Provides an administration user interface for Owl Carousel variables.

REQUIREMENTS
------------
This module requires the following contrib modules:
 *  Views (https://drupal.org/project/views)
 *  Variable (https://drupal.org/project/variable)
 *  jQuery Update (https://drupal.org/project/jquery_update)

INSTALLATION
------------
 *  Install as you would normally install a contributed drupal module. See:
    https://drupal.org/documentation/install/modules-themes/modules-7
    for further information.
 *  Download the Owl Carousel library from http://owlgraphic.com/owlcarousel
 *  Place the library in the appropriate directory E.G.
    sites/all/libraries/owl-carousel
    sites/all/libraries/owl-carousel/owl.carousel.js
    sites/all/libraries/owl-carousel/owl.carousel.min.js
    sites/all/libraries/owl-carousel/owl.carousel.css
    sites/all/libraries/owl-carousel/owl.theme.css
    sites/all/libraries/owl-carousel/owl.transitions.css

CONFIGURATION
-------------
 *  Owl Carousel settings can be found at admin/config/user-interface/owlcarousel.
 *  Settings are defined as groups and may be applied to any number of
    carousel instances.
 *  Once you have configured your settings; create a new view & select 
    Owl Carousel as the display format.
 *  Finally select the desired settings group.
 *  See http://legomenon.io/article/drupal-7-configuration-owl-carousel 
    for instructions with images =).

TROUBLESHOOTING
---------------
 *  If you are receiving this error, TypeError: t.$elem.data(...) is undefined. Please change your jQuery version
    to 1.8, this is required until the patch below is merged into Owlcarousel.
    @ref, https://github.com/OwlFonk/OwlCarousel/pull/133

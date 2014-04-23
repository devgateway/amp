
Views Slideshow: ddblock
------------------------
Note: This is the development version of the views_slideshow_ddblock module for drupal 7. 
This is a first port of the drupal 6 module. At the moment it is unstable and please only use it 
when you want to help with the development and want to create patches. 
Please search the issue queue for existing issues and post your issues in the issue queue at 
http://drupal.org/project/issues/views_slideshow_ddblock 
Further developments needs to be done. There is no timeline when the module will be stable enough for 
testing. The Initial port used is done by Tom Kirkpatrick (http://drupal.org/user/305669)
 
SUMMARY
-------
The Views Slideshow Dynamic display block module enables you to present content in a 
dynamic way. For creating slideshow effects it uses the jQuery Cycle plug-in.

Several effects and other settings can be set in the configuration settings of the Views
Slideshow: ddblock module.

REQUIREMENTS
------------
* Views, Views_slideshow and Libraries are required modules to run the views slideshow: ddblock module.

Optional:
* Emfield can be used to show videos in the views_slideshow_ddblock module.

INSTALLATION
------------
1. Install required module first.
2. Extract the contents of the project into your modules directory, probably at
   /sites/all/modules/views_slideshow_ddblock.
3. Download the jquery cycle plugin from http://malsup.com/jquery/cycle/download.html 
   (use jquery.cycle.all.min.js (packed version) or jquery.cycle.all.js, tested till version 2.99)
4. Install the jquery cycle plugin in /sites/all/libraries/jquery.cycle/
5. Enable the Views slideshow: DDblock module.

STEPS TO CREATE A FIRST SLIDESHOW AFTER INSTALLATION OF THE MODULES
-------------------------------------------------------------------
When using your own content type and view:
   1a. Install the vsdupright10 - 60 views_slideshow_ddblock themes (see below)
      And use one of the vsduprightxxp themes in step 6.
   or
   1b. When you don't install the vsdupright10 - 60 themes you will only be able to 
       select the default theme in step 6

   2. Create or use an existing block display for your view with an image.
   3. Choose slideshow for the style of the block display.
   4. Change the setting for the slideshow style (click on the button after the style).
   5. Choose ddblock for the slideshow mode.
   6. Choose the slideshow theme.
   7. Create the mapping for theme fields.
   8. Leave all other default settings. (you can adjust them later, as needed)
   9. Save the view.
   10. Place the block in a region in the block configuration page.
   
INSTALL VIEWS SLIDESHOW DYNAMIC DISPLAY BLOCK THEMES
----------------------------------------------------
The views slideshow: ddblock module comes with several free themes, other commercial themes
will also be available in the future. (have a look at themes.myalbums.biz for examples of
commercial themes for the ddblock module which will also become available for
the views slideshow: ddblock module).

You can download the free themes from http://ddblock.myalbums.biz/download

* Download the theme package (vsdupright1060V1-1.zip) from http://ddblock.myalbums.biz/download
  Make sure you use the theme package for the views slideshow: ddblock module (Version 2.x for drupal 7)

* Extract the zip file to a temporary directory

* Copy the custom directory with all subdirectories to the theme directory of the
  theme you use. (which is probably at sites/all/themes/[YOUR_THEME_NAME])


DOCUMENTATION
-------------
Documentation will become available at http://ddblock.myalbums.biz 

SUPPORT
-------
Please post bugs and patches for this early release, in the issue queue 
(http://drupal.org/project/issues/views_slideshow_ddblock) of the module.

CONTACT
-------
* Philip Blaauw (ppblaauw) - http://drupal.org/user/155138

PAID SERVICES
-------------
We also offer paid services: installation, development, theming, customization.
You can contact us via the contact form on http://ddblock.myalbums.biz.
or via email to ppblaauw (at) gmail.com

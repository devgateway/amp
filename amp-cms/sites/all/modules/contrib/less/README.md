
LESS CSS Preprocessor
=====================

This module allows for automatic compilation of LESS styles sheets.

Requirements
------------

LESS requires at least one of two possible engines available.

[oyejorge/less.php]: http://lessphp.gpeasy.com/
[less.js]: http://lesscss.org/usage/#command-line-usage

 -  [oyejorge/less.php]
    
    This is a pure PHP implementation, which is good for shared hosting, or if you don't feel comfortable
    installing or configuring software on your server.
    
    It does lack the ability to execute javascript embedded in .less files, so some LESS libraries might not work.
    
    Requirements:
    
    1. [Libraries](https://drupal.org/project/libraries)
    2. [oyejorge/less.php] installed such that 'Less.php' is at `sites/all/libraries/less.php/Less.php`

 -  [less.js]
    
    This requires that PHP is able to use the `proc_open` function and call `lessc` from the `DRUPAL_ROOT` folder.
    
    


 - 
lessphp library unpacked so that 'lessc.inc.php' is located at 'sites/all/libraries/lessphp/lessc.inc.php'.


LESS Development:

Syntax: http://lesscss.org/features/



File placement:
If your source file was "sites/all/modules/test/test.css.less"
Then your compiled file will be "sites/[yoursite]/files/less/[random.string]/sites/all/modules/test/test.css"

Usage
-----

The following two examples provide equivalent functionality.

drupal\_add\_css:

    <?php
    $module_path = drupal_get_path('module', 'less_demo');
    drupal_add_css($module_path . '/styles/less_demo.css.less');
    ?>


.info file:

    stylesheets[all][] = styles/less_demo.css.less

For automatic variable and function association with non globally added
stylesheets, you can associate a stylesheet using this notation in .info files:

    less[sheets][] = relative/path/to/stylesheet.css.less


Compatibility
-------------

Should work with most themes and caching mechanisms.


### CSS Aggregation

Fully compatible with "Optimize CSS files" setting on "Admin->Site configuration->Performance" (admin/settings/performance).


### RTL Support

RTL support will work as long as your file names end with ".css.less".

Assuming your file is named "somename.css.less", Drupal automatically looks for a file name "somename-rtl.css.less"

Variables
---------

Variable defaults can be defined in .info files for modules or themes. Any variables defined will be automatically available inside style sheets associated with the module or theme.

.info file:

    less[vars][@varname] = #bada55

Look in less.api.php for LESS Variable hooks.
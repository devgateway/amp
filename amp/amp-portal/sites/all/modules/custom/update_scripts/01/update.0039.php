<?php
// $Id: update.0039.php,v 1.6 2012/02/29 19:35:57 lananikyan Exp $

$role = user_role_load_by_name('editor');
$permissions = array(
    
    'access user profiles', 'access site reports',
    'access content overview', 'access administration pages',

    'administer content translations', 'administer menu','administer nodequeue',
    'administer page manager', 'administer panelizer node page content','administer panelizer node page layout',
    'administer nodes',
    
    'create donor_profile content', 'create event content', 'create homepage_slideshow content',
    'create news content', 'create page content', 'create map content', 'create feed content',
    'create imported_item content', 'create feed content',

    'delete any event content', 'delete any feed content', 'delete any homepage_slideshow content',
    'delete any map content', 'delete any news content', 'delete any page content',
    'delete own donor_profile content', 'delete own event content',
    'delete own feed content', 'delete own homepage_slideshow content',  'delete own map content',
    'delete own news content', 'delete own page content', 'delete any map content',
    'delete any donor_profile content', 'delete any imported_item content', 'delete own imported_item content',
    'delete own feed content', 'delete any feed content', 'delete revisions', 'revert revisions', 
    
    'edit any event content', 'edit any feed content', 'edit any homepage_slideshow content',
    'edit any news content', 'edit any page content', 'manipulate all queues', 'manipulate queues',
    'edit own page content',  'edit own news content', 'edit any map content', 'edit own map content',
    'edit own donor_profile content', 'edit any donor_profile content', 'edit own event content',
    'edit own homepage_slideshow content', 'edit any imported_item content', 'edit own imported_item content',
    'edit own feed content', 'edit any feed content',
    
    'view own unpublished content','view revisions', 'view feedback messages',
    
    'translate content', 'translate interface', 
    
    'cancel account','change own username'
    );
user_role_grant_permissions($role->rid, $permissions);
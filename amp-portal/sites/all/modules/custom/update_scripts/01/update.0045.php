<?php
// $Id: update.0045.php,v 1.1 2012/03/05 17:26:07 lananikyan Exp $



$role = user_role_load_by_name('editor');
$permissions = array(   
    'access toolbar'
);
user_role_grant_permissions($role->rid, $permissions);

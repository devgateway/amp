<?php
// $Id: update.0042.php,v 1.1 2012/02/28 19:00:01 lananikyan Exp $



$role = user_role_load_by_name('editor');
$permissions = array(
    'access administration menu'
);
user_role_grant_permissions($role->rid, $permissions);

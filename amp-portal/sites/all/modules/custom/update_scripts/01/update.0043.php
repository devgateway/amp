<?php
// $Id: update.0043.php,v 1.1 2012/03/01 16:45:07 lananikyan Exp $



$role = user_role_load_by_name('editor');
$permissions = array(   
    'create imported_item content'
);
user_role_revoke_permissions($role->rid, $permissions);

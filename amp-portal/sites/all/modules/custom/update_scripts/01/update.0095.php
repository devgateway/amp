<?php

_us_revert_feature('ampp_projects');

// Disable log queries on all environments.
variable_set('log_db_queries', 0);

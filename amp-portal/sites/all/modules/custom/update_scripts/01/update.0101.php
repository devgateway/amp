<?php

// Disable feedback on admin sections.
variable_set('feedback_excluded_paths', 'admin');

// Disable user registration.
variable_set('user_register', '0');

// Install diff module.
_us_install_module('diff');

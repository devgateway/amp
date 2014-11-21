
The module was built in-house by Development Gateway in order to ease deployment
for the Drupal platform. It is used for continuous integration development /
micro-release, in order to propagate features and database changes from the
development machine to staging/production and also to the other developers.

This module was needed because Drupal stores its settings mostly in the
database, which makes it hard to "merge" two versions of settings on different
installations. We chose to do this by deploying PHP scripts, which can be stored
in source control. Usually, when a developer commits new code, just before
resolving his ticket, this will be associated with one or more "update scripts",
written in PHP.

It's an alternative to hook_update_N() in your features.module.

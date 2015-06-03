# String Overrides

Provides a quick and easy way to replace any text on the site.

## Features

*   Easily replace anything that's passed through [t()](http://api.drupal.org/api/function/t)
*   Locale support, allowing you to override strings in any language
*   Ability to import/export *.po files, for easy migration from the Locale module
*   Note that this is not a replacement to Locale as having thousands of overrides can cause more pain then benefit. Use this only if you need a few easy text changes

## Installation

1. Extract to your _sites/all/modules_ directory.
2. Enable the module in _admin/build/modules_.
3. Configure the access permission in _admin/user/access_ or _admin/user/permissions_.
4. Visit _admin/settings/stringoverrides_ or _admin/config/regional/stringoverrides_ and fill in the strings you'd like to replace.
5. Dance (if you arn't dancing, then try emptying your cache)!

## Troubleshooting

1. Empty your cache
2. Check your _settings.php_ for any `$conf["locale_custom_strings_en"]` overrides, these conflict with the administration interface
3. Check the code to make sure your override is what's presented in `t()` in the code
4. Overlay module has been reported to break the administration. Please make sure to remove the "overlay=" in the URL.

## Development

Head over to [the project on GitHub](http://github.com/drupalprojects/stringoverrides) for the issue queue. Features and bug fixes are done through pull requests.

## Credits

* Written by [Rob Loach](http://www.robloach.net)
* Idea from the [Lullabot team](http://www.lullabot.com/audiocast/podcast-50-drupal-tips-and-tricks)
* [Jeff Eaton](http://drupal.org/user/16496) suggested the name _Swappy_, but String Overrides was more descriptive
* [Nathan Haug](http://drupal.org/user/35821) did the [Drupal 5 backport](http://www.lullabot.com/articles/replace-any-string-drupal-5-6-without-locale-module)
* [John VanDyk](http://drupal.org/user/2375) for [usability enhancements](http://drupal.org/node/234334)
* [Pasqualle](http://drupal.org/user/80733) for the [the Drupal 7 port](http://drupal.org/node/609948)

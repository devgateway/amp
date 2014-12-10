<?php
/**
 * @file
 * Define helper functions.
 */

/**
 * Returns an array of colors for a navigation menu.
 *
 * Variables are lazy-loaded so we can reference them when creating variants.
 * @TODO: cleanup variables.
 */
function helpertheme_color_pallete() {
  $colors = array();

  // Primary colors.
  $colors['primary'] = array(
    '#title' => 'Primary colors',
    '#weight' => 10,
  );

  $colors['primary']['clmain'] = array(
    '#value' => '#f0f0f0',
    '#desc' => 'page background',
    '#variants' => array(
      'clmain__alt' => array(
        '#value' => '#eceaea',
        '#desc' => 'alternative background color',
      ),
    ),
  );

  $colors['primary']['clcontent'] = array(
    '#value' => '#ffffff',
    '#desc' => 'content background',
    '#variants' => array(
      'clcontent__separator' => array(
        '#value' => '#d5d5d5',
        '#desc' => 'border header and content',
      ),
      'clcontent__featured' => array(
        '#value' => '#c4c4c4',
        '#desc' => 'featured blocks',
      ),
    ),
  );

  $colors['primary']['clnormal'] = array(
    '#value' => '#555555',
    '#desc' => 'normal text',
    '#variants' => array(
      'clnormal__dark' => array(
        '#value' => 'darken(@clnormal, 40%)',
        '#desc' => 'important text',
      ),
    ),
  );

  $colors['primary']['cldetail'] = array(
    '#value' => '#2e5983',
    '#desc' => 'links, highlighted items, page border',
    '#variants' => array(
      'cldetail__highlight' => array(
        '#value' => '#ffca00',
        '#desc' => 'alternative highlight detail',
      ),
      'cldetail__alt' => array(
        '#value' => '#108cd2',
        '#desc' => 'alternative detail',
      ),
      'cldetail__separator' => array(
        '#value' => 'lighten(@cldetail, 35%)',
        '#desc' => 'menu border?!?',
      ),
    ),
  );


  // Secondary colors.
  $colors['secondary'] = array(
    '#title' => 'Secondary colors',
    '#weight' => 20,
  );

  $colors['secondary']['clpassive'] = array(
    '#value' => '#7c7c7c',
    '#desc' => 'pasive items',
    '#variants' => array(
    ),
  );

  $colors['secondary']['clactive'] = array(
    '#value' => '#4fa600',
    '#desc' => 'highlighted items',
    '#variants' => array(
      'clactive__alt' => array(
        '#value' => '#f69c00',
        '#desc' => 'alternative color',
      ),
    ),
  );


  // HTML tag specific colors.
  $colors['htmltag'] = array(
    '#title' => 'HTML tag specific colors',
    '#weight' => 40,
  );

  $colors['htmltag']['clanchor'] = array(
    '#value' => '@cldetail',
    '#desc' => 'anchor tags',
    '#variants' => array(
      'clanchor__hover' => array(
        '#value' => 'darken(@clanchor, 15%)',
        '#desc' => 'focused anchor tags',
      ),
      'clanchor__visited' => array(
        '#value' => 'darken(@clanchor, 15%)',
        '#desc' => 'visited anchor tags',
      ),
      'clanchor__active' => array(
        '#value' => 'darken(@clanchor, 25%)',
        '#desc' => 'active anchor tags',
      ),
      'clanchor__alt' => array(
        '#value' => '#0074bd',
        '#desc' => 'alternative anchor tags',
      ),
    ),
  );


  // Notification colors.
  $colors['notification'] = array(
    '#title' => 'Notification message colors',
    '#weight' => 50,
  );

  $colors['notification']['clstatus'] = array(
    '#value' => '#6bc93f',
    '#desc' => 'success message base color',
    '#variants' => array(
      'clstatus__dark' => array(
        '#value' => 'darken(@clstatus, 40%)',
        '#desc' => 'text',
      ),
      'clstatus__alt' => array(
        '#value' => 'desaturate(@clstatus, 18)', // #73b355
        '#desc' => 'icons and box shadow',
      ),
      'clstatus__faded' => array(
        '#value' => 'lighten(@clstatus__alt, 18%)',
        '#desc' => 'box border',
      ),
      'clstatus__light' => array(
        '#value' => 'lighten(@clstatus, 40%)',
        '#desc' => 'box background',
      ),
    ),
  );

  $colors['notification']['clwarning'] = array(
    '#value' => '#d4920e',
    '#desc' => 'warning message base color',
    '#variants' => array(
      'clwarning__dark' => array(
        '#value' => 'darken(@clwarning, 24%)',
        '#desc' => 'text color',
      ),
      'clwarning__alt' => array(
        '#value' => 'saturate(@clwarning, 18)', // #e29700
        '#desc' => 'icons and box shadow',
      ),
      'clwarning__faded' => array(
        '#value' => 'lighten(@clwarning__alt, 36%)',
        '#desc' => 'box border',
      ),
      'clwarning__light' => array(
        '#value' => 'lighten(@clwarning, 52%)',
        '#desc' => 'box background',
      ),
    ),
  );

  $colors['notification']['clerror'] = array(
    '#value' => '#d53615',
    '#desc' => 'error message base color',
    '#variants' => array(
      'clerror__dark' => array(
        '#value' => 'darken(@clerror, 24%)',
        '#desc' => 'text color',
      ),
      'clerror__alt' => array(
        '#value' => 'saturate(@clerror, 18)', // #ea2800
        '#desc' => 'icons and box shadow',
      ),
      'clerror__faded' => array(
        '#value' => 'lighten(@clerror__alt, 36%)',
        '#desc' => 'box border',
      ),
      'clerror__light' => array(
        '#value' => 'lighten(@clerror, 50%)',
        '#desc' => 'box background',
      ),
    ),
  );

  return $colors;
}

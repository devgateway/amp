<?php
 /**
  * This template is used to print a single field in a view. It is not
  * actually used in default Views, as this is registered as a theme
  * function which has better performance. For single overrides, the
  * template is perfectly okay.
  *
  * Variables available:
  * - $view: The view object
  * - $field: The field handler object that can process the input
  * - $row: The raw SQL result that can be used
  * - $output: The processed output that will normally be used.
  *
  * When fetching output from the $row, this construct should be used:
  * $data = $row->{$field->field_alias}
  *
  * The above will guarantee that you'll always get the correct data,
  * regardless of any changes in the aliasing that might happen if
  * the view is modified.
  */
?>
<?php //print_r($row->_field_data);
  if (  isset($row->_field_data['nid']['entity']->field_slideshow_image_toggle) &&
        isset($row->_field_data['nid']['entity']->field_slideshow_image_toggle['und']) &&
        ($row->_field_data['nid']['entity']->field_slideshow_image_toggle['und']['0']['value'] == 1)) {
    // we use original image, so make sure the view does not return records that do not have original image   
    $img_url = $row->_field_data['nid']['entity']->field_image['und']['0']['uri'];
    $img = theme('image_style', array('style_name' => 'homepage_news_slideshow', 'path' => $img_url));
    // if we see that the item does not have slideshow image than we need to generate the link, otherswise just the img tag
    if (!empty($row->_field_data['nid']['entity']->field_slideshow_image['und']['0']['uri'])) {
      $new_html = $img;
    } else {
      $new_html = l($img, 'node/' . $row->_field_data['nid']['entity']->nid, array('html' => true));
      //echo $img;
    }
    $output = preg_replace('/<img[^>]+\>/i', $new_html, $output);
    $output = str_replace($row->_field_data['nid']['entity']->field_slideshow_image['und']['0']['filename'], $row->_field_data['nid']['entity']->field_image['und']['0']['filename'], $output);     
  }
  print $output;
?>
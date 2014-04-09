<?php
?>
<div class="ampphome-layout" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>
  <div class="ampphome-slideshow clearfix">
    <div class="ampphome-slideshow-container"><?php print $content['slideshow']; ?></div>
  </div>

  <div class="clearfix">
    <div id="col1">
      <div class="ampphome-search clearfix">
        <div class="ampphome-search-container"><?php print $content['search']; ?></div>
      </div>

      <div class="ampphome-donor-map clearfix">
        <div class="ampphome-donor-container"><?php print $content['donor_profile']; ?></div>
        <div class="ampphome-map-container"><?php print $content['map']; ?></div>
      </div>

      <div class="ampphome-contact-social clearfix">
        <div class="ampphome-contact-container"><?php print $content['quick_contact']; ?></div>
      </div>
    </div>

    <div id="col2">
      <div class="ampphome-news">
        <div class="ampphome-news-slideshow-container"><?php print $content['news_slideshow']; ?></div>
      </div>
    </div>
  </div>
</div>

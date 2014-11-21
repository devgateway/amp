<?php

/**
 * @file media_youtube/themes/media-youtube-video.tpl.php
 *
 * We need inline elements here because we can't have DIV elements inside P elements.
 *
 * @see http://www.w3.org/TR/html-markup/span.html
 */

?>
<span class="<?php print $classes; ?> media-resizable-wrapper media-youtube-<?php print $id; ?>">
  <iframe class="media-resizable-element media-youtube-player" <?php print $api_id_attribute; ?>width="<?php print $width; ?>" height="<?php print $height; ?>" title="<?php print $title; ?>" src="<?php print $url; ?>" frameborder="0" allowfullscreen><?php print $alternative_content; ?></iframe>
</span>

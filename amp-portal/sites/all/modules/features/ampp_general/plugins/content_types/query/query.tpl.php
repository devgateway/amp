
<?php if ($message): ?>
  <table class="query_plugin_table">
    <tr><td>
      <?php print $message; ?>
    </td></tr>
  </table>
<?php else: ?>
  <table class="query_plugin_table">
    <tr>
      <?php foreach ($labels as $label): ?>
        <th><?php print $label; ?></th>
      <?php endforeach; ?>
    </tr>

    <?php foreach ($rows as $row): ?>
      <tr>
        <?php foreach ($row as $value): ?>
          <td><?php print $value; ?></td>
        <?php endforeach; ?>
      </tr>
    <?php endforeach; ?>
  </table>
<?php endif; ?>

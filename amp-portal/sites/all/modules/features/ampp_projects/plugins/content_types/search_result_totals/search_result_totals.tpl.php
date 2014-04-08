<?php

/**
 * @file
 * Display search total commitment and disbursement ammounts.
 */
?>

<div class="search-results-total-amounts view-projects-search-result">
  <table class="views-table commitment-disbursement-table">
    <thead>
      <tr>
        <th></th>
        <th><?php print $commitment_header; ?></th>
        <th><?php print $disbursement_header; ?></th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><?php print t('Total') . ':'; ?></td>
        <td class="commitment"><?php print $commitment_amount; ?></td>
        <td class="disbursement"><?php print $disbursement_amount; ?></td>
      </tr>
    </tbody>
  </table>
</div>

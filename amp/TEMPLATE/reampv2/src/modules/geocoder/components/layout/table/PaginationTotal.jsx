import React from 'react';


export const PaginationTotal = (from, to, size) => (
    <span className="react-bootstrap-table-pagination-total">
        {from} to {to} of {size} projects
    </span>
);

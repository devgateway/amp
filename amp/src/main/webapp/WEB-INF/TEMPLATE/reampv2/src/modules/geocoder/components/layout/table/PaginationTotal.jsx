import React from 'react';


export const PaginationTotal = (from, to, size, translations) => (
    <span className="react-bootstrap-table-pagination-total">
        {from} {translations['amp.geocoder:paginationTo']} {to} {translations['amp.geocoder:paginationOf']} {size} {translations['amp.geocoder:paginationProjects']}
    </span>
);

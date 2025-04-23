
export function parseDate(str) {
    let m = str.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/);
    return (m) ? new Date(m[3], m[2]-1, m[1]) : null;
}

export function orderDates(a, b, order) {
    if (order === 'asc') {
        return parseDate(a) - parseDate(b);
    } else {
        return parseDate(b) - parseDate(a);
    }
}
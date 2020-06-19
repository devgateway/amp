import { Img } from 'react-image';
import { FLAG_DEFAULT, FLAGS_DIRECTORY } from './constants';
import React from 'react';

export function splitArray(a, n, balanced) {
    if (n < 2)
        return [a];

    const len = a.length, out = [];
    let i = 0, size;

    if (len % n === 0) {
        size = Math.floor(len / n);
        while (i < len) {
            out.push(a.slice(i, i += size));
        }
    } else if (balanced) {
        while (i < len) {
            size = Math.ceil((len - i) / n--);
            out.push(a.slice(i, i += size));
        }
    } else {

        n--;
        size = Math.floor(len / n);
        if (len % size === 0)
            size--;
        while (i < size * n) {
            out.push(a.slice(i, i += size));
        }
        out.push(a.slice(size * n));

    }

    return out;
}

export function compareArrayNumber(a, b) {
    a.sort((a, b) => a - b);
    b.sort((a, b) => a - b);
    const left = [], both = [], right = [];
    let i = 0, j = 0;
    while (i < a.length && j < b.length) {
        if (a[i] < b[j]) {
            left.push(a[i]);
            ++i;
        } else if (b[j] < a[i]) {
            right.push(b[j]);
            ++j;
        } else {
            both.push(a[i]);
            ++i;
            ++j;
        }
    }
    while (i < a.length) {
        left.push(a[i]);
        ++i;
    }
    while (j < b.length) {
        right.push(b[j]);
        ++j;
    }
    //left and right is the difference not in use but keeping it if needed
    if (a.length !== b.length) {
        return false;
    }
    return a.length === both.length;
}

export function toCamelCase(str) {
    return str.toLowerCase().split(' ').map(s => s.charAt(0).toUpperCase() + s.slice(1)
    ).join(' ');

}

//TODO move to another utility class.
export function getCountryFlag(name) {
    return [`${process.env.PUBLIC_URL}${FLAGS_DIRECTORY}${name.toLowerCase().replace(/ /g, "_")}.svg`
        , `data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAbCAQAAACkGQXlAAAAI0lEQVR42mP8L8lAVcA4auCogaMGjho4auCogaMGjhpINAAAOBcdpLw/CDsAAAAASUVORK5CYII=`];
}

export function calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions) {
    let updatedSelectedOptions;
    if (selectedOptions.includes(ipSelectedFilter)) {
        updatedSelectedOptions = selectedOptions.filter(sc => sc !== ipSelectedFilter);
    } else {
        updatedSelectedOptions = [...selectedOptions];
        updatedSelectedOptions.push(ipSelectedFilter);
    }
    return updatedSelectedOptions;
}


export const FETCH_FILTERS_SECTORS_PENDING = 'FETCH_FILTERS_SECTORS_PENDING';
export const FETCH_FILTERS_SECTORS_SUCCESS = 'FETCH_FILTERS_SECTORS_SUCCESS';
export const FETCH_FILTERS_SECTORS_ERROR = 'FETCH_FILTERS_SECTORS_ERROR';
export const FETCH_FILTERS_COUNTRIES_PENDING = 'FETCH_FILTERS_COUNTRIES_PENDING';
export const FETCH_FILTERS_COUNTRIES_SUCCESS = 'FETCH_FILTERS_COUNTRIES_SUCCESS';
export const FETCH_FILTERS_COUNTRIES_ERROR = 'FETCH_FILTERS_COUNTRIES_ERROR';
export const FETCH_FILTERS_MODALITIES_PENDING = 'FETCH_FILTERS_MODALITIES_PENDING';
export const FETCH_FILTERS_MODALITIES_SUCCESS = 'FETCH_FILTERS_MODALITIES_SUCCESS';
export const FETCH_FILTERS_MODALITIES_ERROR = 'FETCH_FILTERS_MODALITIES_ERROR';

export function fetchSectorsPending() {
    return {
        type: FETCH_FILTERS_SECTORS_PENDING
    }
}

export function fetchSectorsSuccess(sectorsfilter) {
    return {
        type: FETCH_FILTERS_SECTORS_SUCCESS,
        payload: sectorsfilter.items.primary
    }
}

export function fetchSectorsError(error) {
    return {
        type: FETCH_FILTERS_SECTORS_ERROR,
        error: error
    }
};

export function fetchCountriesPending() {
    return {
        type: FETCH_FILTERS_COUNTRIES_PENDING
    }
}

export function fetchCountriesSuccess(countriesFilter) {
    //this filter should check the SSC template as parameter
    const countries =
        countriesFilter.items.locations.filter(c => c['extra_info'] && c['extra_info'].template);
    return {
        type: FETCH_FILTERS_COUNTRIES_SUCCESS,
        payload: countries
    }
}

export function fetchCountriesError(error) {
    return {
        type: FETCH_FILTERS_COUNTRIES_ERROR,
        error: error
    }
}

export function fetchModalitiesPending() {
    return {
        type: FETCH_FILTERS_MODALITIES_PENDING
    }
}

export function fetchModalitiesSuccess(sectorsFilter) {
    return {
        type: FETCH_FILTERS_MODALITIES_SUCCESS,
        payload: sectorsFilter.items.values
    }
}

export function fetchModalitiesError(error) {
    return {
        type: FETCH_FILTERS_MODALITIES_ERROR,
        error: error
    }
}

import { fetchApiData } from '../../../utils/apiOperations';
import {
    CURRENCY_CODE, DEFAULT_CURRENCY,
    DIRECT_INDIRECT_REPORT, FUNDING_TYPE, INCLUDE_LOCATIONS_WITH_CHILDREN, TOP_DONOR_REPORT,
    ACTIVITY_DETAIL_REPORT
} from '../utils/constants';
import { removeFilter } from '../utils/Utils';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

const initialState = {
    topReport: {
        data: [],
        loading: false,
        error: null
    },
    indirectReport: {
        data: [],
        loading: false,
        error: null
    },
    yearDetailReport: {
        data: [],
        loading: false,
        error: null
    }
};

interface CallReportParams {
    fundingType: string;
    settings: any;
    filters: any;
    selectedProgram: any;
    programIds: Array<any | string>;
}

export const callReport = createAsyncThunk(
    'callReports/callReport',
    async (params: CallReportParams, { rejectWithValue }) => {
        const { fundingType, settings, filters, programIds } = params;
        const newSettings = { [FUNDING_TYPE]: fundingType, programIds, ...settings };
        const body = { ...filters };
        body.settings = newSettings;

        const response = await Promise.all([fetchApiData({
            url: DIRECT_INDIRECT_REPORT,
            body
        })]);

        if (response[0].error) {
            return rejectWithValue(response[0]);
        }

        return response[0];
    }
);

interface CallTopReportParams {
    fundingType: string;
    settings: any;
    filterParam: any;
    selectedProgram: any;
    programIds: Array<any | string>;
}

export const callTopReport = createAsyncThunk(
    'callReports/callTopReport',
    async (data: CallTopReportParams, { rejectWithValue }) => {
        const { fundingType, settings, filterParam, selectedProgram } = data;
        let params = { ...filterParam };
        if (!params.filters) {
            params.filters = {};
            params[INCLUDE_LOCATIONS_WITH_CHILDREN] = true;
        }
        if (selectedProgram !== null) {
            if (!params.filters[selectedProgram.filterColumnName]) {
                params.filters[selectedProgram.filterColumnName] = [];
            }
            if (!params.filters[selectedProgram.filterColumnName].find((v: any) => v === selectedProgram.objectId)) {
                params.filters[selectedProgram.filterColumnName].push(selectedProgram.objectId);
            }
        }
        params.settings = { ...settings, [FUNDING_TYPE]: fundingType };
        if (!params.settings[CURRENCY_CODE]) {
            params.settings[CURRENCY_CODE] = DEFAULT_CURRENCY;
        }

        try {
            const response = await fetchApiData({
                url: TOP_DONOR_REPORT,
                body: params
            });

            return response;
        } catch (error) {
            return rejectWithValue(error);
        } finally {
            // We need to revert the extra param added for TopChart or it will affect other calls to the BE (like
            // detail of activities).
            if (selectedProgram !== null) {
                params = removeFilter(params, selectedProgram);
            }
        }
    }
);

interface CallYearDetailReportParams extends Omit<CallReportParams, 'programIds'> {
    programId: string;
    year: number;
}

export const callYearDetailReport = createAsyncThunk(
    'callReports/callYearDetailReport',
    async (data: CallYearDetailReportParams, { rejectWithValue }) => {
        const { fundingType, filters, programId, year, settings } = data;
        const newSettings = {
            [FUNDING_TYPE]: fundingType, id: programId, year, ...settings
        };
        const response = await fetchApiData({
            url: ACTIVITY_DETAIL_REPORT,
            body: {
                settings: newSettings,
                filters: (filters ? filters.filters : null)
            }
        });

        if (response?.error) {
            return rejectWithValue(response);
        }

        return response;
    }
);

const callReportsSlice = createSlice({
    name: 'callReports',
    initialState,
    reducers: {
        clearTopReport: (state) => {
            state.topReport.data = [];
        }
    },
    extraReducers: (builder) => {
        builder.addCase(callReport.pending, (state) => {
            state.indirectReport.loading = true;
        });
        builder.addCase(callReport.fulfilled, (state, action) => {
            state.indirectReport.loading = false;
            state.indirectReport.data = action.payload;
        });
        builder.addCase(callReport.rejected, (state, action) => {
            state.indirectReport.loading = false;
            state.indirectReport.error = action.payload as any;
        });

        // Top Report
        builder.addCase(callTopReport.pending, (state) => {
            state.topReport.loading = true;
        });

        builder.addCase(callTopReport.fulfilled, (state, action) => {
            state.topReport.loading = false;
            state.topReport.data = action.payload;
        });

        builder.addCase(callTopReport.rejected, (state, action) => {
            state.topReport.loading = false;
            state.topReport.error = action.payload as any;
        });

        // Year Detail Report
        builder.addCase(callYearDetailReport.pending, (state) => {
            state.yearDetailReport.loading = true;
        });

        builder.addCase(callYearDetailReport.fulfilled, (state, action) => {
            state.yearDetailReport.loading = false;
            state.yearDetailReport.data = action.payload;
        });

        builder.addCase(callYearDetailReport.rejected, (state, action) => {
            state.yearDetailReport.loading = false;
            state.yearDetailReport.error = action.payload as any;
        });
    }
});

export const { clearTopReport } = callReportsSlice.actions;

import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import {ClassificationType, SectorReport} from "../types";
import { REST_SECTORS_REPORT } from '../../utils/constants';

const REDUCER_NAME = 'sectorReport';

type SectorReportInitialStateType = {
    data: SectorReport | null;
    loading: boolean;
    error: any;
}

const initialState: SectorReportInitialStateType = {
    data: null,
    loading: false,
    error: null,
}

interface FetchSectorReportParams {
    classificationType: string;
    filters?: any;
    settings?: any;
}

export const fetchSectorReport = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async ({ classificationType, filters, settings }: FetchSectorReportParams, { rejectWithValue }) => {
        let urlParams = '';
        if (classificationType === ClassificationType.PRIMARY) {
            urlParams = '/ps?limit=5';
        }
        if (classificationType === ClassificationType.SECONDARY) {
            urlParams = '/ss?limit=5';
        }

        const requestBody = {
            ...filters,
            settings
        };

        const response = await fetch(`${REST_SECTORS_REPORT}${urlParams}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...requestBody }),
        });

        const data: SectorReport = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);


const fetchSectorReportSlice = createSlice({
    name: 'fetchSectorReportReducer',
    initialState,
    reducers: {
        resetState : () => initialState,
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSectorReport.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectorReport.fulfilled, (state, action) => {
            state.data = null;
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchSectorReport.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
        });
    }
});

export const {
    resetState
} = fetchSectorReportSlice.actions;

export default fetchSectorReportSlice.reducer;

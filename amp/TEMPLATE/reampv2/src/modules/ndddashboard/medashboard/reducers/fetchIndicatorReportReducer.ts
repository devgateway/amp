import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { YearValues } from '../types';
import { REST_INDICATOR_REPORT } from "../../utils/constants";

const REDUCER_NAME = 'indicatorReport';

type IndicatorReportInitialStateType = {
    data: YearValues | null;
    loading: boolean;
    error: any;
}

const initialState: IndicatorReportInitialStateType = {
    data: null,
    loading: false,
    error: null,
}

export const fetchIndicatorReport = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async ({ filters, id, yearCount, settings } : { filters: any, id: number, yearCount?: number, settings: any }, { rejectWithValue }) => {
        let count = 5;

        if (yearCount && yearCount > 5) {
            count = yearCount;
        }

        const requestBody = {
            ...filters,
            settings: {
                ...settings,
                yearCount: count
            }
        }

        const response = await fetch(`${REST_INDICATOR_REPORT}/${id}`,{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...requestBody }),
        });
        const data: YearValues = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }
        return data;
    }
);

const indicatorReportSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        resetState : (state) => {
            state.data = null;
            state.loading = false;
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchIndicatorReport.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchIndicatorReport.fulfilled, (state, action) => {
            state.error = null;
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchIndicatorReport.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = indicatorReportSlice.actions;
export default indicatorReportSlice.reducer;

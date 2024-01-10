import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { YearValues } from '../types';
import { REST_INDICATOR_REPORT } from "../../utils/constants";

const REDUCER_NAME = 'indicatorReport';

type IndicatorReportInitialStateType = {
    left: {
        loading: boolean;
        data: YearValues | null;
        error: any;
    }
    right: {
        loading: boolean;
        data: YearValues | null;
        error: any;
    }
}

const initialState: IndicatorReportInitialStateType = {
    left: {
        loading: false,
        data: null,
        error: null,
    },
    right: {
        loading: false,
        data: null,
        error: null,
    }
}

export const fetchIndicatorReport = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async ({ filters, id, yearCount, settings, section } : { filters: any, id: number, yearCount?: number, settings: any, section: 'left' | 'right' }, { rejectWithValue }) => {
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

        if (section === 'left') {
            return {
                leftData: data,
                rightData: null
            }
        }

        return {
            leftData: null,
            rightData: data
        }
    }
);

const indicatorReportSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        resetState : (state) => {
            state.right = {
                loading: false,
                data: null,
                error: null,
            } as const;
            state.left = {
                loading: false,
                data: null,
                error: null,
            } as const;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchIndicatorReport.pending, (state) => {
            state.right.loading = true;
            state.left.loading = true;
        });
        builder.addCase(fetchIndicatorReport.fulfilled, (state, action) => {
            state.right.loading = false;
            state.left.loading = false;
            state.left.data = action.payload.leftData;
            state.right.data = action.payload.rightData;
        });
        builder.addCase(fetchIndicatorReport.rejected, (state, action) => {
            state.right.loading = false;
            state.left.loading = false;
            state.right.error = errorHelper(action.payload);
            state.left.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = indicatorReportSlice.actions;
export default indicatorReportSlice.reducer;

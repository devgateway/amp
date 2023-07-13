import { errorHelper } from '../../../admin/indicator_manager/utils/errorHelper';
import { REST_PROGRAM_PROGRESS_REPORT } from '../../utils/constants';
import { ReportData } from '../types';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

const REDUCER_NAME = 'progressReport';

type ProgressReportInitialStateType = {
    data: ReportData | null;
    loading: boolean;
    error: any;
}

const initialState: ProgressReportInitialStateType = {
    data: null,
    loading: false,
    error: null
}

export const fetchProgramProgressReport = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async ({ filters, id} : { filters: any, id: number }, { rejectWithValue }) => {
        const response = await fetch(`${REST_PROGRAM_PROGRESS_REPORT}/${id}`,{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...filters }),
        });
        const data: ReportData = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const programProgressReportSlice = createSlice({
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
        builder.addCase(fetchProgramProgressReport.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchProgramProgressReport.fulfilled, (state, action) => {
            state.data = null;
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchProgramProgressReport.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const { resetState } = programProgressReportSlice.actions;
export default programProgressReportSlice.reducer;


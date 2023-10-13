import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { PROGRESS_TRACKING_DASHBOARDS, REST_FM_SETTINGS} from "../../utils/constants";
import { extractFmColumnsData } from "../utils/data";

const REDUCER_NAME = 'fm';

type FmInitialStateType = {
    data: any;
    loading: boolean;
    error: any;
}

const initialState: FmInitialStateType = {
    data: null,
    loading: false,
    error: null,
}

export const fetchFm = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async ( _, { rejectWithValue }) => {
        const body = {
            "enabled-modules": false,
            "reporting-fields": false,
            "full-enabled-paths": false,
            "detail-modules": [
                "PROGRESS TRACKING DASHBOARDS"
            ]
        }

        const response = await fetch(REST_FM_SETTINGS,{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...body }),
        });
        const data: any = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }
        return data;
    }
);

const fmSlice = createSlice({
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
        builder.addCase(fetchFm.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchFm.fulfilled, (state, action) => {
            state.error = null;
            state.loading = false;
            state.data = extractFmColumnsData(action.payload, PROGRESS_TRACKING_DASHBOARDS);
        });
        builder.addCase(fetchFm.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
        });
    }
});

export const { resetState } = fmSlice.actions;
export default fmSlice.reducer;

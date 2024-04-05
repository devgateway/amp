import { errorHelper } from './../utils/errorHelper';
import { IndicatorObjectType } from './../types';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";

type CreateIndicatorInitialStateType = {
    createdIndicator: IndicatorObjectType | null;
    loading: boolean;
    error: any;
}

const initialState: CreateIndicatorInitialStateType = {
    createdIndicator: null,
    loading: false,
    error: null,
}

export const createIndicator = createAsyncThunk(
    "indicators/createIndicator",
    async (indicator: any, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/indicators", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(indicator),
        });
        const data: IndicatorObjectType = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const createIndicatorSlice = createSlice({
    name: "createIndicator",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(createIndicator.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(createIndicator.fulfilled, (state, action) => {
            state.loading = false;
            state.createdIndicator = action.payload;
            state.error = null;
        });
        builder.addCase(createIndicator.rejected, (state, action) => {
            state.loading = false;
            state.createdIndicator = null;
            state.error = errorHelper(action.payload);
        });
    }
});

export default createIndicatorSlice.reducer;

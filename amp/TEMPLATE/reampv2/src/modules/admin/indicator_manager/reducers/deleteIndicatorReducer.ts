import { IndicatorObjectType } from './../types';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";

type IndicatorInitialStateType = {
    indicator: IndicatorObjectType | null;
    loading: boolean;
    error: any;
}

const initialState: IndicatorInitialStateType = {
    indicator: null,
    loading: false,
    error: null,
}

export const deleteIndicator = createAsyncThunk(
    "indicators/deleteIndicator",
    async (id: number, { rejectWithValue }) => {
        const response = await fetch(`/rest/indicatorManager/indicators/${id}`, {
            method: "DELETE",
        });
        const data: IndicatorObjectType = await response.json();

        if (response.status !== 204) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const deleteIndicatorSlice = createSlice({
    name: "deleteIndicator",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(deleteIndicator.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(deleteIndicator.fulfilled, (state, action) => {
            state.loading = false;
            state.indicator = action.payload;
        });
        builder.addCase(deleteIndicator.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error;
    
        });
    }
});

export default deleteIndicatorSlice.reducer;
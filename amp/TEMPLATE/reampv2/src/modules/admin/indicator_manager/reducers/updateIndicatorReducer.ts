import { errorHelper } from './../utils/errorHelper';
import { IndicatorObjectType } from './../types';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";

type UpdateIndicatorType = Partial<IndicatorObjectType> & {
    id: number;
};

type updateIndicatorInitialStateType = {
    indicator: UpdateIndicatorType | null;
    loading: boolean;
    error: any;
}

const initialState: updateIndicatorInitialStateType = {
    indicator: null,
    loading: false,
    error: null,
}

export const updateIndicator = createAsyncThunk(
    "indicators/updateIndicator",
    async (indicator: UpdateIndicatorType, { rejectWithValue }) => {
        const response = await fetch(`/rest/indicatorManager/indicators/${indicator.id}`, {
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

const updateIndicatorSlice = createSlice({
    name: "updateIndicator",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(updateIndicator.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(updateIndicator.fulfilled, (state, action) => {
            state.loading = false;
            state.indicator = action.payload;
        });
        builder.addCase(updateIndicator.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
    
        });
    }
});

export default updateIndicatorSlice.reducer;
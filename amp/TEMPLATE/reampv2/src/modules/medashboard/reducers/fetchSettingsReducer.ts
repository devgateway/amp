import { errorHelper } from './../utils/errorHelper';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { SettingsType } from "../types";

export const loadDashboardSettings = createAsyncThunk(
    'fetchSettings',
    async (_, { rejectWithValue }) => {
        const response = await fetch('/rest/amp/settings');
        const data = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

export const fetchSettingsSlice = createSlice({
    name: 'fetchSettings',
    initialState: {
        settings: {} as SettingsType,
        loading: false,
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(loadDashboardSettings.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(loadDashboardSettings.fulfilled, (state, action) => {
            state.loading = false;
            state.settings = action.payload;
        });
        builder.addCase(loadDashboardSettings.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
        });
    }
});

export default fetchSettingsSlice.reducer;

import { errorHelper } from '../../admin/indicator_manager/utils/errorHelper';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { SettingsType } from "../../admin/indicator_manager/types";

export const getSettings = createAsyncThunk(
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
        builder.addCase(getSettings.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getSettings.fulfilled, (state, action) => {
            state.loading = false;
            state.settings = action.payload;
        });
        builder.addCase(getSettings.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
        });
    }
});

export default fetchSettingsSlice.reducer;

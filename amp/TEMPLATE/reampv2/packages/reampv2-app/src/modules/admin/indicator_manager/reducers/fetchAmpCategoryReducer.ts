import { errorHelper } from '../utils/errorHelper';
import {AmpCategoryValue } from '../types';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

type AmpCategoryInitialStateType = {
    categories: AmpCategoryValue[];
    loading: boolean;
    error: any;
}

const initialState: AmpCategoryInitialStateType = {
    categories: [],
    loading: false,
    error: null,
}

export const getAmpCategories = createAsyncThunk(
    "ampCategories/getAmpCategories",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/categoryValues");
        const data: AmpCategoryValue [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data.sort((a, b) => a.value.localeCompare(b.value));
    }
);

const ampCategorySlice = createSlice({
    name: "ampCategories",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getAmpCategories.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getAmpCategories.fulfilled, (state, action) => {
            state.loading = false;
            state.categories = action.payload;
        });
        builder.addCase(getAmpCategories.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
        });
    }
});

export default ampCategorySlice.reducer;



import { errorHelper } from './../utils/errorHelper';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { SectorObjectType } from "../types";

type SectorsInitialStateType = {
    sectors: SectorObjectType[];
    loading: boolean;
    error: any;
};

const initialState: SectorsInitialStateType = {
    sectors: [],
    loading: false,
    error: null
};

export const getSectors = createAsyncThunk(
    "sectors/fetchSectors",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/sectors");
        const data: SectorObjectType[] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data.sort((a, b) => a.name.localeCompare(b.name));
    }
);

const fetchSectorsSlice = createSlice({
    name: "sectorsData",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getSectors.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getSectors.fulfilled, (state, action) => {
            state.loading = false;
            state.sectors = action.payload;
        });
        builder.addCase(getSectors.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export default fetchSectorsSlice.reducer;

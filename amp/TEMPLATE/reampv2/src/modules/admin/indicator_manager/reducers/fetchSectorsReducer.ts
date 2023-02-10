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

export const fetchSectors = createAsyncThunk(
    "sectors/fetchSectors",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/sectors");
        const data = await response.json();
        
        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchSectorsSlice = createSlice({
    name: "sectorsData",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchSectors.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectors.fulfilled, (state, action) => {
            state.loading = false;
            state.sectors = action.payload;
        });
        builder.addCase(fetchSectors.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error;
    
        });
    }
});

export default fetchSectorsSlice.reducer;
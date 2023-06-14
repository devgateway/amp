import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetchApiData } from '../../../utils/apiOperations';
import { INDIRECT_MAPPING_CONFIG } from '../utils/constants';

export const getMappings = createAsyncThunk(
    'mappings/getMappings',
    async (_, { rejectWithValue }) => {
        const response = await fetchApiData({
            url: INDIRECT_MAPPING_CONFIG as any,
            body: {}
        });
        if (response.error) {
            return rejectWithValue(response);
        }

        return response;
    }
);

const initialState = {
    data: [],
    loading: false,
    error: null
};

const getMappingsSlice = createSlice({
    name: 'mappings',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getMappings.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getMappings.fulfilled, (state, action) => {
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(getMappings.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload as any;
        });
    }
});

export const mappingsReducer = getMappingsSlice.reducer;


import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export interface Output {
  id: number;
  name: string;
  description?: string;
}

interface OutputsState {
  outputs: Output[];
  loading: boolean;
  error: string | null;
}

const initialState: OutputsState = {
  outputs: [],
  loading: false,
  error: null,
};

export const fetchOutputs = createAsyncThunk(
  'outputs/fetchOutputs',
  async (_, { rejectWithValue }) => {
    const response = await fetch('/rest/amp-outcome-output/outputs');
    const data = await response.json();
    if (response.status !== 200) {
      return rejectWithValue(data);
    }
    return data;
  }
);

const outputsSlice = createSlice({
  name: 'outputs',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(fetchOutputs.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(fetchOutputs.fulfilled, (state, action) => {
      state.loading = false;
      state.outputs = action.payload;
    });
    builder.addCase(fetchOutputs.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });
  },
});

export default outputsSlice.reducer;


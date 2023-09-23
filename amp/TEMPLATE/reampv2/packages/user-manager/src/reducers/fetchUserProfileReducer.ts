import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { UserProfile } from '../types';
import { REST_FETCH_USER } from '../utils/constants';
import { errorHelper } from '../utils/errorHelper';

const REDUCER_NAME = 'userProfile';

export type FetchUserProfileInitialStateType = {
    user: UserProfile | null;
    loading: boolean;
    error: any;
};

const initialState: FetchUserProfileInitialStateType = {
  user: null,
  loading: false,
  error: null,
};

export const fetchUserProfile = createAsyncThunk(
  `${REDUCER_NAME}/fetchUserProfile`,
  async (_, { rejectWithValue }) => {
    const response = await fetch(REST_FETCH_USER);
    const data: UserProfile | any = await response.json();

    if (response.status !== 200) {
      return rejectWithValue(data);
    }

    return data;
  },
);

const userProfileSlice = createSlice({
  name: REDUCER_NAME,
  initialState,
  reducers: {
    clearUser: (state) => {
      state.user = null;
    },
    updateUser: (state, action: { payload: UserProfile, type: string }) => {
      state.user = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchUserProfile.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(fetchUserProfile.fulfilled, (state, action) => {
      state.loading = false;
      state.user = action.payload;
    });
    builder.addCase(fetchUserProfile.rejected, (state, action) => {
      state.loading = false;
      state.error = errorHelper(action.payload);
    });
  },
});

export const { clearUser, updateUser } = userProfileSlice.actions;
export default userProfileSlice.reducer;

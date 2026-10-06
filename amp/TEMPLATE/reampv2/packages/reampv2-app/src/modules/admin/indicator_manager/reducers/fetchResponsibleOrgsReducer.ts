import { Dispatch } from 'redux';

export interface ResponsibleOrgOption {
  value: number;
  label: string;
}

interface State {
  options: ResponsibleOrgOption[];
  loading: boolean;
  error: string | null;
}

const initialState: State = {
  options: [],
  loading: false,
  error: null,
};

export const FETCH_RESPONSIBLE_ORGS_REQUEST = 'FETCH_RESPONSIBLE_ORGS_REQUEST';
export const FETCH_RESPONSIBLE_ORGS_SUCCESS = 'FETCH_RESPONSIBLE_ORGS_SUCCESS';
export const FETCH_RESPONSIBLE_ORGS_FAILURE = 'FETCH_RESPONSIBLE_ORGS_FAILURE';

export const fetchResponsibleOrgsReducer = (state = initialState, action: any): State => {
  switch (action.type) {
    case FETCH_RESPONSIBLE_ORGS_REQUEST:
      return { ...state, loading: true, error: null };
    case FETCH_RESPONSIBLE_ORGS_SUCCESS:
      return { ...state, loading: false, options: action.payload, error: null };
    case FETCH_RESPONSIBLE_ORGS_FAILURE:
      return { ...state, loading: false, error: action.payload };
    default:
      return state;
  }
};

export const getResponsibleOrgs = () => async (dispatch: Dispatch) => {
  dispatch({ type: FETCH_RESPONSIBLE_ORGS_REQUEST });
  try {
    const res = await fetch('/rest/indicatorManager/responsibleOrgs');
    const data = await res.json();
    const options = data.map((org: { orgId: number; orgName: string }) => ({ value: org.orgId, label: org.orgName }));
    dispatch({ type: FETCH_RESPONSIBLE_ORGS_SUCCESS, payload: options });
  } catch (error: any) {
    dispatch({ type: FETCH_RESPONSIBLE_ORGS_FAILURE, payload: error.message || 'Failed to fetch responsible organizations' });
  }
};

export default fetchResponsibleOrgsReducer;

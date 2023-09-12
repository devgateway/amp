import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchUserProfile } from '../reducers/fetchUserProfileReducer';
import { useAppDispatch, useAppSelector } from '../utils/hooks';
import EditProfile from './EditProfile';
import { fetchTranslations } from '../reducers/translationsReducer';
import defaultTranslationsPack from '../config/initialTranslations.json';

export const TranslationsContext = React.createContext(defaultTranslationsPack);

const Startup = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const userProfileLoading = useAppSelector((state) => state.userProfile.loading);
  const translationsLoading = useAppSelector((state) => state.translations.loading);

  useEffect(() => {
    // eslint-disable-next-line no-unused-vars
    window.addEventListener('openUserModal', (_e) => {
      // if (location.pathname === '/edit-profile') {
      //   return;
      // }

      navigate('/edit-profile');
    });
  }, []);

  useEffect(() => {
    dispatch(fetchTranslations(defaultTranslationsPack));
    dispatch(fetchUserProfile());
  }, []);

  return (
      <>
        {
            userProfileLoading || translationsLoading
              ? <div>Loading...</div>
              : (
                    <div className="App">
                      <EditProfile />
                    </div>
              )
        }
      </>

  );
};

export default Startup;

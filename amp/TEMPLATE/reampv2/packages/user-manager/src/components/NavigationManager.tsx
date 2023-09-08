// eslint-disable-next-line no-unused-vars
import React, { useEffect } from 'react';
import { matchRoutes, useLocation, useNavigate } from 'react-router-dom';
import { routes } from '../routing/routes';

const NavigationManager = ({ children }: { children: any }) => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    function containerNavigationHandler(event: CustomEvent) {
      let pathname: string = event.detail;

      if (pathname.includes('user-manager')) {
        pathname = pathname.replace('user-manager', '');
      }

      if (location.pathname === pathname || !matchRoutes(routes, { pathname })) {
        return;
      }
      navigate(pathname);
    }

    // @ts-ignore
    window.addEventListener('[container] navigated', containerNavigationHandler);

    return () => {
      // @ts-ignore
      window.removeEventListener('[container] navigated', containerNavigationHandler);
    };
  }, [location]);

  useEffect(() => {
    const userManagerEvent = new CustomEvent('[UserManager] navigated', { detail: location.pathname });
    window.dispatchEvent(userManagerEvent);
  }, [location]);

  return children;
};
export default NavigationManager;

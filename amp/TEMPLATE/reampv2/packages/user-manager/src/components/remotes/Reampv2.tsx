import React, { useEffect, useRef } from 'react';
// @ts-ignore
import { mount } from 'reampv2App/Reampv2App';
import { useLocation, useNavigate } from 'react-router-dom';
import { REAMPV2_APP_NAME } from '../../utils/constants';

const reampv2Basename = `/${REAMPV2_APP_NAME}`;

const Reampv2 = () => {
  const ref = useRef<HTMLDivElement>(null);
  const location = useLocation();
  const isFirstRunRef = useRef(true);
  const unmountRef = useRef(() => {});
  // Mount reampv2 MFE
  useEffect(
    () => {
      if (!isFirstRunRef.current) {
        return;
      }
      unmountRef.current = mount({
        mountPoint: ref.current!,
        initialPathName: location.pathname.replace(
          reampv2Basename,
          '',
        ),
        standalone: false,
      });
      isFirstRunRef.current = false;
    },
    [location],
  );

  useEffect(() => unmountRef.current, []);

  return (
        <div ref={ref} id="reampv2-app-mfe" />
  );
};
export default Reampv2;

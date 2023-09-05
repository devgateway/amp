import { MountOptions } from './types';

const DIV_ROOT_ID = 'root';

import('./bootstrap').then(({ mount }) => {
  const appRoot = document.getElementById(DIV_ROOT_ID);

  if (appRoot) {
    mount(<MountOptions>{
      routingStrategy: 'browser',
      mountPoint: appRoot,
    });
  }
});

export {};

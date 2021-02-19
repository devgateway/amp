import { DEVELOPMENT } from './Constants';

function getRootUrl() {
  if (process.env.NODE_ENV === DEVELOPMENT) {
    return '/#';
  } else {
    return `${process.env.PUBLIC_URL}/index.html#`;
  }
}
export default getRootUrl;

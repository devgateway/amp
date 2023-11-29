import {DST_SECTOR, SRC_SECTOR} from "../constants/Constants";

export function showAlert(message) {
  alert(message);
}

export function checkSchemes(src, dst) {
  if (!src) return 1;
  else if (!dst) return 2;
  else if (src.id === dst.id) return 3;
  else return 0;
}

export function checkMappings(data) {
  console.log(data);
  let errorNumber = 0;
  if (data && data.length > 0) {
    data.every((item) => {
      if (Object.keys(item[SRC_SECTOR]).length === 0) {
        errorNumber = 1;
        return false;
      } else if (Object.keys(item[DST_SECTOR]).length === 0) {
        errorNumber = 2;
        return false;
      } else {
        const arrayFound = data.filter(toFind => (
          toFind[SRC_SECTOR].id === item[SRC_SECTOR].id && toFind[DST_SECTOR].id === item[DST_SECTOR].id
        ));
        if (arrayFound.length > 1) {
          errorNumber = 3;
          return false;
        }
      }
      return true;
    });
  }

  return errorNumber;
}

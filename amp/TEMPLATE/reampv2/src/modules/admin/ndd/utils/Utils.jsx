import {
  CHILDREN, DST_PROGRAM, PROGRAM, SRC_PROGRAM, ALL_PROGRAMS, TYPE_SRC, TYPE_DST
} from '../constants/Constants';

export function findProgramInTree(lvl3Id, ndd, type) {
  const lvl1 = {};
  const lvl2 = {};
  let
    lvl3;
  ndd[ALL_PROGRAMS].forEach(p => {
    if (p[CHILDREN]) {
      p[CHILDREN].forEach(l1 => {
        if (l1[CHILDREN]) {
          l1[CHILDREN].forEach(l2 => {
            if (l2[CHILDREN]) {
              const l3 = l2[CHILDREN].find(l3 => l3.id === lvl3Id);
              if (l3) {
                lvl3 = l3;
                lvl2.id = l2.id;
                lvl2.value = l2.value;
                lvl1.id = l1.id;
                lvl1.value = l1.value;
              }
            }
          });
        }
      });
    }
  });
  return { lvl1, lvl2, lvl3 };
}

export function findFullProgramTree(ndd, type, src, dst) {
  let tree = null;
  if (ndd && ndd[ALL_PROGRAMS] && (src || dst)) {
    if (type === TYPE_SRC) {
      tree = ndd[ALL_PROGRAMS].find(p => p.id === src.id);
    } else if (type === TYPE_DST) {
      tree = ndd[ALL_PROGRAMS].find(p => p.id === dst.id);
    }
  }
  return tree;
}

export function validate(data) {
  let ret = 0;
  const dstPairs = [];
  if (data && data.length > 0) {
    data.forEach(pair => {
      if (!pair[SRC_PROGRAM].lvl3 || !pair[DST_PROGRAM].lvl3) {
        ret = 1; // missing value error.
      } else {
        dstPairs.push(pair[DST_PROGRAM].lvl3.id);
      }
    });
  }
  return ret;
}

export function validateMainPrograms(src, dst, levelSrc, levelDst) {
  let ret = 0;
  if (src || dst) {
    if (!src) {
      ret = 3;
    } else if (!dst) {
      ret = 4;
    } else if (src.id === dst.id) {
      ret = 5;
    }
  }
  if (levelSrc <= 0 || Number.isNaN(levelSrc)) {
    ret = 6;
  }
  if (levelDst <= 0 || Number.isNaN(levelDst)) {
    ret = 6;
  }
  return ret;
}

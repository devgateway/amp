import {
  CHILDREN, DST_PROGRAM, SRC_PROGRAM, ALL_PROGRAMS, TYPE_SRC, TYPE_DST
} from '../constants/Constants';

/**
 * id can be lvl1, lvl2 or lvl3.
 * @param id
 * @param ndd
 * @returns {{lvl2: {}, lvl3, lvl1: {}}}
 */
export function findProgramInTree(id, ndd, level) {
  const lvl1 = {};
  const lvl2 = {};
  let lvl3 = {};
  if (level === 3) {
    ndd[ALL_PROGRAMS].forEach(p => {
      if (p[CHILDREN]) {
        p[CHILDREN].forEach(l1 => {
          if (l1[CHILDREN]) {
            l1[CHILDREN].forEach(l2 => {
              if (l2[CHILDREN]) {
                const l3 = l2[CHILDREN].find(l4 => l4.id === id);
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
  } else if (level === 2) {
    ndd[ALL_PROGRAMS].forEach(p => {
      if (p[CHILDREN]) {
        p[CHILDREN].forEach(l1 => {
          if (l1[CHILDREN]) {
            const l2 = l1[CHILDREN].find(l3 => l3.id === id);
            if (l2) {
              lvl2.id = l2.id;
              lvl2.value = l2.value;
              lvl1.id = l1.id;
              lvl1.value = l1.value;
            }
          }
        });
      }
    });
  } else if (level === 1) {
    ndd[ALL_PROGRAMS].forEach(p => {
      if (p[CHILDREN]) {
        const l1 = p[CHILDREN].find(l2 => l2.id === id);
        if (l1) {
          lvl1.id = l1.id;
          lvl1.value = l1.value;
        }
      }
    });
  }
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

export function validate(data, levelSrc, levelDst) {
  let ret = 0;
  if (data && data.length > 0) {
    data.forEach(pair => {
      if (!pair[SRC_PROGRAM][`lvl${levelSrc}`] || !pair[DST_PROGRAM][`lvl${levelDst}`]) {
        ret = 1; // missing value error.
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

import {CHILDREN, DST_PROGRAM, PROGRAM, SRC_PROGRAM} from "../constants/Constants";

export function findProgramInTree(id, ndd, type) {
    let lvl1 = {}, lvl2 = {}, lvl3;
    ndd[type + PROGRAM][CHILDREN].forEach(l1 => {
        l1[CHILDREN].forEach(l2 => {
            const l3 = l2[CHILDREN].find(l3 => l3.id === id);
            if (l3) {
                lvl3 = l3;
                lvl2.id = l2.id;
                lvl2.value = l2.value;
                lvl1.id = l1.id;
                lvl1.value = l1.value;
            }
        });
    });
    return {lvl1, lvl2, lvl3};
}

export function validate(data) {
    let ret = 0;
    const dstPairs = [];
    if (data && data.length > 0) {
        data.forEach(pair => {
            if (!pair[SRC_PROGRAM].lvl3 || !pair[DST_PROGRAM].lvl3) {
                ret = 1; // missing value error.
            } else {
                if (dstPairs.find(p => p === pair[DST_PROGRAM].lvl3.id)) {
                    ret = 2; // duplicated dst value error.
                }
                dstPairs.push(pair[DST_PROGRAM].lvl3.id);
            }
        });
    }
    return ret;
}
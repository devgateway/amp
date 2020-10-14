import {CHILDREN, PROGRAM} from "../constants/Constants";

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
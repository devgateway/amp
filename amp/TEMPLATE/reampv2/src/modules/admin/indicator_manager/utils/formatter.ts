export const formatObjArrayToNumberArray = (array: any[]) => {
    if (array.length === 0) {
        return [];
    }
    if (typeof array[0] === 'number') {
        return array;
    }
    return array.map((item) => Number(item.value));
}
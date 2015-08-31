export default function obj2arr(obj){
    return Object.keys(obj).map(function(key){
        return obj[key];
    });
};
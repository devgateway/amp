import { CommonPossibleValuesHelper } from 'amp-ui';


export default function processPossibleValues(possibleValuesCollection){
return Object.entries(possibleValuesCollection).map(entry => CommonPossibleValuesHelper.transformToClientUsage(entry));
}



import Section from './Section';
import PercentageList from '../fields/PercentageList';
import { NATIONAL_PLAN_OBJECTIVE, PROGRAM, PROGRAM_PERCENTAGE } from '../../utils/ActivityConstants';

/**
 *    
 */
const NationalPlanObjective = Section(PercentageList(NATIONAL_PLAN_OBJECTIVE, PROGRAM, PROGRAM_PERCENTAGE),
'NationalPlan', true, 'AcNationalPlan');

export default NationalPlanObjective;

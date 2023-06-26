import React from 'react'
import IndicatorByProgram from './IndicatorByProgram'
import ProgramGroupedByIndicator from './ProgramGroupedByIndicator'

const LeftSection = () => {
  return (
    <div>
        <IndicatorByProgram />
        <ProgramGroupedByIndicator/>
    </div>
  )
}

export default LeftSection

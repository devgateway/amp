import EllipsisText from 'react-ellipsis-text';
import React from 'react';
import * as FieldsConstants from './FieldsConstants';
import { SECTORS_DECIMAL_POINTS_CHART } from './constants';

export function generateStructureBasedOnSector(objectData) {
  return generateStructureBasedOnSectorProjectCount(objectData).sectors;
}

export function generateStructureBasedOnSectorProjectCount(objectData) {
  const projectSectors = {};
  projectSectors.uniqueProjects = new Set();
  projectSectors.sectors = [];
  objectData[FieldsConstants.PRIMARY_SECTOR].forEach(s => {
    const sector = {};
    sector.id = s.id;
    sector.activities = new Set();
    s[FieldsConstants.MODALITIES].forEach(m => {
      m.activities.forEach(p => {
        sector.activities.add(p.id);
        projectSectors.uniqueProjects.add(p.id);
      });
    });
    projectSectors.sectors.push(sector);
  });
  return projectSectors;
}

export function getProjects(projects, elementId, activitiesDetails, ellipsisLength, na) {
  return [...projects].map(p => {
    const project = getProject(p, activitiesDetails);
    const prj = {};
    prj.projectName = project
      ? project[FieldsConstants.PROJECT_TITLE] : na;
    prj.ampUrl = project
      ? project.ampUrl : '/';
    prj.id = p;
    return prj;
  }).sort((a, b) => (a.projectName > b.projectName ? 1 : -1)).map(p => (
    <li key={`prj_list_${elementId}_${p.id}`}>
      <a href={p.ampUrl} target="_blank" rel="noopener noreferrer">
        <EllipsisText
          text={p.projectName}
          length={ellipsisLength} />
      </a>
    </li>
  ));
}

export function getChartData(projectsBySectors, sectors, includeActivities) {
  const totalActivities = projectsBySectors.sectors.reduce((prev, s) => prev + s.activities.size, 0);

  return projectsBySectors.sectors.map(s => {
    const sector = {};
    const sectorName = sectors.find(ss => ss.id === s.id).name;
    sector.id = s.id.toString();
    sector.value = s.activities.size;
    if (includeActivities) {
      sector.activities = s.activities;
    }
    sector.percentage = ((sector.value * 100) / totalActivities);
    sector.label = `${sectorName} ${sector.percentage.toFixed(SECTORS_DECIMAL_POINTS_CHART)}%`;
    sector.simpleLabel = sectorName;
    return sector;
  }).sort(((a, b) => (a.value > b.value ? -1 : 1)));
}

export function getProject(projectId, activitiesDetails) {
  if (activitiesDetails && activitiesDetails.activities.length > 0) {
    return activitiesDetails.activities.find(a => a[FieldsConstants.ACTIVITY_ID] === projectId);
  }
}

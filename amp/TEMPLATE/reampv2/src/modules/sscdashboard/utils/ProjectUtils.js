import EllipsisText from 'react-ellipsis-text';
import React from 'react';
import * as FieldsConstants from './FieldsConstants';
import {
  COLOR_MAP, COLOR_MAP_CUSTOM, MODALITY_CHART
} from './constants';

export function generateStructureBasedOnSector(objectData) {
  return generateStructureBasedOnSectorProjectCount(objectData).groupings;
}

export function generateStructureBasedOnSectorProjectCount(objectData) {
  const projectSectors = {};
  projectSectors.uniqueProjects = new Set();
  projectSectors.groupings = [];
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
    projectSectors.groupings.push(sector);
  });
  return projectSectors;
}

export function generateStructureBasedOnModalitiesProjectCount(objectData) {
  const projectModalities = {};
  projectModalities.groupings = new Map();
  projectModalities.uniqueProjects = new Set();
  objectData[FieldsConstants.PRIMARY_SECTOR].forEach(s => {
    s[FieldsConstants.MODALITIES].forEach(m => {
      let projectModality;
      if (!projectModalities.groupings.has(m.id)) {
        projectModality = {};
        projectModality.id = m.id;
        projectModality.code = m.id;
        projectModality.activities = new Set();
      } else {
        projectModality = projectModalities.groupings.get(m.id);
      }
      m.activities.forEach(p => {
        projectModality.activities.add(p.id);
        projectModalities.uniqueProjects.add(p.id);
      });
      projectModalities.groupings.set(m.id, projectModality);
    });
  });

  projectModalities.groupings = Array.from(projectModalities.groupings.values());
  calculateColors(projectModalities.groupings, MODALITY_CHART);
  return projectModalities;
}

function calculateColors(groupings, chartSelected) {
  groupings.forEach(g => {
    getColor(g, chartSelected);
  });
}

export function getColor(item, chartSelected) {
  let colorMap;
  let color;
  colorMap = COLOR_MAP.get(chartSelected);
  if (!colorMap) {
    colorMap = new Map();
    COLOR_MAP.set(chartSelected, colorMap);
  }
  color = colorMap.get(item.code);
  if (!color) {
    color = COLOR_MAP_CUSTOM.get(chartSelected).shift();
    colorMap.set(item.code, color);
  }
  return color;
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

export function getChartData(projectsByGroupings, groupings, includeActivities) {
  const totalActivities = projectsByGroupings.groupings.reduce((prev, g) => prev + g.activities.size, 0);
  let tail = 0;
  return projectsByGroupings.groupings.map(g => {
    const grouping = {};
    const groupingsFilter = groupings.find(ss => ss.id === g.id);
    grouping.id = g.id.toString();
    grouping.code = groupingsFilter.code ? groupingsFilter.code : groupingsFilter.id;
    grouping.value = g.activities.size;
    if (includeActivities) {
      grouping.activities = g.activities;
    }

    const per = ((grouping.value * 100) / totalActivities);
    const prevBaseLine = tail;
    tail += per;
    const tailRounded = Math.round((tail + Number.EPSILON) * 100) / 100;
    grouping.percentage = Math.round(((tailRounded - prevBaseLine) + Number.EPSILON) * 100) / 100;
    grouping.label = `${groupingsFilter.name} ${grouping.percentage}%`;
    grouping.simpleLabel = groupingsFilter.name;
    return grouping;
  }).sort(((a, b) => (a.value > b.value ? -1 : 1)));
}

export function getProject(projectId, activitiesDetails) {
  if (activitiesDetails && activitiesDetails.activities.length > 0) {
    return activitiesDetails.activities.find(a => a[FieldsConstants.ACTIVITY_ID] === projectId);
  }
}

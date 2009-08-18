package org.digijava.module.aim.fmtool.form;

import java.util.*;

import org.apache.struts.action.*;
import org.apache.struts.actions.*;
import org.digijava.module.aim.fmtool.types.*;

public class FeatureManagerCheckForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private FMCheckTreeEntry fmeTree = null;


	private List<FMSourceWrapper> sourceList = null;
	
	private List<FMDuplicatesWrapper> duplicatesList = null;

	private List<FMProblemWrapper> problemList = null;

	
	private String fixValue = null;
	
	private String fixValueType = null;
	
	private boolean fixAll = false;
	
	public List<FMSourceWrapper> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<FMSourceWrapper> sourceList) {
		this.sourceList = sourceList;
	}

	public List<FMDuplicatesWrapper> getDuplicatesList() {
		if (this.duplicatesList == null){
			this.duplicatesList =  new ArrayList<FMDuplicatesWrapper>();
		}
		return duplicatesList;
	}

	public void setDuplicatesList(List<FMDuplicatesWrapper> duplicatesList) {
		this.duplicatesList = duplicatesList;
	}

	public void addDuplicatesList(List<FMDuplicatesWrapper> duplicatesList) {
		if (this.duplicatesList == null){
			this.duplicatesList =  new ArrayList<FMDuplicatesWrapper>();
		}
		this.duplicatesList.addAll(duplicatesList);
	}

	public String[] getFMEDuplicatesList(String type){
		List<String> retValue = new ArrayList<String>();
		
		for (FMDuplicatesWrapper item : duplicatesList) {
			if (item.getType().equalsIgnoreCase(type)){
				retValue.add(item.getName());
			}
		}
		
		return retValue.toArray(new String[]{});
	}
	
	public List<FMProblemWrapper> getProblemList() {
		if (this.problemList == null){
			this.problemList =  new ArrayList<FMProblemWrapper>();
		}
		return problemList;
	}

	public void setProblemList(List<FMProblemWrapper> problemList) {
		this.problemList = problemList;
	}
	
	public void addProblemList(List<FMProblemWrapper> problemList) {
		if (this.problemList == null){
			this.problemList =  new ArrayList<FMProblemWrapper>();
		}
		this.problemList.addAll(problemList);
	}

	public String[] getFMEProblemList(String type){
		List<String> retValue = new ArrayList<String>();
		
		for (FMDuplicatesWrapper item : duplicatesList) {
			if (item.getType().equalsIgnoreCase(type)){
				retValue.add(item.getName());
			}
		}
		
		return retValue.toArray(new String[]{});
	}
	
	
/*
	public String[] getFMEProblemList(String type){
		List<String> retValue = new ArrayList<String>();
		
		for (FMProblemWrapper item : problemList) {
			if (item.getType().equalsIgnoreCase(type)){
				retValue.add(item.getName());
			}
		}
		
		return retValue.toArray(new String[]{});
	}	
*/	

	public FMCheckTreeEntry getFmeTree() {
		return fmeTree;
	}

	public void setFmeTree(FMCheckTreeEntry fmeTree) {
		this.fmeTree = fmeTree;
	}

	public String getFixValue() {
		return fixValue;
	}

	public void setFixValue(String fixValue) {
		this.fixValue = fixValue;
	}

	public String getFixValueType() {
		return fixValueType;
	}

	public void setFixValueType(String fixValueType) {
		this.fixValueType = fixValueType;
	}

	public boolean getJspFixAll() {
		boolean jspFixAll = fixAll;
		fixAll = false;
		return jspFixAll;
	}

	public void setJspFixAll(boolean fixAll) {
		this.fixAll = fixAll;
	}
	
	public boolean isFixAll() {
		return fixAll;
	}

	public void setFixAll(boolean fixAll) {
		this.fixAll = fixAll;
	}
}


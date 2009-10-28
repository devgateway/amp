Feature: Custom Report
	In order to get an overview of the country's projects
	As a regular user
	I want to create custom reports
	
	Scenario: Default Report
		Given I go to the report builder
		And I press "Query Database"
		Then the report table should have the headings "Donor, Agency, Donor Project Number"
	
	
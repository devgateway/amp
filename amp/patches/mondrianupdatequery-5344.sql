UPDATE `off_line_reports`
SET query = 'select NON EMPTY Crossjoin({[Donor Dates].[All Periods]}, {[Measures].[Raw Actual Commitments]}) ON COLUMNS, NON EMPTY {([Financing Instrument], [Terms of Assistance], [Donor], [Activity])} ON ROWS from [Donor Funding Weighted]'
WHERE name ='Reports by Sector, Financing Instrument and Donor Information';
UPDATE `off_line_reports`
SET query = 'select NON EMPTY {[Measures].[Actual Commitments], [Measures].[Actual Disbursements], [Measures].[Actual Expenditures]} ON COLUMNS,
NON EMPTY {([Primary Sector].[All Primary Sectors], [Activity].[All Activities])} ON ROWS from [Donor Funding Weighted]'
WHERE id =1;
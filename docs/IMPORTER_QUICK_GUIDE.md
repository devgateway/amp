# Data Importer (Quick Guide)

Use the AMP Data Importer to create or update activities from Excel/CSV/TXT files.

## Where to find it

- Open the main AMP menu and look for `Data Importer` under 'Tools' menu.
- If `Data Importer` is not visible, go to `Global Feature Manager` and enable `Data Importer`.

## 1) Prepare your file

- Supported formats: Excel (`.xlsx`), CSV, TXT (delimiter-based).
- Include at least one identifier column in your mapping:
  - `Project Code` (preferred), or
  - `Project Title`.
- For funding rows, use `Transaction Date` in `yyyy-MM-dd` when possible.
  - Year-only values are interpreted as `31/12/<year>` internally and stored as `yyyy-MM-dd`.

## 2) Configure column mapping

From the importer UI:

1. Upload a template file (same structure as your data file).
2. Map each source column to importer fields (for example: `Project Code`, `Donor Agency`, `Transaction Amount`, `Measure Type`, `Transaction Date`, `Currency`).
3. Save configuration (optional) for reuse.

Minimum mapping rule:

- You must map at least `Project Code` or `Project Title`.

## 3) Run import

1. Upload your data file.
2. Select file type and delimiter (for TXT/CSV).
3. Select sheet (Excel) if prompted.
4. Start import.

Import runs in batches and stores per-file and per-row results.

## 4) Create vs Update behavior

- Existing activity lookup is by `Project Code` first, then `Project Title`.
- If found, importer updates existing activity.
- If not found, importer creates a new activity.

## 5) Funding behavior

- Funding transactions are deduplicated before add when a matching transaction already exists in the target list with same:
  - currency,
  - amount,
  - date.
- `Measure Type` drives whether a value is imported as commitment/disbursement/expenditure and planned/actual.

## 6) Common validation checks

If import fails, first verify:

- Required mapping present (`Project Code` or `Project Title`).
- `Transaction Date` format is valid (`yyyy-MM-dd` recommended).
- Numeric values are valid for funding amount/exchange rate.
- Organization and sector values are mapped to expected columns.

## 7) Useful statuses

- `IN_PROGRESS`: file is currently importing.
- `SUCCESS`: row imported successfully.
- `FAILED`: row/file failed; inspect import response details.
- `SKIPPED`: row skipped by importer logic (for example, missing key identifiers).

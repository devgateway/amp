# Data Importer (Comprehensive Simple Guide)

Use the AMP Data Importer to create or update activities in bulk from Excel, CSV, or text files.

## Where to find it

- Go to `Tools` → `Data Importer`.
- If it is not visible, open `Global Feature Manager` and enable `Data Importer`.
- To monitor past imports, use `Tools` → `Data Importer` → `View Progress`.

## What the importer can do

- Import data from `Excel`, `CSV`, and `Text` files.
- Build mapping from a **template file** (so your real data file can be imported repeatedly).
- For Excel templates, map columns from a **specific template sheet**.
- Save and reuse named mapping configurations.
- Load an existing configuration and add/remove mapping pairs.
- Import either:
  - the **whole Excel workbook**, or
  - only a **specific data sheet**.
- Process imports in batches and track detailed status per file and per project row.

## Before you start

1. Prepare your source file (`.xlsx`, `.csv`, or `.txt`).
2. Ensure your mapping includes at least one project identifier:
   - `Project Code` (preferred), or
   - `Project Title`.
3. For text files, know your delimiter (comma, pipe, etc.).
4. For dates, prefer `yyyy-MM-dd`.

## Step-by-step: configure mapping

### 1) Select file type

- Choose the same type as your data file (`Excel`, `CSV`, or `Text`).
- If `Text`, choose the column separator.

### 2) Use existing config or create new

- **Reuse existing config**: select it from `Select Existing Configuration by name`.
- **Create new config**:
  1. Upload a template file.
  2. Add mapping pairs (`Column Name` → `Selected Field`).
  3. Remove wrong pairs if needed.
  4. Importer saves config automatically during upload when not using an existing one.

### 3) Template-sheet-specific mapping (Excel)

- After uploading an Excel template, importer lists template sheets.
- Select the template sheet to populate its columns.
- Map fields from that sheet’s column list.

## Step-by-step: run import

1. Upload the real data file.
2. Choose `Internal` if you want donor handling with internal donor defaults.
3. For Excel data files, choose:
   - `Whole file` to process all sheets, or
   - `Specific sheet` and pick a sheet name.
4. Start upload/import.

Notes:

- Import is batch-based (large files are chunked automatically).
- If a very similar file is already `IN_PROGRESS`, importer blocks duplicate parallel processing.

## How records are interpreted

### Create vs update

- Existing activity lookup is by `Project Code` first, then `Project Title`.
- Match found: existing activity is updated.
- No match: new activity is created.

### Funding behavior

- `Measure Type` controls commitment/disbursement/expenditure and planned/actual mapping.
- Duplicate transaction protection: importer avoids adding a funding transaction when a matching one already exists (same currency, amount, and date).

### Date behavior

- Recommended format: `yyyy-MM-dd`.
- Year-only values are normalized to end-of-year (31 Dec of that year) and stored in valid date format for import.

## View progress and results

Use `View Progress` to see:

- imported file list and file-level status,
- counts of successful vs failed project rows,
- per-row status (`SUCCESS`, `FAILED`, etc.),
- import response details (including validation errors and warnings).

## Common troubleshooting

If import fails, check these first:

1. Mapping contains `Project Code` or `Project Title`.
2. Date columns are valid (prefer `yyyy-MM-dd`).
3. Numeric columns are valid (`Transaction Amount`, `Exchange Rate`).
4. Organization/sector column mappings are correct.
5. You selected the correct data sheet when using sheet-specific import.

## Status meanings

- `IN_PROGRESS`: file is currently being imported.
- `SUCCESS`: row/file imported successfully.
- `FAILED`: row/file failed; inspect response details in progress view.
- `SKIPPED`: row intentionally skipped by importer logic.

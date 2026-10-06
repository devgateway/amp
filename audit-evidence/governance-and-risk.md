# Governance and Risk Evidence

Scope: AMP production application, PostgreSQL database, Jackrabbit file store,
TruBudget integration, backup process, CI/CD, and administrative access.

---

## 1. ISMS Scope

| Item | Value |
| --- | --- |
| Application owner | _(name)_ |
| Security owner | _(name)_ |
| Operations owner | _(name)_ |
| TruBudget integration owner | _(name)_ |
| Approved date | _(date)_ |
| Out-of-scope exclusions | _(list anything explicitly excluded)_ |

---

## 2. Asset Register

| Asset | Classification | Owner | Backup required | Notes |
| --- | --- | --- | --- | --- |
| AMP web application | Internal | _(name)_ | Reproducible release artifact | |
| AMP PostgreSQL database | Confidential | _(name)_ | Yes — daily | |
| Jackrabbit file store | Confidential | _(name)_ | Confirm scope decision | rep* tables excluded by default |
| TruBudget API credentials | Restricted | _(name)_ | Rotation record only | Never store plaintext here |
| AMP admin accounts | Restricted | _(name)_ | Access review | |
| TruBudget-enabled user accounts | Confidential | _(name)_ | Access review | |
| Application and audit logs | Confidential | _(name)_ | Retain per policy | |
| Backup files | Restricted | _(name)_ | Encrypted/access-controlled storage | |

---

## 3. Risk Register

Risks identified from the repository audit. Score = Impact × Likelihood (1–5).

| ID | Risk | Score | Treatment | Owner | Evidence |
| --- | --- | --- | --- | --- | --- |
| R-01 | No ISMS scope or named owners documented | 12 | Approve scope (Section 1 above) | _(name)_ | Signed scope |
| R-02 | No periodic access reviews for privileged or TruBudget-enabled accounts | 15 | Quarterly review (Section 4 below) | _(name)_ | Review sign-off |
| R-03 | Backup restores not tested | 15 | Run restore test; attach log | _(name)_ | `operations.md` |
| R-04 | No incident response plan or log | 15 | Approve plan; keep log | _(name)_ | `operations.md` |
| R-05 | No DR plan or exercise | 15 | Approve plan; run tabletop | _(name)_ | `operations.md` |

Last reviewed: _(date)_ &nbsp; Next review: _(date)_

---

## 4. Access Review — 2026 Q2

Review period: 2026-04-01 to 2026-06-30

**Evidence to attach in `evidence/` (redacted):**
- AMP user export (roles, workspaces, TruBudget-enabled flag, admin flag)
- Removal or change tickets for any access corrected
- Sign-off from application owner and security owner

| Account type | Review decision | Action taken | Reviewer | Date |
| --- | --- | --- | --- | --- |
| System administrators | _(Approved / Removed / Changed)_ | _(detail)_ | _(name)_ | _(date)_ |
| TruBudget-enabled users | _(Approved / Removed / Changed)_ | _(detail)_ | _(name)_ | _(date)_ |
| Service accounts | _(Approved / Removed / Changed)_ | _(detail)_ | _(name)_ | _(date)_ |
| Database administrators | _(Approved / Removed / Changed)_ | _(detail)_ | _(name)_ | _(date)_ |

Sign-off: Application owner _(name / date)_ · Security owner _(name / date)_

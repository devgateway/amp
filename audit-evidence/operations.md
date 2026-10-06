# Operational Evidence

Covers backup and restore, incident response, and disaster recovery for the AMP production deployment.

---

## 1. Backup and Restore

**Scripts:** `scripts/backup.sh` · `scripts/restore.sh` · `scripts/backup-cron.sh`

### Policy

| Item | Value |
| --- | --- |
| Schedule | Daily at 02:00 (cron) |
| Scope | Database excluding Jackrabbit rep* tables (confirm full-backup decision) |
| Retention | _(e.g. 7 daily, 4 weekly)_ |
| Storage | _(path / bucket — access-controlled)_ |
| Monitoring | _(alert destination for failed jobs)_ |

### Backup run log

Attach redacted `backup.sh` output for the most recent successful run to `evidence/`.

| Date | Result | File size | Reviewer |
| --- | --- | --- | --- |
| _(date)_ | Success / Failed | _(size)_ | _(name)_ |

### Restore test record

Attach `restore.sh` output and smoke-test result to `evidence/`.

| Test date | Backup used | Environment | Result | RTO achieved | Smoke test | Sign-off |
| --- | --- | --- | --- | --- | --- | --- |
| _(date)_ | _(file)_ | Test / staging | Pass / Fail | _(minutes)_ | Pass / Fail | _(name / date)_ |

---

## 2. Incident Response

**Plan owner:** _(name)_ &nbsp; **Approved:** _(date)_

### Contacts

| Role | Name | Primary contact | Backup contact |
| --- | --- | --- | --- |
| Incident commander | _(name)_ | _(contact)_ | _(contact)_ |
| Application owner | _(name)_ | _(contact)_ | _(contact)_ |
| Security owner | _(name)_ | _(contact)_ | _(contact)_ |
| Operations / DBA | _(name)_ | _(contact)_ | _(contact)_ |
| TruBudget owner | _(name)_ | _(contact)_ | _(contact)_ |

### Severity and response targets

| Severity | Example | Response target |
| --- | --- | --- |
| Critical | Confirmed compromise, data exposure, full outage | Immediate 24/7 |
| High | Privilege misuse, backup failure, integration failure | Same business day |
| Medium | Suspicious event, isolated degradation | 2 business days |

### Incident log

| ID | Date | Severity | Summary | Status | Post-incident review |
| --- | --- | --- | --- | --- | --- |
| _(INC-001)_ | _(date)_ | _(sev)_ | _(one line)_ | Open / Closed | _(date)_ |

No incidents in period: _(attestation — security owner name / date)_

---

## 3. Disaster Recovery

**Plan owner:** _(name)_ &nbsp; **Approved:** _(date)_

### Recovery objectives

| Component | RTO | RPO |
| --- | --- | --- |
| AMP application | _(hours)_ | _(hours)_ |
| AMP database | _(hours)_ | _(hours)_ |
| TruBudget integration | _(hours)_ | _(hours)_ |

### Recovery sequence (high level)

1. Declare incident and assign recovery lead.
2. Provision recovery environment.
3. Run `scripts/restore.sh` against latest valid backup.
4. Start AMP; verify login, dashboard, sample activity.
5. Validate TruBudget integration connectivity.
6. Owner sign-off before returning to service.

### DR exercise record

| Date | Type | Scenario | RTO achieved | Issues found | Sign-off |
| --- | --- | --- | --- | --- | --- |
| _(date)_ | Tabletop / Technical | _(scenario)_ | _(minutes)_ | _(count / none)_ | _(name / date)_ |

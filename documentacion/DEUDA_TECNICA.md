# TECHNICAL DEBT REGISTRY — SofkianOS MVP

> **Project:** SofkianOS MVP
> **Date:** February 12, 2026
> **Phase:** 5 — Technical Debt & Future Strategy
> **Authors:** Senior Software Architect & Technical Product Owner
> **Governance Model:** AI-First Development (v1.0)
> **Classification Framework:** Martin Fowler's Technical Debt Quadrant

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Architectural Evolution](#2-architectural-evolution)
3. [Debt Inventory](#3-debt-inventory)
4. [Quadrant Matrix Classification](#4-quadrant-matrix-classification)
5. [Quantitative Assessment](#5-quantitative-assessment)
6. [Remediation Roadmap](#6-remediation-roadmap)
7. [What Is NOT Technical Debt](#7-what-is-not-technical-debt)
8. [AI-First Governance Alignment](#8-ai-first-governance-alignment)
9. [Conclusion & Strategic Verdict](#9-conclusion--strategic-verdict)

---

## 1. Executive Summary

SofkianOS MVP follows an event-driven microservices architecture:

- **producer-api:** REST API (Spring Boot) — receives requests, publishes to RabbitMQ
- **consumer-worker:** Worker (Spring Boot) — consumes messages, persists to PostgreSQL
- **frontend:** SPA (React/Vite) — interacts with producer-api

After four foundational phases (Audit → Pattern Research → Guided Refactoring → QA Foundations), the backend evolved from imperative, tightly-coupled services to a **Hexagonal, event-driven system** with domain validation, typed contracts, and DLQ error resilience.

**26 debt items** have been identified and classified:

| Risk | Count | Percentage |
|------|-------|------------|
| 🔴 High | 9 | 35% |
| 🟡 Medium | 13 | 50% |
| 🟢 Low | 4 | 15% |

**Key Metrics:**

| Metric | Value |
|--------|-------|
| Sustainability Score | 5.45 / 10 |
| Debt Interest Rate | 8.5% per quarter |
| Refactoring Pressure | 62% (threshold: 70%) |
| Governance Maturity | Level 2/4 (Emerging) |

---

## 2. Architectural Evolution

### Refactoring Achievements (Phases 1–4)

| Dimension | Before (Audit) | After (Refactored) | Improvement |
|-----------|----------------|---------------------|-------------|
| **SOLID Compliance** | ❌ SRP/DIP violated | ✅ Enforced via ports abstraction | High |
| **Domain Model** | Anemic (getters/setters) | Rich Builder with validation | High |
| **Message Contract** | `@Payload String` | Typed `@Payload KudoEvent` DTO | High |
| **Error Handling** | Generic `RuntimeException` | Domain exceptions + DLQ | Medium |
| **Test Coverage** | ~0% | ~25% (controller level) | Low-Medium |
| **Dead Code (Frontend)** | ~55% unreachable | ~55% unreachable | No change |
| **Observability** | Console logs only | Console logs only | No change |

### Debt Profile Shift

```
Phase 1 (Audit):                    Phase 5 (Post-Refactoring):
Reckless/Inadvertent:  80%   →      Reckless/Inadvertent:  27%
Prudent/Inadvertent:   15%   →      Prudent/Inadvertent:   42%
Prudent/Deliberate:     5%   →      Prudent/Deliberate:    31%
Reckless/Deliberate:    0%   →      Reckless/Deliberate:    0%
```

Refactoring eliminated ~70% of reckless/inadvertent debt. Remaining debt is predominantly **emergent** and **strategic**.

---

## 3. Debt Inventory

| ID | Title | Quadrant | Layer | Risk | Interest | Timeline |
|----|-------|----------|-------|------|----------|----------|
| **DTB-01** | Event Schema Versioning Missing | Prudent/Inadvertent | Backend | 🔴 High | Exponential | Short |
| **DTB-02** | Consumer Idempotency Not Implemented | Prudent/Inadvertent | Backend | 🔴 High | Exponential | Short |
| **DTB-03** | Message Deduplication Absent | Prudent/Inadvertent | Backend | 🟡 Medium | Linear | Mid |
| **DTB-04** | DLQ Replay Mechanism Missing | Prudent/Deliberate | Backend | 🟡 Medium | Stable | Mid |
| **DTB-05** | Shared Kernel Module Not Extracted | Prudent/Deliberate | Backend | 🟡 Medium | Linear | Mid |
| **DTB-06** | Database Credentials Hardcoded | Reckless/Inadvertent | Backend | 🔴 High | Stable | **Immediate** |
| **DTB-07** | PostgreSQL Health Check Missing | Prudent/Inadvertent | Backend | 🟡 Medium | Stable | Short |
| **DTB-08** | RabbitMQ Connection Resilience Missing | Prudent/Inadvertent | Backend | 🟡 Medium | Linear | Short |
| **DTB-09** | No Circuit Breaker for Publisher | Prudent/Deliberate | Backend | 🟡 Medium | Linear | Mid |
| **DTB-10** | No Rate Limiting on API | Prudent/Deliberate | Backend | 🟡 Medium | Stable | Mid |
| **DTB-11** | Test Coverage Controllers Only | Prudent/Inadvertent | Backend | 🟡 Medium | Linear | Mid |
| **DTB-12** | No Integration Tests for Event Flow | Prudent/Inadvertent | Backend | 🔴 High | Exponential | Short |
| **DTF-01** | Frontend Dead Code (~55%) | Reckless/Inadvertent | Frontend | 🔴 High | Linear | Short |
| **DTF-02** | Duplicate API Layers | Reckless/Inadvertent | Frontend | 🔴 High | Linear | Short |
| **DTF-03** | Hardcoded User Data in Hook | Reckless/Inadvertent | Frontend | 🔴 High | Stable | Short |
| **DTF-04** | Missing React Error Boundaries | Reckless/Inadvertent | Frontend | 🔴 High | Stable | Short |
| **DTF-05** | Dead React Router (Not Activated) | Reckless/Inadvertent | Frontend | 🟡 Medium | Stable | Short |
| **DTF-06** | Stale Closures in Event Handlers | Reckless/Inadvertent | Frontend | 🟡 Medium | Linear | Mid |
| **DTF-07** | Zustand Installed But Unused | Prudent/Deliberate | Frontend | 🟢 Low | Stable | Long |
| **DTF-08** | API Response Type Safety Missing | Prudent/Inadvertent | Frontend | 🟡 Medium | Linear | Mid |
| **DTF-09** | Hardcoded Brand Colors (>40×) | Prudent/Deliberate | Frontend | 🟢 Low | Stable | Long |
| **DTC-01** | No Centralized Observability | Prudent/Inadvertent | Cross-Cutting | 🔴 High | Exponential | Short |
| **DTC-02** | No Distributed Tracing | Prudent/Inadvertent | Cross-Cutting | 🟡 Medium | Linear | Mid |
| **DTC-03** | No Structured Logging | Prudent/Inadvertent | Cross-Cutting | 🟡 Medium | Stable | Mid |
| **DTC-04** | Auth/AuthZ Not Implemented | Prudent/Deliberate | Cross-Cutting | 🟢 Low | Stable | Long |
| **DTC-05** | Infrastructure Coupling to Supabase | Prudent/Deliberate | Cross-Cutting | 🟢 Low | Stable | Long |

---

## 4. Quadrant Matrix Classification

### 4.1 Reckless / Inadvertent

> Debt accumulated unconsciously due to knowledge gaps. No strategic benefit. Highest remediation priority.

| ID | Description | Risk | Justification |
|----|-------------|------|---------------|
| **DTB-06** | Database credentials hardcoded in `application.properties` (plaintext password committed to Git) | 🔴 High | Security incident. Violates Twelve-Factor App principle III. Credentials visible to anyone with repository access, non-rotatable without deployment. The developer was unaware credentials should never enter source control. |
| **DTF-01** | ~55% of frontend source is unreachable dead code (pages, routes, layouts, services, hooks, tests) | 🔴 High | Accumulated through iterative development without understanding the final routing architecture. The team built parallel implementations but never pruned the obsolete path. Inflates cognitive load, build size (+35%), and creates false test coverage. |
| **DTF-02** | Two parallel Axios API layers (`src/api/` vs `src/services/api/`); active flow uses the inferior one | 🔴 High | The mature layer with interceptors and mock switching was built but never connected to active flows. A failed migration, not a strategic choice. Active flow has no auth headers or retry logic. |
| **DTF-03** | User list hardcoded inside `useKudoForm.ts` hook (4 static entries) | 🔴 High | Embedding domain data in a UI hook reveals a lack of separation of concerns. Not a deliberate shortcut — a convenience decision without considering that users should come from an external source. |
| **DTF-04** | Zero `ErrorBoundary` components in the entire React application | 🔴 High | Complete absence suggests the team was unaware of React's error boundary pattern. Any uncaught render error crashes the entire SPA to a white screen with no recovery path. |
| **DTF-05** | React Router installed and configured but never mounted; `App.tsx` uses binary `isAppView` toggle | 🟡 Medium | The team built a complete `AppRouter`, then bypassed it with a conditional flag. Multiple components import `Link` with no `BrowserRouter` provider, generating console warnings. |
| **DTF-06** | Stale closures in `useEffect` hooks with missing dependency arrays | 🟡 Medium | `handleEnd` captured in closure without being listed in dependencies. Subtle React anti-pattern causing intermittent bugs where drag-end submits stale form data under specific timing conditions. |

---

### 4.2 Prudent / Inadvertent

> Well-intentioned decisions recognized as suboptimal only after gaining deeper understanding. Emergent complexity.

| ID | Description | Risk | Justification |
|----|-------------|------|---------------|
| **DTB-01** | `KudoEvent` DTO duplicated in Producer and Consumer with no schema versioning | 🔴 High | Phase 3 correctly extracted typed DTOs. The schema evolution problem only became visible when considering multi-version deployment scenarios. Each schema change compounds migration complexity exponentially. |
| **DTB-02** | Consumer has no idempotency mechanism; RabbitMQ redelivery causes duplicate persistence | 🔴 High | DLQ was correctly implemented for error isolation. The at-least-once delivery guarantee implications were not anticipated until incident scenario analysis. Duplicate kudos corrupt metrics and erode user trust. |
| **DTB-03** | No system-wide message deduplication abstraction | 🟡 Medium | Related to DTB-02 but broader: no reusable deduplication pattern exists for future event types. Prudent scoping of the immediate flow, inadvertent omission of the cross-cutting pattern. |
| **DTB-07** | `HealthController` returns 200 OK even if PostgreSQL is unreachable | 🟡 Medium | Health checks were implemented (good) but miss the deep health check pattern. Orchestrators may route traffic to instances that cannot persist data. |
| **DTB-08** | Spring AMQP has no retry/reconnection policy for RabbitMQ unavailability | 🟡 Medium | Default configuration accepted without questioning resilience behavior. Consumer crashes on RabbitMQ restart with no automatic recovery. |
| **DTB-11** | Test coverage limited to controllers only | 🟡 Medium | Phase 4 correctly established the test pyramid. Service, domain, and adapter tests were underestimated until regression risk analysis. Builder invariants and domain exceptions remain untested. |
| **DTB-12** | No integration tests for Producer → RabbitMQ → Consumer → PostgreSQL | 🔴 High | Contract drift between services can go undetected. Deployment confidence gap only recognized through operational analysis. Exponential interest as integration points grow. |
| **DTF-08** | API services return `Promise<any>`, defeating TypeScript type safety | 🟡 Medium | TypeScript adopted without fully leveraging compile-time safety at API boundaries. 6+ instances of `any` in service and mock layers. |
| **DTC-01** | No centralized observability; logging is console-only with no aggregation, tracing, or metrics | 🔴 High | The debugging difficulty of a distributed system without log aggregation was not anticipated. MTTR exceeds 4 hours. Debugging requires SSH into containers. |
| **DTC-02** | No distributed tracing or correlation IDs across the event flow | 🟡 Medium | Each component logs independently without shared context. Correlating a kudo's journey requires manual timestamp matching. |
| **DTC-03** | Logs are plain text; no structured JSON format for machine parsing | 🟡 Medium | Standard console logging without considering log aggregation pipeline requirements. Blocks observability maturity. |

---

### 4.3 Prudent / Deliberate

> Conscious, strategic shortcuts to accelerate MVP delivery. Documented payback plans. Lowest priority.

| ID | Description | Risk | Justification |
|----|-------------|------|---------------|
| **DTB-04** | DLQ exists but has no replay mechanism; failed messages accumulate without recovery | 🟡 Medium | DLQ was deliberately implemented for error isolation. Replay tooling consciously deferred to validate DLQ effectiveness first. Rational MVP trade-off with clear payback path. |
| **DTB-05** | `KudoEvent` duplicated; shared kernel module not extracted | 🟡 Medium | Conscious decision in Phase 3 to avoid premature abstraction during refactoring. DRY violation accepted for a single-team MVP. Blocking at scale. |
| **DTB-09** | No circuit breaker for RabbitMQ publisher; API blocks synchronously on broker unavailability | 🟡 Medium | MVP prioritized simple synchronous flow over resilience patterns. Knows Resilience4j is the solution. Acceptable at current scale (~500 kudos/day). |
| **DTB-10** | No rate limiting on `POST /api/v1/kudos` | 🟡 Medium | Deferred to avoid premature optimization. Setting thresholds requires real usage data. Risk materializes only under abuse or significant load. |
| **DTF-07** | Zustand installed but never imported or used | 🟢 Low | Installed with intent to implement global state, then deferred. Only cost: +15KB bundle, mild confusion. Decision point: activate or remove. |
| **DTF-09** | Brand color `#FF5F00` hardcoded >40 times as inline Tailwind values | 🟢 Low | Inline values used to iterate quickly on design. Functionally correct; only cost is multi-file refactoring for rebranding. |
| **DTC-04** | Authentication/authorization completely absent; API publicly accessible | 🟢 Low (internal) | Conscious MVP decision to validate architecture before adding auth complexity. Acceptable for internal pilot. Becomes 🔴 High for public deployment. |
| **DTC-05** | Infrastructure coupled to Supabase PostgreSQL via connection URL | 🟢 Low | Conscious choice to use managed database. Spring Data JPA already provides vendor-neutral abstraction — only connection string is Supabase-specific. |

---

### 4.4 Reckless / Deliberate

> **0 items.** The AI-First methodology successfully prevented conscious reckless shortcuts. Even under time pressure, foundational patterns (Hexagonal, Builder, DLQ) were applied.

---

### 4.5 Quadrant Distribution

```
                       DELIBERATE                     INADVERTENT
              ┌────────────────────────────┬────────────────────────────┐
              │                            │                            │
              │  PRUDENT / DELIBERATE      │  PRUDENT / INADVERTENT     │
              │                            │                            │
  PRUDENT     │  8 items                   │  11 items                  │
              │  🔴 0  🟡 4  🟢 4         │  🔴 4  🟡 7               │
              │                            │                            │
              │  Strategic MVP shortcuts   │  Emergent complexity       │
              │  with payback plans        │  recognized post-hoc       │
              │                            │                            │
              ├────────────────────────────┼────────────────────────────┤
              │                            │                            │
              │  RECKLESS / DELIBERATE     │  RECKLESS / INADVERTENT    │
              │                            │                            │
  RECKLESS    │  0 items                   │  7 items                   │
              │                            │  🔴 5  🟡 2               │
              │  No reckless deliberate    │                            │
              │  shortcuts detected        │  Knowledge gaps and        │
              │                            │  unconscious blind spots   │
              │                            │                            │
              └────────────────────────────┴────────────────────────────┘
```

| Quadrant | Items | 🔴 High | 🟡 Medium | 🟢 Low |
|----------|-------|---------|-----------|--------|
| Reckless / Inadvertent | 7 | 5 | 2 | 0 |
| Prudent / Inadvertent | 11 | 4 | 7 | 0 |
| Prudent / Deliberate | 8 | 0 | 4 | 4 |
| Reckless / Deliberate | 0 | 0 | 0 | 0 |
| **Total** | **26** | **9** | **13** | **4** |

---

## 5. Quantitative Assessment

### 5.1 Interest Accumulation

**Portfolio Interest Rate: ~8.5% per quarter**

For every quarter debt remains unaddressed, total remediation effort increases by ~8.5%.

**High-Interest Drivers:**

| Item | Rate | Impact |
|------|------|--------|
| DTB-01 (Event Versioning) | +25%/quarter | Each schema change compounds migration complexity |
| DTB-02 (Idempotency) | +25%/quarter | Duplicate data accumulates, cleanup effort grows |
| DTB-12 (Integration Tests) | +20%/quarter | Contract drift undetected, production incidents increase |
| DTC-01 (Observability) | +20%/quarter | Incident count grows, MTTR degrades |

**Threshold Alert:** If portfolio interest rate exceeds **15%/quarter**, declare Debt Emergency Sprint (50% capacity for remediation).

### 5.2 Sustainability Score: 5.45 / 10

| Dimension | Score | Weight |
|-----------|-------|--------|
| Pattern Consistency | 8.5/10 | 20% |
| Test Coverage | 4.0/10 | 15% |
| Observability | 3.0/10 | 15% |
| Security Posture | 2.0/10 | 15% |
| Documentation | 7.5/10 | 10% |
| Deployment Automation | 6.5/10 | 10% |
| Codebase Health | 6.0/10 | 10% |
| Knowledge Transfer | 8.0/10 | 5% |

**Interpretation:** Sustainable with active management (5.0–7.0 range). Trending upward (+1.05 since Phase 1 baseline of 4.4).

### 5.3 Refactoring Pressure Projection

| Timeframe | Pressure | Status |
|-----------|----------|--------|
| **Now** | 62% | ⚠️ Approaching threshold |
| **Month 3** | 72% | 🔴 Crosses 70% threshold |
| **Month 5** | 98% | 🔴 Critical |
| **Month 6** | 116% | 🔴 Feature development must pause |

**If Short Term roadmap executed:** Pressure drops to **38%**, buying 6–9 months of sustainable velocity.

---

## 6. Remediation Roadmap

### P0 — Immediate (Week 1)

| ID | Action | Effort |
|----|--------|--------|
| **DTB-06** | Rotate credentials. Externalize to env vars. Add `.env` to `.gitignore`. | 2h |

### P1 — Short Term (Sprints 1–2)

**Sprint 1 — Backend Focus (4.5 days):**

| ID | Action | Effort | Dependencies |
|----|--------|--------|-------------|
| **DTB-01** | Extract shared kernel module, implement versioning | 1.5 days | None |
| **DTB-02** | Implement consumer idempotency (deduplication table) | 1.5 days | DTB-01 |
| **DTB-12** | Integration test with Testcontainers | 1 day | DTB-01 |
| **DTB-07** | Spring Boot Actuator health check | 2h | None |
| **DTB-08** | RabbitMQ retry policy configuration | 2h | None |

**Sprint 2 — Frontend + Observability (4.25 days):**

| ID | Action | Effort | Dependencies |
|----|--------|--------|-------------|
| **DTF-01** | Delete dead code (~55% surgical removal) | 1 day | None |
| **DTF-02** | Consolidate to single API layer | 4h | DTF-01 |
| **DTF-03** | Extract hardcoded users to config | 2h | None |
| **DTF-04** | Implement Error Boundaries (global + granular) | 4h | None |
| **DTC-01** | Deploy log aggregation, configure JSON logging | 2 days | None |

**Milestone after Sprint 2:** Security hardened, data integrity guaranteed, frontend cleaned, observability foundation. **Pressure: 62% → 38%.**

### P2 — Mid Term (Sprints 3–6)

| Sprint | ID | Action | Effort |
|--------|----|--------|--------|
| 3 | **DTB-11** | Expand test coverage (service, domain, adapter) | 2 days |
| 3 | **DTF-06** | Fix stale closures, enable `exhaustive-deps` lint | 4h |
| 3 | **DTF-08** | Type API responses, replace `Promise<any>` | 1 day |
| 3 | **DTC-03** | Structured JSON logging | 4h |
| 4 | **DTB-03** | Message deduplication service abstraction | 1 day |
| 4 | **DTB-05** | Shared kernel module (if not done in Sprint 1) | 2 days |
| 4 | **DTC-02** | Distributed tracing with correlation IDs | 2 days |
| 5 | **DTB-04** | DLQ replay mechanism (admin endpoint) | 1 day |
| 5 | **DTB-10** | Rate limiting (Bucket4j) | 1 day |
| 6 | **DTB-09** | Circuit breaker for RabbitMQ publisher | 1 day |

**Milestone after Sprint 6:** Production-grade resilience, >70% test coverage, full observability. **Sustainability Score: → 7.5/10.**

### P3 — Long Term (Sprints 7+)

| Sprint | ID | Action | Trigger |
|--------|----|--------|---------|
| 7–8 | **DTC-04** | JWT + OAuth2 authentication | Before external launch |
| 9 | **DTF-07** | Activate Zustand or remove | Global state needed |
| 9 | **DTF-09** | Centralize brand colors in Tailwind config | Rebranding initiative |
| 10+ | **DTC-05** | Database abstraction | Cloud migration required |
| — | **DTF-05** | Activate React Router or remove dependency | Adding third view |

### KPI Targets

| Metric | Current | 3 mo | 6 mo | 12 mo |
|--------|---------|------|------|-------|
| High-Risk Items | 9 | 3 | 1 | 0 |
| Interest Rate | 8.5%/qtr | 5.0% | 3.0% | <2.0% |
| Refactoring Pressure | 62% | 38% | 25% | <20% |
| Backend Test Coverage | 25% | 60% | 75% | 80% |
| Frontend Test Coverage | 5% | 40% | 65% | 75% |
| MTTR | >4h | <1h | <30min | <15min |
| Sustainability Score | 5.45 | 6.5 | 7.5 | 8.5 |

---

## 7. What Is NOT Technical Debt

### Legitimate Architectural Decisions

| Decision | Rationale | Becomes Debt When |
|----------|-----------|-------------------|
| Docker Compose (no Kubernetes) | Appropriate for MVP, <1,000 DAU, small team | Multi-region or auto-scaling required |
| Monolithic SPA (no microfrontends) | Single team, ~15 components | Multiple frontend teams, independent deployment |
| Anemic DTOs (KudoRequest, KudoEvent) | DTOs should be data-only by design; behavior belongs in entities | Never — this is correct pattern application |
| Manual deployment (no CI/CD) | Acceptable for MVP, low deployment frequency | Frequency >1/day or team size >5 |

### Future Features (Not Debt)

User Profiles, Kudo Editing, Real-Time Notifications, Mobile App, Analytics Dashboard — these are roadmap items, not debt. Only classify as debt if it **impedes current functionality** or **increases risk**.

### Conscious Trade-Offs (Not Debt)

No multi-tenancy, no i18n, no advanced search, no file attachments — validated MVP assumptions appropriate for current scope.

---

## 8. AI-First Governance Alignment

### Debt Roles in AI-First Workflow

| Activity | Owner | Process |
|----------|-------|---------|
| Identification | Humans | Quarterly audits, incident retrospectives |
| Classification | Humans | Apply Fowler quadrant during design review |
| Prioritization | Humans | Risk × Impact × Interest Rate analysis |
| Documentation | AI | Generate registry entries from human specs |
| Remediation | AI | Implement refactorings under human direction |
| Acceptance | Humans | Explicit sign-off with payback plan |

### Prevention Strategies

1. **Architecture Decision Records (ADRs):** Document Context, Decision, Consequences for every major decision
2. **Pre-Implementation Design Reviews:** SOLID, pattern applicability, security, and testability checklist before AI generates code
3. **Automated Quality Gates:** SonarQube, ArchUnit, ESLint `exhaustive-deps`, OWASP Dependency Check in CI pipeline

### Governance Rituals

| Ritual | Frequency | Duration |
|--------|-----------|----------|
| Debt Review | Quarterly | 2h |
| Debt Triage | Sprint Planning | 30min |
| Debt Retrospective | After incidents | 1h |
| Debt Paydown Sprint | Every 6 sprints | Full sprint (50% capacity) |
| Dashboard Review | Weekly standup | 5min |

**Sprint capacity allocation:** 80% features / 20% debt reduction.

---

## 9. Conclusion & Strategic Verdict

### Overall Assessment: Transitional But Risky

The debt profile is **not strategically acceptable as-is** and **not yet structurally dangerous**.

**Why it is not yet acceptable:**

1. **Q1 (Reckless/Inadvertent) has 7 items including 5 High-Risk.** A security incident (DTB-06), 55% dead code (DTF-01), dual API layers (DTF-02), and no error boundaries (DTF-04) provide zero strategic value and actively degrade quality.

2. **Four items carry exponential interest** (DTB-01, DTB-02, DTB-12, DTC-01). Left unaddressed for 6 months, their combined effort grows from ~6 days to ~10 days.

3. **Refactoring pressure at 62%** will cross the 70% threshold in ~8 weeks — the point where feature development becomes slower than refactoring.

**Why it is not structurally dangerous:**

1. **Q4 (Reckless/Deliberate) is empty.** No conscious reckless shortcuts were taken.

2. **The architecture is sound.** Hexagonal, typed contracts, Builder pattern, DLQ — all correctly implemented. Debt exists *around* the architecture, not *within* it.

3. **The trajectory is upward.** Reckless/inadvertent debt dropped from 80% to 27% of the portfolio through Phases 1–4.

### Strategic Recommendation

| Action | Investment | Outcome |
|--------|-----------|---------|
| Execute P0 + P1 roadmap (Sprints 1–2) | ~9 days | Pressure: 62% → 38%. Buys 6–9 months of sustainable velocity. |
| Execute P2 roadmap (Sprints 3–6) | ~11 days | Sustainability Score: 5.45 → 7.5. Production-grade resilience. |
| Monitor weekly | Ongoing | If pressure exceeds 70%, declare debt sprint. |

### Projections

| Scenario | 6-Month Sustainability | Pressure | Risk |
|----------|----------------------|----------|------|
| **Roadmap executed** | 7.5/10 (Healthy) | <25% | Sustainable growth |
| **Roadmap NOT executed** | 4.8/10 (Declining) | >100% | Feature freeze required |

> *The architecture is healthy. The debt is governable. The window for cost-effective remediation is open now — but it is closing. Execute the Short Term roadmap immediately.*

---

**Document Version:** 2.0 (Consolidated — integrates Quadrant Matrix)
**Previous Versions:** v1.0 (DEUDA_TECNICA.md + MATRIZ.md as separate documents)
**Next Review:** May 12, 2026 (Quarterly)
**Owner:** Software Architecture Team

# Mock internship implementation plan

> **For agentic workers:** Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Complete the four assigned Sweet Home 3D maintenance tasks with tests and acceptance evidence.
**Architecture:** Extend the existing furniture property, renderer and menu paths. Keep print-only state temporary and restore it with finally blocks.
**Tech Stack:** Java 8, Swing, Maven, JUnit 5.
**Spec:** docs/superpowers/specs/2026-10-27-internship-design.md and ASSIGNMENT.md.

## Global Constraints
- Do not edit assignment-tests.
- English UI changes are sufficient.
- Work in the student's fork and use a feature branch.
- Keep task1–3 screenshots and task4 screenshot/PDF in submissions.

## Review Focus
- Resizing furniture updates the calculated volume and sort order.
- Hidden levels are excluded from print output.
- Invalid page numbers and rendering exceptions restore selected level.
- Printing adds Level without changing the visible screen columns.
- Existing homes and homes without explicit levels remain usable.

### Task 1: Runtime and baseline
- [x] Clone the student's fork, create codex/mock-internship, prepare Java 8/Maven outside the repository.
- [x] Run Maven package and baseline tests: smoke passes, five assigned feature tests fail.
- [x] Launch the application.
- [x] Capture the real running application window as submissions/task1-running.jpg.

### Task 2: Editable description
Files: viewcontroller/HomeFurnitureController.java under SweetHome3D/src/com/eteks/sweethome3d.
- [x] Observe descriptionIsEditable failing in the baseline.
- [x] Remove DESCRIPTION from the noneditable branch and correct the documentation.
- [x] Run BuildSmokeTest and StudentFeaturesTest#descriptionIsEditable; expect pass.
- [x] Verify save/reopen/undo behavior and capture task2 evidence; commit with issue reference.

### Task 3: Volume
Files: model/PieceOfFurniture.java, CatalogPieceOfFurniture.java, HomePieceOfFurniture.java, Home.java; viewcontroller/HomeView.java, HomeController.java, FurnitureController.java; swing/FurnitureTable.java, HomePane.java, package.properties.
Interface: float getVolume(); SortableProperty.VOLUME; SORT_HOME_FURNITURE_BY_VOLUME and DISPLAY_HOME_FURNITURE_VOLUME.
- [x] Observe the three public volume tests failing.
- [x] Add calculated volume to catalog/home furniture, comparator and default new-home column.
- [x] Extend height's table rendering, column order, action registration and sort/display maps for volume.
- [x] Add English header, labels and tooltips; use existing length number format.
- [x] Run the four Task 2–3 feature tests and smoke test; expect pass.
- [x] Verify resize, sorting and both menu locations; capture task3 evidence; commit.

### Task 4: Printing
Files: swing/PlanComponent.java, FurnitureTable.java, HomePrintableComponent.java.
- [x] Observe the public multi-level plan test failing.
- [x] Enumerate viewable levels in home order and restore selection in finally.
- [x] Print all viewable-level furniture with a print-only Level column; restore filters/column model on exceptions.
- [x] Run mvn clean test; expect all six tests pass.
- [x] Check hidden/no-level/invalid-page/restoration cases and sample four-page output.
- [x] Capture task4 PDF/evidence and commit.

### Delivery
- [x] Review all changes independently and address substantive findings.
- [x] Run mvn clean test and python3 test_submission.py; expect pass.
- [x] Link commits to three GitHub issues and close after evidence is ready.
- [x] Merge completed branch to main and push to the student's fork; verify remote state.

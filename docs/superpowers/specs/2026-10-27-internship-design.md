# Sweet Home 3D internship design

## Goal and constraints
Complete Tasks 1–4 in ASSIGNMENT.md on the student's fork. Keep assignment-tests unchanged. Use Java 8 on macOS, Maven, the existing Swing/MVC architecture, and English UI resources. Commit authentic screenshots/PDF evidence under submissions and link feature commits to three closed GitHub issues after acceptance.

## Approaches
Recommended: extend the existing height property path for Volume and adapt the existing printing components. This keeps changes small and consistent with the course's maintenance exercise.
Alternative: replace the furniture table and printing pipeline. This adds unnecessary code and regression risk, so it is not recommended.

## Design
1. Run the unmodified application and capture its complete window. Baseline: BuildSmokeTest passes; five feature tests fail for the missing assigned features.
2. In HomeFurnitureController, enable DESCRIPTION and correct its method documentation. Reuse the existing controller save/undo path.
3. Add calculated getVolume() for catalog and home furniture, VOLUME sorting, the default new-home column, numeric rendering using existing length formatting, and English menu labels/tooltips. Extend the existing sort/display action maps so both main and context menus work. Dimensions remain the source of truth; volume is not separately stored.
4. Print the furniture table across viewable levels with a print-only Level column. Preserve the screen's column selection and table filter, including on exceptions. Enumerate viewable levels in Home order for plan pages and restore the original selected level in finally. Verify the sample's four-page output manually.

## Acceptance
Run all provided tests without altering them; exercise edited description persistence, Volume values and ascending/descending sorting, both menu locations, and all-level print preview/PDF. Verify viewable-level filtering and restoration after invalid page requests or rendering failure. Ensure task1–3 image files and task4 image/PDF pass test_submission.py. Push work to the student's fork, with no course-upstream changes.

## Execution choices
Use the freshly cloned fork and codex/mock-internship branch. Keep the downloaded Java/Maven tools outside the repository. Do not alter the system Java default. The author must understand and be able to explain submitted code.

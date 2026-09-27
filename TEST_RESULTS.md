# Test results - current NetBeans version

Run: 2026-09-28T00:43:16+02:00

Compiler: javac 25.0.1 (release 25).

Source: `src/main/java/com/mycompany/rawmaterialsystem/MaterialSystem.java`

Source SHA-256: `c6ea381db8fbd864da6eba94f8510ccb7b89d0b026cd108c08a6a6c8d78001dd`

**PASS: 45 Java checks. 27/27 menu scenarios passed.**

The source was compiled for this run; these results are not copied from an earlier version.
Java checks cover record fields, duplicate and invalid data, stock arithmetic, unchanged stock
on rejected operations, thresholds, overflow, link preservation, sorting and removal.

| Scenario | Expected | Actual | Result |
|---|---|---|---|
| Exit option 11 | Thank you for using the system. | Matched expected output; exit code 0. | PASS |
| Empty inventory | No materials in the inventory.; Material not found.; Number of materials: 0 | Matched expected output; exit code 0. | PASS |
| Add and display | ID: M1; Unit: kg; Supplier: Supplier A; Reorder level: 100.0 | Matched expected output; exit code 0. | PASS |
| Case-insensitive search | Material found! | Matched expected output; exit code 0. | PASS |
| Duplicate ID | That material ID already exists.; Number of materials: 1 | Matched expected output; exit code 0. | PASS |
| Receive option 7 | New quantity: 700.0 kg; Quantity: 700.0 | Matched expected output; exit code 0. | PASS |
| Issue option 8 | Remaining quantity: 50.0 kg; Low stock: Steel | Matched expected output; exit code 0. | PASS |
| Excess issue | Not enough stock available.; Quantity: 500.0 | Matched expected output; exit code 0. | PASS |
| Exact depletion | Remaining quantity: 0.0 kg; Number of materials: 1 | Matched expected output; exit code 0. | PASS |
| Threshold option 9 equality | Reorder level updated successfully.; Low stock: Steel | Matched expected output; exit code 0. | PASS |
| Check option 10 | Stock level is sufficient. | Matched expected output; exit code 0. | PASS |
| Zero movement | Enter a valid amount greater than zero.; Quantity: 500.0 | Matched expected output; exit code 0. | PASS |
| Decimal movement | Remaining quantity: 502.25 kg | Matched expected output; exit code 0. | PASS |
| Menu text and trailing text | Please enter a menu number.; Enter only a menu number.; Invalid choice. | Matched expected output; exit code 0. | PASS |
| Quantity validation | Please enter a number.; Enter only a number.; Quantity: 5.0 | Matched expected output; exit code 0. | PASS |
| Missing stock ID | Material not found. | Matched expected output; exit code 0. | PASS |
| Remove head middle tail | Number of materials: 1; Material found!; Number of materials: 0 | Matched expected output; exit code 0. | PASS |
| Overflow protected | The resulting quantity is too large.; Quantity: 1.0E308 | Matched expected output; exit code 0. | PASS |
| EOF at menu | Input closed. Goodbye. | Matched expected output; exit code 0. | PASS |
| EOF during add | Input closed. Goodbye. | Matched expected output; exit code 0. | PASS |
| EOF after blank menu lines | Input closed. Goodbye. | Matched expected output; exit code 0. | PASS |
| EOF after blank numeric lines | Input closed. Goodbye. | Matched expected output; exit code 0. | PASS |
| Exit without final newline | Thank you for using the system. | Matched expected output; exit code 0. | PASS |
| Quantity without final newline | Input closed. Goodbye. | Matched expected output; exit code 0. | PASS |
| Sorting ascending | Ascending quantities; ties keep insertion order. | Quantities: [1.0, 2.0, 3.0, 4.0]; IDs: M0, M1, M2, M3 | PASS |
| Sorting descending | Ascending quantities; ties keep insertion order. | Quantities: [1.0, 2.0, 3.0, 4.0]; IDs: M3, M2, M1, M0 | PASS |
| Sorting mixed with ties | Ascending quantities; ties keep insertion order. | Quantities: [0.0, 5.0, 5.0, 20.0, 30.0]; IDs: M4, M1, M3, M0, M2 | PASS |

## Repeating this run

`python tests/test_menu.py`

This command compiles the current source, runs both test sets and updates this report.
Detailed console transcripts are saved in `target/test-logs/` (excluded from Git).

## Limits

These checks cover the listed scenarios, not every possible input. They test the console
program directly, not the NetBeans interface or a Maven lifecycle run. The plain Java
check class is not JUnit and is not automatically run by Maven Surefire.
Inventory is held in memory and quantities retain the original double rounding behaviour.
Re-run after code changes. Assignment report screenshots have not been regenerated.

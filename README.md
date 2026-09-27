# Raw Material Inventory System - Group 15

COM2224, Problem 15: manage raw materials used in production.
The project uses Java, a handwritten singly linked list, linear search and
insertion sort. It does not use Java's built-in LinkedList or sorting methods.

## Run in NetBeans

Open this Maven project and run it. The main class is
`com.mycompany.rawmaterialsystem.MaterialSystem`.
The existing pom.xml targets Java 25, so use JDK 25 or newer.

## Menu

1. Add Material
2. Display Materials
3. Search Material
4. Remove Material
5. Sort Materials
6. Count Materials
7. Receive Stock
8. Issue Material
9. Update Reorder Level
10. Check Reorder Level
11. Exit

Records contain an ID, name, quantity, unit, supplier and reorder level.
IDs are checked without regard to letter case. Receiving stock increases an
existing record's quantity. Issuing stock reduces it without deleting the record.
A quantity at or below the reorder level produces a warning.

Use a dot for decimals, such as 2.5. Stock amounts must be positive. Initial
quantity and reorder level may be zero. Enter amounts in the stored unit.

## Files

- `src/main/java/com/mycompany/rawmaterialsystem/MaterialSystem.java`: application.
- `src/test/java/com/mycompany/rawmaterialsystem/MaterialSystemChecks.java`: Java checks.
- `tests/test_menu.py`: runs the Java checks and 27 console scenarios.
- `TEST_RESULTS.md`: results from the latest recorded run of the current source.
- `pom.xml`: existing NetBeans/Maven project settings.
- `.gitignore`: keeps generated files out of future Git commits.

## Run all tests

With Python 3 and JDK 25+ available, open a terminal in the project folder:

```text
python tests/test_menu.py
```

The script compiles the actual project source, runs both test sets, prints
PASS/FAIL results and updates TEST_RESULTS.md. No Python packages or external
Java testing libraries are required. If Java is not found, set JAVA_HOME to your
JDK folder. The runner also checks the usual Windows NetBeans JDK location.

## Run just the Java checks

In NetBeans, open MaterialSystemChecks.java under Test Packages and choose
Run File. This is a plain Java main program, not a JUnit class. Normal Maven
Test does not automatically execute it.

Alternatively, from PowerShell in the project folder:

```powershell
New-Item -ItemType Directory -Force target/manual-tests/com/mycompany/rawmaterialsystem | Out-Null
javac --release 25 -d target/manual-tests src/main/java/com/mycompany/rawmaterialsystem/MaterialSystem.java src/test/java/com/mycompany/rawmaterialsystem/MaterialSystemChecks.java
java -cp target/manual-tests com.mycompany.rawmaterialsystem.MaterialSystemChecks
```

## Quick demonstration

Add Steel with ID M1, quantity 500, unit kg, supplier Supplier A and reorder
level 100. Receive 200: quantity becomes 700. Issue 650: quantity becomes 50
and a low-stock warning appears. Trying to issue another 60 must be rejected.
Display the record to confirm that its quantity remains 50.

## Analysis notes

- Linear search: best O(1), average/worst O(n).
- Adding: O(n), including duplicate checking and walking to the tail.
- Removing: best O(1), worst O(n).
- Display and count: O(n).
- Stock actions include linear searches, so their worst-case cost is O(n).
- Insertion sort: O(n) for ascending or descending input in this implementation;
  average/worst O(n^2), with O(1) extra working space. Equal quantities keep their
  original order. Nearly sorted input is not guaranteed to be linear.

## Boundaries

Data is kept in memory and is lost on exit. Changing quantities can change their
sorted order, so select Sort again when needed. Sorting compares the stored
numbers; it does not convert between different units. The application retains
Java double arithmetic, including its normal rounding limitations.

These support files describe the current source. Update the separate assignment
report and screenshots to match this version before submitting.

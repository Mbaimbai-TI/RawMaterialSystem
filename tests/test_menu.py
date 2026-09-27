"""Compile and test the current project. Requires Python 3 and JDK 25+."""
from pathlib import Path
import datetime
import hashlib
import os
import re
import shutil
import subprocess
import sys

root = Path(__file__).resolve().parents[1]
source = root / 'src/main/java/com/mycompany/rawmaterialsystem/MaterialSystem.java'
checks = root / 'src/test/java/com/mycompany/rawmaterialsystem/MaterialSystemChecks.java'
classes = root / 'target/manual-tests'
logs = root / 'target/test-logs'
(classes / 'com/mycompany/rawmaterialsystem').mkdir(parents=True, exist_ok=True)
logs.mkdir(parents=True, exist_ok=True)

def tool(name):
    if os.environ.get('JAVA_HOME'):
        candidate = Path(os.environ['JAVA_HOME']) / 'bin' / (name + '.exe' if os.name == 'nt' else name)
        if candidate.exists():
            return str(candidate)
    found = shutil.which(name)
    if found:
        return found
    candidate = Path(os.environ.get('ProgramFiles', 'C:/Program Files')) / 'Apache NetBeans/jdk/bin' / (name + '.exe')
    if candidate.exists():
        return str(candidate)
    raise SystemExit('JDK tool not found: ' + name + '. Set JAVA_HOME to your JDK folder.')

javac, java = tool('javac'), tool('java')
subprocess.run([javac, '--release', '25', '-d', str(classes.relative_to(root)),
                str(source.relative_to(root)), str(checks.relative_to(root))], cwd=root, check=True)
version = subprocess.run([javac, '-version'], text=True, capture_output=True, check=True)
java_checks = subprocess.run([java, '-cp', str(classes),
                            'com.mycompany.rawmaterialsystem.MaterialSystemChecks'],
                            text=True, capture_output=True, timeout=30)
(logs / 'java-checks.txt').write_text(java_checks.stdout + java_checks.stderr, encoding='utf-8')
java_ok = java_checks.returncode == 0 and 'PASS:' in java_checks.stdout
java_summary = java_checks.stdout.splitlines()[-1] if java_ok else 'FAIL: Java checks; see target/test-logs/java-checks.txt'
print(java_summary)
cmd = [java, '-cp', str(classes), 'com.mycompany.rawmaterialsystem.MaterialSystem']
def add(i='M1',q='500',level='100'):
 return f'1\n{i}\nSteel\n{q}\nkg\nSupplier A\n{level}\n'
a=add()
cases=[
 ('Exit option 11','11\n',['Thank you for using the system.']),
 ('Empty inventory','2\n3\nX\n4\nX\n5\n6\n11\n',['No materials in the inventory.','Material not found.','Number of materials: 0']),
 ('Add and display',a+'2\n11\n',['ID: M1','Unit: kg','Supplier: Supplier A','Reorder level: 100.0']),
 ('Case-insensitive search',a+'3\nm1\n11\n',['Material found!']),
 ('Duplicate ID',a+'1\nm1\n6\n11\n',['That material ID already exists.','Number of materials: 1']),
 ('Receive option 7',a+'7\nM1\n200\n2\n11\n',['New quantity: 700.0 kg','Quantity: 700.0']),
 ('Issue option 8',a+'8\nM1\n450\n11\n',['Remaining quantity: 50.0 kg','Low stock: Steel']),
 ('Excess issue',a+'8\nM1\n501\n2\n11\n',['Not enough stock available.','Quantity: 500.0']),
 ('Exact depletion',a+'8\nM1\n500\n6\n11\n',['Remaining quantity: 0.0 kg','Number of materials: 1']),
 ('Threshold option 9 equality',a+'9\nM1\n500\n11\n',['Reorder level updated successfully.','Low stock: Steel']),
 ('Check option 10',a+'10\nM1\n11\n',['Stock level is sufficient.']),
 ('Zero movement',a+'7\nM1\n0\n8\nM1\n0\n2\n11\n',['Enter a valid amount greater than zero.','Quantity: 500.0']),
 ('Decimal movement',a+'7\nM1\n2.5\n8\nM1\n0.25\n11\n',['Remaining quantity: 502.25 kg']),
 ('Menu text and trailing text','hello\n7 abc\n12\n11\n',['Please enter a menu number.','Enter only a menu number.','Invalid choice.']),
 ('Quantity validation','1\nM1\nSteel\nhello\n-1\nNaN\nInfinity\n5 abc\n5\nkg\nSupplier A\n0\n2\n11\n',['Please enter a number.','Enter only a number.','Quantity: 5.0']),
 ('Missing stock ID','7\nX\n8\nX\n9\nX\n10\nX\n11\n',['Material not found.']),
 ('Remove head middle tail',a+add('M2')+add('M3')+add('M4')+'4\nM1\n4\nM3\n4\nM4\n6\n3\nM2\n4\nM2\n6\n11\n',['Number of materials: 1','Material found!','Number of materials: 0']),
 ('Overflow protected',add(q='1e308')+'7\nM1\n1e308\n2\n11\n',['The resulting quantity is too large.','Quantity: 1.0E308']),
 ('EOF at menu','',['Input closed. Goodbye.']),
 ('EOF during add','1\nM1\nSteel\n',['Input closed. Goodbye.']),
 ('EOF after blank menu lines','\n\n',['Input closed. Goodbye.']),
 ('EOF after blank numeric lines','1\nM1\nSteel\n\n\n',['Input closed. Goodbye.']),
 ('Exit without final newline','11',['Thank you for using the system.']),
 ('Quantity without final newline','1\nM1\nSteel\n5',['Input closed. Goodbye.']),
]

results = []
def run_case(label, data, expected, sort_values=None):
    try:
        run = subprocess.run(cmd, input=data, text=True, capture_output=True, timeout=15)
        ok = run.returncode == 0 and not run.stderr and all(value in run.stdout for value in expected)
        details = 'Matched expected output; exit code 0.'
        if sort_values is not None:
            actual = [float(value) for value in re.findall(r'^Quantity: (.+)$', run.stdout, re.M)]
            ids = re.findall(r'^ID: (.+)$', run.stdout, re.M)
            expected_ids = ['M' + str(i) for i in sorted(range(len(sort_values)), key=lambda i: sort_values[i])]
            ok = ok and actual == sorted(sort_values) and ids == expected_ids
            details = 'Quantities: ' + str(actual) + '; IDs: ' + ', '.join(ids)
        if not ok:
            details = run.stderr.strip() or 'Expected output did not match.'
        transcript = 'INPUT:\n' + data + '\nOUTPUT:\n' + run.stdout + '\nERRORS:\n' + run.stderr
    except subprocess.TimeoutExpired:
        ok = False
        details = 'Timed out after 15 seconds.'
        transcript = details
    filename = re.sub(r'[^a-z0-9]+', '-', label.lower()).strip('-') + '.txt'
    (logs / filename).write_text(transcript, encoding='utf-8')
    results.append((label, '; '.join(expected) if expected else 'Ascending quantities; ties keep insertion order.', details, ok))
    print(('PASS: ' if ok else 'FAIL: ') + label)

for label, data, expected in cases:
    run_case(label, data, expected)
for label, values in [('ascending', [1,2,3,4]), ('descending', [4,3,2,1]), ('mixed with ties', [20,5,30,5,0])]:
    data = ''.join(add('M' + str(i), str(value), '0') for i, value in enumerate(values)) + '5\n2\n11\n'
    run_case('Sorting ' + label, data, [], values)
passed = sum(row[3] for row in results)
summary = f'{passed}/{len(results)} menu scenarios passed.'
print(summary)

def cell(text):
    return text.replace('|', '/').replace('\n', ' ')

report = [
    '# Test results - current NetBeans version',
    '',
    'Run: ' + datetime.datetime.now().astimezone().isoformat(timespec='seconds'),
    '',
    'Compiler: ' + (version.stdout + version.stderr).strip() + ' (release 25).',
    '',
    'Source: `src/main/java/com/mycompany/rawmaterialsystem/MaterialSystem.java`',
    '',
    'Source SHA-256: `' + hashlib.sha256(source.read_bytes()).hexdigest() + '`',
    '',
    '**' + java_summary + ' ' + summary + '**',
    '',
    'The source was compiled for this run; these results are not copied from an earlier version.',
    'Java checks cover record fields, duplicate and invalid data, stock arithmetic, unchanged stock',
    'on rejected operations, thresholds, overflow, link preservation, sorting and removal.',
    '',
    '| Scenario | Expected | Actual | Result |',
    '|---|---|---|---|',
]
for label, expected, actual, ok in results:
    report.append('| ' + ' | '.join([cell(label), cell(expected), cell(actual), 'PASS' if ok else 'FAIL']) + ' |')
report += ['', '## Repeating this run', '', '`python tests/test_menu.py`', '',
           'This command compiles the current source, runs both test sets and updates this report.',
           'Detailed console transcripts are saved in `target/test-logs/` (excluded from Git).', '',
           '## Limits', '',
           'These checks cover the listed scenarios, not every possible input. They test the console',
           'program directly, not the NetBeans interface or a Maven lifecycle run. The plain Java',
           'check class is not JUnit and is not automatically run by Maven Surefire.',
           'Inventory is held in memory and quantities retain the original double rounding behaviour.',
           'Re-run after code changes. Assignment report screenshots have not been regenerated.', '']
(root / 'TEST_RESULTS.md').write_text('\n'.join(report), encoding='utf-8')
sys.exit(0 if java_ok and passed == len(results) else 1)

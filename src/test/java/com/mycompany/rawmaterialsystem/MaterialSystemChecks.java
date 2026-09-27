package com.mycompany.rawmaterialsystem;

// A separate test program. It does not change the inventory application's code.
public class MaterialSystemChecks {
    static int checks = 0;

    static void check(boolean result, String name) {
        if (!result) {
            throw new AssertionError("FAILED: " + name);
        }
        checks++;
    }

    static MaterialSystem.RawMaterial material(String id, double quantity) {
        return new MaterialSystem.RawMaterial(
                id, "Steel", quantity, "kg", "Supplier A", 100);
    }

    public static void main(String[] args) {
        MaterialSystem.Inventory inventory = new MaterialSystem.Inventory();
        check(inventory.count() == 0, "Empty count");
        check(inventory.search("M1") == null, "Search empty list");
        check(!inventory.remove("M1"), "Remove from empty list");
        inventory.sort();

        MaterialSystem.RawMaterial steel = material("M1", 500);
        inventory.add(steel);
        check(steel.unit.equals("kg"), "Unit saved");
        check(steel.supplier.equals("Supplier A"), "Supplier saved");
        check(steel.reorderLevel == 100, "Reorder level saved");
        check(inventory.search("m1") == steel, "Case-insensitive search");
        inventory.add(material("m1", 10));
        check(inventory.count() == 1, "Duplicate ID rejected");

        for (double bad : new double[]{-1, Double.NaN, Double.POSITIVE_INFINITY}) {
            inventory.add(material("BAD", bad));
            check(inventory.count() == 1, "Invalid quantity rejected");
            MaterialSystem.RawMaterial invalid = material("BAD", 10);
            invalid.reorderLevel = bad;
            inventory.add(invalid);
            check(inventory.count() == 1, "Invalid threshold rejected");
        }

        MaterialSystem.Node firstNode = inventory.head;
        inventory.receiveStock("M1", 200);
        check(steel.quantity == 700, "Receive stock");
        inventory.useMaterial("M1", 650);
        check(steel.quantity == 50, "Issue stock");
        inventory.useMaterial("M1", 60);
        check(steel.quantity == 50, "Insufficient stock leaves quantity unchanged");
        for (double bad : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY}) {
            inventory.receiveStock("M1", bad);
            inventory.useMaterial("M1", bad);
            check(steel.quantity == 50, "Invalid movement rejected");
        }
        inventory.updateReorderLevel("M1", 50);
        check(steel.reorderLevel == 50, "Update threshold");
        for (double bad : new double[]{-1, Double.NaN, Double.POSITIVE_INFINITY}) {
            inventory.updateReorderLevel("M1", bad);
            check(steel.reorderLevel == 50, "Invalid threshold update rejected");
        }
        inventory.useMaterial("M1", 50);
        check(steel.quantity == 0 && inventory.count() == 1, "Zero stock keeps record");
        inventory.receiveStock("M1", 2.5);
        inventory.useMaterial("M1", 0.25);
        check(steel.quantity == 2.25, "Decimal quantities");
        inventory.updateReorderLevel("M1", 0);
        check(steel.reorderLevel == 0, "Zero threshold allowed");
        check(inventory.head == firstNode && firstNode.next == null, "Stock preserves links");
        steel.quantity = Double.MAX_VALUE;
        inventory.receiveStock("M1", Double.MAX_VALUE);
        check(steel.quantity == Double.MAX_VALUE, "Overflow rejected");

        MaterialSystem.Inventory sorted = new MaterialSystem.Inventory();
        double[] quantities = {20, 5, 30, 5, 0};
        for (int i = 0; i < quantities.length; i++) {
            sorted.add(material("S" + i, quantities[i]));
        }
        sorted.sort();
        String[] expected = {"S4", "S1", "S3", "S0", "S2"};
        MaterialSystem.Node current = sorted.head;
        for (String id : expected) {
            check(current != null && current.data.id.equals(id), "Sorted position " + id);
            current = current.next;
        }
        check(current == null, "Sort ends without a cycle");
        check(sorted.count() == 5, "Sort preserves record count");
        sorted.receiveStock("S4", 40);
        sorted.sort();
        check(sorted.head.data.id.equals("S1"), "Sort again after changing stock");
        check(sorted.search("S4").quantity == 40, "Search after sorting");

        MaterialSystem.Inventory removal = new MaterialSystem.Inventory();
        for (int i = 0; i < 4; i++) removal.add(material("R" + i, i));
        check(removal.remove("R0"), "Remove head");
        check(removal.remove("R2"), "Remove middle");
        check(removal.remove("R3"), "Remove tail");
        check(!removal.remove("X"), "Remove missing record");
        check(removal.count() == 1, "Remaining count");
        check(removal.remove("R1") && removal.head == null, "Remove final record");
        System.out.println("PASS: " + checks + " Java checks.");
    }
}

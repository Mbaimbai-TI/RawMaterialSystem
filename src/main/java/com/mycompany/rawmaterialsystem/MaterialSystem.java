package com.mycompany.rawmaterialsystem;

import java.util.Scanner;

public class MaterialSystem {

    // Raw material information
    static class RawMaterial {

        String id;
        String name;
        double quantity;
        String unit;
        String supplier;
        double reorderLevel;

        RawMaterial(String id, String name, double quantity,
                    String unit, String supplier, double reorderLevel) {

            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
            this.supplier = supplier;
            this.reorderLevel = reorderLevel;
        }
    }

    // Node for the linked list
    static class Node {

        RawMaterial data;
        Node next;

        Node(RawMaterial data) {
            this.data = data;
            this.next = null;
        }
    }

    // Singly linked list
    static class Inventory {

        Node head;

        // Add a material
        void add(RawMaterial material) {

            if (search(material.id) != null) {
                System.out.println("That material ID already exists.");
                return;
            }

            if (!(material.quantity >= 0
                    && material.quantity <= Double.MAX_VALUE)) {
                System.out.println("Enter a valid, non-negative quantity.");
                return;
            }

            if (!(material.reorderLevel >= 0
                    && material.reorderLevel <= Double.MAX_VALUE)) {
                System.out.println("Enter a valid, non-negative reorder level.");
                return;
            }

            Node newNode = new Node(material);

            if (head == null) {
                head = newNode;
            } else {
                Node current = head;

                while (current.next != null) {
                    current = current.next;
                }

                current.next = newNode;
            }

            System.out.println("Material added successfully.");
        }

        // Display one material
        void displayMaterial(RawMaterial material) {

            System.out.println("-------------------------");
            System.out.println("ID: " + material.id);
            System.out.println("Name: " + material.name);
            System.out.println("Quantity: " + material.quantity);
            System.out.println("Unit: " + material.unit);
            System.out.println("Supplier: " + material.supplier);
            System.out.println("Reorder level: " + material.reorderLevel);
            System.out.println("-------------------------");
        }

        // Display all materials
        void display() {

            if (head == null) {
                System.out.println("No materials in the inventory.");
                return;
            }

            Node current = head;

            while (current != null) {
                displayMaterial(current.data);
                current = current.next;
            }
        }

        // Linear search
        RawMaterial search(String id) {

            Node current = head;

            while (current != null) {

                if (current.data.id.equalsIgnoreCase(id)) {
                    return current.data;
                }

                current = current.next;
            }

            return null;
        }

        // Remove a material
        boolean remove(String id) {

            if (head == null) {
                return false;
            }

            if (head.data.id.equalsIgnoreCase(id)) {
                head = head.next;
                return true;
            }

            Node current = head;

            while (current.next != null) {

                if (current.next.data.id.equalsIgnoreCase(id)) {
                    current.next = current.next.next;
                    return true;
                }

                current = current.next;
            }

            return false;
        }

        // Receive stock
        void receiveStock(String id, double amount) {

            RawMaterial material = search(id);

            if (material == null) {
                System.out.println("Material not found.");
                return;
            }

            if (!(amount > 0 && amount <= Double.MAX_VALUE)) {
                System.out.println("Enter a valid amount greater than zero.");
                return;
            }

            double newQuantity = material.quantity + amount;

            if (newQuantity > Double.MAX_VALUE) {
                System.out.println("The resulting quantity is too large.");
                return;
            }

            material.quantity = newQuantity;

            System.out.println("Stock received successfully.");
            System.out.println("New quantity: "
                    + material.quantity + " " + material.unit);

            checkReorderLevel(id);
        }

        // Issue material for production
        void useMaterial(String id, double amount) {

            RawMaterial material = search(id);

            if (material == null) {
                System.out.println("Material not found.");
                return;
            }

            if (!(amount > 0 && amount <= Double.MAX_VALUE)) {
                System.out.println("Enter a valid amount greater than zero.");
                return;
            }

            if (amount > material.quantity) {
                System.out.println("Not enough stock available.");
                return;
            }

            material.quantity = material.quantity - amount;

            System.out.println("Material issued successfully.");
            System.out.println("Remaining quantity: "
                    + material.quantity + " " + material.unit);

            checkReorderLevel(id);
        }

        // Update the reorder level
        void updateReorderLevel(String id, double level) {

            RawMaterial material = search(id);

            if (material == null) {
                System.out.println("Material not found.");
                return;
            }

            if (!(level >= 0 && level <= Double.MAX_VALUE)) {
                System.out.println("Enter a valid, non-negative reorder level.");
                return;
            }

            material.reorderLevel = level;

            System.out.println("Reorder level updated successfully.");

            checkReorderLevel(id);
        }

        // Check whether a material needs to be reordered
        void checkReorderLevel(String id) {

            RawMaterial material = search(id);

            if (material == null) {
                System.out.println("Material not found.");
                return;
            }

            if (material.quantity <= material.reorderLevel) {
                System.out.println("Low stock: " + material.name);
                System.out.println("Please reorder this material.");
            } else {
                System.out.println("Stock level is sufficient.");
            }
        }

        // Insertion sort: smallest quantity first
        void sort() {

            if (head == null) {
                System.out.println("No materials to sort.");
                return;
            }

            Node sorted = null;
            Node sortedTail = null;
            Node current = head;

            while (current != null) {

                Node next = current.next;

                if (sorted == null) {
                    current.next = null;
                    sorted = current;
                    sortedTail = current;

                } else if (current.data.quantity >= sortedTail.data.quantity) {
                    // Add directly at the end
                    sortedTail.next = current;
                    current.next = null;
                    sortedTail = current;

                } else if (current.data.quantity < sorted.data.quantity) {
                    // Insert before the first sorted node
                    current.next = sorted;
                    sorted = current;

                } else {
                    // Find the correct position inside the sorted list
                    Node temp = sorted;

                    while (temp.next != null
                            && temp.next.data.quantity <= current.data.quantity) {
                        temp = temp.next;
                    }

                    current.next = temp.next;
                    temp.next = current;
                }

                current = next;
            }

            head = sorted;

            System.out.println("Materials sorted by quantity.");
        }

        // Count materials
        int count() {

            int total = 0;
            Node current = head;

            while (current != null) {
                total++;
                current = current.next;
            }

            return total;
        }
    }

    // Read text and prevent empty entries
    static String readText(Scanner input, String message) {

        while (true) {
            System.out.print(message);

            if (!input.hasNextLine()) {
                return null;
            }

            String text = input.nextLine().trim();

            if (!text.isEmpty()) {
                return text;
            }

            System.out.println("Please enter a value.");
        }
    }

    // Read a non-negative number
    static double readNumber(Scanner input, String message) {

        while (true) {
            System.out.print(message);

            if (!input.hasNextLine()) {
                return -1;
            }

            if (input.hasNextDouble()) {
                double number = input.nextDouble();
                String remainingText = "";

                if (input.hasNextLine()) {
                    remainingText = input.nextLine().trim();
                }

                if (!remainingText.isEmpty()) {
                    System.out.println("Enter only a number.");
                    continue;
                }

                if (number >= 0 && number <= Double.MAX_VALUE) {
                    return number;
                }

                System.out.println("Enter a valid number that is zero or greater.");

            } else {
                System.out.println("Please enter a number.");
                input.nextLine();
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        input.useLocale(java.util.Locale.US);
        Inventory inventory = new Inventory();

        int choice = 0;

        do {
            System.out.println();
            System.out.println("RAW MATERIAL INVENTORY SYSTEM");
            System.out.println("*****************************");
            System.out.println("1. Add Material");
            System.out.println("2. Display Materials");
            System.out.println("3. Search Material");
            System.out.println("4. Remove Material");
            System.out.println("5. Sort Materials");
            System.out.println("6. Count Materials");
            System.out.println("7. Receive Stock");
            System.out.println("8. Issue Material");
            System.out.println("9. Update Reorder Level");
            System.out.println("10. Check Reorder Level");
            System.out.println("11. Exit");
            System.out.println("*****************************");
            System.out.print("Enter your choice: ");

            if (!input.hasNextLine()) {
                System.out.println("Input closed. Goodbye.");
                break;
            }

            if (!input.hasNextInt()) {
                System.out.println("Please enter a menu number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            String remainingText = "";

            if (input.hasNextLine()) {
                remainingText = input.nextLine().trim();
            }

            if (!remainingText.isEmpty()) {
                System.out.println("Enter only a menu number.");
                choice = 0;
                continue;
            }

            switch (choice) {

                case 1: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    if (inventory.search(id) != null) {
                        System.out.println("That material ID already exists.");
                        break;
                    }

                    String name = readText(input, "Enter material name: ");

                    if (name == null) {
                        break;
                    }

                    double quantity = readNumber(input, "Enter quantity: ");

                    if (quantity < 0) {
                        break;
                    }

                    String unit = readText(input, "Enter unit: ");

                    if (unit == null) {
                        break;
                    }

                    String supplier = readText(input, "Enter supplier: ");

                    if (supplier == null) {
                        break;
                    }

                    double level = readNumber(input, "Enter reorder level: ");

                    if (level < 0) {
                        break;
                    }

                    RawMaterial material = new RawMaterial(
                            id, name, quantity, unit, supplier, level);

                    inventory.add(material);
                    break;
                }

                case 2:
                    inventory.display();
                    break;

                case 3: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    RawMaterial material = inventory.search(id);

                    if (material != null) {
                        System.out.println("Material found!");
                        inventory.displayMaterial(material);
                    } else {
                        System.out.println("Material not found.");
                    }

                    break;
                }

                case 4: {
                    String id = readText(input, "Enter material ID to remove: ");

                    if (id == null) {
                        break;
                    }

                    if (inventory.remove(id)) {
                        System.out.println("Material removed.");
                    } else {
                        System.out.println("Material not found.");
                    }

                    break;
                }

                case 5:
                    inventory.sort();
                    break;

                case 6:
                    System.out.println("Number of materials: " + inventory.count());
                    break;

                case 7: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    RawMaterial material = inventory.search(id);

                    if (material == null) {
                        System.out.println("Material not found.");
                        break;
                    }

                    double amount = readNumber(
                            input, "Enter amount received in " + material.unit + ": ");

                    if (amount < 0) {
                        break;
                    }

                    inventory.receiveStock(id, amount);
                    break;
                }

                case 8: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    RawMaterial material = inventory.search(id);

                    if (material == null) {
                        System.out.println("Material not found.");
                        break;
                    }

                    double amount = readNumber(
                            input, "Enter amount to issue in " + material.unit + ": ");

                    if (amount < 0) {
                        break;
                    }

                    inventory.useMaterial(id, amount);
                    break;
                }

                case 9: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    RawMaterial material = inventory.search(id);

                    if (material == null) {
                        System.out.println("Material not found.");
                        break;
                    }

                    double level = readNumber(
                            input, "Enter new reorder level in " + material.unit + ": ");

                    if (level < 0) {
                        break;
                    }

                    inventory.updateReorderLevel(id, level);
                    break;
                }

                case 10: {
                    String id = readText(input, "Enter material ID: ");

                    if (id == null) {
                        break;
                    }

                    inventory.checkReorderLevel(id);
                    break;
                }

                case 11:
                    System.out.println("Thank you for using the system.");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1 to 11.");
            }

        } while (choice != 11);

        input.close();
    }
}
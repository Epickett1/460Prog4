/*
 * Author: Group 12
 * Course: CSC 460
 * Assignment: Prog4
 * Instructor: Prof McCann
 * TA's: Xinyu (Joyce) Gu and Jianwei (James) Shen
 * Due Date: April 30th 2025
 * Purpose: 
 * Language: Java 16
 * Input:
 * Output: 
 * Missing Features: 
 */

 import java.math.BigDecimal;
 import java.sql.*;
 import java.util.Scanner;
 
 public class menu {
     private static final String URL  = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:ORCL";
     private static final String USER = "<YOUR_USER>";
     private static final String PASS = "<YOUR_PASS>";
     private Connection conn;
     private Scanner scanner = new Scanner(System.in);
 
     public static void main(String[] args) {
         new menu().run();
     }
 
     public void run() {
         try {
             conn = DriverManager.getConnection(URL, USER, PASS);
             boolean exit = false;
             while (!exit) {
                 printMainMenu();
                 String choice = scanner.nextLine().trim();
                 switch (choice) {
                     case "1": memberMenu();    break;
                     case "2": skiPassMenu();   break;
                     case "3": equipmentMenu(); break;
                     case "4": rentalMenu();    break;
                     case "5": lessonMenu();    break;
                     case "6": instructorMenu();break;
                     case "7": employeeMenu();  break;
                     case "8": trailMenu();     break;
                     case "9": liftMenu();      break;
                     case "10": liftUsageMenu();break;
                     case "11": queryMenu();    break;
                     case "0": exit = true;     break;
                     default: System.out.println("Invalid option");
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
         }
     }
 
     private void printMainMenu() {
         System.out.println("\n=== CSc 460 Ski Resort CLI ===");
         System.out.println("1) Member CRUD");
         System.out.println("2) SkiPass CRUD");
         System.out.println("3) Equipment CRUD");
         System.out.println("4) Rental CRUD");
         System.out.println("5) Lesson Purchase CRUD");
         System.out.println("6) Instructor CRUD");
         System.out.println("7) Employee CRUD");
         System.out.println("8) Trail CRUD");
         System.out.println("9) Lift CRUD");
         System.out.println("10) Lift Usage CRUD");
         System.out.println("11) Queries");
         System.out.println("0) Exit");
         System.out.print("> ");
     }
 
     private void memberMenu() { /* as before */ }
 
     private void skiPassMenu() {
         while (true) {
             System.out.println("-- SkiPass -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
             System.out.print("> ");
             String c = scanner.nextLine().trim();
             try {
                 switch (c) {
                 case "1": { // Add
                     System.out.print("PID: "); int pid = Integer.parseInt(scanner.nextLine());
                     System.out.print("Member ID: "); int mid = Integer.parseInt(scanner.nextLine());
                     System.out.print("Pass Type: "); String type = scanner.nextLine();
                     System.out.print("Purchase Time (yyyy-mm-dd HH:MI:SS): "); Timestamp pt = Timestamp.valueOf(scanner.nextLine());
                     System.out.print("Expiration Date: "); Date exp = Date.valueOf(scanner.nextLine());
                     System.out.print("Total Uses: "); int total = Integer.parseInt(scanner.nextLine());
                     System.out.print("Remaining Uses: "); int rem = Integer.parseInt(scanner.nextLine());
                     System.out.print("Price: "); BigDecimal price = new BigDecimal(scanner.nextLine());
                     System.out.print("IsValid (Y/N): "); String valid = scanner.nextLine();
                     String sql = "INSERT INTO SkiPass (PID,MID,PassType,PurchaseDateTime,ExpirationDate,TotalUses,RemainingUses,Price,IsValid) " +
                                  "VALUES (?,?,?,?,?,?,?,?,?)";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, pid);
                         ps.setInt(2, mid);
                         ps.setString(3, type);
                         ps.setTimestamp(4, pt);
                         ps.setDate(5, exp);
                         ps.setInt(6, total);
                         ps.setInt(7, rem);
                         ps.setBigDecimal(8, price);
                         ps.setString(9, valid);
                         ps.executeUpdate();
                     }
                     System.out.println("SkiPass added.");
                     break;
                 }
                 case "2": { // List
                     String sql = "SELECT PID,MID,PassType,ExpirationDate,RemainingUses FROM SkiPass";
                     try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                         System.out.println("PID | MID | Type | ExpDate | RemUses");
                         while (rs.next()) {
                             System.out.printf("%d | %d | %s | %s | %d%n",
                                 rs.getInt("PID"), rs.getInt("MID"), rs.getString("PassType"),
                                 rs.getDate("ExpirationDate"), rs.getInt("RemainingUses"));
                         }
                     }
                     break;
                 }
                 case "3": { // Update
                     System.out.print("PID to update: "); int pid = Integer.parseInt(scanner.nextLine());
                     System.out.print("New Remaining Uses: "); int rem = Integer.parseInt(scanner.nextLine());
                     String sql = "UPDATE SkiPass SET RemainingUses=? WHERE PID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, rem);
                         ps.setInt(2, pid);
                         ps.executeUpdate();
                     }
                     System.out.println("SkiPass updated.");
                     break;
                 }
                 case "4": { // Delete
                     System.out.print("PID to delete: "); int pid = Integer.parseInt(scanner.nextLine());
                     String sql = "DELETE FROM SkiPass WHERE PID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, pid);
                         int cnt = ps.executeUpdate();
                         System.out.println(cnt>0?"SkiPass deleted.":"No SkiPass found.");
                     }
                     break;
                 }
                 case "5": { // ByID
                     System.out.print("PID: "); int pid = Integer.parseInt(scanner.nextLine());
                     String sql = "SELECT * FROM SkiPass WHERE PID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, pid);
                         try (ResultSet rs = ps.executeQuery()) {
                             if (rs.next()) {
                                 System.out.printf("%d | %d | %s | %s | %d | %d | %s%n",
                                     rs.getInt("PID"), rs.getInt("MID"), rs.getString("PassType"),
                                     rs.getTimestamp("PurchaseDateTime"), rs.getInt("TotalUses"), rs.getInt("RemainingUses"), rs.getString("IsValid"));
                             } else {
                                 System.out.println("No SkiPass found.");
                             }
                         }
                     }
                     break;
                 }
                 case "0": return;
                 default: System.out.println("Invalid choice.");
                 }
             } catch (SQLException e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
     }
 
     private void equipmentMenu() {
         while (true) {
             System.out.println("-- Equipment -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
             System.out.print("> ");
             String c = scanner.nextLine().trim();
             try {
                 switch (c) {
                 case "1": { // Add
                     System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                     System.out.print("Type: "); String type = scanner.nextLine();
                     System.out.print("SizeOrLength: "); double sz = Double.parseDouble(scanner.nextLine());
                     System.out.print("Status: "); String status = scanner.nextLine();
                     String sql = "INSERT INTO Equipment (EID,Type,SizeOrLength,Status) VALUES (?,?,?,?)";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, eid);
                         ps.setString(2, type);
                         ps.setDouble(3, sz);
                         ps.setString(4, status);
                         ps.executeUpdate();
                     }
                     System.out.println("Equipment added.");
                     break;
                 }
                 case "2": { // List
                     String sql = "SELECT EID,Type,SizeOrLength,Status FROM Equipment";
                     try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                         System.out.println("EID | Type | Size | Status");
                         while (rs.next()) {
                             System.out.printf("%d | %s | %s | %s%n",
                                 rs.getInt("EID"), rs.getString("Type"), rs.getString("SizeOrLength"), rs.getString("Status"));
                         }
                     }
                     break;
                 }
                 case "3": { // Update
                     System.out.print("EID to update: "); int eid = Integer.parseInt(scanner.nextLine());
                     System.out.print("New Status: "); String status = scanner.nextLine();
                     String sql = "UPDATE Equipment SET Status=? WHERE EID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setString(1, status);
                         ps.setInt(2, eid);
                         ps.executeUpdate();
                     }
                     System.out.println("Equipment updated.");
                     break;
                 }
                 case "4": { // Delete
                     System.out.print("EID to delete: "); int eid = Integer.parseInt(scanner.nextLine());
                     String sql = "DELETE FROM Equipment WHERE EID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, eid);
                         int cnt = ps.executeUpdate();
                         System.out.println(cnt>0?"Equipment deleted.":"No equipment found.");
                     }
                     break;
                 }
                 case "5": { // ByID
                     System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                     String sql = "SELECT * FROM Equipment WHERE EID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, eid);
                         try (ResultSet rs = ps.executeQuery()) {
                             if (rs.next()) {
                                 System.out.printf("%d | %s | %s | %s%n",
                                     rs.getInt("EID"), rs.getString("Type"), rs.getString("SizeOrLength"), rs.getString("Status"));
                             } else {
                                 System.out.println("No equipment found.");
                             }
                         }
                     }
                     break;
                 }
                 case "0": return;
                 default: System.out.println("Invalid choice.");
                 }
             } catch (SQLException e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
     }
 
     private void rentalMenu() {
         while (true) {
             System.out.println("-- Equipment Rental -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
             System.out.print("> ");
             String c = scanner.nextLine().trim();
             try {
                 switch (c) {
                 case "1": { // Add
                     System.out.print("RID: "); int rid = Integer.parseInt(scanner.nextLine());
                     System.out.print("MID: "); int mid = Integer.parseInt(scanner.nextLine());
                     System.out.print("PID: "); int pid = Integer.parseInt(scanner.nextLine());
                     System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                     System.out.print("Status: "); String status = scanner.nextLine();
                     String sql = "INSERT INTO EquipmentRental (RID,MID,PID,EID,Status) VALUES (?,?,?,?,?)";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, rid);
                         ps.setInt(2, mid);
                         ps.setInt(3, pid);
                         ps.setInt(4, eid);
                         ps.setString(5, status);
                         ps.executeUpdate();
                     }
                     System.out.println("Rental added.");
                     break;
                 }
                 case "2": { // List
                     String sql = "SELECT RID,MID,PID,EID,Status FROM EquipmentRental";
                     try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                         System.out.println("RID | MID | PID | EID | Status");
                         while (rs.next()) {
                             System.out.printf("%d | %d | %d | %d | %s%n",
                                 rs.getInt("RID"), rs.getInt("MID"), rs.getInt("PID"), rs.getInt("EID"), rs.getString("Status"));
                         }
                     }
                     break;
                 }
                 case "3": { // Update
                     System.out.print("RID to update: "); int rid = Integer.parseInt(scanner.nextLine());
                     System.out.print("New Status: "); String status = scanner.nextLine();
                     String sql = "UPDATE EquipmentRental SET Status=? WHERE RID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setString(1, status);
                         ps.setInt(2, rid);
                         ps.executeUpdate();
                     }
                     System.out.println("Rental updated.");
                     break;
                 }
                 case "4": { // Delete
                     System.out.print("RID to delete: "); int rid = Integer.parseInt(scanner.nextLine());
                     String sql = "DELETE FROM EquipmentRental WHERE RID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, rid);
                         int cnt = ps.executeUpdate();
                         System.out.println(cnt>0?"Rental deleted.":"No rental found.");
                     }
                     break;
                 }
                 case "5": { // ByID
                     System.out.print("RID: "); int rid = Integer.parseInt(scanner.nextLine());
                     String sql = "SELECT * FROM EquipmentRental WHERE RID=?";
                     try (PreparedStatement ps = conn.prepareStatement(sql)) {
                         ps.setInt(1, rid);
                         try (ResultSet rs = ps.executeQuery()) {
                             if (rs.next()) {
                                 System.out.printf("%d | %d | %d | %d | %s%n",
                                     rs.getInt("RID"), rs.getInt("MID"), rs.getInt("PID"), rs.getInt("EID"), rs.getString("Status"));
                             } else {
                                 System.out.println("No rental found.");
                             }
                         }
                     }
                     break;
                 }
                 case "0": return;
                 default: System.out.println("Invalid choice.");
                 }
             } catch (SQLException e) {
                 System.out.println("Error: " + e.getMessage());
             }
         }
     }
     private void lessonMenu() {
        while (true) {
            System.out.println("-- Lesson Purchase -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("OID: "); int oid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Member ID: "); int mid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Lesson ID: "); int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Number of Sessions: "); int num = Integer.parseInt(scanner.nextLine());
                    String sql = "INSERT INTO LessonOrder (OID,MID,LID,NumberOfSessions,SessionsLeft,PurchaseDate) " +
                                 "VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        ps.setInt(2, mid);
                        ps.setInt(3, lid);
                        ps.setInt(4, num);
                        ps.setInt(5, num);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson order added.");
                    break;
                }
                case "2": { // List
                    String sql = "SELECT OID,MID,LID,NumberOfSessions,SessionsLeft,PurchaseDate FROM LessonOrder";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("OID | MID | LID | Num | Left | Date");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %d | %d | %d | %s%n",
                                rs.getInt("OID"), rs.getInt("MID"), rs.getInt("LID"),
                                rs.getInt("NumberOfSessions"), rs.getInt("SessionsLeft"), rs.getTimestamp("PurchaseDate"));
                        }
                    }
                    break;
                }
                case "3": { // Update
                    System.out.print("OID to update: "); int oid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Sessions Left: "); int sl = Integer.parseInt(scanner.nextLine());
                    String sql = "UPDATE LessonOrder SET SessionsLeft=? WHERE OID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, sl);
                        ps.setInt(2, oid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson order updated.");
                    break;
                }
                case "4": { // Delete
                    System.out.print("OID to delete: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM LessonOrder WHERE OID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0?"Lesson order deleted.":"No order found.");
                    }
                    break;
                }
                case "5": { // ByID
                    System.out.print("OID: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM LessonOrder WHERE OID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %d | %d | %d | %d | %s%n",
                                    rs.getInt("OID"), rs.getInt("MID"), rs.getInt("LID"),
                                    rs.getInt("NumberOfSessions"), rs.getInt("SessionsLeft"), rs.getTimestamp("PurchaseDate"));
                            } else {
                                System.out.println("No lesson order found.");
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void instructorMenu() {
        while (true) {
            System.out.println("-- Instructor -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("IID: "); int iid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Employee ID: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Certification: "); String cert = scanner.nextLine();
                    String sql = "INSERT INTO Instructor (IID,EID,Certification) VALUES (?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, iid);
                        ps.setInt(2, eid);
                        ps.setString(3, cert);
                        ps.executeUpdate();
                    }
                    System.out.println("Instructor added.");
                    break;
                }
                case "2": { // List
                    String sql = "SELECT IID,EID,Certification FROM Instructor";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("IID | EID | Certification");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %s%n",
                                rs.getInt("IID"), rs.getInt("EID"), rs.getString("Certification"));
                        }
                    }
                    break;
                }
                case "3": { // Update
                    System.out.print("IID to update: "); int iid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Employee ID: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Certification: "); String cert = scanner.nextLine();
                    String sql = "UPDATE Instructor SET EID=?,Certification=? WHERE IID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        ps.setString(2, cert);
                        ps.setInt(3, iid);
                        ps.executeUpdate();
                    }
                    System.out.println("Instructor updated.");
                    break;
                }
                case "4": { // Delete
                    System.out.print("IID to delete: "); int iid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Instructor WHERE IID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, iid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0?"Instructor deleted.":"No instructor found.");
                    }
                    break;
                }
                case "5": { // ByID
                    System.out.print("IID: "); int iid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Instructor WHERE IID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, iid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %d | %s%n",
                                    rs.getInt("IID"), rs.getInt("EID"), rs.getString("Certification"));
                            } else {
                                System.out.println("No instructor found.");
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private void employeeMenu() {
        while (true) {
            System.out.println("-- Employee -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Property ID: "); int pid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Position: "); String pos = scanner.nextLine();
                    System.out.print("Demographics: "); String demo = scanner.nextLine();
                    System.out.print("Start Date (YYYY-MM-DD): "); Date sd = Date.valueOf(scanner.nextLine());
                    System.out.print("Monthly Salary: "); BigDecimal sal = new BigDecimal(scanner.nextLine());
                    String sql = "INSERT INTO Employee (EID,PropertyID,Name,Position,Demographics,StartDate,MonthlySalary) VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        ps.setInt(2, pid);
                        ps.setString(3, name);
                        ps.setString(4, pos);
                        ps.setString(5, demo);
                        ps.setDate(6, sd);
                        ps.setBigDecimal(7, sal);
                        ps.executeUpdate();
                    }
                    System.out.println("Employee added.");
                    break;
                }
                case "2": { // List
                    String sql = "SELECT EID,PropertyID,Name,Position,Demographics,StartDate,MonthlySalary FROM Employee";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("EID | PID | Name | Position | Demo | Start | Salary");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %s | %s | %s | %s | %s%n",
                                rs.getInt("EID"), rs.getInt("PropertyID"), rs.getString("Name"),
                                rs.getString("Position"), rs.getString("Demographics"),
                                rs.getDate("StartDate"), rs.getBigDecimal("MonthlySalary"));
                        }
                    }
                    break;
                }
                case "3": { // Update
                    System.out.print("EID to update: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Position: "); String pos = scanner.nextLine();
                    System.out.print("New Demographics: "); String demo = scanner.nextLine();
                    String sql = "UPDATE Employee SET Position=?,Demographics=? WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, pos);
                        ps.setString(2, demo);
                        ps.setInt(3, eid);
                        ps.executeUpdate();
                    }
                    System.out.println("Employee updated.");
                    break;
                }
                case "4": { // Delete
                    System.out.print("EID to delete: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Employee WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0?"Employee deleted.":"No employee found.");
                    }
                    break;
                }
                case "5": { // ByID
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Employee WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %d | %s | %s | %s | %s | %s%n",
                                    rs.getInt("EID"), rs.getInt("PropertyID"), rs.getString("Name"),
                                    rs.getString("Position"), rs.getString("Demographics"),
                                    rs.getDate("StartDate"), rs.getBigDecimal("MonthlySalary"));
                            } else {
                                System.out.println("No employee found.");
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void trailMenu() {
        while (true) {
            System.out.println("-- Trail -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {
                    System.out.print("TID: "); int tid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Name: "); String nm = scanner.nextLine();
                    System.out.print("Start Loc: "); String st = scanner.nextLine();
                    System.out.print("End Loc: "); String en = scanner.nextLine();
                    System.out.print("Status (Open/Closed): "); String ss = scanner.nextLine();
                    System.out.print("Difficulty: "); String df = scanner.nextLine();
                    System.out.print("Category: "); String ct = scanner.nextLine();
                    String sql = "INSERT INTO Trail (TID,Name,StartLocation,EndLocation,Status,Difficulty,Category) VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, tid);
                        ps.setString(2, nm);
                        ps.setString(3, st);
                        ps.setString(4, en);
                        ps.setString(5, ss);
                        ps.setString(6, df);
                        ps.setString(7, ct);
                        ps.executeUpdate();
                    }
                    System.out.println("Trail added.");
                    break;
                }
                case "2": {
                    String sql = "SELECT * FROM Trail";
                    try (Statement stt = conn.createStatement(); ResultSet rs = stt.executeQuery(sql)) {
                        System.out.println("TID | Name | Start | End | Status | Diff | Cat");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                                rs.getInt("TID"), rs.getString("Name"), rs.getString("StartLocation"),
                                rs.getString("EndLocation"), rs.getString("Status"), rs.getString("Difficulty"), rs.getString("Category"));
                        }
                    }
                    break;
                }
                case "3": {
                    System.out.print("TID to update: "); int tid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Status: "); String ss = scanner.nextLine();
                    String sql = "UPDATE Trail SET Status=? WHERE TID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, ss);
                        ps.setInt(2, tid);
                        ps.executeUpdate();
                    }
                    System.out.println("Trail updated.");
                    break;
                }
                case "4": {
                    System.out.print("TID to delete: "); int tid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Trail WHERE TID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, tid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0?"Trail deleted.":"No trail found.");
                    }
                    break;
                }
                case "5": {
                    System.out.print("TID: "); int tid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Trail WHERE TID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, tid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                                    rs.getInt("TID"), rs.getString("Name"), rs.getString("StartLocation"),
                                    rs.getString("EndLocation"), rs.getString("Status"), rs.getString("Difficulty"), rs.getString("Category"));
                            } else {
                                System.out.println("No trail found.");
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void liftMenu() {
        while (true) {
            System.out.println("-- Lift -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {
                    System.out.print("LiftID: "); int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Name: "); String nm = scanner.nextLine();
                    System.out.print("OpenTime (HH24:MI): "); String ot = scanner.nextLine();
                    System.out.print("CloseTime: "); String ct = scanner.nextLine();
                    System.out.print("Status: "); String ss = scanner.nextLine();
                    System.out.print("AbilityLevel: "); String al = scanner.nextLine();
                    String sql = "INSERT INTO Lift (LiftID,Name,OpenTime,CloseTime,Status,AbilityLevel) VALUES (?,?,?,?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        ps.setString(2, nm);
                        ps.setString(3, ot);
                        ps.setString(4, ct);
                        ps.setString(5, ss);
                        ps.setString(6, al);
                        ps.executeUpdate();
                    }
                    System.out.println("Lift added.");
                    break;
                }
                case "2": {
                    String sql = "SELECT * FROM Lift";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("LID | Name | Open | Close | Status | Level");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %s | %s | %s%n",
                                rs.getInt("LiftID"), rs.getString("Name"), rs.getString("OpenTime"),
                                rs.getString("CloseTime"), rs.getString("Status"), rs.getString("AbilityLevel"));
                        }
                    }
                    break;
                }
                case "3": {
                    System.out.print("LiftID to update: "); int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Status: "); String ss = scanner.nextLine();
                    String sql = "UPDATE Lift SET Status=? WHERE LiftID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, ss);
                        ps.setInt(2, lid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lift updated.");
                    break;
                }
                case "4": {
                    System.out.print("LiftID to delete: "); int lid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Lift WHERE LiftID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0?"Lift deleted.":"No lift found.");
                    }
                    break;
                }
                case "5": {
                    System.out.print("LiftID: "); int lid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Lift WHERE LiftID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %s | %s | %s | %s | %s%n",
                                    rs.getInt("LiftID"), rs.getString("Name"), rs.getString("OpenTime"),
                                    rs.getString("CloseTime"), rs.getString("Status"), rs.getString("AbilityLevel"));
                            } else {
                                System.out.println("No lift found.");
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void liftUsageMenu() {
        while (true) {
            System.out.println("-- Lift Usage -- 1)Add 2)List 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {
                    System.out.print("UID: "); int uid = Integer.parseInt(scanner.nextLine());
                    System.out.print("PassID: "); int pid = Integer.parseInt(scanner.nextLine());
                    System.out.print("LiftID: "); int lid = Integer.parseInt(scanner.nextLine());
                    String sql = "INSERT INTO LiftUsage (UID,PID,LiftID,DateTimeOfUse) VALUES (?,?,?,CURRENT_TIMESTAMP)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, uid);
                        ps.setInt(2, pid);
                        ps.setInt(3, lid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lift usage recorded.");
                    break;
                }
                case "2": {
                    String sql = "SELECT UID,PID,LiftID,DateTimeOfUse FROM LiftUsage";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("UID | PID | LiftID | DateTime");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %d | %s%n",
                                rs.getInt("UID"), rs.getInt("PID"), rs.getInt("LiftID"), rs.getTimestamp("DateTimeOfUse"));
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void queryMenu() {
        while (true) {
            System.out.println("--- Queries ---");
            System.out.println("1) Member lessons");
            System.out.println("2) Pass history");
            System.out.println("3) Open intermediate trails + lifts");
            System.out.println("4) Custom: instructor lessons & members");
            System.out.println("0) Back");
            System.out.print("> ");
            String q = scanner.nextLine().trim();
            try {
                switch (q) {
                case "1": {
                    System.out.print("Member ID: "); int mid = Integer.parseInt(scanner.nextLine());
                    String sql ="";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("LID | Left | Cert | Time");
                            while (rs.next()) {
                                System.out.printf("%d | %d | %s | %s%n",
                                    rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4));
                            }
                        }
                    }
                    break;
                }
                case "2": {
                    System.out.print("Pass ID: "); int pid = Integer.parseInt(scanner.nextLine());
                    String sql ="";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid); ps.setInt(2, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("Type | ID | Time");
                            while (rs.next()) {
                                System.out.printf("%s | %d | %s%n",
                                    rs.getString(1), rs.getInt(2), rs.getTimestamp(3));
                            }
                        }
                    }
                    break;
                }
                case "3": {
                    String sql ="";
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("TID | Trail | Cat | LiftID | LiftName");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %d | %s%n",
                                rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
                        }
                    }
                    break;
                }
                case "4": {
                    System.out.print("Instructor ID: "); int iid = Integer.parseInt(scanner.nextLine());
                    String sql ="";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, iid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("LID | Type | Member | Left");
                            while (rs.next()) {
                                System.out.printf("%d | %s | %s | %d%n",
                                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                            }
                        }
                    }
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid query option");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}


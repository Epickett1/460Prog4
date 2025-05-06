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
import java.time.Instant;
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
                     case "12": propertyMenu(); break;
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
         System.out.println("1) Member ");
         System.out.println("2) SkiPass ");
         System.out.println("3) Equipment ");
         System.out.println("4) Rental ");
         System.out.println("5) Lesson Purchase ");
         System.out.println("6) Instructor ");
         System.out.println("7) Employee ");
         System.out.println("8) Trail ");
         System.out.println("9) Lift ");
         System.out.println("10) Lift Usage ");
         System.out.println("11) Queries");
         System.out.println("12) Property ");
         System.out.println("0) Exit");
         System.out.print("> ");
     }
 
    private void memberMenu() {
        while (true) {
            System.out.println("-- Member -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Create
                    System.out.print("MID: ");
                    int mid = Integer.parseInt(scanner.nextLine());
                    System.out.print("First Name: ");
                    String first = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String last = scanner.nextLine();
                    System.out.print("Phone Number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Email Address: ");
                    String email = scanner.nextLine();
                    System.out.print("Date of Birth (yyyy-mm-dd): ");
                    Date dob = Date.valueOf(scanner.nextLine());
                    System.out.print("Emergency Contact: ");
                    String ec = scanner.nextLine();

                    String sql = """
                        INSERT INTO Member
                        (MID, FirstName, LastName,
                        PhoneNumber, EmailAddress,
                        DateOfBirth, EmergencyContact)
                        VALUES (?,?,?,?,?,?,?)
                    """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, mid);
                        ps.setString(2, first);
                        ps.setString(3, last);
                        ps.setString(4, phone);
                        ps.setString(5, email);
                        ps.setDate(6, dob);
                        ps.setString(7, ec);
                        ps.executeUpdate();
                    }
                    System.out.println("Member added.");
                } break;

                case "2": { // List all
                    String sql = """
                        SELECT MID, FirstName, LastName,
                            PhoneNumber, EmailAddress,
                            DateOfBirth, EmergencyContact
                        FROM Member
                        """;
                    try (Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("MID | First | Last | Phone | Email | DOB | Emergency");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                            rs.getInt("MID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("EmailAddress"),
                            rs.getDate("DateOfBirth"),
                            rs.getString("EmergencyContact"));
                        }
                    }
                } break;

                case "3": { // Update
                    System.out.print("MID to update: ");
                    int mid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New First Name: ");
                    String first = scanner.nextLine();
                    System.out.print("New Last Name: ");
                    String last = scanner.nextLine();
                    System.out.print("New Phone Number: ");
                    String phone = scanner.nextLine();
                    System.out.print("New Email Address: ");
                    String email = scanner.nextLine();
                    System.out.print("New DOB (yyyy-mm-dd): ");
                    Date dob = Date.valueOf(scanner.nextLine());
                    System.out.print("New Emergency Contact: ");
                    String ec = scanner.nextLine();

                    String sql = """
                        UPDATE Member
                        SET FirstName=?, LastName=?,
                            PhoneNumber=?, EmailAddress=?,
                            DateOfBirth=?, EmergencyContact=?
                        WHERE MID=?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, first);
                        ps.setString(2, last);
                        ps.setString(3, phone);
                        ps.setString(4, email);
                        ps.setDate(5, dob);
                        ps.setString(6, ec);
                        ps.setInt(7, mid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Member updated." : "No such MID.");
                    }
                } break;

                case "4": { // Delete with pre‐checks
                    System.out.print("MID to delete: ");
                    int mid = Integer.parseInt(scanner.nextLine());

                    // 1) Active ski‐passes?
                    String passCheck =
                    "SELECT COUNT(*) FROM SkiPass " +
                    "WHERE MID = ? AND (RemainingUses > 0 OR ExpirationDate > SYSTIMESTAMP)";
                    try (PreparedStatement ps = conn.prepareStatement(passCheck)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            if (rs.getInt(1) > 0) {
                                System.out.println("Cannot delete: member has active ski-passes.");
                                break;
                            }
                        }
                    }

                    // 2) Open equipment rentals? (Status = 0 ⇒ out)
                    String rentCheck =
                    "SELECT COUNT(*) FROM EquipmentRental WHERE MID = ? AND Status = 0";
                    try (PreparedStatement ps = conn.prepareStatement(rentCheck)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            if (rs.getInt(1) > 0) {
                                System.out.println("Cannot delete: member has open equipment rentals.");
                                break;
                            }
                        }
                    }

                    // 3) Unused lesson sessions?
                    String lessonCheck =
                    "SELECT COUNT(*) FROM LessonOrder WHERE MID = ? AND SessionsLeft > 0";
                    try (PreparedStatement ps = conn.prepareStatement(lessonCheck)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            if (rs.getInt(1) > 0) {
                                System.out.println("Cannot delete: member has unused lesson sessions.");
                                break;
                            }
                        }
                    }

                    // All clear ⇒ delete
                    String delSql = "DELETE FROM Member WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delSql)) {
                        ps.setInt(1, mid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Member deleted." : "No such MID.");
                    }
                } break;

                case "5": { // By ID
                    System.out.print("MID: ");
                    int mid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Member WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                "%d | %s | %s | %s | %s | %s | %s%n",
                                rs.getInt("MID"),
                                rs.getString("FirstName"),
                                rs.getString("LastName"),
                                rs.getString("PhoneNumber"),
                                rs.getString("EmailAddress"),
                                rs.getDate("DateOfBirth"),
                                rs.getString("EmergencyContact")
                                );
                            } else {
                                System.out.println("No member found.");
                            }
                        }
                    }
                } break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void skiPassMenu() {
        while (true) {
            System.out.println("-- SkiPass -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("PID: ");       int pid   = Integer.parseInt(scanner.nextLine());
                    System.out.print("Total Uses: ");int total = Integer.parseInt(scanner.nextLine());
                    System.out.print("Remaining Uses: "); int rem = Integer.parseInt(scanner.nextLine());
                    System.out.print("Purchase Time (yyyy-MM-dd HH:mm:ss): ");
                    Timestamp pt = Timestamp.valueOf(scanner.nextLine());
                    System.out.print("Expiration (yyyy-MM-dd): ");
                    Date exp = Date.valueOf(scanner.nextLine());
                    System.out.print("Price: ");      BigDecimal price = new BigDecimal(scanner.nextLine());
                    System.out.print("Pass Type: ");  String type = scanner.nextLine();
                    System.out.print("Member ID: ");  int mid   = Integer.parseInt(scanner.nextLine());
                    System.out.print("IsValid (1=Y,0=N): ");
                    int valid = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO SkiPass
                          (PID, TotalUses, RemainingUses,
                           PurchaseDateTime, ExpirationDate,
                           Price, PassType, MID, IsValid)
                        VALUES (?,?,?,?,?,?,?,?,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        ps.setInt(2, total);
                        ps.setInt(3, rem);
                        ps.setTimestamp(4, pt);
                        ps.setDate(5, exp);
                        ps.setBigDecimal(6, price);
                        ps.setString(7, type);
                        ps.setInt(8, mid);
                        ps.setInt(9, valid);
                        ps.executeUpdate();
                    }
                    System.out.println("SkiPass added.");
                    break;
                }
                case "2": { // List all
                    String sql = """
                        SELECT PID, MID, PassType,
                               PurchaseDateTime, ExpirationDate,
                               TotalUses, RemainingUses, Price, IsValid
                          FROM SkiPass
                        """;
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("PID | MID | Type | PurchaseTime        | ExpDate    | Tot | Rem | Price | Valid");
                        while (rs.next()) {
                            System.out.printf(
                              "%d | %d | %s | %s | %s | %d | %d | %s | %d%n",
                              rs.getInt("PID"),
                              rs.getInt("MID"),
                              rs.getString("PassType"),
                              rs.getTimestamp("PurchaseDateTime"),
                              rs.getDate("ExpirationDate"),
                              rs.getInt("TotalUses"),
                              rs.getInt("RemainingUses"),
                              rs.getBigDecimal("Price"),
                              rs.getInt("IsValid")
                            );
                        }
                    }
                    break;
                }
                case "3": { // Update remaining uses
                    System.out.print("PID to update: "); int pid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Remaining Uses: "); int rem = Integer.parseInt(scanner.nextLine());
                    String sql = "UPDATE SkiPass SET RemainingUses=? WHERE PID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, rem);
                        ps.setInt(2, pid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "SkiPass updated." : "No such PID.");
                    }
                    break;
                }
                case "4": { // Delete
                    System.out.print("PID to delete: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    String check = 
                    "SELECT RemainingUses, ExpirationDate FROM SkiPass WHERE PID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(check)) {
                        ps.setInt(1, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                System.out.println("No such PID.");
                            } else {
                                int rem = rs.getInt("RemainingUses");
                                Timestamp exp = rs.getTimestamp("ExpirationDate");
                                if (rem == 0 && exp.before(Timestamp.from(Instant.now()))) {
                                    // safe to delete
                                    try (PreparedStatement del = conn.prepareStatement("DELETE FROM SkiPass WHERE PID = ?")) {
                                    del.setInt(1, pid);
                                    del.executeUpdate();
                                    System.out.println("SkiPass deleted.");
                                }
                                } else {
                                    System.out.println("Cannot delete: pass must be expired AND have zero uses.");
                                }
                            }
                        }
                    }
                } break;
                case "5": { // By ID
                    System.out.print("PID: "); int pid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM SkiPass WHERE PID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                  "%d | %d | %s | %s | %s | %d | %d | %s | %d%n",
                                  rs.getInt("PID"),
                                  rs.getInt("MID"),
                                  rs.getString("PassType"),
                                  rs.getTimestamp("PurchaseDateTime"),
                                  rs.getDate("ExpirationDate"),
                                  rs.getInt("TotalUses"),
                                  rs.getInt("RemainingUses"),
                                  rs.getBigDecimal("Price"),
                                  rs.getInt("IsValid")
                                );
                            } else {
                                System.out.println("No SkiPass found.");
                            }
                        }
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
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
                    System.out.print("EID: ");           int eid      = Integer.parseInt(scanner.nextLine());
                    System.out.print("Type (numeric code): ");   int type     = Integer.parseInt(scanner.nextLine());
                    System.out.print("Status (numeric): ");      int status   = Integer.parseInt(scanner.nextLine());
                    System.out.print("SizeOrLength: ");          double size   = Double.parseDouble(scanner.nextLine());
                    System.out.print("IsArchived (0/1): ");      int archived = Integer.parseInt(scanner.nextLine());
                    System.out.print("ChangeID: ");              int cid      = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO Equipment
                          (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
                        VALUES (?,?,?,?,?,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        ps.setInt(2, type);
                        ps.setInt(3, status);
                        ps.setDouble(4, size);
                        ps.setInt(5, archived);
                        ps.setInt(6, cid);
                        ps.executeUpdate();
                    }
                    System.out.println("Equipment added.");
                    break;
                }
                case "2": { // List all
                    String sql = """
                        SELECT EID, Type, Status,
                               SizeOrLength, IsArchived, ChangeID
                          FROM Equipment
                        """;
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("EID | Type | Status | Size | Archived | ChangeID");
                        while (rs.next()) {
                            System.out.printf(
                              "%d | %d | %d | %.1f | %d | %d%n",
                              rs.getInt("EID"),
                              rs.getInt("Type"),
                              rs.getInt("Status"),
                              rs.getDouble("SizeOrLength"),
                              rs.getInt("IsArchived"),
                              rs.getInt("ChangeID")
                            );
                        }
                    }
                    break;
                }
                case "3": { // Update status & archived flag
                    System.out.print("EID to update: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Status (numeric): ");     int status   = Integer.parseInt(scanner.nextLine());
                    System.out.print("New IsArchived (0/1): ");     int archived = Integer.parseInt(scanner.nextLine());
    
                    String sql = "UPDATE Equipment SET Status=?, IsArchived=? WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, status);
                        ps.setInt(2, archived);
                        ps.setInt(3, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Equipment updated." : "No such EID.");
                    }
                    break;
                }
                case "4": { // Delete
                    System.out.print("EID to delete: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Equipment WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Equipment deleted." : "No such EID.");
                    }
                    break;
                }
                case "5": { // By ID
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Equipment WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                  "%d | %d | %d | %.1f | %d | %d%n",
                                  rs.getInt("EID"),
                                  rs.getInt("Type"),
                                  rs.getInt("Status"),
                                  rs.getDouble("SizeOrLength"),
                                  rs.getInt("IsArchived"),
                                  rs.getInt("ChangeID")
                                );
                            } else {
                                System.out.println("No equipment found.");
                            }
                        }
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
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
                case "1": { // Add (uses current timestamp)
                    System.out.print("RID: "); int rid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Member ID (MID): ");  int mid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Pass ID (PID): ");    int pid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Equipment ID (EID): "); int eid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Status (numeric): ");  int status = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO EquipmentRental
                          (RID, MID, PID, EID, RentalDateTime, Status)
                        VALUES (?,?,?,?,CURRENT_TIMESTAMP,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, rid);
                        ps.setInt(2, mid);
                        ps.setInt(3, pid);
                        ps.setInt(4, eid);
                        ps.setInt(5, status);
                        ps.executeUpdate();
                    }
                    System.out.println("Rental added.");
                    break;
                }
                case "2": { // List all
                    String sql = """
                        SELECT RID, MID, PID, EID,
                               RentalDateTime, Status
                          FROM EquipmentRental
                        """;
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("RID | MID | PID | EID | Time                    | Status");
                        while (rs.next()) {
                            System.out.printf(
                              "%d | %d | %d | %d | %s | %d%n",
                              rs.getInt("RID"),
                              rs.getInt("MID"),
                              rs.getInt("PID"),
                              rs.getInt("EID"),
                              rs.getTimestamp("RentalDateTime"),
                              rs.getInt("Status")
                            );
                        }
                    }
                    break;
                }
                case "3": { // Update status
                    System.out.print("RID to update: "); int rid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Status (numeric): "); int status = Integer.parseInt(scanner.nextLine());
                    String sql = "UPDATE EquipmentRental SET Status=? WHERE RID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, status);
                        ps.setInt(2, rid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Rental updated." : "No such RID.");
                    }
                    break;
                }
                case "4": { // Delete
                    System.out.print("RID to delete: "); int rid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM EquipmentRental WHERE RID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, rid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Rental deleted." : "No such RID.");
                    }
                    break;
                }
                case "5": { // By ID
                    System.out.print("RID: "); int rid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM EquipmentRental WHERE RID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, rid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                  "%d | %d | %d | %d | %s | %d%n",
                                  rs.getInt("RID"),
                                  rs.getInt("MID"),
                                  rs.getInt("PID"),
                                  rs.getInt("EID"),
                                  rs.getTimestamp("RentalDateTime"),
                                  rs.getInt("Status")
                                );
                            } else {
                                System.out.println("No rental found.");
                            }
                        }
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }    
    private void lessonMenu() {
        while (true) {
            System.out.println("-- Lesson Purchase  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("OID: "); int oid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Member ID: "); int mid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Lesson ID: "); int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Number of Sessions: "); int num = Integer.parseInt(scanner.nextLine());
                    String sql = 
                      "INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate) " +
                      "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        ps.setInt(2, mid);
                        ps.setInt(3, lid);
                        ps.setInt(4, num);
                        ps.setInt(5, num);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson order added.");
                } break;
                case "2": { // List
                    String sql = 
                      "SELECT OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate " +
                      "FROM LessonOrder";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("OID | MID | LID | Num | Left | Date");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %d | %d | %d | %s%n",
                              rs.getInt("OID"),
                              rs.getInt("MID"),
                              rs.getInt("LID"),
                              rs.getInt("NumberOfSessions"),
                              rs.getInt("SessionsLeft"),
                              rs.getTimestamp("PurchaseDate"));
                        }
                    }
                } break;
                case "3": { // Update
                    System.out.print("OID to update: "); int oid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Sessions Left: "); int sl = Integer.parseInt(scanner.nextLine());
                    String sql = "UPDATE LessonOrder SET SessionsLeft = ? WHERE OID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, sl);
                        ps.setInt(2, oid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson order updated.");
                } break;
                case "4": { // Delete
                    System.out.print("OID to delete: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM LessonOrder WHERE OID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt > 0 ? "Lesson order deleted." : "No order found.");
                    }
                } break;
                case "5": { // By ID
                    System.out.print("OID: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM LessonOrder WHERE OID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %d | %d | %d | %d | %s%n",
                                  rs.getInt("OID"),
                                  rs.getInt("MID"),
                                  rs.getInt("LID"),
                                  rs.getInt("NumberOfSessions"),
                                  rs.getInt("SessionsLeft"),
                                  rs.getTimestamp("PurchaseDate"));
                            } else {
                                System.out.println("No lesson order found.");
                            }
                        }
                    }
                } break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void instructorMenu() {
    while (true) {
        System.out.println("-- Instructor  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
        System.out.print("> ");
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
            case "1": {  // Add
                System.out.print("IID: "); int iid = Integer.parseInt(scanner.nextLine());
                System.out.print("Employee ID: "); int eid = Integer.parseInt(scanner.nextLine());
                System.out.print("Certification (1/2/3): "); int cert = Integer.parseInt(scanner.nextLine());
                String sql = "INSERT INTO Instructor (IID, EID, Certification) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, iid);
                    ps.setInt(2, eid);
                    ps.setInt(3, cert);
                    ps.executeUpdate();
                }
                System.out.println("Instructor added.");
            } break;

            case "2": {  // List all
                String sql = "SELECT IID, EID, Certification FROM Instructor";
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {
                    System.out.println("IID | EID | Certification");
                    while (rs.next()) {
                        System.out.printf("%d | %d | %d%n",
                            rs.getInt("IID"),
                            rs.getInt("EID"),
                            rs.getInt("Certification"));
                    }
                }
            } break;

            case "3": {  // Update
                System.out.print("IID to update: "); int iid = Integer.parseInt(scanner.nextLine());
                System.out.print("New Employee ID: "); int eid = Integer.parseInt(scanner.nextLine());
                System.out.print("New Certification (1/2/3): "); int cert = Integer.parseInt(scanner.nextLine());
                String sql = "UPDATE Instructor SET EID = ?, Certification = ? WHERE IID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, eid);
                    ps.setInt(2, cert);
                    ps.setInt(3, iid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Instructor updated." : "No such IID.");
                }
            } break;

            case "4": {  // Delete
                System.out.print("IID to delete: "); int iid = Integer.parseInt(scanner.nextLine());
                String sql = "DELETE FROM Instructor WHERE IID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, iid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Instructor deleted." : "No such IID.");
                }
            } break;

            case "5": {  // By ID
                System.out.print("IID: "); int iid = Integer.parseInt(scanner.nextLine());
                String sql = "SELECT * FROM Instructor WHERE IID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, iid);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.printf("%d | %d | %d%n",
                                rs.getInt("IID"),
                                rs.getInt("EID"),
                                rs.getInt("Certification"));
                        } else {
                            System.out.println("No instructor found.");
                        }
                    }
                }
            } break;

            case "0":
                return;

            default:
                System.out.println("Invalid choice.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

    private void employeeMenu() {
        while (true) {
            System.out.println("-- Employee  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Property ID: "); int pid = Integer.parseInt(scanner.nextLine());
                    System.out.print("First Name: "); String fn = scanner.nextLine();
                    System.out.print("Last Name: ");  String ln = scanner.nextLine();
                    System.out.print("Position: ");   String pos = scanner.nextLine();
                    System.out.print("Education: "); String edu = scanner.nextLine();
                    System.out.print("Gender: ");    String gen = scanner.nextLine();
                    System.out.print("Age: ");       int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Start Date (yyyy-mm-dd): "); Date sd = Date.valueOf(scanner.nextLine());
                    System.out.print("Monthly Salary: "); BigDecimal sal = new BigDecimal(scanner.nextLine());
                    String sql =
                      "INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        ps.setInt(2, pid);
                        ps.setString(3, fn);
                        ps.setString(4, ln);
                        ps.setString(5, pos);
                        ps.setString(6, edu);
                        ps.setString(7, gen);
                        ps.setInt(8, age);
                        ps.setDate(9, sd);
                        ps.setBigDecimal(10, sal);
                        ps.executeUpdate();
                    }
                    System.out.println("Employee added.");
                } break;
                case "2": { // List
                    String sql =
                      "SELECT EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary " +
                      "FROM Employee";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("EID | PID | First | Last | Pos | Edu | Gen | Age | Start | Salary");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %s | %s | %s | %s | %s | %d | %s | %s%n",
                              rs.getInt("EID"),
                              rs.getInt("PID"),
                              rs.getString("FirstName"),
                              rs.getString("LastName"),
                              rs.getString("Position"),
                              rs.getString("Education"),
                              rs.getString("Gender"),
                              rs.getInt("Age"),
                              rs.getDate("StartDate"),
                              rs.getBigDecimal("MonthlySalary"));
                        }
                    }
                } break;
                case "3": { // Update
                    System.out.print("EID to update: "); int eid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Position: "); String pos = scanner.nextLine();
                    System.out.print("New Education: "); String edu = scanner.nextLine();
                    String sql = 
                      "UPDATE Employee SET Position = ?, Education = ? WHERE EID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, pos);
                        ps.setString(2, edu);
                        ps.setInt(3, eid);
                        ps.executeUpdate();
                    }
                    System.out.println("Employee updated.");
                } break;
                case "4": { // Delete
                    System.out.print("EID to delete: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Employee WHERE EID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt > 0 ? "Employee deleted." : "No employee found.");
                    }
                } break;
                case "5": { // By ID
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Employee WHERE EID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf("%d | %d | %s | %s | %s | %s | %s | %d | %s | %s%n",
                                  rs.getInt("EID"),
                                  rs.getInt("PID"),
                                  rs.getString("FirstName"),
                                  rs.getString("LastName"),
                                  rs.getString("Position"),
                                  rs.getString("Education"),
                                  rs.getString("Gender"),
                                  rs.getInt("Age"),
                                  rs.getDate("StartDate"),
                                  rs.getBigDecimal("MonthlySalary"));
                            } else {
                                System.out.println("No employee found.");
                            }
                        }
                    }
                } break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    

    private void trailMenu() {
    while (true) {
        System.out.println("-- Trail  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
        System.out.print("> ");
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
            case "1": {  // Create
                System.out.print("TID: "); int tid = Integer.parseInt(scanner.nextLine());
                System.out.print("Name: "); String name = scanner.nextLine();
                System.out.print("Start Location: "); String start = scanner.nextLine();
                System.out.print("End Location: "); String end = scanner.nextLine();
                System.out.print("Status (1=open, 0=closed): "); int st = Integer.parseInt(scanner.nextLine());
                System.out.print("Difficulty: "); String diff = scanner.nextLine();
                System.out.print("Category: "); String cat = scanner.nextLine();
                String sql =
                  "INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, tid);
                    ps.setString(2, name);
                    ps.setString(3, start);
                    ps.setString(4, end);
                    ps.setInt(5, st);
                    ps.setString(6, diff);
                    ps.setString(7, cat);
                    ps.executeUpdate();
                }
                System.out.println("Trail added.");
            } break;

            case "2": {  // List all
                String sql = "SELECT TID, Name, StartLocation, EndLocation, Status, Difficulty, Category FROM Trail";
                try (Statement stt = conn.createStatement();
                     ResultSet rs = stt.executeQuery(sql)) {
                    System.out.println("TID | Name | Start | End | Status | Difficulty | Category");
                    while (rs.next()) {
                        int s = rs.getInt("Status");
                        String stat = (s == 1 ? "Open" : "Closed");
                        System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                            rs.getInt("TID"),
                            rs.getString("Name"),
                            rs.getString("StartLocation"),
                            rs.getString("EndLocation"),
                            stat,
                            rs.getString("Difficulty"),
                            rs.getString("Category"));
                    }
                }
            } break;

            case "3": {  // Update status
                System.out.print("TID to update: "); int tid = Integer.parseInt(scanner.nextLine());
                System.out.print("New Status (1=open, 0=closed): "); int st = Integer.parseInt(scanner.nextLine());
                String sql = "UPDATE Trail SET Status = ? WHERE TID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, st);
                    ps.setInt(2, tid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Trail status updated." : "No such TID.");
                }
            } break;

            case "4": {  // Delete
                System.out.print("TID to delete: "); int tid = Integer.parseInt(scanner.nextLine());
                String sql = "DELETE FROM Trail WHERE TID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, tid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Trail deleted." : "No such TID.");
                }
            } break;

            case "5": {  // By ID
                System.out.print("TID: "); int tid = Integer.parseInt(scanner.nextLine());
                String sql = "SELECT * FROM Trail WHERE TID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, tid);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int s = rs.getInt("Status");
                            String stat = (s == 1 ? "Open" : "Closed");
                            System.out.printf("%d | %s | %s | %s | %s | %s | %s%n",
                                rs.getInt("TID"),
                                rs.getString("Name"),
                                rs.getString("StartLocation"),
                                rs.getString("EndLocation"),
                                stat,
                                rs.getString("Difficulty"),
                                rs.getString("Category"));
                        } else {
                            System.out.println("No trail found.");
                        }
                    }
                }
            } break;

            case "0":
                return;
            default:
                System.out.println("Invalid choice.");
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
                case "1": {  // Create
                    System.out.print("LiftID: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Open Time (HH24:MI): ");
                    String open = scanner.nextLine();
                    System.out.print("Close Time (HH24:MI): ");
                    String close = scanner.nextLine();
                    System.out.print("Status (1=open, 0=closed): ");
                    int status = Integer.parseInt(scanner.nextLine());
    
                    String sql = 
                      "INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status) " +
                      "VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        ps.setString(2, name);
                        ps.setString(3, open);
                        ps.setString(4, close);
                        ps.setInt(5, status);
                        ps.executeUpdate();
                    }
                    System.out.println("Lift added.");
                } break;
    
                case "2": {  // List all
                    String sql = 
                      "SELECT LiftID, Name, OpenTime, CloseTime, Status FROM Lift";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("LiftID | Name | Open  | Close | Status");
                        while (rs.next()) {
                            int s = rs.getInt("Status");
                            String stat = (s == 1 ? "Open" : "Closed");
                            System.out.printf("%d | %s | %s | %s | %s%n",
                              rs.getInt("LiftID"),
                              rs.getString("Name"),
                              rs.getString("OpenTime"),
                              rs.getString("CloseTime"),
                              stat);
                        }
                    }
                } break;
    
                case "3": {  // Update status
                    System.out.print("LiftID to update: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Status (1=open, 0=closed): ");
                    int status = Integer.parseInt(scanner.nextLine());
    
                    String sql = "UPDATE Lift SET Status = ? WHERE LiftID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, status);
                        ps.setInt(2, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Lift status updated." : "No such LiftID.");
                    }
                } break;
    
                case "4": {  // Delete
                    System.out.print("LiftID to delete: ");
                    int lid = Integer.parseInt(scanner.nextLine());
    
                    String sql = "DELETE FROM Lift WHERE LiftID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Lift deleted." : "No such LiftID.");
                    }
                } break;
    
                case "5": {  // By ID
                    System.out.print("LiftID: ");
                    int lid = Integer.parseInt(scanner.nextLine());
    
                    String sql = "SELECT * FROM Lift WHERE LiftID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int s = rs.getInt("Status");
                                String stat = (s == 1 ? "Open" : "Closed");
                                System.out.printf("%d | %s | %s | %s | %s%n",
                                  rs.getInt("LiftID"),
                                  rs.getString("Name"),
                                  rs.getString("OpenTime"),
                                  rs.getString("CloseTime"),
                                  stat);
                            } else {
                                System.out.println("No lift found.");
                            }
                        }
                    }
                } break;
    
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }    
    
    private void liftUsageMenu() {
        while (true) {
            System.out.println("-- Lift Usage  -- 1)Add 2)List 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {  // Record a ride
                    System.out.print("UID: ");   int uid  = Integer.parseInt(scanner.nextLine());
                    System.out.print("PassID: ");int pid  = Integer.parseInt(scanner.nextLine());
                    System.out.print("LiftID: ");int lid  = Integer.parseInt(scanner.nextLine());
                    String sql = 
                      "INSERT INTO LiftUsage (UID, PID, LiftID, DateTimeOfUse) " +
                      "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, uid);
                        ps.setInt(2, pid);
                        ps.setInt(3, lid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lift usage recorded.");
                } break;
                case "2": {  // List all logs
                    String sql = 
                      "SELECT UID, PID, LiftID, DateTimeOfUse FROM LiftUsage";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("UID | PID | LiftID | DateTime");
                        while (rs.next()) {
                            System.out.printf("%d | %d | %d | %s%n",
                              rs.getInt("UID"),
                              rs.getInt("PID"),
                              rs.getInt("LiftID"),
                              rs.getTimestamp("DateTimeOfUse"));
                        }
                    }
                } break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
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
    private void propertyMenu() {
        while (true) {
            System.out.println("-- Property  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {  // Add
                    System.out.print("Property ID: ");    int pid   = Integer.parseInt(scanner.nextLine());
                    System.out.print("Type: ");           String ty  = scanner.nextLine();
                    System.out.print("Name: ");           String nm  = scanner.nextLine();
                    System.out.print("Monthly Income: "); BigDecimal inc = new BigDecimal(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO Property
                          (PropertyID, Type, Name, MonthlyIncome)
                        VALUES (?,?,?,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        ps.setString(2, ty);
                        ps.setString(3, nm);
                        ps.setBigDecimal(4, inc);
                        ps.executeUpdate();
                    }
                    System.out.println("Property added.");
                } break;
    
                case "2": {  // List all
                    String sql = "SELECT PropertyID, Type, Name, MonthlyIncome FROM Property";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("PID | Type | Name | Income");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %s%n",
                              rs.getInt("PropertyID"),
                              rs.getString("Type"),
                              rs.getString("Name"),
                              rs.getBigDecimal("MonthlyIncome"));
                        }
                    }
                } break;
    
                case "3": {  // Update income
                    System.out.print("Property ID to update: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Monthly Income: ");
                    BigDecimal inc = new BigDecimal(scanner.nextLine());
    
                    String sql = "UPDATE Property SET MonthlyIncome = ? WHERE PropertyID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setBigDecimal(1, inc);
                        ps.setInt(2, pid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Property updated." : "No such PropertyID.");
                    }
                } break;
    
                case "4": {  // Delete
                    System.out.print("Property ID to delete: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Property WHERE PropertyID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Property deleted." : "No such PropertyID.");
                    }
                } break;
    
                case "5": {  // By ID
                    System.out.print("Property ID: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Property WHERE PropertyID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                  "%d | %s | %s | %s%n",
                                  rs.getInt("PropertyID"),
                                  rs.getString("Type"),
                                  rs.getString("Name"),
                                  rs.getBigDecimal("MonthlyIncome")
                                );
                            } else {
                                System.out.println("No property found.");
                            }
                        }
                    }
                } break;
    
                case "0":
                    return;
    
                default:
                    System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }    
}



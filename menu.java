/*
 * Author: Group 12
 * Course: CSC 460
 * Assignment: Prog4
 * In
 * This Java program implements a text-based, menu-driven command-line interface 
 * that connects to an Oracle database via JDBC. It is designed for a ski resort
 * to manage and interact with their operational data including members, ski passes,
 * equipment rentals, employee records, lessons, trails, lifts, and properties.
 *
 * The system allows authorized users to perform insertions, deletions, updates,
 * and complex queries on the underlying database. Each category of data is handled
 * through dedicated sub-menus that prompt the user for relevant input and issue
 * SQL statements to the database accordingly.
 *
 * Built as part of a two-tier client-server application, this front-end
 * communicates with a relational schema stored on the university’s Oracle server.
 * Its purpose is to enhance both customer experience and staff efficiency by
 * providing operations such as ski pass usage tracking, rental returns,
 * lesson scheduling, and property income monitoring.
 *
 *
 *              -- Missing Features --
 *  - need to include your username and password for the jdbc
 *
 *                -- To compile --
 * export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * javac menu.java
 * java menu
 */

// Imports
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.Scanner;
 
 public class menu {

     // Global variables
     private static final String URL  = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle"; // URL to oracle database
     private static final String USER = "<YOUR_USER>";                                       // User's Oracle username
     private static final String PASS = "<YOUR_PASS>";                                       // User's Oracle password
     
  
     private Connection conn;
     private Scanner scanner = new Scanner(System.in);
 
     // Main
     public static void main(String[] args) {
         new menu().run();
     }
     
    /*
     * Method run()
     *
     * This method serves as the main control loop for the ski resort's database management CLI.
     * It establishes a JDBC connection to the Oracle database and repeatedly presents the user
     * with a menu of available operations until the user chooses to exit.
     * Based on user input, it dispatches control to the appropriate submenu method.
     *
     * The method handles database connection setup and teardown, as well as basic input validation
     * to ensure valid option selection.
     *
     * Parameters:
     *      None
     *
     * Returns:
     *      void -- This method does not return a value. It manages program control flow and DB session.
     */
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
                     case "5": lessonOrderMenu(); break;
                     case "6": instructorMenu();break;
                     case "7": employeeMenu();  break;
                     case "8": trailMenu();     break;
                     case "9": liftMenu();      break;
                     case "10": liftUsageMenu();break;
                     case "11": queryMenu();    break;
                     case "12": propertyMenu(); break;
                     case "13": lessonMenu();   break;
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
     
     // Display the user menu
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
         System.out.println("13) Lesson Details ");
         System.out.println("0) Exit");
         System.out.print("> ");
     }
     
    /*
     * Method memberMenu()
     *
     * This method provides a submenu for managing resort members within the database system.
     * It allows users to perform CRUD operations (Create, Read, Update, Delete) and retrieve member
     * details by ID. Each option corresponds to a specific SQL interaction and is executed
     * based on user input through the command-line interface.
     *
     * Parameters:
     *      None
     *
     * Returns:
     *      void -- This method does not return a value. It handles database modifications and output display.
     */
    private void memberMenu() {
        while (true) {
            System.out.println("-- Member -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Create
                    System.out.print("MID: ");
                    int mid;
                    try (Statement s = conn.createStatement();
                    ResultSet r = s.executeQuery("SELECT member_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        mid = r.getInt(1);
                    }
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
                        INSERT INTO pruiz2.Member
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
                        FROM pruiz2.Member
                        ORDER BY MID ASC
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
                        UPDATE pruiz2.Member
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

                    // Check for active passes
                    String passCheck =
                   "SELECT COUNT(*) FROM pruiz2.SkiPass " +
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

                    // Check for Rentals
                    String rentCheck =
                    "SELECT COUNT(*) FROM pruiz2.EquipmentRental WHERE MID = ? AND Status = 0";
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

                    // Check for unused lessons
                    String lessonCheck =
                     "SELECT COUNT(*) FROM pruiz2.LessonOrder WHERE MID = ? AND SessionsLeft > 0";
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

                    // Lift usage 
                    String delLiftUsage =
                      "DELETE FROM LiftUsage WHERE PID IN (SELECT PID FROM SkiPass WHERE MID = ?)";
                    try (PreparedStatement ps = conn.prepareStatement(delLiftUsage)) {
                        ps.setInt(1, mid);
                        ps.executeUpdate();
                    }
                
                    // Equipment rentals
                    String delRentals = "DELETE FROM EquipmentRental WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delRentals)) {
                        ps.setInt(1, mid);
                        ps.executeUpdate();
                    }
                
                    // Lesson orders
                    String delLessons = "DELETE FROM LessonOrder WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delLessons)) {
                        ps.setInt(1, mid);
                        ps.executeUpdate();
                    }
                
                    // Ski passes
                    String delPasses = "DELETE FROM SkiPass WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delPasses)) {
                        ps.setInt(1, mid);
                        ps.executeUpdate();
                    }
                
                    // Member
                    String delMember = "DELETE FROM Member WHERE MID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delMember)) {
                        ps.setInt(1, mid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0
                            ? "Member and all related data deleted."
                            : "No such MID.");
                    }
                } break;

                case "5": { // By ID
                    System.out.print("MID: ");
                    int mid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.Member WHERE MID = ?";
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

    /*
     * Method skiPassMenu()
     *
     * This method shows a submenu that lets the user manage ski passes in the system.
     * Users can add new ski passes, view all passes, update the remaining uses on a pass,
     * delete expired and used-up passes, or look up a pass by its ID.
     *

     * Parameters:
     *      None
     *
     * Returns:
     *      void -- Does not return anything. It handles user input and updates the database.
     */
    private void skiPassMenu() {
        while (true) {
            System.out.println("-- SkiPass -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    int pid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT skipass_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        pid = r.getInt(1);
                    }
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
                        INSERT INTO pruiz2.SkiPass
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
                          FROM pruiz2.SkiPass
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
                    String sql = "UPDATE pruiz2.SkiPass SET RemainingUses=? WHERE PID=?";
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
                    "SELECT RemainingUses, ExpirationDate FROM pruiz2.SkiPass WHERE PID = ?";
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
                                    try (PreparedStatement del = conn.prepareStatement("DELETE FROM pruiz2.SkiPass WHERE PID = ?")) {
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
                    String sql = "SELECT * FROM pruiz2.SkiPass WHERE PID=?";
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
    
    /*
     * Method equipmentMenu()
     *
     * This method opens a menu for managing ski equipment records in the database.
     * Users can add new equipment, view all records, update equipment status,
     * delete equipment, or look up equipment by ID.

     * Parameters:
     *      None
     *
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void equipmentMenu() {
        while (true) {
            System.out.println("-- Equipment -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                     int eid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT equipment_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        eid = r.getInt(1);
                    }
                    System.out.print("Type (numeric code): ");   int type     = Integer.parseInt(scanner.nextLine());
                    System.out.print("Status (numeric): ");      int status   = Integer.parseInt(scanner.nextLine());
                    System.out.print("SizeOrLength: ");          double size   = Double.parseDouble(scanner.nextLine());
                    System.out.print("IsArchived (0/1): ");      int archived = Integer.parseInt(scanner.nextLine());
                    System.out.print("ChangeID: ");              int cid      = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO pruiz2.Equipment
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
                          FROM pruiz2.Equipment
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
    
                    String sql = "UPDATE pruiz2.Equipment SET Status=?, IsArchived=? WHERE EID=?";
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
                    String sql = "DELETE FROM pruiz2.Equipment WHERE EID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Equipment deleted." : "No such EID.");
                    }
                    break;
                }
                case "5": { // By ID
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.Equipment WHERE EID=?";
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
    
    /*
     * Method rentalMenu()
     *
     * This method opens a menu for managing rental records in the database.
     * Users can add new rentals, view all rentals, update rental status,
     * delete rentals, or look up rentals by ID.

     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void rentalMenu() {
        while (true) {
            System.out.println("-- Equipment Rental -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add (uses current timestamp)
                    int rid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT equipmentrental_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        rid = r.getInt(1);
                    }
                    System.out.print("Member ID (MID): ");  int mid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Pass ID (PID): ");    int pid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Equipment ID (EID): "); int eid    = Integer.parseInt(scanner.nextLine());
                    System.out.print("Status (numeric): ");  int status = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO pruiz2.EquipmentRental
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
                          FROM pruiz2.EquipmentRental
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
                    String sql = "UPDATE pruiz2.EquipmentRental SET Status=? WHERE RID=?";
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
                    String sql = "DELETE FROM pruiz2.EquipmentRental WHERE RID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, rid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Rental deleted." : "No such RID.");
                    }
                    break;
                }
                case "5": { // By ID
                    System.out.print("RID: "); int rid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.EquipmentRental WHERE RID=?";
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

    /*
     * Method lessonOrderMenu()
     *
     * This method opens a menu for managing lesson order records in the database.
     * Users can add new lesson orders, view all lessons, or look up lessons by ID.

     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void lessonOrderMenu() {
        while (true) {
            System.out.println("-- Lesson Purchase  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                     int oid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT lessonorder_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        oid = r.getInt(1);
                    }
                    System.out.print("Member ID: "); int mid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Lesson ID: "); int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Number of Sessions: "); int num = Integer.parseInt(scanner.nextLine());
                    String sql = 
                      "INSERT INTO pruiz2.LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate) " +
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
                      "FROM pruiz2.LessonOrder";
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
                    String sql = "UPDATE pruiz2.LessonOrder SET SessionsLeft = ? WHERE OID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, sl);
                        ps.setInt(2, oid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson order updated.");
                } break;
                case "4": { // Delete
                    System.out.print("OID to delete: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM pruiz2.LessonOrder WHERE OID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt > 0 ? "Lesson order deleted." : "No order found.");
                    }
                } break;
                case "5": { // By ID
                    System.out.print("OID: "); int oid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.LessonOrder WHERE OID = ?";
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

    /*
     * Method instructorMenu()
     *
     * This method opens a menu for managing instructor records in the database.
     * Users can add, view, delete, or look up instructors by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    
    private void instructorMenu() {
    while (true) {
        System.out.println("-- Instructor  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
        System.out.print("> ");
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
            case "1": {  // Add
                 int iid;
                try (Statement s = conn.createStatement();
                    ResultSet r = s.executeQuery("SELECT instructor_seq.NEXTVAL FROM DUAL")) {
                    r.next();
                    iid = r.getInt(1);
                }
                System.out.print("Employee ID: "); int eid = Integer.parseInt(scanner.nextLine());
                System.out.print("Certification (1/2/3): "); int cert = Integer.parseInt(scanner.nextLine());
                String sql = "INSERT INTO pruiz2.Instructor (IID, EID, Certification) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, iid);
                    ps.setInt(2, eid);
                    ps.setInt(3, cert);
                    ps.executeUpdate();
                }
                System.out.println("Instructor added.");
            } break;

            case "2": {  // List all
                String sql = "SELECT IID, EID, Certification FROM pruiz2.Instructor";
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
                String sql = "UPDATE pruiz2.Instructor SET EID = ?, Certification = ? WHERE IID = ?";
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
                String sql = "DELETE FROM pruiz2.Instructor WHERE IID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, iid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Instructor deleted." : "No such IID.");
                }
            } break;

            case "5": {  // By ID
                System.out.print("IID: "); int iid = Integer.parseInt(scanner.nextLine());
                String sql = "SELECT * FROM pruiz2.Instructor WHERE IID = ?";
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

    /*
     * Method employeeMenu()
     *
     * This method opens a menu for managing employee records in the database.
     * Users can add, view, delete, update, or look up employees by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    
    private void employeeMenu() {
        while (true) {
            System.out.println("-- Employee  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    // Employee
                    int eid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT employee_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        eid = r.getInt(1);
                    }
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
                      "INSERT INTO pruiz2.Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary) " +
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
                      "FROM pruiz2.Employee";
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
                      "UPDATE pruiz2.Employee SET Position = ?, Education = ? WHERE EID = ?";
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
                    String sql = "DELETE FROM pruiz2.Employee WHERE EID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, eid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt > 0 ? "Employee deleted." : "No employee found.");
                    }
                } break;
                case "5": { // By ID
                    System.out.print("EID: "); int eid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.Employee WHERE EID = ?";
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
    
    /*
     * Method trailMenu()
     *
     * This method opens a menu for managing trail records in the database.
     * Users can add, view, delete, or look up trail by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void trailMenu() {
    while (true) {
        System.out.println("-- Trail  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
        System.out.print("> ");
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
            case "1": {  // Create
                 int tid;
                try (Statement s = conn.createStatement();
                    ResultSet r = s.executeQuery("SELECT trail_seq.NEXTVAL FROM DUAL")) {
                    r.next();
                    tid = r.getInt(1);
                }
                System.out.print("Name: "); String name = scanner.nextLine();
                System.out.print("Start Location: "); String start = scanner.nextLine();
                System.out.print("End Location: "); String end = scanner.nextLine();
                System.out.print("Status (1=open, 0=closed): "); int st = Integer.parseInt(scanner.nextLine());
                System.out.print("Difficulty: "); String diff = scanner.nextLine();
                System.out.print("Category: "); String cat = scanner.nextLine();
                String sql =
                  "INSERT INTO pruiz2.Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category) " +
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
                String sql = "SELECT TID, Name, StartLocation, EndLocation, Status, Difficulty, Category FROM pruiz2.Trail";
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
                String sql = "UPDATE pruiz2.Trail SET Status = ? WHERE TID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, st);
                    ps.setInt(2, tid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Trail status updated." : "No such TID.");
                }
            } break;

            case "4": {  // Delete
                System.out.print("TID to delete: "); int tid = Integer.parseInt(scanner.nextLine());
                String sql = "DELETE FROM pruiz2.Trail WHERE TID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, tid);
                    int cnt = ps.executeUpdate();
                    System.out.println(cnt > 0 ? "Trail deleted." : "No such TID.");
                }
            } break;

            case "5": {  // By ID
                System.out.print("TID: "); int tid = Integer.parseInt(scanner.nextLine());
                String sql = "SELECT * FROM pruiz2.Trail WHERE TID = ?";
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
    
    /*
     * Method liftMenu()
     *
     * This method opens a menu for managing lift records in the database.
     * Users can add, view, update, delete, or look up lift by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void liftMenu() {
        while (true) {
            System.out.println("-- Lift -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {  // Create
                     int lid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT lift_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        lid = r.getInt(1);
                    }
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Open Time (HH24:MI): ");
                    String open = scanner.nextLine();
                    System.out.print("Close Time (HH24:MI): ");
                    String close = scanner.nextLine();
                    System.out.print("Status (1=open, 0=closed): ");
                    int status = Integer.parseInt(scanner.nextLine());
    
                    String sql = 
                      "INSERT INTO pruiz2.Lift (LiftID, Name, OpenTime, CloseTime, Status) " +
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
                      "SELECT LiftID, Name, OpenTime, CloseTime, Status FROM pruiz2.Lift";
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
    
                    String sql = "UPDATE pruiz2.Lift SET Status = ? WHERE LiftID = ?";
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
    
                    String sql = "DELETE FROM pruiz2.Lift WHERE LiftID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Lift deleted." : "No such LiftID.");
                    }
                } break;
    
                case "5": {  // By ID
                    System.out.print("LiftID: ");
                    int lid = Integer.parseInt(scanner.nextLine());
    
                    String sql = "SELECT * FROM pruiz2.Lift WHERE LiftID = ?";
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
    
    /*
     * Method liftUsageMenu()
     *
     * This method opens a menu for managing lift usage records in the database.
     * Users can add or look up lift usage by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void liftUsageMenu() {
        while (true) {
            System.out.println("-- Lift Usage  -- 1)Add 2)List 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {  // Record a ride
                    int uid;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT liftusage_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        uid = r.getInt(1);
                    }
                    System.out.print("PassID: ");int pid  = Integer.parseInt(scanner.nextLine());
                    System.out.print("LiftID: ");int lid  = Integer.parseInt(scanner.nextLine());
                    String sql = 
                      "INSERT INTO pruiz2.LiftUsage (UID, PID, LiftID, DateTimeOfUse) " +
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
                      "SELECT UID, PID, LiftID, DateTimeOfUse FROM pruiz2.LiftUsage";
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

    /*
     * Method queryMenu()
     *
     * This method opens a menu which prompts the user to select a query they would like to perform. The
     * queries are based on predetermined and specific searches set by the project scope.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void queryMenu() {
        while (true) { // Show menu
            System.out.println("--- Queries ---");
            System.out.println("1) Member lessons");
            System.out.println("2) Pass history");
            System.out.println("3) Open intermediate trails + lifts");
            System.out.println("4) Lesson instructor cert");
            System.out.println("0) Back");
            System.out.print("> ");
            String q = scanner.nextLine().trim();
            try {
                switch (q) {
                case "1": { // Query 1
                    System.out.print("Member ID: ");
                    int mid = Integer.parseInt(scanner.nextLine());
                    String sql = """
                        SELECT m.FirstName, m.LastName,
                               lo.NumberOfSessions, lo.SessionsLeft,
                               e.FirstName, e.LastName,
                               l.TimeOfClass
                          FROM pruiz2.LessonOrder lo
                          JOIN Lesson l        ON lo.LID = l.LID
                          JOIN pruiz2.Instructor i    ON l.IID = i.IID
                          JOIN pruiz2.Employee e      ON i.EID = e.EID
                          JOIN pruiz2.Member m        ON lo.MID = m.MID
                         WHERE m.MID = ?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, mid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("MemFirst | MemLast | #Purchased | #Left | InstrFirst | InstrLast | TimeOfClass");
                            while (rs.next()) {
                                System.out.printf("%s | %s | %d | %d | %s | %s | %s%n",
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getInt(3),
                                    rs.getInt(4),
                                    rs.getString(5),
                                    rs.getString(6),
                                    rs.getTimestamp(7));
                            }
                        }
                    }
                } break;
    
                case "2": { // Query 2
                    System.out.print("Pass ID: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    // lift rides
                    System.out.println("=== Lift Rides ===");
                    String sql1 = """
                        SELECT lf.Name, lu.DateTimeOfUse
                          FROM pruiz2.LiftUsage lu
                          JOIN pruiz2.Lift lf ON lu.LiftID = lf.LiftID
                         WHERE lu.PID = ?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                        ps.setInt(1, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("LiftName | DateTimeOfUse");
                            while (rs.next()) {
                                System.out.printf("%s | %s%n",
                                    rs.getString(1), rs.getTimestamp(2));
                            }
                        }
                    }
                    // equipment rentals
                    System.out.println("=== Equipment Rentals ===");
                    String sql2 = """
                        SELECT eq.Type, er.Status, er.RentalDateTime
                          FROM pruiz2.EquipmentRental er
                          JOIN Equipment eq ON er.EID = eq.EID
                         WHERE er.PID = ?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                        ps.setInt(1, pid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("EquipmentType | Status | RentalDateTime");
                            while (rs.next()) {
                                System.out.printf("%s | %d | %s%n",
                                    rs.getString(1), rs.getInt(2), rs.getTimestamp(3));
                            }
                        }
                    }
                } break;

                case "3": { // Query 3
                    String sql = """
                        SELECT t.Name, t.Category, lf.Name
                          FROM pruiz2.Trail t
                          JOIN pruiz2.Trail lt ON t.TID = lt.TID
                          JOIN pruiz2.Lift lf      ON lt.LiftID = lf.LiftID
                         WHERE t.Difficulty = 'Intermediate'
                           AND t.Status = 1
                           AND lf.Status = 1
                        """;
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("TrailName | Category | LiftName");
                        while (rs.next()) {
                            System.out.printf("%s | %s | %s%n",
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3));
                        }
                    }
                } break;
    
                case "4": { // Query 4
                    System.out.print("Lesson ID: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    String sql = """
                        SELECT i.Certification
                          FROM pruiz2.Instructor i
                          JOIN Lesson l ON i.IID = l.IID
                         WHERE l.LID = ?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        try (ResultSet rs = ps.executeQuery()) {
                            System.out.println("Certification");
                            if (rs.next()) {
                                System.out.println(rs.getString(1));
                            } else {
                                System.out.println("No such lesson.");
                            }
                        }
                    }
                } break;
    
                case "0":
                    return;
    
                default:
                    System.out.println("Invalid query option");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }    

    /*
     * Method propertyMenu()
     *
     * This method opens a menu for managing property records in the database.
     * Users can add, view, update, delete, or look up property by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void propertyMenu() {
        while (true) {
            System.out.println("-- Property  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": {  // Add
                    int propertyId;
                    try (Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery("SELECT property_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        propertyId = r.getInt(1);
                    }
                    System.out.print("Type: ");           String ty  = scanner.nextLine();
                    System.out.print("Name: ");           String nm  = scanner.nextLine();
                    System.out.print("Monthly Income: "); BigDecimal inc = new BigDecimal(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO pruiz2.Property
                          (PropertyID, Type, Name, MonthlyIncome)
                        VALUES (?,?,?,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, propertyId);
                        ps.setString(2, ty);
                        ps.setString(3, nm);
                        ps.setBigDecimal(4, inc);
                        ps.executeUpdate();
                    }
                    System.out.println("Property added.");
                } break;
    
                case "2": {  // List all
                    String sql = "SELECT PropertyID, Type, Name, MonthlyIncome FROM pruiz2.Property";
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
    
                    String sql = "UPDATE pruiz2.Property SET MonthlyIncome = ? WHERE PropertyID = ?";
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
                    String sql = "DELETE FROM pruiz2.Property WHERE PropertyID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, pid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Property deleted." : "No such PropertyID.");
                    }
                } break;
    
                case "5": {  // By ID
                    System.out.print("Property ID: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM pruiz2.Property WHERE PropertyID = ?";
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

    /*
     * Method lessonMenu()
     *
     * This method opens a menu for managing lesson records in the database.
     * Users can add, view, update, delete, or look up lesson by ID.
     * 
     * Parameters:
     *      None
     * Returns:
     *      void -- Doesn’t return anything. It handles user actions and updates the database.
     */
    private void lessonMenu() {
        while (true) {
            System.out.println("-- Lesson -- 1)Add  2)List  3)Upd  4)Del  5)ByID  0)Back");
            System.out.print("> ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                case "1": { // Add
                    int lid;
                    try (Statement s = conn.createStatement();
                         ResultSet r = s.executeQuery("SELECT lesson_seq.NEXTVAL FROM DUAL")) {
                        r.next();
                        lid = r.getInt(1);
                    }
                    System.out.print("Type (Private/Group): ");
                    String type = scanner.nextLine();
                    System.out.print("Difficulty: ");
                    String diff = scanner.nextLine();
                    System.out.print("TimeOfClass (HH24:MI): ");
                    String toc  = scanner.nextLine();
                    System.out.print("Duration: ");
                    String dur  = scanner.nextLine();
                    System.out.print("Price: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());
                    System.out.print("Instructor ID: ");
                    int iid = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        INSERT INTO Lesson
                          (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
                        VALUES (?,?,?,?,?,?,?)
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        ps.setString(2, type);
                        ps.setString(3, diff);
                        ps.setString(4, toc);
                        ps.setString(5, dur);
                        ps.setBigDecimal(6, price);
                        ps.setInt(7, iid);
                        ps.executeUpdate();
                    }
                    System.out.println("Lesson added.");
                } break;
    
                case "2": { // List all
                    String sql = "SELECT LID, Type, Difficulty, TimeOfClass, Duration, Price, IID FROM Lesson";
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(sql)) {
                        System.out.println("LID | Type | Diff | Time | Duration | Price | IID");
                        while (rs.next()) {
                            System.out.printf("%d | %s | %s | %s | %s | %s | %d%n",
                              rs.getInt("LID"),
                              rs.getString("Type"),
                              rs.getString("Difficulty"),
                              rs.getString("TimeOfClass"),
                              rs.getString("Duration"),
                              rs.getBigDecimal("Price"),
                              rs.getInt("IID"));
                        }
                    }
                } break;
    
                case "3": { // Update
                    System.out.print("LID to update: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Type: ");
                    String type = scanner.nextLine();
                    System.out.print("New Difficulty: ");
                    String diff = scanner.nextLine();
                    System.out.print("New TimeOfClass (HH24:MI): ");
                    String toc  = scanner.nextLine();
                    System.out.print("New Duration: ");
                    String dur  = scanner.nextLine();
                    System.out.print("New Price: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());
                    System.out.print("New Instructor ID: ");
                    int iid = Integer.parseInt(scanner.nextLine());
    
                    String sql = """
                        UPDATE Lesson
                           SET Type=?, Difficulty=?, TimeOfClass=?, Duration=?, Price=?, IID=?
                         WHERE LID=?
                        """;
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, type);
                        ps.setString(2, diff);
                        ps.setString(3, toc);
                        ps.setString(4, dur);
                        ps.setBigDecimal(5, price);
                        ps.setInt(6, iid);
                        ps.setInt(7, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Lesson updated." : "No such LID.");
                    }
                } break;
    
                case "4": { // Delete
                    System.out.print("LID to delete: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    String sql = "DELETE FROM Lesson WHERE LID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        int cnt = ps.executeUpdate();
                        System.out.println(cnt>0 ? "Lesson deleted." : "No such LID.");
                    }
                } break;
    
                case "5": { // By ID
                    System.out.print("LID: ");
                    int lid = Integer.parseInt(scanner.nextLine());
                    String sql = "SELECT * FROM Lesson WHERE LID = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, lid);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                System.out.printf(
                                  "%d | %s | %s | %s | %s | %s | %d%n",
                                  rs.getInt("LID"),
                                  rs.getString("Type"),
                                  rs.getString("Difficulty"),
                                  rs.getString("TimeOfClass"),
                                  rs.getString("Duration"),
                                  rs.getBigDecimal("Price"),
                                  rs.getInt("IID")
                                );
                            } else {
                                System.out.println("No lesson found.");
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

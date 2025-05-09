/*
 * Author: Group 12
 * Course: CSC 460
 * Assignment: Prog4
 * 
 *              -- menu.java (Java 16) --
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
	private static final String URL = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
	private Connection conn;
	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("\nUsage: java menu <username> <password>\n"
					+ "    where <username> is your Oracle DBMS username,\n"
					+ "    and <password> is your Oracle password (not your system password).\n");
			System.exit(-1);
		}

		String username = args[0];
		String password = args[1];

		new menu().run(username, password);
	}

	/*
	 * Method run()
	 *
	 * This method serves as the main control loop for the ski resort's database
	 * management CLI. It establishes a JDBC connection to the Oracle database and
	 * repeatedly presents the user with a menu of available operations until the
	 * user chooses to exit. Based on user input, it dispatches control to the
	 * appropriate submenu method.
	 *
	 * The method handles database connection setup and teardown, as well as basic
	 * input validation to ensure valid option selection.
	 *
	 * Parameters: None
	 *
	 * Returns: void -- This method does not return a value. It manages program
	 * control flow and DB session.
	 */
	public void run(String username, String password) {
		try {
			conn = getOracleConnection(username, password);
			boolean exit = false;
			while (!exit) {
				printMainMenu();
				String choice = scanner.nextLine().trim();
				switch (choice) {
				case "1":
					memberMenu();
					break;
				case "2":
					skiPassMenu();
					break;
				case "3":
					equipmentMenu();
					break;
				case "4":
					rentalMenu();
					break;
				case "5":
					lessonOrdMenu();
					break;
				case "6":
					lessonMenu();
					break;
				case "7":
					instructorMenu();
					break;
				case "8":
					employeeMenu();
					break;
				case "9":
					trailMenu();
					break;
				case "10":
					liftMenu();
					break;
				case "11":
					liftUsageMenu();
					break;
				case "12":
					propertyMenu();
					break;
				case "13":
					queryMenu();
					break;
				case "0":
					exit = true;
					break;
				default:
					System.out.println("Invalid option");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}

	/*---------------------------------------------------------------------
	|  Method getOracleConnection(username, password)
	|
	|  Purpose:  To get a connection to the Oracle server
	|
	|  Pre-condition:  Valid username and password provided
	|
	|  Post-condition: Returns a database connection
	|
	|  Parameters:
	|      username -- Oracle username
	|      password -- Oracle password
	|
	|  Credit:   This method's code was given by  L. McCann
	|
	|  Returns:  Connection object to the Oracle database
	*-------------------------------------------------------------------*/
	private Connection getOracleConnection(String username, String password) {
		Connection dbconn = null;

		try {
			// Load the Oracle JDBC driver
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.err.println("*** ClassNotFoundException: Error loading Oracle JDBC driver.\n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);
		}

		try {
			// Establish connection to Oracle
			dbconn = DriverManager.getConnection(URL, username, password);
			System.out.println("Connected to Oracle database successfully.");
		} catch (SQLException e) {
			System.err.println("*** SQLException: Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

		return dbconn;
	}

	private void printMainMenu() {
		System.out.println("\n=== CSc 460 Ski Resort CLI ===");
		System.out.println("1) Member ");
		System.out.println("2) SkiPass ");
		System.out.println("3) Equipment ");
		System.out.println("4) Rental ");
		System.out.println("5) Lesson Purchase ");
		System.out.println("6) Lesson ");
		System.out.println("7) Instructor ");
		System.out.println("8) Employee ");
		System.out.println("9) Trail ");
		System.out.println("10) Lift ");
		System.out.println("11) Lift Usage ");
		System.out.println("12) Property ");
		System.out.println("13) Queries ");
		System.out.println("0) Exit");
		System.out.print("> ");
	}

	/*
	 * Method memberMenu()
	 *
	 * This method provides a submenu for managing resort members within the
	 * database system. It allows users to perform CRUD operations (Create, Read,
	 * Update, Delete) and retrieve member details by ID. Each option corresponds to
	 * a specific SQL interaction and is executed based on user input through the
	 * command-line interface.
	 *
	 * Parameters: None
	 *
	 * Returns: void -- This method does not return a value. It handles database
	 * modifications and output display.
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
					try {
						mid = Integer.parseInt(scanner.nextLine());
						if (mid == -1) {
							System.out.println("Operation cancelled.");
							break;
						}
					} catch (NumberFormatException e) {
						System.out.println("Error: MID must be a valid integer.");
						break;
					}
					System.out.print("First Name: ");
					String first = scanner.nextLine();
					if (first.equals("-1")) {
					    System.out.println("Operation cancelled.");
					    break;
					}
					System.out.print("Last Name: ");
					String last = scanner.nextLine();
					if (last.equals("-1")) {
					    System.out.println("Operation cancelled.");
					    break;
					}
					System.out.print("Phone Number: ");
					String phone = scanner.nextLine();
					if (phone.equals("-1")) {
					    System.out.println("Operation cancelled.");
					    break;
					}
					System.out.print("Email Address: ");
					String email = scanner.nextLine();
					if (email.equals("-1")) {
					    System.out.println("Operation cancelled.");
					    break;
					}
					System.out.print("Date of Birth (yyyy-mm-dd): ");
					Date dob;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    dob = Date.valueOf(input);
					} catch (IllegalArgumentException e) {
					    System.out.println("Error: Invalid date format. Use yyyy-mm-dd.");
					    break;
					}
					System.out.print("Emergency Contact: ");
					String ec = scanner.nextLine();
					if (ec.equals("-1")) {
					    System.out.println("Operation cancelled.");
					    break;
					}

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
				}
					break;

				case "2": { // List all
					String sql = """
							SELECT MID, FirstName, LastName,
							    PhoneNumber, EmailAddress,
							    DateOfBirth, EmergencyContact
							FROM pruiz2.Member
							ORDER BY MID ASC
							""";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("MID | First | Last | Phone | Email | DOB | Emergency");
						while (rs.next()) {
							System.out.printf("%d | %s | %s | %s | %s | %s | %s%n", rs.getInt("MID"),
									rs.getString("FirstName"), rs.getString("LastName"), rs.getString("PhoneNumber"),
									rs.getString("EmailAddress"), rs.getDate("DateOfBirth"),
									rs.getString("EmergencyContact"));
						}
					}
				}
					break;

				case "3": { // Update
					System.out.print("MID to update: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New First Name: ");
					String first = scanner.nextLine();
					System.out.print("New Last Name: ");
					String last = scanner.nextLine();
					System.out.print("New Phone Number: ");
					String phone = scanner.nextLine();
					System.out.print("New Email Address: ");
					String email = scanner.nextLine();
					System.out.print("New DOB (yyyy-mm-dd): ");
					Date dob;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    dob = Date.valueOf(input);
					} catch (IllegalArgumentException e) {
					    System.out.println("Error: Invalid date format. Use yyyy-mm-dd.");
					    break;
					}
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
						System.out.println(cnt > 0 ? "Member updated." : "No such MID.");
					}
				}
					break;

				case "4": { // Delete with pre‐checks
					System.out.print("MID to delete: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					// 1) Active ski‐passes?
					String passCheck = "SELECT COUNT(*) FROM pruiz2.SkiPass "
							+ "WHERE MID = ? AND (RemainingUses > 0 OR ExpirationDate > SYSTIMESTAMP)";
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
					String rentCheck = "SELECT COUNT(*) FROM pruiz2.EquipmentRental WHERE MID = ? AND Status = 0";
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
					String lessonCheck = "SELECT COUNT(*) FROM pruiz2.LessonOrder WHERE MID = ? AND SessionsLeft > 0";
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
					String delSql = "DELETE FROM pruiz2.Member WHERE MID = ?";
					try (PreparedStatement ps = conn.prepareStatement(delSql)) {
						ps.setInt(1, mid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Member deleted." : "No such MID.");
					}
				}
					break;

				case "5": { // By ID
					System.out.print("MID: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.Member WHERE MID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, mid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %s | %s | %s | %s | %s | %s%n", rs.getInt("MID"),
										rs.getString("FirstName"), rs.getString("LastName"),
										rs.getString("PhoneNumber"), rs.getString("EmailAddress"),
										rs.getDate("DateOfBirth"), rs.getString("EmergencyContact"));
							} else {
								System.out.println("No member found.");
							}
						}
					}
				}
					break;

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
	 * This method shows a submenu that lets the user manage ski passes in the
	 * system. Users can add new ski passes, view all passes, update the remaining
	 * uses on a pass, delete expired and used-up passes, or look up a pass by its
	 * ID.
	 *
	 * 
	 * Parameters: None
	 *
	 * Returns: void -- Does not return anything. It handles user input and updates
	 * the database.
	 */
	private void skiPassMenu() {
		while (true) {
			System.out.println("-- SkiPass -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("PID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Total Uses: ");
					int total = Integer.parseInt(scanner.nextLine());
					try {
					    total = Integer.parseInt(scanner.nextLine());
					    if (total == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Remaining Uses: ");
					int rem = Integer.parseInt(scanner.nextLine());
					try {
					    rem = Integer.parseInt(scanner.nextLine());
					    if (rem == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Purchase Time (yyyy-MM-dd HH:mm:ss): ");
					Timestamp pt;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    pt = Timestamp.valueOf(input);
					} catch (IllegalArgumentException e) {
					    System.out.println("Error: Invalid timestamp format. Use yyyy-MM-dd HH:mm:ss.");
					    break;
					}
					System.out.print("Expiration (yyyy-MM-dd): ");
					Date exp;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    exp = Date.valueOf(input);
					} catch (IllegalArgumentException e) {
					    System.out.println("Error: Invalid date format. Use yyyy-mm-dd.");
					    break;
					}
					System.out.print("Price: ");
					BigDecimal price;
					try {
					    price = new BigDecimal(scanner.nextLine());
					    if (price.compareTo(new BigDecimal(-1)) == 0) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: Price must be a valid number.");
					    break;
					}
					System.out.print("Pass Type (OneDay, TwoDay, Season): ");
					String type = scanner.nextLine();
					System.out.print("Member ID: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("IsValid (1=Y,0=N): ");
					int valid;
					try {
					    valid = Integer.parseInt(scanner.nextLine());
					    if (valid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

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
							  ORDER BY PID ASC
							""";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println(
								"PID | MID | Type | PurchaseTime        | ExpDate    | Tot | Rem | Price | Valid");
						while (rs.next()) {
							System.out.printf("%d | %d | %s | %s | %s | %d | %d | %s | %d%n", rs.getInt("PID"),
									rs.getInt("MID"), rs.getString("PassType"), rs.getTimestamp("PurchaseDateTime"),
									rs.getDate("ExpirationDate"), rs.getInt("TotalUses"), rs.getInt("RemainingUses"),
									rs.getBigDecimal("Price"), rs.getInt("IsValid"));
						}
					}
					break;
				}
				case "3": { // Update remaining uses
					System.out.print("PID to update: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Remaining Uses: ");
					int rem;
					try {
					    rem = Integer.parseInt(scanner.nextLine());
					    if (rem == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "UPDATE pruiz2.SkiPass SET RemainingUses=? WHERE PID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, rem);
						ps.setInt(2, pid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "SkiPass updated." : "No such PID.");
					}
					break;
				}
				case "4": { // Delete
					System.out.print("PID to delete: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String check = "SELECT RemainingUses, ExpirationDate FROM pruiz2.SkiPass WHERE PID = ?";
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
									try (PreparedStatement del = conn
											.prepareStatement("DELETE FROM SkiPass WHERE PID = ?")) {
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
				}
					break;
				case "5": { // By ID
					System.out.print("PID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.SkiPass WHERE PID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, pid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %s | %s | %s | %d | %d | %s | %d%n", rs.getInt("PID"),
										rs.getInt("MID"), rs.getString("PassType"), rs.getTimestamp("PurchaseDateTime"),
										rs.getDate("ExpirationDate"), rs.getInt("TotalUses"),
										rs.getInt("RemainingUses"), rs.getBigDecimal("Price"), rs.getInt("IsValid"));
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
	 * 
	 * Parameters: None
	 *
	 * Returns: void -- Doesn’t return anything. It handles user actions and updates
	 * the database.
	 */
	private void equipmentMenu() {
		while (true) {
			System.out.println("-- Equipment -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("EID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Type (Boot = 1, Ski Pole = 2, Snowboard = 3, Alpine Skis = 4): ");
					int type;
					try {
					    type = Integer.parseInt(scanner.nextLine());
					    if (type == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("SizeOrLength (1: 4-14, 2: 100-140, 3: 115-200, 4: 90-178): ");
					double size;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    size = Double.parseDouble(input);
					    
					    // Validate size based on equipment type
					    boolean validSize = false;
					    switch (type) {
					        case 1: // Boot
					            validSize = (size >= 4 && size <= 14);
					            break;
					        case 2: // Ski Pole
					            validSize = (size >= 100 && size <= 140);
					            break;
					        case 3: // Snowboard
					            validSize = (size >= 115 && size <= 200);
					            break;
					        case 4: // Alpine Skis
					            validSize = (size >= 90 && size <= 178);
					            break;
					        default:
					            System.out.println("Error: Invalid equipment type.");
					            break;
					    }
					    
					    if (!validSize) {
					        System.out.println("Error: Size out of valid range for the selected equipment type.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: Size must be a valid number.");
					    break;
					}
					int changeid = eid;

					// Insert the changelog entry first
					String sqlChangeLog = "INSERT INTO pruiz2.ChangeLog (ChangeID, Description) VALUES (?, '1')";
					try (PreparedStatement psChange = conn.prepareStatement(sqlChangeLog)) {
						psChange.setInt(1, changeid);
						psChange.executeUpdate();
					}

					// Now insert equipment with the CID
					String sqlEquipment = "INSERT INTO pruiz2.Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID) "
							+ "VALUES (?, ?, 1, ?, 0, ?)";
					try (PreparedStatement ps = conn.prepareStatement(sqlEquipment)) {
						ps.setInt(1, eid);
						ps.setInt(2, type);
						ps.setDouble(3, size);
						ps.setInt(4, changeid);
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
							  ORDER BY EID ASC
							""";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("EID | Type | Status | Size | Archived | ChangeID");
						while (rs.next()) {
							System.out.printf("%d | %d | %d | %.1f | %d | %d%n", rs.getInt("EID"), rs.getInt("Type"),
									rs.getInt("Status"), rs.getDouble("SizeOrLength"), rs.getInt("IsArchived"),
									rs.getInt("ChangeID"));
						}
					}
					break;
				}
				case "3": { // Update status & archived flag
					System.out.print("EID to update: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Status (numeric): ");
					int status;
					try {
					    status = Integer.parseInt(scanner.nextLine());
					    if (status == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New IsArchived (0/1): ");
					int archived;
					try {
					    archived = Integer.parseInt(scanner.nextLine());
					    if (archived == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "UPDATE pruiz2.Equipment SET Status=?, IsArchived=? WHERE EID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, status);
						ps.setInt(2, archived);
						ps.setInt(3, eid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Equipment updated." : "No such EID.");
					}
					break;
				}
				case "4": { // Delete
					System.out.print("EID to delete: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM pruiz2.Equipment WHERE EID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, eid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Equipment deleted." : "No such EID.");
					}
					break;
				}
				case "5": { // By ID
					System.out.print("EID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.Equipment WHERE EID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, eid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %d | %.1f | %d | %d%n", rs.getInt("EID"),
										rs.getInt("Type"), rs.getInt("Status"), rs.getDouble("SizeOrLength"),
										rs.getInt("IsArchived"), rs.getInt("ChangeID"));
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
	 * This method opens a menu for managing rental records in the database. Users
	 * can add new rentals, view all rentals, update rental status, delete rentals,
	 * or look up rentals by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void rentalMenu() {
		while (true) {
			System.out.println("-- Equipment Rental -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add (uses current timestamp)
					System.out.print("RID: ");
					int rid;
					try {
					    rid = Integer.parseInt(scanner.nextLine());
					    if (rid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Member ID (MID): ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Pass ID (PID): ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Equipment ID (EID): ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Status (numeric): ");
					int status;
					try {
					    status = Integer.parseInt(scanner.nextLine());
					    if (status == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

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
							  ORDER BY RID ASC
							""";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("RID | MID | PID | EID | Time                    | Status");
						while (rs.next()) {
							System.out.printf("%d | %d | %d | %d | %s | %d%n", rs.getInt("RID"), rs.getInt("MID"),
									rs.getInt("PID"), rs.getInt("EID"), rs.getTimestamp("RentalDateTime"),
									rs.getInt("Status"));
						}
					}
					break;
				}
				case "3": { // Update status
					System.out.print("RID to update: ");
					int rid;
					try {
					    rid = Integer.parseInt(scanner.nextLine());
					    if (rid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Status (numeric): ");
					int status;
					try {
					    status = Integer.parseInt(scanner.nextLine());
					    if (status == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "UPDATE pruiz2.EquipmentRental SET Status=? WHERE RID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, status);
						ps.setInt(2, rid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Rental updated." : "No such RID.");
					}
					break;
				}
				case "4": { // Delete
					System.out.print("RID to delete: ");
					int rid;
					try {
					    rid = Integer.parseInt(scanner.nextLine());
					    if (rid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM pruiz2.EquipmentRental WHERE RID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, rid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Rental deleted." : "No such RID.");
					}
					break;
				}
				case "5": { // By ID
					System.out.print("RID: ");
					int rid;
					try {
					    rid = Integer.parseInt(scanner.nextLine());
					    if (rid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.EquipmentRental WHERE RID=?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, rid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %d | %d | %s | %d%n", rs.getInt("RID"), rs.getInt("MID"),
										rs.getInt("PID"), rs.getInt("EID"), rs.getTimestamp("RentalDateTime"),
										rs.getInt("Status"));
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
	 * Method lessonOrdMenu()
	 *
	 * This method opens a menu for managing lesson order records in the database.
	 * Users can add new lesson orders, view all lessons, or look up lessons by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void lessonOrdMenu() {
		while (true) {
			System.out.println("-- Lesson Purchase  -- 1)Buy 2)List Lessons 3)Update 4)Delete 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("OID: ");
					int oid;
					try {
					    oid = Integer.parseInt(scanner.nextLine());
					    if (oid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Member ID: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Lesson ID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Number of Sessions: ");
					int num;
					try {
					    num = Integer.parseInt(scanner.nextLine());
					    if (num == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "INSERT INTO pruiz2.LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate) "
							+ "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, oid);
						ps.setInt(2, mid);
						ps.setInt(3, lid);
						ps.setInt(4, num);
						ps.setInt(5, num);
						ps.executeUpdate();
					}
					System.out.println("Lesson order added.");
				}
					break;
				case "2": { // List
					String sql = "SELECT OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate "
							+ "FROM pruiz2.LessonOrder";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("OID | MID | LID | Num | Left | Date");
						while (rs.next()) {
							System.out.printf("%d | %d | %d | %d | %d | %s%n", rs.getInt("OID"), rs.getInt("MID"),
									rs.getInt("LID"), rs.getInt("NumberOfSessions"), rs.getInt("SessionsLeft"),
									rs.getTimestamp("PurchaseDate"));
						}
					}
				}
					break;
				case "3": { // Update
					System.out.print("OID to update: ");
					int oid;
					try {
					    oid = Integer.parseInt(scanner.nextLine());
					    if (oid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Sessions Left: ");
					int sl = Integer.parseInt(scanner.nextLine());
					String sql = "UPDATE pruiz2.LessonOrder SET SessionsLeft = ? WHERE OID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, sl);
						ps.setInt(2, oid);
						ps.executeUpdate();
					}
					System.out.println("Lesson order updated.");
				}
					break;
				case "4": { // Delete
					System.out.print("OID to delete: ");
					int oid;
					try {
					    oid = Integer.parseInt(scanner.nextLine());
					    if (oid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM pruiz2.LessonOrder WHERE OID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, oid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Lesson order deleted." : "No order found.");
					}
				}
					break;
				case "5": { // By ID
					System.out.print("OID: ");
					int oid;
					try {
					    oid = Integer.parseInt(scanner.nextLine());
					    if (oid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.LessonOrder WHERE OID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, oid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %d | %d | %d | %s%n", rs.getInt("OID"), rs.getInt("MID"),
										rs.getInt("LID"), rs.getInt("NumberOfSessions"), rs.getInt("SessionsLeft"),
										rs.getTimestamp("PurchaseDate"));
							} else {
								System.out.println("No lesson order found.");
							}
						}
					}
				}
					break;
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
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void instructorMenu() {
		while (true) {
			System.out.println("-- Instructor  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("IID: ");
					int iid;
					try {
					    iid = Integer.parseInt(scanner.nextLine());
					    if (iid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Employee ID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Certification (1/2/3): ");
					int cert;
					try {
					    cert = Integer.parseInt(scanner.nextLine());
					    if (cert == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "INSERT INTO Instructor (IID, EID, Certification) VALUES (?, ?, ?)";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, iid);
						ps.setInt(2, eid);
						ps.setInt(3, cert);
						ps.executeUpdate();
					}
					System.out.println("Instructor added.");
				}
					break;

				case "2": { // List all
					String sql = "SELECT IID, EID, Certification FROM Instructor ORDER BY IID ASC";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("IID | EID | Certification");
						while (rs.next()) {
							System.out.printf("%d | %d | %d%n", rs.getInt("IID"), rs.getInt("EID"),
									rs.getInt("Certification"));
						}
					}
				}
					break;

				case "3": { // Update
					System.out.print("IID to update: ");
					int iid;
					try {
					    iid = Integer.parseInt(scanner.nextLine());
					    if (iid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Employee ID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Certification (1/2/3): ");
					int cert;
					try {
					    cert = Integer.parseInt(scanner.nextLine());
					    if (cert == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "UPDATE Instructor SET EID = ?, Certification = ? WHERE IID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, eid);
						ps.setInt(2, cert);
						ps.setInt(3, iid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Instructor updated." : "No such IID.");
					}
				}
					break;

				case "4": { // Delete
					System.out.print("IID to delete: ");
					int iid;
					try {
					    iid = Integer.parseInt(scanner.nextLine());
					    if (iid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM Instructor WHERE IID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, iid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Instructor deleted." : "No such IID.");
					}
				}
					break;

				case "5": { // By ID
					System.out.print("IID: ");
					int iid;
					try {
					    iid = Integer.parseInt(scanner.nextLine());
					    if (iid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM Instructor WHERE IID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, iid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %d%n", rs.getInt("IID"), rs.getInt("EID"),
										rs.getInt("Certification"));
							} else {
								System.out.println("No instructor found.");
							}
						}
					}
				}
					break;

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
	 * This method opens a menu for managing employee records in the database. Users
	 * can add, view, delete, update, or look up employees by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void employeeMenu() {
		while (true) {
			System.out.println("-- Employee  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("EID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Property ID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("First Name: ");
					String fn = scanner.nextLine();
					System.out.print("Last Name: ");
					String ln = scanner.nextLine();
					System.out.print("Position: ");
					String pos = scanner.nextLine();
					System.out.print("Education: ");
					String edu = scanner.nextLine();
					System.out.print("Gender: ");
					String gen = scanner.nextLine();
					System.out.print("Age: ");
					int age;
					try {
					    age = Integer.parseInt(scanner.nextLine());
					    if (age == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Start Date (yyyy-mm-dd): ");
					Date sd;
					try {
					    String input = scanner.nextLine();
					    if (input.equals("-1")) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					    sd = Date.valueOf(input);
					} catch (IllegalArgumentException e) {
					    System.out.println("Error: Invalid date format. Use yyyy-mm-dd.");
					    break;
					}
					System.out.print("Monthly Salary: ");
					BigDecimal sal;
					try {
					    sal = new BigDecimal(scanner.nextLine());
					    if (sal.compareTo(new BigDecimal(-1)) == 0) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: Price must be a valid number.");
					    break;
					}
					String sql = "INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
				}
					break;
				case "2": { // List
					String sql = "SELECT EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary "
							+ "FROM Employee";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("EID | PID | First | Last | Pos | Edu | Gen | Age | Start | Salary");
						while (rs.next()) {
							System.out.printf("%d | %d | %s | %s | %s | %s | %s | %d | %s | %s%n", rs.getInt("EID"),
									rs.getInt("PID"), rs.getString("FirstName"), rs.getString("LastName"),
									rs.getString("Position"), rs.getString("Education"), rs.getString("Gender"),
									rs.getInt("Age"), rs.getDate("StartDate"), rs.getBigDecimal("MonthlySalary"));
						}
					}
				}
					break;
				case "3": { // Update
					System.out.print("EID to update: ");
					int eid = Integer.parseInt(scanner.nextLine());
					System.out.print("New Position: ");
					String pos = scanner.nextLine();
					System.out.print("New Education: ");
					String edu = scanner.nextLine();
					String sql = "UPDATE Employee SET Position = ?, Education = ? WHERE EID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setString(1, pos);
						ps.setString(2, edu);
						ps.setInt(3, eid);
						ps.executeUpdate();
					}
					System.out.println("Employee updated.");
				}
					break;
				case "4": { // Delete
					System.out.print("EID to delete: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM Employee WHERE EID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, eid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Employee deleted." : "No employee found.");
					}
				}
					break;
				case "5": { // By ID
					System.out.print("EID: ");
					int eid;
					try {
					    eid = Integer.parseInt(scanner.nextLine());
					    if (eid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM Employee WHERE EID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, eid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %d | %s | %s | %s | %s | %s | %d | %s | %s%n", rs.getInt("EID"),
										rs.getInt("PID"), rs.getString("FirstName"), rs.getString("LastName"),
										rs.getString("Position"), rs.getString("Education"), rs.getString("Gender"),
										rs.getInt("Age"), rs.getDate("StartDate"), rs.getBigDecimal("MonthlySalary"));
							} else {
								System.out.println("No employee found.");
							}
						}
					}
				}
					break;
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
	 * This method opens a menu for managing trail records in the database. Users
	 * can add, view, delete, or look up trail by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void trailMenu() {
		while (true) {
			System.out.println("-- Trail  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Create
					System.out.print("TID: ");
					int tid;
					try {
					    tid = Integer.parseInt(scanner.nextLine());
					    if (tid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Name: ");
					String name = scanner.nextLine();
					System.out.print("Start Location: ");
					String start = scanner.nextLine();
					System.out.print("End Location: ");
					String end = scanner.nextLine();
					System.out.print("Status (1=open, 0=closed): ");
					int st;
					try {
					    st = Integer.parseInt(scanner.nextLine());
					    if (st == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Difficulty: ");
					String diff = scanner.nextLine();
					System.out.print("Category: ");
					String cat = scanner.nextLine();
					String sql = "INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
				}
					break;

				case "2": { // List all
					String sql = "SELECT TID, Name, StartLocation, EndLocation, Status, Difficulty, Category FROM Trail ORDER BY TID ASC";
					try (Statement stt = conn.createStatement(); ResultSet rs = stt.executeQuery(sql)) {
						System.out.println("TID | Name | Start | End | Status | Difficulty | Category");
						while (rs.next()) {
							int s = rs.getInt("Status");
							String stat = (s == 1 ? "Open" : "Closed");
							System.out.printf("%d | %s | %s | %s | %s | %s | %s%n", rs.getInt("TID"),
									rs.getString("Name"), rs.getString("StartLocation"), rs.getString("EndLocation"),
									stat, rs.getString("Difficulty"), rs.getString("Category"));
						}
					}
				}
					break;

				case "3": { // Update status
					System.out.print("TID to update: ");
					int tid;
					try {
					    tid = Integer.parseInt(scanner.nextLine());
					    if (tid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Status (1=open, 0=closed): ");
					int st;
					try {
					    st = Integer.parseInt(scanner.nextLine());
					    if (st == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "UPDATE Trail SET Status = ? WHERE TID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, st);
						ps.setInt(2, tid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Trail status updated." : "No such TID.");
					}
				}
					break;

				case "4": { // Delete
					System.out.print("TID to delete: ");
					int tid;
					try {
					    tid = Integer.parseInt(scanner.nextLine());
					    if (tid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM Trail WHERE TID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, tid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Trail deleted." : "No such TID.");
					}
				}
					break;

				case "5": { // By ID
					System.out.print("TID: ");
					int tid;
					try {
					    tid = Integer.parseInt(scanner.nextLine());
					    if (tid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM Trail WHERE TID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, tid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								int s = rs.getInt("Status");
								String stat = (s == 1 ? "Open" : "Closed");
								System.out.printf("%d | %s | %s | %s | %s | %s | %s%n", rs.getInt("TID"),
										rs.getString("Name"), rs.getString("StartLocation"),
										rs.getString("EndLocation"), stat, rs.getString("Difficulty"),
										rs.getString("Category"));
							} else {
								System.out.println("No trail found.");
							}
						}
					}
				}
					break;

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
	 * This method opens a menu for managing lift records in the database. Users can
	 * add, view, update, delete, or look up lift by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void liftMenu() {
		while (true) {
			System.out.println("-- Lift -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Create
					System.out.print("LiftID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Name: ");
					String name = scanner.nextLine();
					System.out.print("Open Time (HH24:MI): ");
					String open = scanner.nextLine();
					System.out.print("Close Time (HH24:MI): ");
					String close = scanner.nextLine();
					System.out.print("Status (1=open, 0=closed): ");
					int status;
					try {
					    status = Integer.parseInt(scanner.nextLine());
					    if (status == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status) "
							+ "VALUES (?, ?, ?, ?, ?)";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						ps.setString(2, name);
						ps.setString(3, open);
						ps.setString(4, close);
						ps.setInt(5, status);
						ps.executeUpdate();
					}
					System.out.println("Lift added.");
				}
					break;

				case "2": { // List all
					String sql = "SELECT LiftID, Name, OpenTime, CloseTime, Status FROM Lift ORDER BY LiftID ASC";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("LiftID | Name | Open  | Close | Status");
						while (rs.next()) {
							int s = rs.getInt("Status");
							String stat = (s == 1 ? "Open" : "Closed");
							System.out.printf("%d | %s | %s | %s | %s%n", rs.getInt("LiftID"), rs.getString("Name"),
									rs.getString("OpenTime"), rs.getString("CloseTime"), stat);
						}
					}
				}
					break;

				case "3": { // Update status
					System.out.print("LiftID to update: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Status (1=open, 0=closed): ");
					int status;
					try {
					    status = Integer.parseInt(scanner.nextLine());
					    if (status == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "UPDATE Lift SET Status = ? WHERE LiftID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, status);
						ps.setInt(2, lid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Lift status updated." : "No such LiftID.");
					}
				}
					break;

				case "4": { // Delete
					System.out.print("LiftID to delete: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "DELETE FROM Lift WHERE LiftID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Lift deleted." : "No such LiftID.");
					}
				}
					break;

				case "5": { // By ID
					System.out.print("LiftID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "SELECT * FROM Lift WHERE LiftID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								int s = rs.getInt("Status");
								String stat = (s == 1 ? "Open" : "Closed");
								System.out.printf("%d | %s | %s | %s | %s%n", rs.getInt("LiftID"), rs.getString("Name"),
										rs.getString("OpenTime"), rs.getString("CloseTime"), stat);
							} else {
								System.out.println("No lift found.");
							}
						}
					}
				}
					break;

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
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void liftUsageMenu() {
		while (true) {
			System.out.println("-- Lift Usage  -- 1)Add 2)List 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Record a ride
					System.out.print("UsageID: ");
					int uid;
					try {
					    uid = Integer.parseInt(scanner.nextLine());
					    if (uid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("PassID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("LiftID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = """
							INSERT INTO LiftUsage (UsageID, PID, LiftID, DateTimeOfUse)
							VALUES (?, ?, ?, CURRENT_TIMESTAMP)
							""";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, uid);
						ps.setInt(2, pid);
						ps.setInt(3, lid);
						ps.executeUpdate();
					}
					System.out.println("Lift usage recorded.");
				}
					break;
				case "2": { // List all logs
					String sql = "SELECT UsageID, PID, LiftID, DateTimeOfUse FROM LiftUsage ORDER BY UsageID ASC";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("UID | PID | LiftID | DateTime");
						while (rs.next()) {
							System.out.printf("%d | %d | %d | %s%n", rs.getInt("UsageID"), rs.getInt("PID"),
									rs.getInt("LiftID"), rs.getTimestamp("DateTimeOfUse"));
						}
					}
				}
					break;
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
	 * This method opens a menu which prompts the user to select a query they would
	 * like to perform. The queries are based on predetermined and specific searches
	 * set by the project scope.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void queryMenu() {
		while (true) {
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
				case "1": {
					System.out.print("Member ID: ");
					int mid;
					try {
					    mid = Integer.parseInt(scanner.nextLine());
					    if (mid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = """
							SELECT
							    m.FirstName AS MemFirst,
							    m.LastName AS MemLast,
							    lo.NumberOfSessions,
							    lo.SessionsLeft,
							    e.FirstName AS InstrFirst,
							    e.LastName AS InstrLast,
							    l.TimeOfClass
							FROM pruiz2.LessonOrder lo
							JOIN pruiz2.Lesson l ON lo.LID = l.LID
							JOIN pruiz2.Instructor i ON l.IID = i.IID
							JOIN pruiz2.Employee e ON i.EID = e.EID
							JOIN pruiz2.Member m ON lo.MID = m.MID
							WHERE m.MID = ?
							ORDER BY lo.OID""";

					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, mid);

						try (ResultSet rs = ps.executeQuery()) {
							System.out.println(
									"MemFirst | MemLast | #Purchased | #Left | InstrFirst | InstrLast | TimeOfClass");
							while (rs.next()) {
								System.out.printf("%s | %s | %d | %d | %s | %s | %s%n", rs.getString(1),
										rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6),
										rs.getTimestamp(7));
							}
						}
					}
				}
					break;

				case "2": {
					System.out.print("Pass ID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
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
								System.out.printf("%s | %s%n", rs.getString(1), rs.getTimestamp(2));
							}
						}
					}
					// equipment rentals
					System.out.println("=== Equipment Rentals ===");
					String sql2 = """
							SELECT eq.Type, er.Status, er.RentalDateTime
							  FROM pruiz2.EquipmentRental er
							  JOIN pruiz2.Equipment eq ON er.EID = eq.EID
							 WHERE er.PID = ?
							""";
					try (PreparedStatement ps = conn.prepareStatement(sql2)) {
						ps.setInt(1, pid);
						try (ResultSet rs = ps.executeQuery()) {
							System.out.println("EquipmentType | Status | RentalDateTime");
							while (rs.next()) {
								System.out.printf("%s | %d | %s%n", rs.getString(1), rs.getInt(2), rs.getTimestamp(3));
							}
						}
					}
				}
					break;

				case "3": {
					String sql = """
							SELECT t.Name, t.Category, lf.Name
							  FROM pruiz2.Trail t
							  JOIN pruiz2.LiftTrail lt ON t.TID = lt.TID
							  JOIN pruiz2.Lift lf      ON lt.LiftID = lf.LiftID
							 WHERE t.Difficulty = 'Intermediate'
							   AND t.Status = 1
							   AND lf.Status = 1
							""";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("TrailName | Category | LiftName");
						while (rs.next()) {
							System.out.printf("%s | %s | %s%n", rs.getString(1), rs.getString(2), rs.getString(3));
						}
					}
				}
					break;

				case "4": {
					System.out.print("Lesson ID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = """
							SELECT i.Certification
							  FROM pruiz2.Instructor i
							  JOIN pruiz2.Lesson l ON i.IID = l.IID
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
				}
					break;

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
	 * This method opens a menu for managing property records in the database. Users
	 * can add, view, update, delete, or look up property by ID.
	 * 
	 * Parameters: None Returns: void -- Doesn’t return anything. It handles user
	 * actions and updates the database.
	 */
	private void propertyMenu() {
		while (true) {
			System.out.println("-- Property  -- 1)Add 2)List 3)Upd 4)Del 5)ByID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add
					System.out.print("Property ID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Type: ");
					String ty = scanner.nextLine();
					System.out.print("Name: ");
					String nm = scanner.nextLine();
					System.out.print("Monthly Income: ");
					BigDecimal inc;
					try {
					    inc = new BigDecimal(scanner.nextLine());
					    if (inc.compareTo(new BigDecimal(-1)) == 0) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: Price must be a valid number.");
					    break;
					}

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
				}
					break;

				case "2": { // List all
					String sql = "SELECT PropertyID, Type, Name, MonthlyIncome FROM Property ORDER BY PropertyID ASC";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("PID | Type | Name | Income");
						while (rs.next()) {
							System.out.printf("%d | %s | %s | %s%n", rs.getInt("PropertyID"), rs.getString("Type"),
									rs.getString("Name"), rs.getBigDecimal("MonthlyIncome"));
						}
					}
				}
					break;

				case "3": { // Update income
					System.out.print("Property ID to update: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Monthly Income: ");
					BigDecimal inc;
					try {
					    inc = new BigDecimal(scanner.nextLine());
					    if (inc.compareTo(new BigDecimal(-1)) == 0) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: Price must be a valid number.");
					    break;
					}

					String sql = "UPDATE Property SET MonthlyIncome = ? WHERE PropertyID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setBigDecimal(1, inc);
						ps.setInt(2, pid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Property updated." : "No such PropertyID.");
					}
				}
					break;

				case "4": { // Delete
					System.out.print("Property ID to delete: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "DELETE FROM Property WHERE PropertyID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, pid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Property deleted." : "No such PropertyID.");
					}
				}
					break;

				case "5": { // By ID
					System.out.print("Property ID: ");
					int pid;
					try {
					    pid = Integer.parseInt(scanner.nextLine());
					    if (pid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM Property WHERE PropertyID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, pid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %s | %s | %s%n", rs.getInt("PropertyID"), rs.getString("Type"),
										rs.getString("Name"), rs.getBigDecimal("MonthlyIncome"));
							} else {
								System.out.println("No property found.");
							}
						}
					}
				}
					break;

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
	 * Method lesson()
	 *
	 * This method opens a menu for managing lesson records in the database. Users
	 * can add new lessons, update existing ones, delete lessons, and view lessons
	 * by ID or list all available lessons.
	 *
	 * Parameters: None Returns: void -- Doesn't return anything. It handles user
	 * actions and updates the database.
	 */
	private void lessonMenu() {
		while (true) {
			System.out.println("-- Lesson -- 1)Add 2)List All 3)Update 4)Delete 5)View By ID 0)Back");
			System.out.print("> ");
			String c = scanner.nextLine().trim();
			try {
				switch (c) {
				case "1": { // Add new lesson
					System.out.print("Lesson ID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("Type (Private/Group): ");
					String type = scanner.nextLine();
					System.out.print("Difficulty (Beginner/Intermediate/Expert): ");
					String difficulty = scanner.nextLine();
					System.out.print("Time of Class (YYYY-MM-DD HH:MM:SS): ");
					String timeOfClass = scanner.nextLine();
					System.out.print("Duration (minutes): ");
					int duration = Integer.parseInt(scanner.nextLine());
					System.out.print("Price: ");
					double price = Double.parseDouble(scanner.nextLine());
					System.out.print("Instructor ID: ");
					int iid;
					try {
					    iid = Integer.parseInt(scanner.nextLine());
					    if (iid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					String sql = "INSERT INTO pruiz2.Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID) "
							+ "VALUES (?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?)";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						ps.setString(2, type);
						ps.setString(3, difficulty);
						ps.setString(4, timeOfClass);
						ps.setInt(5, duration);
						ps.setDouble(6, price);
						ps.setInt(7, iid);
						ps.executeUpdate();
					}
					System.out.println("Lesson added successfully.");
				}
					break;
				case "2": { // List all lessons
					String sql = "SELECT l.LID, l.Type, l.Difficulty, l.TimeOfClass, l.Duration, l.Price, "
							+ "l.IID, i.Certification " + "FROM pruiz2.Lesson l "
							+ "LEFT JOIN pruiz2.Instructor i ON l.IID = i.IID " + "ORDER BY l.TimeOfClass";
					try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
						System.out.println("LID | Type | Difficulty | Time | Duration | Price | IID | Certification");
						System.out.println("------------------------------------------------------------------");
						while (rs.next()) {
							System.out.printf("%d | %s | %s | %s | %d mins | $%.2f | %d | %s%n", rs.getInt("LID"),
									rs.getString("Type"), rs.getString("Difficulty"), rs.getTimestamp("TimeOfClass"),
									rs.getInt("Duration"), rs.getDouble("Price"), rs.getInt("IID"),
									rs.getInt("Certification"));
						}
					}
				}
					break;
				case "3": { // Update lesson
					System.out.print("Lesson ID to update: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					System.out.print("New Type: ");
					String type = scanner.nextLine();
					System.out.print("New Difficulty: ");
					String difficulty = scanner.nextLine();
					System.out.print("New Duration: ");
					int duration = Integer.parseInt(scanner.nextLine());
					System.out.print("New Price: ");
					double price = Double.parseDouble(scanner.nextLine());

					String sql = "UPDATE pruiz2.Lesson SET Type = ?, Difficulty = ?, "
							+ "Duration = ?, Price = ? WHERE LID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setString(1, type);
						ps.setString(2, difficulty);
						ps.setInt(3, duration);
						ps.setDouble(4, price);
						ps.setInt(5, lid);
						int rowsUpdated = ps.executeUpdate();
						if (rowsUpdated > 0) {
							System.out.println("Lesson updated successfully.");
						} else {
							System.out.println("No lesson found with that ID.");
						}
					}
				}
					break;
				case "4": { // Delete lesson
					System.out.print("Lesson ID to delete: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}

					// Check if any lesson orders reference this lesson
					String checkOrdersSql = "SELECT COUNT(*) FROM pruiz2.LessonOrder WHERE LID = ?";
					try (PreparedStatement ps = conn.prepareStatement(checkOrdersSql)) {
						ps.setInt(1, lid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next() && rs.getInt(1) > 0) {
								System.out.println(
										"Cannot delete lesson - it has associated orders. Delete the orders first.");
								break;
							}
						}
					}

					String sql = "DELETE FROM pruiz2.Lesson WHERE LID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						int cnt = ps.executeUpdate();
						System.out.println(cnt > 0 ? "Lesson deleted." : "No lesson found with that ID.");
					}
				}
					break;
				case "5": { // View by ID
					System.out.print("Lesson ID: ");
					int lid;
					try {
					    lid = Integer.parseInt(scanner.nextLine());
					    if (lid == -1) {
					        System.out.println("Operation cancelled.");
					        break;
					    }
					} catch (NumberFormatException e) {
					    System.out.println("Error: MID must be a valid integer.");
					    break;
					}
					String sql = "SELECT * FROM pruiz2.Lesson WHERE LID = ?";
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setInt(1, lid);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								System.out.printf("%d | %s | %s | %s | %d | %.2f | %d%n", rs.getInt("LID"),
										rs.getString("Type"), rs.getString("Difficulty"),
										rs.getTimestamp("TimeOfClass"), rs.getInt("Duration"), rs.getDouble("Price"),
										rs.getInt("IID"));
							} else {
								System.out.println("No lesson found with that ID.");
							}
						}
					}
				}
					break;
				case "0":
					return;
				default:
					System.out.println("Invalid choice.");
				}
			} catch (SQLException e) {
				System.out.println("Database error: " + e.getMessage());
			} catch (NumberFormatException e) {
				System.out.println("Invalid number entered. Please try again.");
			}
		}
	}
}

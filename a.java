
package com.resort.util;
import java.sql.*;
public class DBConnection {
    private static final String URL  = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:ORCL";
    private static final String USER = "<USER>";
    private static final String PASS = "<PASS>";
    static { try { Class.forName("oracle.jdbc.driver.OracleDriver"); } catch(Exception e){ throw new RuntimeException(e);} }
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL,USER,PASS); }
}

package com.resort.model;
import java.sql.*;
import java.math.BigDecimal;

public class Member {
    private int mid;
    private String name;
    private String phone;
    private String email;
    private Date dob;
    private String emergencyContact;

    public Member() {}
    public Member(int mid, String name, String phone, String email, Date dob, String emergencyContact) {
        this.mid = mid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.emergencyContact = emergencyContact;
    }
    public int getMid() { return mid; }
    public void setMid(int mid) { this.mid = mid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
}

public class SkiPass {
    private int pid, mid;
    private String passType, isArchived;
    private Timestamp purchaseTime;
    private Date expirationDate;
    private int totalUses, remainingUses;
    private BigDecimal price;

    public SkiPass() {}
    public SkiPass(int pid, int mid, String passType, Timestamp purchaseTime, Date expirationDate,
                   int totalUses, int remainingUses, BigDecimal price, String isArchived) {
        this.pid = pid;
        this.mid = mid;
        this.passType = passType;
        this.purchaseTime = purchaseTime;
        this.expirationDate = expirationDate;
        this.totalUses = totalUses;
        this.remainingUses = remainingUses;
        this.price = price;
        this.isArchived = isArchived;
    }
    public int getPid() { return pid; }
    public void setPid(int pid) { this.pid = pid; }
    public int getMid() { return mid; }
    public void setMid(int mid) { this.mid = mid; }
    public String getPassType() { return passType; }
    public void setPassType(String passType) { this.passType = passType; }
    public Timestamp getPurchaseTime() { return purchaseTime; }
    public void setPurchaseTime(Timestamp purchaseTime) { this.purchaseTime = purchaseTime; }
    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
    public int getTotalUses() { return totalUses; }
    public void setTotalUses(int totalUses) { this.totalUses = totalUses; }
    public int getRemainingUses() { return remainingUses; }
    public void setRemainingUses(int remainingUses) { this.remainingUses = remainingUses; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getIsArchived() { return isArchived; }
    public void setIsArchived(String isArchived) { this.isArchived = isArchived; }
}

public class Equipment {
    private int eid;
    private String type;
    private int sizeOrLength;
    private String status;

    public Equipment() {}
    public Equipment(int eid, String type, int sizeOrLength, String status) {
        this.eid = eid;
        this.type = type;
        this.sizeOrLength = sizeOrLength;
        this.status = status;
    }
    public int getEid() { return eid; }
    public void setEid(int eid) { this.eid = eid; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getSizeOrLength() { return sizeOrLength; }
    public void setSizeOrLength(int sizeOrLength) { this.sizeOrLength = sizeOrLength; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

public class EquipmentRental {
    private int rid, mid, pid, eid;
    private Timestamp rentalTime;
    private String status;

    public EquipmentRental() {}
    public EquipmentRental(int rid, int mid, int pid, int eid, Timestamp rentalTime, String status) {
        this.rid = rid;
        this.mid = mid;
        this.pid = pid;
        this.eid = eid;
        this.rentalTime = rentalTime;
        this.status = status;
    }
    public int getRid() { return rid; }
    public void setRid(int rid) { this.rid = rid; }
    public int getMid() { return mid; }
    public void setMid(int mid) { this.mid = mid; }
    public int getPid() { return pid; }
    public void setPid(int pid) { this.pid = pid; }
    public int getEid() { return eid; }
    public void setEid(int eid) { this.eid = eid; }
    public Timestamp getRentalTime() { return rentalTime; }
    public void setRentalTime(Timestamp rentalTime) { this.rentalTime = rentalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

public class LessonOrder {
    private int oid, mid, lid, numberOfSessions, sessionsLeft;
    private Timestamp purchaseDate;

    public LessonOrder() {}
    public LessonOrder(int oid, int mid, int lid, int numberOfSessions, int sessionsLeft, Timestamp purchaseDate) {
        this.oid = oid;
        this.mid = mid;
        this.lid = lid;
        this.numberOfSessions = numberOfSessions;
        this.sessionsLeft = sessionsLeft;
        this.purchaseDate = purchaseDate;
    }
    public int getOid() { return oid; }
    public void setOid(int oid) { this.oid = oid; }
    public int getMid() { return mid; }
    public void setMid(int mid) { this.mid = mid; }
    public int getLid() { return lid; }
    public void setLid(int lid) { this.lid = lid; }
    public int getNumberOfSessions() { return numberOfSessions; }
    public void setNumberOfSessions(int numberOfSessions) { this.numberOfSessions = numberOfSessions; }
    public int getSessionsLeft() { return sessionsLeft; }
    public void setSessionsLeft(int sessionsLeft) { this.sessionsLeft = sessionsLeft; }
    public Timestamp getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Timestamp purchaseDate) { this.purchaseDate = purchaseDate; }
}

public class Lesson {
    private int lid;
    private String type, difficulty;
    private Timestamp timeOfClass;
    private int duration;
    private BigDecimal price;
    private int iid;

    public Lesson() {}
    public Lesson(int lid, String type, String difficulty, Timestamp timeOfClass, int duration, BigDecimal price, int iid) {
        this.lid = lid;
        this.type = type;
        this.difficulty = difficulty;
        this.timeOfClass = timeOfClass;
        this.duration = duration;
        this.price = price;
        this.iid = iid;
    }
    public int getLid() { return lid; }
    public void setLid(int lid) { this.lid = lid; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Timestamp getTimeOfClass() { return timeOfClass; }
    public void setTimeOfClass(Timestamp timeOfClass) { this.timeOfClass = timeOfClass; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getIid() { return iid; }
    public void setIid(int iid) { this.iid = iid; }
}

public class Instructor {
    private int iid, eid;
    private String certification;

    public Instructor() {}
    public Instructor(int iid, int eid, String certification) {
        this.iid = iid;
        this.eid = eid;
        this.certification = certification;
    }
    public int getIid() { return iid; }
    public void setIid(int iid) { this.iid = iid; }
    public int getEid() { return eid; }
    public void setEid(int eid) { this.eid = eid; }
    public String getCertification() { return certification; }
    public void setCertification(String certification) { this.certification = certification; }
}

public class Employee {
    private int eid, propertyId;
    private String name, position, demographics;
    private Date startDate;
    private BigDecimal monthlySalary;

    public Employee() {}
    public Employee(int eid, int propertyId, String name, String position, String demographics, Date startDate, BigDecimal monthlySalary) {
        this.eid = eid;
        this.propertyId = propertyId;
        this.name = name;
        this.position = position;
        this.demographics = demographics;
        this.startDate = startDate;
        this.monthlySalary = monthlySalary;
    }
    public int getEid() { return eid; }
    public void setEid(int eid) { this.eid = eid; }
    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getDemographics() { return demographics; }
    public void setDemographics(String demographics) { this.demographics = demographics; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public BigDecimal getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(BigDecimal monthlySalary) { this.monthlySalary = monthlySalary; }
}

public class Trail {
    private int tid;
    private String name, startLocation, endLocation, status, difficulty, category;

    public Trail() {}
    public Trail(int tid, String name, String startLocation, String endLocation, String status, String difficulty, String	category) {
        this.tid = tid;
        this.name = name;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = status;
        this.difficulty = difficulty;
        this.category = category;
    }
    public int getTid() { return tid; }
    public void setTid(int tid) { this.tid = tid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

public class Lift {
    private int liftId;
    private String name, openTime, closeTime, status, abilityLevel;

    public Lift() {}
    public Lift(int liftId, String name, String	openTime, String closeTime, String status, String abilityLevel) {
        this.liftId = liftId;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.abilityLevel = abilityLevel;
    }
    public int getLiftId() { return liftId; }
    public void setLiftId(int liftId) { this.liftId = liftId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOpenTime() { return openTime; }
    public void setOpenTime(String openTime) { this.openTime = openTime; }
    public String getCloseTime() { return closeTime; }
    public void setCloseTime(String closeTime) { this.closeTime = closeTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAbilityLevel() { return abilityLevel; }
    public void setAbilityLevel(String abilityLevel) { this.abilityLevel = abilityLevel; }
}

public class LiftUsage {
    private int uid, pid, liftId;
    private Timestamp dateTimeOfUse;

    public LiftUsage() {}
    public LiftUsage(int uid, int pid, int liftId, Timestamp dateTimeOfUse) {
        this.uid = uid;
        this.pid = pid;
        this.liftId = liftId;
        this.dateTimeOfUse = dateTimeOfUse;
    }
    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }
    public int getPid() { return pid; }
    public void setPid(int pid) { this.pid = pid; }
    public int getLiftId() { return liftId; }
    public void setLiftId(int liftId) { this.liftId = liftId; }
    public Timestamp getDateTimeOfUse() { return dateTimeOfUse; }
    public void setDateTimeOfUse(Timestamp dateTimeOfUse) { this.dateTimeOfUse = dateTimeOfUse; }
}

package com.resort.dao;

import com.resort.model.Instructor;
import java.sql.*;
import java.util.*;

public class InstructorDAO {
    private final Connection conn;
    public InstructorDAO(Connection conn) { this.conn = conn; }

    public void insert(Instructor i) throws SQLException {
        String sql = "INSERT INTO Instructor (IID, EID, Certification) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, i.getIid());
            ps.setInt(2, i.getEid());
            ps.setString(3, i.getCertification());
            ps.executeUpdate();
        }
    }

    public void update(Instructor i) throws SQLException {
        String sql = "UPDATE Instructor SET EID = ?, Certification = ? WHERE IID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, i.getEid());
            ps.setString(2, i.getCertification());
            ps.setInt(3, i.getIid());
            ps.executeUpdate();
        }
    }

    public void delete(int iid) throws SQLException {
        String sql = "DELETE FROM Instructor WHERE IID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, iid);
            ps.executeUpdate();
        }
    }

    public Instructor findById(int iid) throws SQLException {
        String sql = "SELECT * FROM Instructor WHERE IID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, iid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Instructor(
                        rs.getInt("IID"),
                        rs.getInt("EID"),
                        rs.getString("Certification")
                    );
                }
            }
        }
        return null;
    }

    public List<Instructor> findAll() throws SQLException {
        List<Instructor> list = new ArrayList<>();
        String sql = "SELECT * FROM Instructor";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Instructor(
                    rs.getInt("IID"),
                    rs.getInt("EID"),
                    rs.getString("Certification")
                ));
            }
        }
        return list;
    }
}

package com.resort.main;

import com.resort.util.DBConnection;
import com.resort.dao.*;
import com.resort.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Connection conn;

    public static void main(String[] args) throws Exception {
        conn = DBConnection.getConnection();
        MemberDAO mdao = new MemberDAO(conn);
        SkiPassDAO pdao = new SkiPassDAO(conn);
        EquipmentDAO edao = new EquipmentDAO(conn);
        RentalDAO rdao = new RentalDAO(conn);
        LessonOrderDAO ldao = new LessonOrderDAO(conn);
        LessonDAO lessonDao = new LessonDAO(conn);
        InstructorDAO idao = new InstructorDAO(conn);
        EmployeeDAO empDao = new EmployeeDAO(conn);
        TrailDAO tdao = new TrailDAO(conn);
        LiftDAO lftDao = new LiftDAO(conn);
        LiftUsageDAO luDao = new LiftUsageDAO(conn);

        while (true) {
            showMain();
            int c = Integer.parseInt(scanner.nextLine());
            switch (c) {
                case 1: memberMenu(mdao);           break;
                case 2: passMenu(pdao);             break;
                case 3: equipmentMenu(edao);        break;
                case 4: rentalMenu(rdao);           break;
                case 5: lessonMenu(ldao);           break;
                case 6: instructorMenu(idao);       break;
                case 7: employeeMenu(empDao);       break;
                case 8: trailMenu(tdao);            break;
                case 9: liftMenu(lftDao);           break;
                case 10: liftUsageMenu(luDao);      break;
                case 11: queryMenu(mdao, pdao);     break;
                case 0: conn.close(); return;
                default: System.out.println("Invalid");            
            }
        }
    }

    private static void showMain() {
        System.out.println("=== Ski Resort Management ===");
        System.out.println("1) Member  2) Pass  3) Equipment  4) Rental  5) LessonPurch");
        System.out.println("6) Instructor 7) Employee 8) Trail 9) Lift 10) LiftUsage 11) Queries  0) Exit");
        System.out.print("> ");
    }


    private static void instructorMenu(InstructorDAO dao) {
        while (true) {
            System.out.println("-- Instructor -- 1) Add 2)Upd 3)Del 4)All 5)ByID 0)Back");
            int c = Integer.parseInt(scanner.nextLine());
            try {
                switch (c) {
                    case 1:
                        System.out.print("IID>"); int iid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Employee ID>"); int eid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Certification>"); String cert = scanner.nextLine();
                        dao.insert(new Instructor(iid, eid, cert));
                        System.out.println("Added");
                        break;
                    case 2:
                        System.out.print("IID to update>"); iid = Integer.parseInt(scanner.nextLine());
                        Instructor ins = dao.findById(iid);
                        if (ins==null) { System.out.println("Not found"); break; }
                        System.out.print("Certification("+ins.getCertification()+")>");
                        String nc = scanner.nextLine(); if (!nc.isEmpty()) ins.setCertification(nc);
                        dao.update(ins); System.out.println("Updated");
                        break;
                    case 3:
                        System.out.print("IID to delete>"); iid = Integer.parseInt(scanner.nextLine());
                        dao.delete(iid); System.out.println("Deleted");
                        break;
                    case 4:
                        List<Instructor> allI = dao.findAll();
                        allI.forEach(i->System.out.println(i.getIid()+" eid="+i.getEid()+" cert="+i.getCertification()));
                        break;
                    case 5:
                        System.out.print("IID>"); iid = Integer.parseInt(scanner.nextLine());
                        Instructor i2 = dao.findById(iid);
                        System.out.println(i2!=null ? i2.getIid()+" eid="+i2.getEid()+" cert="+i2.getCertification() : "N/A");
                        break;
                    case 0: return;
                }
            } catch(Exception e){ System.out.println("Err: "+e.getMessage()); }
        }
    }

    private static void employeeMenu(EmployeeDAO dao) {
        while (true) {
            System.out.println("
-- Employee -- 1) Add 2)Upd 3)Del 4)All 5)ByID 0)Back");
            int c = Integer.parseInt(scanner.nextLine());
            try {
                switch(c) {
                    case 1:
                        System.out.print("EID>"); int eid = Integer.parseInt(scanner.nextLine());
                        System.out.print("PropertyID>"); int pid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Name>"); String nm = scanner.nextLine();
                        System.out.print("Position>"); String pos = scanner.nextLine();
                        System.out.print("Demographics>"); String d = scanner.nextLine();
                        System.out.print("StartDate (yyyy-mm-dd)>"); Date sd = Date.valueOf(scanner.nextLine());
                        System.out.print("Salary>"); BigDecimal sal = new BigDecimal(scanner.nextLine());
                        dao.insert(new Employee(eid, pid, nm, pos, d, sd, sal)); System.out.println("Added");
                        break;
                    case 2:
                        System.out.print("EID to update>"); eid = Integer.parseInt(scanner.nextLine());
                        Employee e2 = dao.findById(eid);
                        if(e2==null){ System.out.println("Not found"); break;}                        
                        System.out.print("Position("+e2.getPosition()+")>"); nm=scanner.nextLine(); if(!nm.isEmpty()) e2.setPosition(nm);
                        dao.update(e2); System.out.println("Updated");
                        break;
                    case 3:
                        System.out.print("EID to delete>"); eid=Integer.parseInt(scanner.nextLine()); dao.delete(eid); System.out.println("Deleted"); break;
                    case 4:
                        List<Employee> emps = dao.findAll(); emps.forEach(emp->System.out.println(emp.getEid()+" " + emp.getName())); break;
                    case 5:
                        System.out.print("EID>"); eid=Integer.parseInt(scanner.nextLine());
                        Employee e3=dao.findById(eid); System.out.println(e3!=null? e3.getEid()+" " + e3.getName():"N/A"); break;
                    case 0: return;
                }
            }catch(Exception e){System.out.println("Err: "+e.getMessage());}
        }
    }

    private static void trailMenu(TrailDAO dao) {
        while(true) {
            System.out.println("
-- Trail -- 1) Add 2)Upd 3)Del 4)All 5)ByID 0)Back");
            int c=Integer.parseInt(scanner.nextLine()); try {
                switch(c) {
                    case 1:
                        System.out.print("TID>"); int tid=Integer.parseInt(scanner.nextLine());
                        System.out.print("Name>"); String nm=scanner.nextLine();
                        System.out.print("Start>"); String st=scanner.nextLine();
                        System.out.print("End>"); String en=scanner.nextLine();
                        System.out.print("Status>"); String ss=scanner.nextLine();
                        System.out.print("Difficulty>"); String df=scanner.nextLine();
                        System.out.print("Category>"); String ct=scanner.nextLine();
                        dao.insert(new Trail(tid,nm,st,en,ss,df,ct)); System.out.println("Added");
                        break;
                    case 2:
                        System.out.print("TID to update>"); tid=Integer.parseInt(scanner.nextLine());
                        Trail t2=dao.findById(tid); if(t2==null){System.out.println("Not found");break;}
                        System.out.print("Status("+t2.getStatus()+")>"); ss=scanner.nextLine(); if(!ss.isEmpty()) t2.setStatus(ss);
                        dao.update(t2); System.out.println("Updated"); break;
                    case 3:
                        System.out.print("TID to delete>"); tid=Integer.parseInt(scanner.nextLine()); dao.delete(tid); System.out.println("Deleted"); break;
                    case 4:
                        dao.findAll().forEach(tr->System.out.println(tr.getTid()+" " + tr.getName())); break;
                    case 5:
                        System.out.print("TID>"); tid=Integer.parseInt(scanner.nextLine());
                        Trail t3=dao.findById(tid); System.out.println(t3!=null? t3.getTid()+" " +t3.getName():"N/A"); break;
                    case 0: return;
                }
            }catch(Exception e){System.out.println("Err: "+e.getMessage());}
        }
    }

    private static void liftMenu(LiftDAO dao) {
        while(true) {
            System.out.println("
-- Lift -- 1) Add 2)Upd 3)Del 4)All 5)ByID 0)Back");
            int c=Integer.parseInt(scanner.nextLine()); try {
                switch(c) {
                    case 1:
                        System.out.print("LiftID>"); int lid=Integer.parseInt(scanner.nextLine());
                        System.out.print("Name>"); String nm=scanner.nextLine();
                        System.out.print("OpenTime>"); String ot=scanner.nextLine();
                        System.out.print("CloseTime>"); String ct=scanner.nextLine();
                        System.out.print("Status>"); String ss=scanner.nextLine();
                        System.out.print("AbilityLevel>"); String al=scanner.nextLine();
                        dao.insert(new Lift(lid,nm,ot,ct,ss,al)); System.out.println("Added"); break;
                    case 2:
                        System.out.print("LiftID to update>"); lid=Integer.parseInt(scanner.nextLine());
                        Lift lf=dao.findById(lid); if(lf==null){System.out.println("Not found");break;}
                        System.out.print("Status("+lf.getStatus()+")>"); ss=scanner.nextLine(); if(!ss.isEmpty()) lf.setStatus(ss);
                        dao.update(lf); System.out.println("Updated"); break;
                    case 3:
                        System.out.print("LiftID to delete>"); lid=Integer.parseInt(scanner.nextLine()); dao.delete(lid); System.out.println("Deleted"); break;
                    case 4:
                        dao.findAll().forEach(lf2->System.out.println(lf2.getLiftId()+" " + lf2.getName())); break;
                    case 5:
                        System.out.print("LiftID>"); lid=Integer.parseInt(scanner.nextLine());
                        Lift lf3=dao.findById(lid); System.out.println(lf3!=null? lf3.getLiftId()+" " +lf3.getName():"N/A"); break;
                    case 0: return;
                }
            }catch(Exception e){System.out.println("Err: "+e.getMessage());}
        }
    }

    private static void liftUsageMenu(LiftUsageDAO dao) {
        while(true) {
            System.out.println("
-- Lift Usage -- 1) Add 2)All 0)Back");
            int c=Integer.parseInt(scanner.nextLine());
            try {
                switch(c) {
                    case 1:
                        System.out.print("UID>"); int uid=Integer.parseInt(scanner.nextLine());
                        System.out.print("PassID>"); int pid=Integer.parseInt(scanner.nextLine());
                        System.out.print("LiftID>"); int lid=Integer.parseInt(scanner.nextLine());
                        dao.insert(new LiftUsage(uid,pid,lid,null)); System.out.println("Added");
                        break;
                    case 2:
                        dao.findAll().forEach(lu->System.out.println(lu.getUid()+" pid="+lu.getPid()+" lid="+lu.getLiftId()));
                        break;
                    case 0: return;
                }
            }catch(Exception e){System.out.println("Err: "+e.getMessage());}
        }
    }

    private static void queryMenu(MemberDAO mdao, SkiPassDAO pdao) {
        System.out.println("
--- Queries ---");
        System.out.println("1) List my lessons");
        System.out.println("2) Show pass history");
        System.out.println("3) Open intermediate trails + lifts");
        System.out.println("4) Custom query");
        System.out.print("> ");
        int q = Integer.parseInt(scanner.nextLine());
        switch(q) {
            case 1: listLessons(); break;
            case 2: listPassHistory(); break;
            case 3: listTrailsAndLifts(); break;
            case 4: customQuery(); break;
            default: System.out.println("Invalid");
        }
    }


}

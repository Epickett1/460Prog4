-- ChangeLog
INSERT INTO ChangeLog (ChangeID, Description) VALUES (1, '1');
INSERT INTO ChangeLog (ChangeID, Description) VALUES (2, '1');
INSERT INTO ChangeLog (ChangeID, Description) VALUES (3, '1');
INSERT INTO ChangeLog (ChangeID, Description) VALUES (4, '1');
INSERT INTO ChangeLog (ChangeID, Description) VALUES (5, '1');

-- Property
INSERT INTO Property (PropertyID, Type, Name, MonthlyIncome) VALUES (1, 'Lodge', 'FoxInn', 150000);
INSERT INTO Property (PropertyID, Type, Name, MonthlyIncome) VALUES (2, 'Shop', 'LivingRainbow', 20000);
INSERT INTO Property (PropertyID, Type, Name, MonthlyIncome) VALUES (3, 'RentalCenter', 'RentOCenter', 40000);
INSERT INTO Property (PropertyID, Type, Name, MonthlyIncome) VALUES (4, 'Restaurant', 'GusteausGem', 30000);
INSERT INTO Property (PropertyID, Type, Name, MonthlyIncome) VALUES (5, 'Office', 'Sade', 100000);

-- Member
INSERT INTO Member (MID, FirstName, LastName, PhoneNumber, EmailAddress, DateOfBirth, EmergencyContact)
VALUES (1, 'Liam', 'Dubois', '5204358434', 'liamdubois@aol.com', TO_DATE('2000-03-20', 'YYYY-MM-DD'), '54083727857');
INSERT INTO Member (MID, FirstName, LastName, PhoneNumber, EmailAddress, DateOfBirth, EmergencyContact)
VALUES (2, 'Emma', 'Bernard', '5307597383', 'emmaber@aol.com', TO_DATE('1999-04-20', 'YYYY-MM-DD'), '6356209396');
INSERT INTO Member (MID, FirstName, LastName, PhoneNumber, EmailAddress, DateOfBirth, EmergencyContact)
VALUES (3, 'Matthew', 'Simon', '5405773843', 'matsim@yahoo.com', TO_DATE('1987-02-10', 'YYYY-MM-DD'), '5819076237');
INSERT INTO Member (MID, FirstName, LastName, PhoneNumber, EmailAddress, DateOfBirth, EmergencyContact)
VALUES (4, 'Dylan', 'Dumont', '5555974492', 'dyldum2@gmail.com', TO_DATE('1986-07-11', 'YYYY-MM-DD'), '6118358104');
INSERT INTO Member (MID, FirstName, LastName, PhoneNumber, EmailAddress, DateOfBirth, EmergencyContact)
VALUES (5, 'Samantha', 'Morin', '5604736835', 'sammor43@hotmail.com', TO_DATE('2001-12-12', 'YYYY-MM-DD'), '5159452988');

-- Employee
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (1, 1, 'Zoe', 'Kravitz', 'Instructor', 'Bachelors', 'F', 36, TO_DATE('2024-03-20', 'YYYY-MM-DD'), 3000);
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (2, 2, 'Hedi', 'Laurent', 'Instructor', 'Bachelors', 'M', 56, TO_DATE('2020-04-20', 'YYYY-MM-DD'), 4000);
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (3, 3, 'Brandon', 'Leroy', 'Officer', 'GED', 'M', 30, TO_DATE('2022-01-01', 'YYYY-MM-DD'), 3000);
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (4, 4, 'Alex', 'Fortin', 'Associate', 'HighSchool', 'F', 20, TO_DATE('2024-11-11', 'YYYY-MM-DD'), 2000);
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (5, 5, 'Cole', 'Jay', 'Instructor', 'GED', 'M', 26, TO_DATE('2023-12-10', 'YYYY-MM-DD'), 3500);
INSERT INTO Employee (EID, PID, FirstName, LastName, Position, Education, Gender, Age, StartDate, MonthlySalary)
VALUES (6, 4, 'Auguste', 'Gusteau', 'Chef', 'PHD', 'M', 50, TO_DATE('2015-05-01', 'YYYY-MM-DD'), 3500);

-- Instructor
INSERT INTO Instructor (IID, EID, Certification) VALUES (1, 1, 1);
INSERT INTO Instructor (IID, EID, Certification) VALUES (2, 2, 3);
INSERT INTO Instructor (IID, EID, Certification) VALUES (3, 5, 2);

-- SkiPass
INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (1, 100, 100, TO_TIMESTAMP('2025-05-01 14:37:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-05-03 14:37:00', 'YYYY-MM-DD HH24:MI:SS'), 100, 'OneDay', 1, 1);
INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (2, 500, 500, TO_TIMESTAMP('2025-05-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-11-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 500, 'Season', 2, 1);
INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (3, 200, 200, TO_TIMESTAMP('2025-05-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-05-05 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 200, 'TwoDay', 3, 1);
INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (4, 500, 500, TO_TIMESTAMP('2025-05-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-11-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 500, 'Season', 4, 1);
INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (5, 500, 500, TO_TIMESTAMP('2025-05-01 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-11-01 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 500, 'Season', 5, 1);

-- Trail
INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category)
VALUES (1, 'Monarch', 'TopOfElbert', 'BottomOfElbert', 0, 'Beginner', 'Groomed');
INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category)
VALUES (2, 'ChinaBowl', 'MiddleOfElbert', 'BottomOfElbert', 1, 'Expert', 'Park');
INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category)
VALUES (3, 'Northwoods', 'TopOfBaldy', 'BottomOfBaldy', 1, 'Intermediate', 'Mogul');
INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category)
VALUES (4, 'Ramshom', 'TopOfRamshom', 'MiddleOfRamshom', 0, 'Expert', 'Glade');
INSERT INTO Trail (TID, Name, StartLocation, EndLocation, Status, Difficulty, Category)
VALUES (5, 'Snowman', 'TopOfSnow', 'BottomOfSnow', 1, 'Beginner', 'Mogul');


-- Lift
INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status)
VALUES (1, 'Chair 1', '08:00', '16:00', 1);
INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status)
VALUES (2, 'Gondola A', '07:30', '17:00', 1);
INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status)
VALUES (3, 'Surface Tow', '09:00', '15:00', 1);
INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status)
VALUES (4, 'Magic Carpet', '08:30', '14:30', 1);
INSERT INTO Lift (LiftID, Name, OpenTime, CloseTime, Status)
VALUES (5, 'Chair 5', '10:00', '16:30', 1);

-- LiftTrail
INSERT INTO LiftTrail (TID, LiftID) VALUES (1, 1);
INSERT INTO LiftTrail (TID, LiftID) VALUES (2, 2);
INSERT INTO LiftTrail (TID, LiftID) VALUES (3, 3);
INSERT INTO LiftTrail (TID, LiftID) VALUES (4, 4);
INSERT INTO LiftTrail (TID, LiftID) VALUES (5, 5);

-- Lesson
INSERT INTO Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
VALUES (1, 'Ski', 'Beginner', '10:00', '2h', 80, 1);
INSERT INTO Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
VALUES (2, 'Snowboard', 'Intermediate', '13:00', '1.5h', 90, 2);
INSERT INTO Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
VALUES (3, 'Ski', 'Advanced', '15:00', '2h', 100, 3);
INSERT INTO Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
VALUES (4, 'Snowboard', 'Beginner', '09:00', '1h', 75, 1);
INSERT INTO Lesson (LID, Type, Difficulty, TimeOfClass, Duration, Price, IID)
VALUES (5, 'Ski', 'Expert', '12:00', '3h', 120, 2);

-- LessonOrder
INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (1, 1, 1, 5, 5, TO_DATE('2025-04-01', 'YYYY-MM-DD'));
INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (2, 2, 2, 3, 3, TO_DATE('2025-04-02', 'YYYY-MM-DD'));
INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (3, 3, 3, 4, 4, TO_DATE('2025-04-03', 'YYYY-MM-DD'));
INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (4, 4, 4, 2, 2, TO_DATE('2025-04-04', 'YYYY-MM-DD'));
INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (5, 5, 5, 6, 6, TO_DATE('2025-04-05', 'YYYY-MM-DD'));

-- Equipment
INSERT INTO Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
VALUES (1, 1, 1, 150, 0, 1);
INSERT INTO Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
VALUES (2, 2, 1, 160, 0, 2);
INSERT INTO Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
VALUES (3, 1, 0, 145, 1, 3);
INSERT INTO Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
VALUES (4, 3, 1, 120, 0, 4);
INSERT INTO Equipment (EID, Type, Status, SizeOrLength, IsArchived, ChangeID)
VALUES (5, 1, 1, 155, 0, 5);

-- EquipmentRental
INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (1, 1, 1, 1, TO_TIMESTAMP('2025-05-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (2, 2, 2, 2, TO_TIMESTAMP('2025-05-01 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (3, 3, 3, 3, TO_TIMESTAMP('2025-05-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (4, 4, 4, 4, TO_TIMESTAMP('2025-05-01 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (5, 5, 5, 5, TO_TIMESTAMP('2025-05-01 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);

-- LiftUsage
INSERT INTO LiftUsage (LUID, PID, LiftID, DateTimeofUse)
VALUES (1, 1, 1, TO_TIMESTAMP('2025-05-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO LiftUsage (LUID, PID, LiftID, DateTimeofUse)
VALUES (2, 2, 2, TO_TIMESTAMP('2025-05-01 09:15:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO LiftUsage (LUID, PID, LiftID, DateTimeofUse)
VALUES (3, 3, 3, TO_TIMESTAMP('2025-05-01 09:30:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO LiftUsage (LUID, PID, LiftID, DateTimeofUse)
VALUES (4, 4, 4, TO_TIMESTAMP('2025-05-01 09:45:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO LiftUsage (LUID, PID, LiftID, DateTimeofUse)
VALUES (5, 5, 5, TO_TIMESTAMP('2025-05-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));

-- Final commit
COMMIT;


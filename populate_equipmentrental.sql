INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (1, 1, 1, 1, TO_TIMESTAMP('2025-05-01 14:37', 'YYYY-MM-DD HH24:MI'), 0);

INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (2, 2, 2, 2, TO_TIMESTAMP('2025-05-01 12:00', 'YYYY-MM-DD HH24:MI'), 0);

INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (3, 3, 3, 3, TO_TIMESTAMP('2025-05-03 10:00', 'YYYY-MM-DD HH24:MI'), 0);

INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (4, 4, 4, 4, TO_TIMESTAMP('2025-05-01 10:00', 'YYYY-MM-DD HH24:MI'), 0);

INSERT INTO EquipmentRental (RID, MID, PID, EID, RentalDateTime, Status)
VALUES (5, 5, 5, 5, TO_TIMESTAMP('2025-05-01 13:00', 'YYYY-MM-DD HH24:MI'), 0);

COMMIT;

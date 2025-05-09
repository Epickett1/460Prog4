INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (1, 100, 100, TO_TIMESTAMP('2025-05-01 14:37', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2025-05-03 14:37', 'YYYY-MM-DD HH24:MI'), 100, 'OneDay', 1, 1);

INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (2, 500, 500, TO_TIMESTAMP('2025-05-01 12:00', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2025-11-01 12:00', 'YYYY-MM-DD HH24:MI'), 500, 'Season', 2, 1);

INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (3, 200, 200, TO_TIMESTAMP('2025-05-03 10:00', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2025-05-05 10:00', 'YYYY-MM-DD HH24:MI'), 200, 'TwoDay', 3, 1);

INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (4, 500, 500, TO_TIMESTAMP('2025-05-01 10:00', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2025-11-01 10:00', 'YYYY-MM-DD HH24:MI'), 500, 'Season', 4, 1);

INSERT INTO SkiPass (PID, TotalUses, RemainingUses, PurchaseDateTime, ExpirationDate, Price, PassType, MID, IsValid)
VALUES (5, 500, 500, TO_TIMESTAMP('2025-05-01 13:00', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2025-11-01 13:00', 'YYYY-MM-DD HH24:MI'), 500, 'Season', 5, 1);

COMMIT;

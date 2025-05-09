INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (1, 1, 1, 2, 2, TO_DATE('2025-03-25', 'YYYY-MM-DD'));

INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (2, 2, 3, 30, 30, TO_DATE('2025-05-01', 'YYYY-MM-DD'));

INSERT INTO LessonOrder (OID, MID, LID, NumberOfSessions, SessionsLeft, PurchaseDate)
VALUES (3, 3, 5, 4, 4, TO_DATE('2025-05-01', 'YYYY-MM-DD'));

COMMIT;

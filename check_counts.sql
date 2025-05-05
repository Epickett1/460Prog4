
SELECT 'ChangeLog' AS TableName, COUNT(*) AS RowCount FROM ChangeLog UNION ALL
SELECT 'Property', COUNT(*) FROM Property UNION ALL
SELECT 'Member', COUNT(*) FROM Member UNION ALL
SELECT 'Trail', COUNT(*) FROM Trail UNION ALL
SELECT 'Lift', COUNT(*) FROM Lift UNION ALL
SELECT 'LiftTrail', COUNT(*) FROM LiftTrail UNION ALL
SELECT 'Employee', COUNT(*) FROM Employee UNION ALL
SELECT 'Instructor', COUNT(*) FROM Instructor UNION ALL
SELECT 'Lesson', COUNT(*) FROM Lesson UNION ALL
SELECT 'LessonOrder', COUNT(*) FROM LessonOrder UNION ALL
SELECT 'SkiPass', COUNT(*) FROM SkiPass UNION ALL
SELECT 'Equipment', COUNT(*) FROM Equipment UNION ALL
SELECT 'EquipmentRental', COUNT(*) FROM EquipmentRental UNION ALL
SELECT 'LiftUsage', COUNT(*) FROM LiftUsage;


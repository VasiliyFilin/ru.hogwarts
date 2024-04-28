SELECT student.name, student.age, faculty.name
FROM student
inner join faculty on faculty.id = student.faculty_id;

SELECT student.name, avatar.file_path
FROM student
inner join avatar on student.id = avatar.student_id;


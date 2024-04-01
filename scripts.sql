select *
from student;


select *
from student
where age > 14
   and age < 20;

select s.name
from student as s;

select * from student
where student.name like '%H%';

select * from student
where student.age < student.id;

select * from student
ORDER BY age;
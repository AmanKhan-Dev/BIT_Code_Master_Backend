/*
SQLyog Community v13.1.9 (64 bit)
MySQL - 8.0.38 : Database - quiz_website_database
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`quiz_website_database` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `quiz_website_database`;

/*Table structure for table `admin_details` */

DROP TABLE IF EXISTS `admin_details`;

CREATE TABLE `admin_details` (
  `admin_email` varchar(255) NOT NULL,
  `admin_full_name` varchar(255) NOT NULL,
  `admin_id` bigint DEFAULT NULL,
  `admin_password` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `admin_details` */

/*Table structure for table `coding_questions` */

DROP TABLE IF EXISTS `coding_questions`;

CREATE TABLE `coding_questions` (
  `question_set_id` varchar(255) NOT NULL,
  `Question_no` int NOT NULL,
  `question` text NOT NULL,
  `question_category` varchar(255) DEFAULT NULL,
  `question_description` text,
  `test_case_input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `test_case_output` text,
  PRIMARY KEY (`question_set_id`,`Question_no`),
  UNIQUE KEY `id_and_no` (`question_set_id`,`Question_no`),
  UNIQUE KEY `UKq3s8l9qewjpn1skv6chrnsgyi` (`question_set_id`,`Question_no`),
  KEY `idx_Question_no` (`Question_no`),
  KEY `available_category` (`question_category`),
  CONSTRAINT `available_category` FOREIGN KEY (`question_category`) REFERENCES `question_set_categories` (`available_category`),
  CONSTRAINT `coding_questions_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `question_sets` (`question_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `coding_questions` */

insert  into `coding_questions`(`question_set_id`,`Question_no`,`question`,`question_category`,`question_description`,`test_case_input`,`test_case_output`) values 
('BTES204',1,'Write a C Program to Print \"Hello, World!\"','Basic Operations','Write a C program that prints the message \"Hello World!\" to the screen. \r\nThis is a simple program for beginners to learn the basic structure of a C program,\r\n including the use of #include, main() function, and printf() for output.',' ','Hello World'),
('BTES204',2,'Write a C Program to Print an Integer Entered by the User','Basic Operations','Create a C program that asks the user to input an integer and then displays that number.','25','Enter an integer: You entered: 25'),
('BTES204',3,'Write a C Program to Find the Size of Different Data Types','Basic Operations','Write a C program that prints the size (in bytes) of various data types such as int, float, double, and char.',' ','Size of int: 4 bytes\nSize of float: 4 bytes\nSize of double: 8 bytes\nSize of char: 1 byte'),
('BTES204',4,'Write a C Program to Find the Quotient and Remainder','Basic Operations','Develop a C program that accepts two integers as input (dividend and divisor), and then calculates and prints the quotient and remainder.','23 4','Enter dividend: Enter divisor: Quotient = 5\r\nRemainder = 3'),
('BTES204',5,'Write a C Program to Find the ASCII Value of a Character','Basic Operations','Write a program that reads a character from the user and prints its corresponding ASCII value.','A','Enter a character: ASCII value of A = 65'),
('BTES204',6,'Write a C Program to Add Two Integers and Find Their Average','Basic Operations','Create a C program that takes two integers as input from the user, calculates their sum, and also finds their average.','50 10','Enter first number :Enter second number :\r\nSum of 50 and 10 is = 60\r\nAverage of 50 and 10 is = 30.000000'),
('BTES204',7,'Write a C Program to Convert Celsius to Fahrenheit','Basic Operations','Write a program that converts a temperature from Celsius to Fahrenheit using the formula fahrenheit = (1.8 * celsius) + 32.','25',' Enter the Temparature in Celsius : \r\n Temperature in Fahrenheit : 77.000000 '),
('BTES204',8,'Write a C Program to Calculate Simple Interest','Basic Operations','Write a program to calculate simple interest. The program will ask the user to input the principal amount, rate of interest, and time (in years), and then calculate the simple interest.','10 5 2','Enter principal (Amount) :Enter rate :Enter time (in years) :\r\nSimple Interest is = 1.000000\r\n'),
('BTES204',9,'Write a C Program to Calculate the Gross Salary of an Employee','Basic Operations','Create a C program that calculates the gross salary of an employee. The program should prompt the user to enter the employee\'s name, basic salary, HRA (House Rent Allowance), and DA (Dearness Allowance).','John Doe \r\n50000 \r\n10000 \r\n5000 ','Enter name: Enter Basic Salary: Enter HRA: Enter D.A.: \r\nName: John Doe  \r\nBASIC: 50000.000000 \r\nHRA: 10000.000000 \r\nDA: 5000.000000 \r\nPF: 6000.000000 \r\n***GROSS SALARY: 71000.000000 ***'),
('BTES204',10,'Write a C Program to Calculate the Area and Perimeter of a Circle','Basic Operations','Write a program that calculates the area and perimeter of a circle. The user will input the radius of the circle, and the program will use the formulas Area = PI * radius^2 and Perimeter = 2 * PI * radius.','7','Enter radius of circle: Area of circle: 153.860016 \r\nPerimeter of circle: 43.960003'),
('BTES204',11,'Write a C Program to Check Whether a Number is Even or Odd','Simple Conditions','Write a program that checks if a given integer is even or odd.','4','Enter an integer number: 4 is an EVEN number.'),
('BTES204',12,'Write a C Program to Find the Largest of Three Numbers','Simple Conditions','Write a C program that takes three integers as input and prints the largest among them.','4 9 2','Enter three numbers?9 is largest'),
('BTES204',13,'Write a C Program to Check if a Number is Positive or Negative','Simple Conditions','Write a C program to check whether a given integer is positive or negative.','-5','Enter a number \r\n-5 is a negative number '),
('BTES204',14,'Write a C Program to Check Voting Eligibility','Simple Conditions','Write a C program that checks whether a person is eligible to vote (age >= 18).','20','Enter the age of the person: Eigibal for voting'),
('BTES204',15,'Write a C Program to Check for a Leap Year','Simple Conditions','Write a C program to check if a given year is a leap year.','2024','Enter year : LEAP YEAR'),
('BTES204',16,'Write a C Program to Check the Type of Triangle','Simple Conditions','1.	Input sides of a triangle from user. Store it in some variables say side1, side2 and side3.\r\n2.	Check if(side1 == side2 && side2 == side3), then the triangle is equilateral.\r\n3.	If it is not an equilateral triangle then it may be isosceles. Check if(side1 == side2 || side1 == side3 || side2 == side3), then triangle is isosceles.\r\n4.	If it is neither equilateral nor isosceles then it scalene triangle.\r\n','3 4 5','Enter three sides of triangle: Scalene triangle.'),
('BTES204',17,'Write a C Program to Check if a Character is Alphabet, Digit, or Special Symbol','Simple Conditions','•	A character is alphabet if it in between a-z or A-Z.\r\n•	A character is digit if it is in between 0-9.\r\n•	A character is special symbol character if it neither alphabet nor digit.\r\nStep by step descriptive logic to check alphabet, digit or special character.\r\n1.	Input a character from user. Store it in some variable say ch.\r\n2.	First check if character is alphabet or not. A character is alphabet if((ch >= \'a\' && ch <= \'z\') || (ch >= \'A\' && ch <= \'Z\')).\r\n3.	Next, check condition for digits. A character is digit if(ch >= \'0\' && ch <= \'9\').\r\n4.	Finally, if a character is neither alphabet nor digit, then character is a special character.\r\n\r\n\r\n','$','Enter any character: \'$\' is special character.'),
('BTES204',18,'Write a C Program to Check Triangle Validity Based on Angles','Simple Conditions','Step by step descriptive logic to check whether a triangle can be formed or not, if angles are given.\r\n1.	Input all three angles of triangle in some variable say angle1, angle2 and angle3.\r\n2.	Find sum of all three angles, store sum in some variable say sum = angle1 + angle2 + angle3.\r\n3.	Check if(sum == 180) then, triangle can be formed otherwise not. In addition, make sure angles are greater than 0 i.e. check condition for angles if(angle1 > 0 && angle2 > 0 && angle3 > 0).\r\n','60 60 60','Enter three angles of triangle: \r\nTriangle is valid.'),
('BTES204',19,'Write a C Program to Calculate Percentage and Grade','Simple Conditions','Computer, calculate percentage and grade according to given conditions:\r\nIf percentage >= 90% : Grade A\r\nIf percentage >= 80% : Grade B\r\nIf percentage >= 70% : Grade C\r\nIf percentage >= 60% : Grade D\r\nIf percentage >= 40% : Grade E\r\nIf percentage < 40% : Grade F\r\nStep by step descriptive logic to find percentage and grade.\r\n1.	Input marks of five subjects in some variable say phy, chem, bio, math and comp.\r\n2.	Calculate percentage using formula per = (phy + chem + bio + math + comp) / 5.0;.\r\nCarefully notice I have divided sum with 5.0, instead of 5 to avoid integer division.\r\n3.	On the basis of per find grade of the student.\r\n4.	Check if(per >= 90) then, print “Grade A”.\r\n5.	If per is not more than 90, then check remaining conditions mentioned and print grade.\r\n','85 90 78 88 92','Enter five subjects marks: Percentage = 86.60\r\nGrade B'),
('BTES204',20,'Write a C Program to Calculate Electricity Bill','Simple Conditions','For first 50 units Rs. 0.50/unit\r\nFor next 100 units Rs. 0.75/unit\r\nFor next 100 units Rs. 1.20/unit\r\nFor unit above 250 Rs. 1.50/unit\r\nAn additional surcharge of 20% is added to the bill\r\n\r\n1.	Input unit consumed by customer in some variable say unit.\r\n2.	If unit consumed less or equal to 50 units. Then amt = unit * 0.50.\r\n3.	If unit consumed more than 50 units but less than 100 units. Then add the first 50 units amount i.e. 25 to final amount and compute the rest 50 units amount. Which is given by amt = 25 + (unit-50) * 0.75. I have used units-50, since I already calculated first 50 units which is 25.\r\n4.	Similarly check rest of the conditions and calculate total amount.\r\n5.	After calculating total amount. Calculate the surcharge amount i.e. sur_charge = total_amt * 0.20. Add surcharge amount to net amount. Which is given by net_amt = total_amt + sur_charge.\r\n','275','Enter total units consumed: Electricity Bill = Rs. 309.00'),
('BTES204',21,'C program to print all natural numbers from 1 to n','Loops','Step by step descriptive logic to print natural numbers from 1 to n.\r\n1.	Input upper limit to print natural number from user. Store it in some variable say N.\r\n2.	Run a for loop from 1 to N with 1 increment. The loop structure should be like for(i=1; i<=N; i++). At this point you might be thinking of various things such as.\r\nWhy starting from 1? Because we need to print natural numbers from 1.\r\nWhy going till N? Because we need to print natural numbers up to N.\r\nWhy increment loop counter by 1? Because difference between two natural numbers is 1. Therefore if n is one natural number then next natural number is given by n+1.\r\n3.	Inside the loop body print the value of i. You might think, why print value of i inside loop? Because we need to print natural numbers from 1 to N and from loop structure it is clear that i will iterate from 1 to N. So to print from 1 to N print the value of i.\r\n','10','Enter any number: Natural numbers from 1 to 10 : \r\n1\r\n2\r\n3\r\n4\r\n5\r\n6\r\n7\r\n8\r\n9\r\n10'),
('BTES204',22,'Write a C program to print all natural numbers in reverse from n to 1','Loops','Input start range from user.','20','Enter starting value: 20\r\n19\r\n18\r\n17\r\n16\r\n15\r\n14\r\n13\r\n12\r\n11\r\n10\r\n9\r\n8\r\n7\r\n6\r\n5\r\n4\r\n3\r\n2\r\n1'),
('BTES204',23,'C program to find sum of natural numbers from 1 to n','Loops','Step by step descriptive logic to find sum of n natural numbers.\r\n1.	Input upper limit to find sum of natural numbers. Store it in some variable say N.\r\n2.	Initialize another variable to store sum of numbers say sum = 0.\r\n3.	In order to find sum we need to iterate through all natural numbers between 1 to n. Initialize a loop from 1 to N, increment loop counter by 1 for each iteration. The loop structure should look like while(i<=N).\r\n4.	Inside the loop add previous value of sum with i. Which is sum = sum + i.\r\n5.	Finally after loop print the value of sum.\r\n','10','Enter upper limit: Sum of first 10 natural numbers = 55'),
('BTES204',24,'C program to find sum of even numbers between 1 to n','Loops','Step by step descriptive logic to find sum of even numbers.\r\n1.	Input upper limit to find sum of even number. Store it in some variable say N.\r\n2.	Initialize another variable to store sum with 0 say sum = 0.\r\n3.	To find sum of even numbers we need to iterate through even numbers from 1 to n. Initialize a loop from 2 to N and increment 2 on each iteration. The loop structure should look like do {}while(i<N );\r\n4.	Inside the loop body add previous value of sum with i i.e. sum = sum + i.\r\n5.	After loop print final value of sum.\r\n','15','Enter upper limit: Sum of all even number between 1 to 15 = 56'),
('BTES204',25,'C program to print multiplication table of a given number','Loops','Step by step descriptive logic to print multiplication table.\r\n1.	Input a number from user to generate multiplication table. Store it in some variable say num.\r\n2.	To print multiplication table we need to iterate from 1 to 10. Run a loop from 1 to 10, increment 1 on each iteration. The loop structure should look like for(i=1; i<=10; i++).\r\n3.	Inside loop generate multiplication table using num * i and print in specified format.\r\n','7','Enter number to print table: 7 * 1 = 7\r\n7 * 2 = 14\r\n7 * 3 = 21\r\n7 * 4 = 28\r\n7 * 5 = 35\r\n7 * 6 = 42\r\n7 * 7 = 49\r\n7 * 8 = 56\r\n7 * 9 = 63\r\n7 * 10 = 70'),
('BTES204',26,'C program to count number of digits in an integer','Loops','Step by step descriptive logic to count number of digits in given integer using loop.\r\n1.	Input a number from user. Store it in some variable say num.\r\n2.	Initialize another variable to store total digits say digit = 0.\r\n3.	If num > 0 then increment count by 1 i.e. count++.\r\n4.	Divide num by 10 to remove last digit of the given number i.e. num = num / 10.\r\n5.	Repeat step 3 to 4 till num > 0 or num != 0\r\n','45678','Enter any number: Total digits: 5'),
('BTES204',27,'Write a C program to input a number from user and find reverse of the given number','Loops','Step by step descriptive logic to find reverse of a number.\r\n1.	Input a number from user to find reverse. Store it in some variable say num.\r\n2.	Declare and initialize another variable to store reverse of num, say reverse = 0.\r\n3.	Extract last digit of the given number by performing modulo division. Store the last digit to some variable say lastDigit = num % 10.\r\n4.	Increase the place value of reverse by one. To increase place value multiply reverse variable by 10 i.e. reverse = reverse * 10.\r\n5.	Add lastDigit to reverse i.e. reverse = reverse + lastDigit.\r\n6.	Since last digit of num is processed hence, remove last digit of num. To remove last digit divide num by 10 i.e. num = num / 10.\r\n7.	Repeat step 3 to 6 till num is not equal to (or greater than) zero.\r\n','98765','Enter any number to find reverse: Reverse = 56789'),
('BTES204',28,'Write a C program to input number from user and check number is palindrome','Loops','Step by step descriptive logic to check palindrome number.\r\n1.	Input a number from user. Store it in some variable say num.\r\n2.	Find reverse of the given number. Store it in some variable say reverse.\r\n3.	Compare num with reverse. If both are same then the number is palindrome otherwise not.\r\n','11111','Enter any number to check palindrome: 11111 is palindrome.'),
('BTES204',29,'Write a C program to print Fibonacci series up to n terms using loop','Loops','Step by step descriptive logic to print n Fibonacci terms.\r\n1.	Input number of Fibonacci terms to print from user. Store it in a variable say terms.\r\n2.	Declare and initialize three variables, I call it as Fibonacci magic initialization. a=0, b=1 and c=0.\r\nHere c is the current term, b is the n-1th term and a is n-2th term.\r\n3.	Run a loop from 1 to terms, increment loop counter by 1. The loop structure should look like for(i=1; i<=term; i++). It will iterate through n terms\r\n4.	Inside the loop copy the value of n-1th term to n-2th term i.e. a = b.\r\nNext, copy the value of nth to n-1th term b = c.\r\nFinally compute the new term by adding previous two terms i.e. c = a + b.\r\n5.	Print the value of current Fibonacci term i.e. c.\r\n','10','Enter number of terms: Fibonacci terms: \r\n0, 1, 1, 2, 3, 5, 8, 13, 21, 34, '),
('BTES204',30,'C program to find factorial of a number','Loops','Input an integer number.','9','Enter an integer number: \r\nFactorial of 9 is = 362880');

/*Table structure for table `question` */

DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `datas` json DEFAULT NULL,
  `question` varchar(255) NOT NULL,
  `question_type` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `question` */

/*Table structure for table `question_choices` */

DROP TABLE IF EXISTS `question_choices`;

CREATE TABLE `question_choices` (
  `question_id` bigint NOT NULL,
  `choices` varchar(255) DEFAULT NULL,
  KEY `FKifc0cyjdk3ijjhtju0fual7a6` (`question_id`),
  CONSTRAINT `FKifc0cyjdk3ijjhtju0fual7a6` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `question_choices` */

/*Table structure for table `question_correct_answers` */

DROP TABLE IF EXISTS `question_correct_answers`;

CREATE TABLE `question_correct_answers` (
  `question_id` bigint NOT NULL,
  `correct_answers` varchar(255) DEFAULT NULL,
  KEY `FK3nr4qylvsx1obopacubbv012h` (`question_id`),
  CONSTRAINT `FK3nr4qylvsx1obopacubbv012h` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `question_correct_answers` */

/*Table structure for table `question_set_categories` */

DROP TABLE IF EXISTS `question_set_categories`;

CREATE TABLE `question_set_categories` (
  `question_set_id` varchar(255) DEFAULT NULL,
  `available_category` varchar(255) DEFAULT NULL,
  UNIQUE KEY `available_category` (`available_category`),
  KEY `question_set_id` (`question_set_id`),
  CONSTRAINT `question_set_categories_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `question_sets` (`question_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `question_set_categories` */

insert  into `question_set_categories`(`question_set_id`,`available_category`) values 
('BTES204','Basic Operations'),
('BTES204','Simple Conditions'),
('BTES204','Loops'),
('BTES204','Loops And Jump'),
('BTES204','Case Control and Conditonal Operator'),
('BTES204','Functions'),
('BTES204','1D Array and String');

/*Table structure for table `question_sets` */

DROP TABLE IF EXISTS `question_sets`;

CREATE TABLE `question_sets` (
  `admin_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `question_set_id` varchar(255) NOT NULL,
  `set_password` varchar(255) NOT NULL,
  `question_set_name` varchar(255) NOT NULL,
  PRIMARY KEY (`question_set_id`),
  KEY `admin_email` (`admin_email`),
  CONSTRAINT `question_sets_ibfk_1` FOREIGN KEY (`admin_email`) REFERENCES `admin_details` (`admin_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `question_sets` */

insert  into `question_sets`(`admin_email`,`question_set_id`,`set_password`,`question_set_name`) values 
(NULL,'BTES204','$2a$10$eW9zCC9F2JXavt9/2/H0vOe4oFYjhkefcB0ffu8VNhRHAe.VzGOcG','100 Questions Of C');

/*Table structure for table `student_details` */

DROP TABLE IF EXISTS `student_details`;

CREATE TABLE `student_details` (
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) NOT NULL,
  `prn` varchar(255) NOT NULL,
  `roll_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`email`,`prn`),
  UNIQUE KEY `PRN` (`prn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `student_details` */

insert  into `student_details`(`full_name`,`email`,`prn`,`roll_no`,`password`) values 
('Aman Khan','uchihaitachi39990@gmail.com','12344','B354','$2a$10$RoY9qM1O30mYoNtdhE77zeEkNK3Jg7U9820OlImc/nRYJju3KYnhG');

/*Table structure for table `student_results` */

DROP TABLE IF EXISTS `student_results`;

CREATE TABLE `student_results` (
  `question_set_id` varchar(255) DEFAULT NULL,
  `Question_no` int DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `student_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `prn` varchar(255) DEFAULT NULL,
  `student_roll_no` varchar(255) DEFAULT NULL,
  `solved_counter` int DEFAULT NULL,
  UNIQUE KEY `unique_student_question` (`email`,`question_set_id`,`Question_no`),
  KEY `question_set_id` (`question_set_id`),
  KEY `Question_no` (`Question_no`),
  KEY `prn` (`prn`),
  CONSTRAINT `student_results_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `coding_questions` (`question_set_id`),
  CONSTRAINT `student_results_ibfk_2` FOREIGN KEY (`Question_no`) REFERENCES `coding_questions` (`Question_no`),
  CONSTRAINT `student_results_ibfk_3` FOREIGN KEY (`email`) REFERENCES `student_details` (`email`),
  CONSTRAINT `student_results_ibfk_4` FOREIGN KEY (`prn`) REFERENCES `student_details` (`prn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `student_results` */

insert  into `student_results`(`question_set_id`,`Question_no`,`email`,`student_name`,`prn`,`student_roll_no`,`solved_counter`) values 
('BTES204',2,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',5,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',6,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',4,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',1,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',3,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',7,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',8,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',10,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',11,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',12,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',13,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',14,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',15,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',16,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',17,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',18,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',19,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',20,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',21,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',22,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',23,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',24,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',25,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',26,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',27,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',28,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',29,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1),
('BTES204',30,'uchihaitachi39990@gmail.com','Aman Khan','12344','B354',1);

/*Table structure for table `test_cases` */

DROP TABLE IF EXISTS `test_cases`;

CREATE TABLE `test_cases` (
  `question_set_id` varchar(255) NOT NULL,
  `Question_no` int NOT NULL,
  `tc_id` int NOT NULL AUTO_INCREMENT,
  `test_case_input` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `test_case_output` text,
  PRIMARY KEY (`question_set_id`,`Question_no`,`tc_id`),
  KEY `fk_Question_no` (`Question_no`),
  KEY `tc_id` (`tc_id`),
  CONSTRAINT `fk_Question_no` FOREIGN KEY (`Question_no`) REFERENCES `coding_questions` (`Question_no`),
  CONSTRAINT `fk_question_set_id` FOREIGN KEY (`question_set_id`) REFERENCES `coding_questions` (`question_set_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `test_cases` */

insert  into `test_cases`(`question_set_id`,`Question_no`,`tc_id`,`test_case_input`,`test_case_output`) values 
('BTES204',2,57,'45','Enter an integer: You entered: 45'),
('BTES204',2,61,'100','Enter an integer: You entered: 100'),
('BTES204',2,62,'22','Enter an integer: You entered: 22'),
('BTES204',4,63,'93 4','Enter dividend: Enter divisor: Quotient = 23\r\nRemainder = 1'),
('BTES204',5,64,'B','Enter a character: ASCII value of B = 66'),
('BTES204',5,65,'F','Enter a character: ASCII value of F = 70'),
('BTES204',6,66,'25 9','Enter first number :Enter second number :\r\nSum of 25 and 9 is = 34\r\nAverage of 25 and 9 is = 17.000000'),
('BTES204',6,67,'37 5','Enter first number :Enter second number :\r\nSum of 37 and 5 is = 42\r\nAverage of 37 and 5 is = 21.000000'),
('BTES204',7,70,'40',' Enter the Temparature in Celsius : \r\n Temperature in Fahrenheit : 104.000000 '),
('BTES204',7,71,'15',' Enter the Temparature in Celsius : \r\n Temperature in Fahrenheit : 59.000000'),
('BTES204',8,72,'20 8 3','Enter principal (Amount) :Enter rate :Enter time (in years) :\r\nSimple Interest is = 4.800000'),
('BTES204',8,73,'65 7 5','Enter principal (Amount) :Enter rate :Enter time (in years) :\r\nSimple Interest is = 22.750000'),
('BTES204',10,74,'10','Enter radius of circle: Area of circle: 314.000000 \r\nPerimeter of circle: 62.800003'),
('BTES204',10,75,'45','Enter radius of circle: Area of circle: 6358.500000 \r\nPerimeter of circle: 282.600006'),
('BTES204',11,76,'7','Enter an integer number: 7 is an ODD number.'),
('BTES204',11,77,'24','Enter an integer number: 24 is an EVEN number.'),
('BTES204',12,78,'56 2 38','Enter three numbers?56 is largest'),
('BTES204',12,79,'1 1 1','Enter three numbers?All are equal'),
('BTES204',13,80,'0','Enter a number \r\n0 is a positive number '),
('BTES204',13,81,'45','Enter a number \r\n45 is a positive number '),
('BTES204',14,82,'10','Enter the age of the person: Not eligibal for voting'),
('BTES204',15,83,'1973','Enter year : COMMON YEAR'),
('BTES204',16,84,'6 6 6','Enter three sides of triangle: Equilateral triangle.'),
('BTES204',16,85,'4 7 4','Enter three sides of triangle: Isosceles triangle.'),
('BTES204',17,86,'Y','Enter any character: \'Y\' is alphabet.'),
('BTES204',17,87,'9','Enter any character: \'9\' is digit.'),
('BTES204',18,88,'23 76 29','Enter three angles of triangle: \r\nTriangle is not valid.'),
('BTES204',19,89,'54 23 19 34 45','Enter five subjects marks: Percentage = 35.00\r\nGrade F'),
('BTES204',19,90,'91 90 97 93 94','Enter five subjects marks: Percentage = 93.00\r\nGrade A'),
('BTES204',19,91,'27 56  58 62 53','Enter five subjects marks: Percentage = 51.20\r\nGrade E'),
('BTES204',20,92,'364','Enter total units consumed: Electricity Bill = Rs. 469.20'),
('BTES204',21,93,'34','Enter any number: Natural numbers from 1 to 34 : \r\n1\r\n2\r\n3\r\n4\r\n5\r\n6\r\n7\r\n8\r\n9\r\n10\r\n11\r\n12\r\n13\r\n14\r\n15\r\n16\r\n17\r\n18\r\n19\r\n20\r\n21\r\n22\r\n23\r\n24\r\n25\r\n26\r\n27\r\n28\r\n29\r\n30\r\n31\r\n32\r\n33\r\n34'),
('BTES204',22,94,'10','Enter starting value: 10\r\n9\r\n8\r\n7\r\n6\r\n5\r\n4\r\n3\r\n2\r\n1'),
('BTES204',23,95,'15','Enter upper limit: Sum of first 15 natural numbers = 120'),
('BTES204',24,96,'28','Enter upper limit: Sum of all even number between 1 to 28 = 210'),
('BTES204',25,97,'12','Enter number to print table: 12 * 1 = 12\r\n12 * 2 = 24\r\n12 * 3 = 36\r\n12 * 4 = 48\r\n12 * 5 = 60\r\n12 * 6 = 72\r\n12 * 7 = 84\r\n12 * 8 = 96\r\n12 * 9 = 108\r\n12 * 10 = 120'),
('BTES204',26,98,'120','Enter any number: Total digits: 3'),
('BTES204',27,99,'24341','Enter any number to find reverse: Reverse = 14342'),
('BTES204',28,100,'682628','Enter any number to check palindrome: 682628 is not palindrome.'),
('BTES204',29,101,'15','Enter number of terms: Fibonacci terms: \r\n0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, '),
('BTES204',30,102,'0','Enter an integer number: \r\nFactorial of 0 is = 1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

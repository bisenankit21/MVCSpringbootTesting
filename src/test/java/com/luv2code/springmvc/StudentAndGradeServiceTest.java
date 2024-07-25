package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

   @Autowired
   private StudentAndGradeService studentService;

   @Autowired
   private JdbcTemplate jdbc;   //this allow us to do jdbc oprations
   @Autowired
   private StudentDao studentDao;

   @BeforeEach
   public void setUpDatabase(){
       jdbc.execute("insert into student(id,firstname, lastname, email_address)" +
               "values(1,'Eric','Roy','test@gmail.com')");
   }
    @Test
    public void createStudentService(){
        studentService.createStudent("Ankit","Bisen","ankit@gmail.com");

        CollegeStudent student = studentDao.findByEmailAddress("ankit@gmail.com");

        assertEquals("ankit@gmail.com", student.getEmailAddress(),"find by email");
    }

    @Test
    public void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService(){
        Optional<CollegeStudent> deeletedCollgeStudent=studentDao.findById(1);
        assertTrue(deeletedCollgeStudent.isPresent(),"Return True");
        studentService.deleteStudent(1);
        deeletedCollgeStudent = studentDao.findById(1);
        assertFalse(deeletedCollgeStudent.isPresent(),"Return False");
    }

    @Sql("/insertData.sql")      //@BeforeEach will execute first then it will execute @Sql
    @Test
    public void getGradebookService(){
       Iterable<CollegeStudent> collegeStudentIterable = studentService.getGradebook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for(CollegeStudent collegeStudent : collegeStudentIterable){
            collegeStudents.add(collegeStudent);

        }
        assertEquals(6,collegeStudents.size());
    }
    @AfterEach
    public void setupAfterTransaction(){
       jdbc.execute("Delete From Student");
    }

}

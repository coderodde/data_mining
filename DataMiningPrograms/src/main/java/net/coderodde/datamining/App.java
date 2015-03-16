package net.coderodde.datamining;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.coderodde.datamining.loader.support.DataLoaderv1;
import net.coderodde.datamining.model.AppDataStorage;
import net.coderodde.datamining.model.Course;
import net.coderodde.datamining.model.CourseAttendanceEntry;
import net.coderodde.datamining.model.Student;

/**
 * This class defines the entry point of the program.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class App {
    
    private static final String HELP = 
            "usage: java -jar program.jar <file>\n" + 
            "  where <file> is the path to the data file.\n";
    
    private AppDataStorage appData;
    
    public App(final AppDataStorage appData) {
        this.appData = appData;
    }
    
    public void printAllCourseCodes() {
        for (final Course course : appData.getCourseList()) {
            System.out.println(course.getCode());
        }
    }
    
    
    public static void main(final String... args) {
        if (args.length == 0) {
            System.out.println(HELP);
            System.exit(1);
        }
        
        final AppDataStorage appData = 
                new DataLoaderv1().load(new File(args[0]));
        
        final App app = new App(appData);
        
        app.printAllCourseCodes();
        app.printAmountOfIntroProgrammingStudents();
    }

    private void printAmountOfIntroProgrammingStudents() {
        final List<Student> studentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        
        System.out.println("The amount of students attaining " +
                           "\"Ohjelmoinnin perusteet\": " +
                           studentList.size());
    }
}

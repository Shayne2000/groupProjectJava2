/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package folder;
import java.io.IOException;
import java.util.*;
import java.nio.file.Path;

/**
 *
 * @author Shayne
 */
public class main {
    
    private static final String inputFolder = "src/main/java/folder/";

    public static void main(String[] args) {
        
        readFile("config_1.txt");
        
    }
    
    private static Scanner readFile (String name) {
        Scanner scanObject ;
        Scanner userInput = new Scanner(System.in);
        while (true) {
            try {
                scanObject = new Scanner(Path.of(inputFolder+name));
                break;
            } catch (IOException e) {
                System.out.printf("%s (The system cannot find the file specified)\n", e);
                System.out.printf("New file name =   ");
                name = userInput.nextLine();
            }
        }
        return scanObject;
    }
    
    private static void overridePrintFormat () {
        System.setOut(new java.io.PrintStream(System.out) {
            @Override
            public java.io.PrintStream printf(String format, Object... args) {
                return super.printf("%16s".formatted(Thread.currentThread().getName()) + "  >>  " + format, args);
            }
        });
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package folder;

/**
 *
 * @author Shayne
 */
public class main {

    public static void main(String[] args) {
        
        System.out.printf("This is normal printing\n\n");
        
        overridePrintFormat();
        // Trigger code in another file
        testClass a = new testClass();
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


Prinprfoapoifmipamif
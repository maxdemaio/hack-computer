import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Assembler {
    public static void main(String args[]) {
//        Codegen myCodegen = new Codegen();
//        Parser myParser = new Parser();

        // Option 1) use Scanner and just use parser for each line
        // Option 2) use Parser like a Scanner


        // while myParser.hasMoreCommands... {}
        File file = new File("src/main/resources/add/Add.asm");
        try {
            Scanner myScanner = new Scanner(file);
            String fileContent = "";
            while(myScanner.hasNext()) {
                fileContent = fileContent.concat(myScanner.nextLine() + "\n");
            }
            FileWriter myWriter = new FileWriter("src/main/resources/output/Add.hack");
            myWriter.write(fileContent);
            myWriter.close();

            System.out.println(fileContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

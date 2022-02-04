import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Assembler {
    public static void main(String args[]) {
//        Codegen myCodegen = new Codegen();

        Parser myParser = new Parser();

        // Option 1) use Scanner and just use parser for each line
        // Option 2) use Parser like a Scanner

        // Start by removing whitespace and comments

        // while myParser.hasMoreCommands... {}
        File file = new File("src/main/resources/add/Add.asm");
        try {
            Scanner myScanner = new Scanner(file);
            String hackFile = "";
            while(myScanner.hasNext()) {
                // Remove whitespace and comments
                String line = myScanner.nextLine();
                String stripLine = myParser.removeWhitespace(line);
                String cleanLine = myParser.removeComments(stripLine);

                // If it was an empty line, continue to next line without writing
                if (cleanLine.length() == 0) {
                    continue;
                }

                // Write to hack file
                hackFile = hackFile.concat(cleanLine + "\n");
            }
            FileWriter myWriter = new FileWriter("src/main/resources/output/Add.hack");
            myWriter.write(hackFile);
            myWriter.close();

            System.out.println(hackFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

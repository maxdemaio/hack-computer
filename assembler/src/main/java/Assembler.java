import enums.Command;
import exceptions.MyAssemblerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Assembler {
    public static void main(String args[]) throws MyAssemblerException {
//        Codegen myCodegen = new Codegen();


        // Option 1) use Scanner and just use parser for each line
        // Option 2) use Parser like a Scanner
        // This implementation uses option 1
        Parser myParser = new Parser();

        File file = new File("src/main/resources/add/Add.asm");

        try {
            Scanner myScanner = new Scanner(file);
            String hackFile = "";
            while (myScanner.hasNext()) {
                // Remove whitespace and comments
                String line = myScanner.nextLine();
                line = myParser.cleanLine(line);

                // If it was an empty line, continue to next line without writing
                if (line.length() == 0) {
                    continue;
                }

                // Get command and symbol
                Command currCommand = myParser.commandType(line);
                String symbol = myParser.symbol(line);

                // A-command logic
                if (currCommand == Command.A_COMMAND) {
                    // If it's a number, convert to binary and write to ouput
                    if (myParser.stringIsNum(symbol)) {
                        String binaryPrelim = Integer.toBinaryString(Integer.parseInt(symbol));
                        String binaryFinal = myParser.addZeros(binaryPrelim);
                        line = binaryFinal;
                    }
                } else if (currCommand == Command.C_COMMAND) {
                    System.out.println("C command!!");

                } else if (currCommand == Command.L_COMMAND) {
                    System.out.println("L command!!");

                }
                // Write line to hack file string
                hackFile = hackFile.concat(line + "\n");
            }
            // Write to hack file
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

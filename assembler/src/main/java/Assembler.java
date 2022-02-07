import enums.Command;
import exceptions.MyAssemblerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Assembler {
    public static void main(String args[]) throws MyAssemblerException {


        // Option 1) use Scanner and just use parser for each line
        // Option 2) use Parser like a Scanner
        // This implementation uses option 1

        // First pass we add symbols (variables and loop labels)
        // To our hashmap
        // We store the variable as the key, and the memory address as value
        // R0 R1 R2 already given but new ones get allocated to memory
        // per Parses static variable for memory index
        Parser myParser = new Parser();
        Codegen myCodegen = new Codegen();

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
                    // C-command logic
                    // **Note: both dest and jump are optional**
                } else if (currCommand == Command.C_COMMAND) {
                    System.out.println("C command!!, Printing dest,comp,jump");
                    String dest = myParser.dest(line);
                    String comp = myParser.comp(line);
                    String jump = myParser.jump(line);
                    System.out.println(dest);
                    System.out.println(comp);
                    System.out.println(jump);
                    // Take dest,comp,jump and convert them to binary
                    line = "111" + myCodegen.getAll(comp, dest, jump);
                    // L-command logic (LOOP)
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

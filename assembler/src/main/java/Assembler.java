import enums.Command;
import exceptions.MyAssemblerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {
    public static void main(String args[]) throws MyAssemblerException {

        // Option 1) use Scanner and just use parser for each line
        // Option 2) use Parser like a Scanner
        // This implementation uses option 1

        // First pass we add symbols (variables and loop labels)
        // To our hashmap
        // We store the variable as the key, and the memory address as value
        // R0 R1 R2 already given but new variables get allocated to memory
        // per Symbol Table static variable for memory index
        int lineCount = 0;
        Parser myParser = new Parser();
        Codegen myCodegen = new Codegen();
        SymbolTable mySymbolTable = new SymbolTable();

        if (args[0] == null) {
            System.out.println("You need to specify a path to an Assembly file!");
            return;
        }

        // Example: "C:/Users/maxde/Desktop/MyCodingFiles/nand2tetris/hack-computer/assembler/src/main/resources/max/Max.asm"
        File file = new File(args[0]);

        try {
            /*
                FIRST PASS
                Add symbols to symbol table
            */
            Scanner firstPass = new Scanner(file);

            while (firstPass.hasNext()) {
                String line = firstPass.nextLine();

                // Remove whitespace and comments
                line = myParser.cleanLine(line);

                // If it was an empty line, continue to next line without writing
                if (line.length() == 0) {
                    continue;
                }

                Command currCommand = myParser.commandType(line);
                String symbol = myParser.symbol(line);

                // Label symbols
                if (currCommand == Command.L_COMMAND) {
                    // Since we'll be ignoring the pseudo-commands (labels)
                    // The "next" instruction will be shifted up right where the label is
                    // Store the location of the next instruction in the program
                    if (lineCount == 0) {
                        mySymbolTable.addLabel(symbol, Integer.toString(++lineCount));
                    } else {
                        mySymbolTable.addLabel(symbol, Integer.toString(++lineCount - 1));
                    }
                }
                // Variable symbols (pre-defined already in symbol table)
                else if (currCommand == Command.A_COMMAND) {
                    // If it's not a number, put its value into our symbol table
                    if (!myParser.stringIsNum(symbol) && !mySymbolTable.containKey(symbol)) {
                        mySymbolTable.addVariable(symbol);
                    }
                }

                lineCount++;
            }

            /*
                SECOND PASS
                Convert assembly to binary
            */
            Scanner secondPass = new Scanner(file);

            String hackFile = "";

            while (secondPass.hasNext()) {
                String line = secondPass.nextLine();

                // Remove whitespace and comments
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
                    // Else it's a variable, grab value from symbol table
                    else {
                        String binaryPrelim = Integer.toBinaryString(Integer.parseInt(mySymbolTable.getValue(symbol)));
                        String binaryFinal = myParser.addZeros(binaryPrelim);
                        line = binaryFinal;
                    }
                    // C-command logic
                    // ** Note: both dest and jump are optional **
                } else if (currCommand == Command.C_COMMAND) {
                    String dest = myParser.dest(line);
                    String comp = myParser.comp(line);
                    String jump = myParser.jump(line);
                    // Take dest,comp,jump and convert them to binary
                    line = "111" + myCodegen.getAll(comp, dest, jump);
                    // L-command logic (LOOP)
                } else if (currCommand == Command.L_COMMAND) {
                    // Skip pseudo instruction
                    continue;
                }

                // Write line to hack file string
                hackFile = hackFile.concat(line + "\n");
            }
            // Create file name based on Assembly file passed
            String name = args[0];
            Pattern pattern = Pattern.compile("(\\w+)\\.asm");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) {
                System.out.println("Hack filename not found with regex!");
                return;
            }
            String hackName = matcher.group(1);

            // Write to hack file change output based on filepath
            FileWriter myWriter = new FileWriter("C:/Users/maxde/Desktop/MyCodingFiles/nand2tetris/hack-computer/assembler/src/main/resources/output/Max.hack");
            myWriter.write(hackFile);
            myWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

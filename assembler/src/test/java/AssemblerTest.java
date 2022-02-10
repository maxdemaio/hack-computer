import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exceptions.MyAssemblerException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AssemblerTest {
    
    String[] args = new String[]{"C:/Users/maxde/Desktop/MyCodingFiles/nand2tetris/hack-computer/assembler/src/main/resources/"};

    // Compare correct *.hack file and our assembled version
    @Test
    void validAdd() throws MyAssemblerException, IOException {
        args[0] += "add/Add.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("Add.hack");
        assertTrue(areEqual);
    }

    @Test
    void validMax() throws MyAssemblerException, IOException {
        args[0] += "max/Max.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("Max.hack");
        assertTrue(areEqual);
    }

    @Test
    void validMaxL() throws MyAssemblerException, IOException {
        args[0] += "max/MaxL.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("MaxL.hack");
        assertTrue(areEqual);
    }

    @Test
    void validPong() throws MyAssemblerException, IOException {
        args[0] += "pong/Pong.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("Pong.hack");
        assertTrue(areEqual);
    }

    @Test
    void validPongL() throws MyAssemblerException, IOException {
        args[0] += "pong/PongL.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("PongL.hack");
        assertTrue(areEqual);
    }

    @Test
    void validRect() throws MyAssemblerException, IOException {
        args[0] += "rect/Rect.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("Rect.hack");
        assertTrue(areEqual);
    }

    @Test
    void validRectL() throws MyAssemblerException, IOException {
        args[0] += "rect/RectL.asm";
        Assembler.main(args);
        Boolean areEqual = binaryCompare("RectL.hack");
        assertTrue(areEqual);
    }

    Boolean binaryCompare(String hackName) throws IOException {

        BufferedReader correctHack = new BufferedReader(new FileReader("C:/Users/maxde/Desktop/MyCodingFiles/nand2tetris/hack-computer/assembler/src/main/resources/test/" + hackName));
        BufferedReader generatedHack = new BufferedReader(new FileReader("C:/Users/maxde/Desktop/MyCodingFiles/nand2tetris/hack-computer/assembler/src/main/resources/output/" + hackName));

        boolean areEqual = true;
        int lineNum = 1;
        String line1 = correctHack.readLine();
        String line2 = generatedHack.readLine();

        while (line1 != null || line2 != null) {
            if (line1 == null || line2 == null) {
                areEqual = false;
                break;
            } else if (!line1.equalsIgnoreCase(line2)) {
                areEqual = false;
                break;
            }

            line1 = correctHack.readLine();
            line2 = generatedHack.readLine();
            lineNum++;
        }

        return areEqual;
    }
}

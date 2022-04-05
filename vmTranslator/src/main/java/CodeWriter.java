import java.io.*;
import java.util.HashMap;

public class CodeWriter {
    // heap ~ memory that isn't the stack
    // stack ~ all info stored in the stack

    // RAM[1] LCL - local pointer that points to
    // base address of current VM function's local segment

    // RAM[2] ARG - arg pointer that points to
    // base address of current VM function's arg segment

    // RAM[3] - points to the base of the current this segment
    // (within heap)

    // RAM[4] - points to the base of the current that segment
    // (within heap)

    // RAM[5-12] holds contents of the temp segment
    // RAM [13-15] used by VM as general purpose registers

    /*
    example 1: push constant 10
    // constant is completely virtual
    // we can use @value assembly commands
    // RAM[0] SP - stack pointer that points to
    // topmost location in the stack
    @10
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
    */

    /*
    example 2: push local i
       @LCL
       D=M

       @i
       A=D+A // offset base address to get local[i]
       D=M // D register is equal to the value of local[i]

       @SP
       A=M // causes A to take value inside of the selected mem reg (stack pointer) (0 is ref, other is value)
       M=D // ram[stack pointer location] is equal to local[i]
       @SP
       M=M+1
     */

    /*
    example 3: pop arg i
       // R13 = @segment + i
       @ARG
       D=M // D=base address of arg

       @i
       D=D+A // set D equal to address of arg[i]

       @R13
       M=D // R13 is equal to the address of arg[i]

       // &R13 = D
       @SP
       M=M-1 // SP = RAM[address of value at top of stack]
       @SP
       A=M // A takes ref to value of top of stack
       D=M // D takes value of top of stack

       @R13
       A=M // A is equal to address of arg[i]
       M=D // arg[i] is equal to value of top of stack
     */

    /*
    example 4: add/sub
    @SP
    AM=A-1 // make stack pointer now look at last element so it'll get overwritten in future
    D=M // set D-register to 1/2 of the operands

    // Set
    A=A-1 // get the other operand 2/2
    M=D+M // set 2/2 to the result OR if sub (M=D-M)
     */

    String outputFileName;
    File fout;
    FileOutputStream fos;
    BufferedWriter bw;
    HashMap<String, String> locationMap;

    CodeWriter(String outputFileName) throws FileNotFoundException {
        this.outputFileName = outputFileName;
        fout = new File(outputFileName);
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));

        locationMap = new HashMap<>();
        locationMap.put("local", "LCL");
        locationMap.put("argument", "ARG");
        locationMap.put("this", "THIS");
        locationMap.put("that", "THAT");
        locationMap.put("temp", "TEMP");
        locationMap.put("static", "STATIC");
    }

    void write(String command) throws IOException {
        bw.write(command);
        bw.newLine();
    }

    void writeArithmetic(String command) throws IOException {
        // write the assembly code that is the translation of arith cmd

        // write assembly to file (see example 4)
        if (command.equals("add")) {
            write("@SP");
            write("AM=A-1");
            write("D=M");

            write("A=A-1");
            write("M=D+M");
            return;
        } else if (command.equals("sub")) {
            write("@SP");
            write("AM=A-1");
            write("D=M");

            write("A=A-1");
            write("M=D-M");
            return;
        }
        return;
    }

    void writePushPop(CommandType command, int index, String location) throws IOException {
        // write assembly code that is the translation of given command
        // where command is either c_push/pop

        String memLocation = locationMap.get(location);

        // TODO: exception check to make sure commandtype, segment index are set
        // if memLocation null, unsupported location
        // index = -1 invalid index
        // command type null invalid command type

        if (location.equals("constant")) {
            write("@" + index);
            write("D=A");

            write("@SP");
            write("A=M");
            write("M=D");

            write("@SP");
            write("M=M+1");
            return;
        } else if (command == CommandType.C_PUSH) {
            // write assembly to file (see example 2)
            write("@" + memLocation);
            write("D=M");

            write("@" + index);
            write("A=D+A");
            write("D=M");

            write("@SP");
            write("A=M");
            write("M=D");
            write("@SP");
            write("M=M+1");
        } else if (command == CommandType.C_POP) {
            // write assembly to file (see example 3)
            write("@" + memLocation);
            write("D=M");

            write("@" + index);
            write("D=D+A");

            write("@R13");
            write("M=D");

            // &R13 = D
            write("@SP");
            write("M=M-1");

            write("@SP");
            write("A=M");
            write("D=M");

            write("@R13");
            write("A=M");
            write("M=D");

        }

        return;
    }

    void writeVmCommand(String command) throws IOException {
        bw.write("// " + command);
        bw.newLine();
        return;
    }

    void close() throws IOException {
        bw.close();
        return;
    }
}

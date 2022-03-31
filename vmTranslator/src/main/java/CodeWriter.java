import java.io.*;

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
       A=M // causes A to take value of the selected mem reg (stack pointer) (0 is ref, other is value)
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
       A=M // A takes value of top of stack
       D=M // D takes value of top of stack

       @R13
       A=M // A is equal to address of arg[i]
       M=D // arg[i] is equal to value of top of stack
     */

    String outputFileName;
    File fout;
    FileOutputStream fos;
    BufferedWriter bw;

    CodeWriter(String outputFileName) throws FileNotFoundException {
        this.outputFileName = outputFileName;
        fout = new File(outputFileName);
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));
    }

    void setFileName(String fileName) {
        // inform that the translation of new vm file has started
        System.out.println("Starting translation of VM program " + fileName + "!");
        System.out.println();
    }

    void writeArithmetic(String command) {
        // Write the assembly code that is the translation of arith cmd
        // example:
        // vm: add

        // assembly:
        // ~~~
    }

    void writePushPop(CommandType command, int index) throws IOException {
        // write assembly code that is the translation of given command
        // where command is either c_push/pop
        if (command == CommandType.C_PUSH) {
            // write assembly to file (see example 2)
            bw.write("@" + "LCL");
            bw.newLine();
            bw.write("D=M");
            bw.newLine();

            bw.write("@" + Integer.toString(index));
            bw.newLine();
            bw.write("A=D+A");
            bw.newLine();
            bw.write("D=M");

            bw.write("@SP");
            bw.newLine();
            bw.write("A=M");
            bw.newLine();
            bw.write("M=D");
            bw.newLine();
            bw.write("@SP");
            bw.newLine();
            bw.write("M=M+1");
            bw.newLine();

        } else if (command == CommandType.C_POP) {
            // TODO: write comment to file
            // TODO: write assembly to file (see example 3)
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

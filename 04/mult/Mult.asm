// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

//// Pseudo code ////
// x = r0
// y = r1
// mult = 0
// for (i = 0; i < x; i++):
    // mult += y
// }
// return mult

// ----

// i = 0
// x = r0
// y = r1
// mult = 0

// LOOP
    // if i > x goto END
    // RAM[mult] = RAM[mult] + y
    // i = i + 1
    // goto LOOP
// END
    // goto END

@i      // Some mem. location 
M=0     // i = 0
@2
M=0     // init product to 0

(LOOP)
    @i
    D=M      // D = i
    D=D-M[0] // D = i - x
    @END
    D;JEQ    // if (i - x = 0) goto END [0...x-1]

    @i
    M=M+1 // i++;
    // increase mult variable
    @1
    D=M
    @2
    M=D+M

    @LOOP
    0;JMP // goto LOOP

(END)
    @END
    0;JMP




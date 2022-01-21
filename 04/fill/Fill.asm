// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Pseudo code
// while(true){
// if (keyboard != 0) {
//     // black screen
//     for (i=screenStart; i < screen.length; i++) {
//         screen[i] = -1
//     }
// } else {
    // white screen
//     for (i=screenStart; i < screen.length; i++) {
//         screen[i] = 0
//     }
// }
//}

(RESTART)
    // 256 rows of 512 pixels per row
    @SCREEN // set A register to point to memory
    D=A
    @0
    M=D // R0 = 16384 (base addr)

    @KBD
    D=M // set D register to value of kbd

    @BLACKEN
    D;JNE // black screen if kbd != 0

    @WHITEN
    D;JEQ

    @RESTART
    0;JMP // infinte loop to keep checking KBD

(BLACKEN)
    @1
    M=-1 // change R1 to 1111111111111111
    @CHANGE
    0;JMP

(WHITEN)
    @1
    M=0	// change R1 to 000000000000000
    @CHANGE
    0;JMP
    
(CHANGE)
    @1	// what to fill screen's pixel register with from R1
    D=M	// D will be black or white (-1 or 0)

    @0
    A=M	// get screen's pixel address from R0 to fill register value
    M=D	// fill register with -1 or 0

    @0
    D=M+1	// set D register to next pixel of SCREEN

    //*** D=KBD-SCREEN ***
    // 256 rows of 512 pixels per row
    // only 32 registers/row bc each has 16-bit word width
    // D = 24576 - 16384
    // D = 8192 registers (steadily will decrease as we increment screen pixels)
    // (8192registers) * (1row/32registers) = 256 rows

    @KBD
    D=A-D	

    @0
    M=M+1	// increment R0 to next pixel
    A=M

    @CHANGE
    D;JGT	// if D > 0 we change next pixel, else we restart screen

    /////////////////////////
    @RESTART
    0;JMP
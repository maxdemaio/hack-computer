# hack-computer

Implementation of a computer system as described in the book "The Elements of Computing Systems" by Nisan and Schocken.

## Blog Posts

Throughout this project I published a series of blog posts:

- [Nand to Tetris - Boolean Functions & Gate Logic](https://www.maxdemaio.com/blog/boolean-functions-and-gate-logic)
- [Nand to Tetris - Boolean Arithmetic & the ALU](https://www.maxdemaio.com/blog/boolean-arithmetic-alu)
- [Nand to Tetris - Memory](https://www.maxdemaio.com/blog/memory)
- [Nand to Tetris - Machine Language](https://www.maxdemaio.com/blog/machine-language)
- [Nand to Tetris - Building an Assembler](https://www.maxdemaio.com/blog/assemblers)
- [Nand to Tetris - Virtual Machine I](https://www.maxdemaio.com/blog/vm-stack)
- [Nand to Tetris - Virtual Machine II](https://www.maxdemaio.com/blog/vm2)

---

## Hardware platform

### 1. Boolean Logic

- Boolean algebra
- Gate logic
  - Multibit gate logic
- Hardware construction
- Hardware description language
- Hardware simulation

### 2. Boolean Arithmetic

- Binary numbers
- Binary addition
- Signed binary numbers
- Adders
  - Half-adder
  - Full-adder
  - Adder
  - Incrementer
- ALU

### 3. Sequential Logic

- The Clock
- Flip-Flops
- Registers
- Memories
- Counters
- Why Time Matters
  - Combinational chips (depend on inputs regardless of time)
  - Sequential chips (containing one or more DFF gates)

### 4. Machine Language

- Machines
  - Memory
  - Processor
  - Registers
- Languages
- Commands
  - Arithmetic and logic operations
  - Boolean operations
  - Memory access
    - Direct addressing
    - Immediate addressing
    - Indirect addressing
  - Flow of control
- Hack language specification overview

### 5. Computer Architecture

- The stored program concept
- The von Neumann architecture
- Memory
- CPU
- Registers
- Input and output

## Software Hierarchy System

### 6. Assembler Compiler

The assembler is the first module in the software hierarchy. Chapter 4 presented machine languages in both assembly and binary. This chapter explains how to develop a Hack assembler that generates binary code to run on chapter 5's hardware platform. Assemblers must manage user-defined symbols and resolve them to physical memory addresses, often using a symbol table data structure.

The software projects creating translator programs (assembler, virtual machine, compiler) can be coded in any programming language.

Machine languages are specified in symbolic and binary forms. Binary codes, e.g. `110000101000000110000000000000111`, represent machine instructions for the hardware. For example, the 8 leftmost bits could represent an opcode (e.g. LOAD), the next 8 bits a register (e.g. R3), and the final 16 bits an address (e.g. 7).

Depending on the hardware logic design and agreed-upon machine language, a 32-bit pattern can cause the hardware to 'load the contents of Memory[7] into register R3'. To simplify the complexity of machine languages with many operation codes, memory addressing modes, and instruction formats, instructions can be documented using an agreed-upon syntax, e.g. `LOAD R3,7` instead of `110000101000000110 000000000000111`.

### 7. Virtual Machine I: Stack Operation

We will approach this task in two stages, each spanning two chapters. High-level programs will first be translated into an intermediate code, which then gets translated into machine language. This two-tier translation model is an old idea from the 1970s, recently revived by modern languages like Java and C#. The intermediate code is designed to run on a Virtual Machine, allowing software to run on many processors and systems without changing the source code. This chapter presents a VM architecture modeled after the Java VM. We focus on two perspectives: motivate and specify the VM, then implement it over the Hack platform with a VM translator. Another implementation is a VM emulator, which runs the VM on a standard PC using Java.

We present a VM language consisting of four types of commands: arithmetic, memory access, program flow, and subroutine calling. We split the implementation into two parts, each covered in a separate chapter and project. This chapter builds a basic translator for arithmetic and memory access commands. The next chapter adds program flow and subroutine calling. The virtual machine illustrates many ideas in computer science, such as emulation, code compatibility, and stack processing.

### 8. Virtual Machine II: Program Control

- Program control
- Branching
- Functions

### 9. High-level language

### 10. Compiler I: Syntax analysis

### 11. Compiler II: code generation

### 12. Operating System

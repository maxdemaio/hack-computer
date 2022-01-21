# hack-computer

Implementation of a computer system as described in the book "The Elements of Computing Systems" by Nisan and Schocken.

## Blog Posts

Throughout this project I published a series of blog posts:

- [Nand to Tetris - Boolean Functions & Gate Logic](https://www.maxdemaio.com/posts/boolean-functions-and-gate-logic)
- [Nand to Tetris - Boolean Arithmetic & the ALU](https://www.maxdemaio.com/posts/boolean-arithmetic-alu)
- [Nand to Tetris - Memory](https://www.maxdemaio.com/posts/memory)
- [Nand to Tetris - Machine Language](https://www.maxdemaio.com/posts/machine-language)

## Hardware platform

### 1. Boolean logic

Various basic logic gates are introduced, and all gates are implemented based on nand gates

- and and16
- dmux dmux4way dmux8way
- mux mux16 mux4way16 mux8way16
- not not16
- or or16 or8way
- xor

### 2. Boolean operations

- Binary number
- Binary addition
- Half adder
- Full adder
- Adder
- Incrementer
- ALU

### 3. Temporal logic

#### Combination Chip

- Boolean chip
- Arithmetic chip

#### Timing Chip

The timing chip is based on a large number of DFF gates

- Clock
- Trigger
- Register
- RAM
- Counter

### 4. Machine language

- A command
- C command
- Addressing mode: direct addressing, immediate addressing, indirect addressing

### 5. Computer Architecture

- RAM
- CPU
- Register
- input Output

## Software Hierarchy System

6. Assembler Compiler
7. Virtual Machine I: Stack Operation
8. Virtual Machine II: Program Control
9. High-level language
10. Compiler I: Syntax analysis
11. Compiler II: code generation
12. Operating System

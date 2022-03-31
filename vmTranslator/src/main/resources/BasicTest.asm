// push constant 10
@LCL
D=M
@10
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// pop local 0
@ARG
D=M
@0
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// push constant 21
@LCL
D=M
@21
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// push constant 22
@LCL
D=M
@22
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// pop argument 2
@ARG
D=M
@2
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// pop argument 1
@ARG
D=M
@1
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// push constant 36
@LCL
D=M
@36
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// pop this 6
@ARG
D=M
@6
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// push constant 42
@LCL
D=M
@42
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// push constant 45
@LCL
D=M
@45
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// pop that 5
@ARG
D=M
@5
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// pop that 2
@ARG
D=M
@2
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// push constant 510
@LCL
D=M
@510
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// pop temp 6
@ARG
D=M
@6
D=D+A
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M@R13
A=M
M=D

// push local 0
@LCL
D=M
@0
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// push that 5
@LCL
D=M
@5
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// add
// push argument 1
@LCL
D=M
@1
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// sub
// push this 6
@LCL
D=M
@6
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// push this 6
@LCL
D=M
@6
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// add
// sub
// push temp 6
@LCL
D=M
@6
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1

// add

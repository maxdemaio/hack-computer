# Bat Catcher Game (jack program example)

Originally programmed in Scratch at https://github.com/maxdemaio/batCatcher

- Game starts
- Bat moves around randomly
- If the square touches a bat, it switches caught to true
- Game ends when you press the `0` key or the bat is caught

---

## Notes for programming in Jack:

1. Put your program in directory, say `Xxx`. This would be a set of one or more Jack classes—each stored in a separate ClassName.jack text file. Along with all your program files, copy all the files from tools/OS into the `Xxx` directory too (set of `.vm` files).

2. Compile your program using the supplied Jack compiler. This is best done by applying the compiler to the name of the program directory (Xxx). This will cause the compiler to translate all the .jack classes to corresponding .vm files. If a compilation error is reported, debug the program and recompile the `Xxx` directory until no error messages are issued.

3. At this point the program directory should contain three sets of files: (i) your source `.jack` files, (ii) the compiled `.vm` files, one for each of your `.jack` class files, and (iii) additional `.vm` files, comprising the supplied Jack OS.

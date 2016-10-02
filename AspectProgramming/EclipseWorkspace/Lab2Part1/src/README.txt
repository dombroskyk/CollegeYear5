Lab 2 Part 1
Author: Kevin Dombrosky

Memoization.aj stores the output of previous calls to functions by first
object, then method signature calls, and finally arguments to those methods.
This ultimately allows us to quickly look up the previous output value
without having the method call do any actual work. For values to be stored,
the method must be annotated with Memoizable.

Generates compile time errors for any static or void return type methods
annotated with Memoizable.

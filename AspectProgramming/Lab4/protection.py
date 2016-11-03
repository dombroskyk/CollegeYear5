'''
protection.py
author James Heliotis (code outline only)
author Kevin Dombrosky
Decorations for checking the privilege level of a caller
'''

# This program has stubs for the code you must write.
# Consider most of the imports below hints for implementation.

from ticket_booth import SecurityBreach
from sys import argv
from os.path import basename
from inspect import stack, getmodule, getframeinfo

def log( fn ):
    """ Decorator, unparameterized

        Create and return a function that writes a line to a log file
        about the original function being called and then calls the
        original function and returns what it returns.

        The format of the line to be written to the log file is:

        <fn-name> called with arguments arg1 arg2 ... argn

        Keyword arguments are shown at the end. Each one is preceded
        by the keyword and an equals (=) sign.
        The fn-name (fn's name) is the name of the user function, not
        the name of this or any other decorator provided in this module.
        The returned function does no writing if the log file is undefined.

        Parameter: fn: the original function to be decorated
    """
    #print(fn.__name__ )
    #target_fn = lambda *args, **kwargs: fn(*args, **kwargs)
    def log_to_file(*args, **kwargs):
        main_program.log_file.write(fn.__name__ + "\n")
        return fn(*args, **kwargs)
    return log_to_file

def privilege( min_clearance ):
    """ Decorator, parameterized

        Purpose:
        Assign a privilege level to a function. The function is expected
        to take as its first argument a 'caller tag'.

        The decorated function is assumed to have a "caller tag" (from
        module ticket_booth) as its first parameter. This of course means
        that the function that privilege() returns will expect the tag
        as well.

        This function takes one parameter, min_clearance: the minimum
        clearance value that the "caller tag" should have.

        Return:
           a function that wraps a function (we'll call it the inner
           function) that makes sure a caller's tag is of the minimum
           level of clearance needed to execute its argument function.
           If the level of clearance is insufficient, the inner function
           (1) Writes a detailed log message in the log file, if it
               is defined (see main_program);
           (2) throws an instance of SecurityBreach.
           However, if the python command line includes an argument
           '--unchecked', the returned function will not add any
           functionality, essentially allowing protection violations.

        Format of the log message:
        <fname>, function <function>, line <lineno>:
            Caller with clearance <clr> called admin_only with minimum clearance <min>

        where
        <fname> = relative name of file containing the decorated function's
                  caller;
        <function> = name of decorated function's calling function;
        <lineno> = line in file of the call to the decorated function;
        <clr> = clearance level of caller, found in 1st argument (tag);
        <min> = the min_clearance parameter value in this function.
        
    """
    def true_decorator( fn ):
        print("true decorator")
        #main_program.log_file.write("true decorator")
        #if min_clearance > *args[0]:
        #    raise SecurityBreach( min_clearance, fn.args[0] )
        return lambda *args, **kwargs: fn( *args, **kwargs )
    return true_decorator

class main_program( object ):
    """ Decorator, parameterized

        Purpose:
        Identify the main (root) function of an entire Python program.
        This class/function should decorate the top-level function in
        the program.

        The decorator parameter is optional. It is a string representing
        a log file name.

        The log file is opened immediately (at decoration time). It is closed
        when the function it decorates has completed.

        In order to allow other code (presumably other decorations)
        to access the log file, there is within this class a class variable
        named log_file of type File (actually, _io.TextIOWrapper) that is
        used to refer to the opened file.
    """

    log_file = None

    __slots__ = ()

    def __init__( self, logFileName=None ):
        """
        Create a decorator object for this class.
        Store necessary information.
        """
        if logFileName is not None:
            print("file name updated")
            main_program.log_file = open(logFileName, 'w')
        pass

    def __call__( self, main_f ):
        """
        Call this object as if it were a function.
        This is the actual decorator function.
        Return the decorated main_f.
        """
        print("hit")
        return lambda *args, **kwargs: main_f( *args, **kwargs )

NAKED = "--unchecked"


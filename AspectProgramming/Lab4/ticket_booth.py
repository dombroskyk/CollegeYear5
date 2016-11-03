'''
ticket_booth
author James Heliotis
'''

class Ticket( object ):
    "A capability used to access system services"
    
    __slots__ = ( "clearance", "tag" )
    
    USER = 0
    MANAGER = 1
    ADMIN = 2

    def __init__( self, user_id ):
        """Create a ticket given a user's integer ID.
           This constructor can determine from the ID what
           privilege levels the user should have:
           in [0,99] => USER
           in [100,999] => MANAGER
           greater than 999 => ADMIN
        """
        if user_id < 100:
            self.clearance = Ticket.USER
        elif user_id < 1000:
            self.clearance = Ticket.MANAGER
        else:
            self.clearance = Ticket.ADMIN
        self.tag = user_id * 1000000 + 1000 # stand-in for real signature

    def __str__( self ):
        user_id = self.tag // 1000000
        if user_id < 100:
            return "<User" + str( user_id ) + ">"
        elif user_id < 1000:
            return "<Manager" + str( user_id ) + ">"
        else:
            return "<Admin" + str( user_id ) + ">"


class SecurityBreach( Exception ):
    __slots__ = ( "needed_clearance", "caller_clearance" )
    def __init__( self, needed_clearance, caller_clearance ):
        """Create an exception object containing the following information.
           needed_clearance: the minimum clearance that a called function
                             required;
           caller_clearance: the actual clearance level of the caller.
        """
        Exception.__init__( self, \
                "Function that required clearance " + str( needed_clearance ) \
                + " was called from a client that had clearance " \
                + str( caller_clearance )
        )
        self.needed_clearance = needed_clearance
        self.caller_clearance = caller_clearance


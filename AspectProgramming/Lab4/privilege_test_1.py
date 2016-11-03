'''
Test program 1
author James Heliotis
'''

from ticket_booth import Ticket, SecurityBreach
from protection import privilege, main_program, log

@privilege( Ticket.ADMIN )
@log
def admin_only( tag, x, y ):
    return x + y

@log
@privilege( Ticket.MANAGER )
def mgr_admin( tag, q ):
    print( "Quota set to", q )

@log
@privilege( Ticket.USER )
def anyone( tag, name ):
    return "Hello, " + name

JAMES = "James"
MOHAN = "Mohan"
LINUS = "Linus"

@main_program( "log1.txt" )
def test1():
    tickets = dict()
    tickets[ JAMES ] = Ticket( 6 );
    tickets[ MOHAN ] = Ticket( 106 );
    tickets[ LINUS ] = Ticket( 2000 );
    for who in JAMES, MOHAN, LINUS:
        try:
            print( "TEST: calling 'anyone' function as", who )
            print( anyone( tickets[ who ], who ) )
        except SecurityBreach as e:
            print( e )
        try:
            print( "TEST: calling 'mgr_admin' function as", who )
            mgr_admin( tickets[ who ], 1000 )
        except SecurityBreach as e:
            print( e )
        try:
            print( "TEST: calling 'admin_only' function as", who )
            print( "3 + 11 = " + str( admin_only( tickets[ who ], 3, 11 ) ) )
        except SecurityBreach as e:
            print( e )

if __name__ == '__main__':
    test1()

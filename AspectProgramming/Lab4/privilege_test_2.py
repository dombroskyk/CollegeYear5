'''
Test program 2
author James Heliotis
'''

from ticket_booth import Ticket, SecurityBreach
from protection import privilege, main_program, log

@privilege( Ticket.ADMIN )
def admin_only( tag, x, y ):
    return x - y

@privilege( Ticket.MANAGER )
@log
def mgr_admin( tag, q ):
    print( "Quota =", q )

@privilege( Ticket.USER )
def anyone( tag ):
    return 10* tag.tag

@main_program( "log2.txt" )
def test2():
    tona = Ticket( 7 );
    evie = Ticket( 107 );
    pam = Ticket( 2001 );
    for ticket in tona, evie, pam:
        try:
            print( anyone( ticket ) )
        except SecurityBreach as e:
            print( e )
        try:
            mgr_admin( ticket, 1000 )
        except SecurityBreach as e:
            print( e )
        try:
            print( "3 - 11 = " + str( admin_only( ticket, 3, 11 ) ) )
        except SecurityBreach as e:
            print( e )

if __name__ == '__main__':
    test2()

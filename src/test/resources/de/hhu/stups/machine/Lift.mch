MACHINE Lift

VARIABLES  floors

INVARIANT  floors:NAT & floors : 0..100 /* NAT */

INITIALISATION floors := 0

OPERATIONS

	inc = PRE floors<100 THEN floors := floors + 1 END ;
	dec = PRE floors>0 THEN floors := floors - 1 END;
	open_door = skip;
	close_door = skip

END
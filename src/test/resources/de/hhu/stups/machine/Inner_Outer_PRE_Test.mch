MACHINE Inner_Outer_PRE_Test

VARIABLES  floors

INVARIANT  floors:NAT & floors : 0..100 /* NAT */

INITIALISATION floors := 0

OPERATIONS

	inc = ASSERT floors<100 THEN floors := floors + 1 END ;
	dec = PRE floors>0
	THEN PRE floors > 0 THEN  floors := floors - 1 END END;
	open_door = skip;
	close_door = skip

END
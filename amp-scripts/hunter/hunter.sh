#!/usr/bin/awk -f

#Author: Arty
BEGIN {
    #printf ("Analizam!\n");
    foundFunction=0;
    inFunction=0;
    sessions=0;
    functionLine=0;
    functionName="";
    notcomment = 1;
    errors=0;
}



/(public|private) (static)? ([A-Za-z0-9.]*) ([A-Za-z0-9.]*)([ ])*\([^\)]*\)/ {
    #print;
    foundFunction++;
    functionLine=NR;
    if ($2 != "static"){
        if (index($3, "(") > 0)
	    functionName=substr($3, 1, index($3,"(") - 1);
	else
	    functionName=$3;
    }
    else{
        if (index($4, "(") > 0)
            functionName=substr($4, 1, index($4,"(") - 1);
	else
	    functionName=$4;
    }
    if ($0 ~ /{/){
	#printf("BEGIN %s\n", functionName);
	#inFunction++;
    }
    #printf("[%s]", functionName);
    #if (functionName == ""){
    #    print;
    #}
    if (inFunction != 0){
	printf("ERRRRRRR\n");
    }
}
foundFunction && /\{/ {
    inFunction++;
    #printf("BEGIN\n");
}

foundFunction && /\}/ {
    inFunction--;
    if (inFunction == 0){
       foundFunction=0;
       #printf("\n");
       if (sessions != 0){
          printf("Warning in function %s unclosed sessions!\n", functionName);
	  sessions = 0;
	  errors++;
       }
    }
    #printf("END\n");
}
inFunction && /PersistenceManager.getSession/ {
    sessions++;
    #printf("+");
}
inFunction && /PersistenceManager.releaseSession\([^\(]*\)/{
    sessions--;
    #printf("-");
}


END {
    if (errors == 0)
	printf("Ok!\n");
    else
        printf("Total: %i errors\n", errors);
}

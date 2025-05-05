#include <stdio.h>
#include <string.h>
#include "c_env.h"
#include "c_char.h"
#include "c_io.h"
#include "sorting.h"

int main(int argc, char *argv[]) {
    char lines[MAXLINE * MAXLINES];
    char *lineptr[MAXLINES];
    int i;
    for (i = 0; i < MAXLINES; i++) {
        lineptr[i] = &lines[i * MAXLINE];
    }

    int nlines;
    int numeric = 0; /* 1 if numeric sort */

    if (argc > 1 && strcomp(argv[1], "-n") == 0)
        numeric = 1;
    if ((nlines = readlines(lineptr, MAXLINE, MAXLINES)) >= 0) {
        g_qsort((void **) lineptr, 0, nlines-1, (int (*)(void*,void*)) (numeric ? numcmp : strcomp));
        writelines(lineptr, nlines);
        return 0;
    } else {
        printf("input too big to sort\n");
        return 1;
    }
}



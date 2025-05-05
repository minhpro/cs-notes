#include<stdio.h>
#include "c_env.h"
#include "c_io.h"
#include "sorting.h"
#define ARRAY_LEN 12

int main() {
    char lines[MAXLINE * MAXLINES];
    char *lineptr[MAXLINES];
    int i;
    for (i = 0; i < MAXLINES; i++) {
        lineptr[i] = &lines[i * MAXLINE];
    }

    int nlines = readlines(lineptr, MAXLINE, MAXLINES);
    writelines(lineptr, nlines);
    
    s_qsort(lineptr, 0, nlines-1);
    printf("Sorted:\n");
    writelines(lineptr, nlines);
    return 0;
}
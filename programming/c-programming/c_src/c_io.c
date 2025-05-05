#include <stdio.h>
#include "c_io.h"

#define BUFSIZE 100
char buf[BUFSIZE]; /* buffer for ungetch */
int bufp = 0;

int getch(void) {
    return (bufp > 0) ? buf[--bufp] : getchar();
}

void ungetch(int c) {
    if (bufp >= BUFSIZE)
        printf("ungetch: too many characters\n");
    else
        buf[bufp++] = c;
}

int get_line(char *line, int maxline) {
    int i = 0;
    while (i < maxline && (*line=getchar()) != EOF && *line != '\n')
    {
        i++;
        line++;
    }
    *line = '\0';
    return i;
}

int readlines(char *lineptr[], int maxline, int maxlines) {
    int i = 0;
    while (i < maxlines && get_line(lineptr[i], maxline) > 0)
        i++;
    return i;
}

void writelines(char *lineptr[], int nlines) {
    int i;
    for (i = 0; i < nlines; i++)
        printf("%s\n", lineptr[i]);
}
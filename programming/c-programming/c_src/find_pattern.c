#include <stdio.h>
#include <string.h>
#include "c_io.h"
#define MAXLINE 1000

int main(int argc, char *argv[]) {
    char line[MAXLINE];
    int found = 0;

    if (argc != 2)
        printf("Usage: find pattern\n");
    else
        while (get_line(line, MAXLINE) > 0)
            if (strstr(line, argv[1]) != NULL) {
                printf("%s", line);
                found++;
            }
            
    return found;
}


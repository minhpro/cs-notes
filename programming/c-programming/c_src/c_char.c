#include <stdlib.h>
#include "c_char.h"

int strcomp(char *x, char *y) {
    for (;*x == *y; x++, y++) {
        if (*x == '\0')
            return 0;
    }
    return *x - *y;
}

int numcmp(char *s1, char *s2) {
    double v1, v2;
    v1 = atof(s1);
    v2 = atof(s2);
    if (v1 < v2)
        return -1;
    else if (v1 > v2)
        return 1;
    else
        return 0;
}
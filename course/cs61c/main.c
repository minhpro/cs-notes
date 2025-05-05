#include <stdio.h>
#include "string.h"
#define MAXLINE 1000 /* maximum input line length */

/* print the longest input line */
void print_longest()
{
    int len; /* current line length */
    int max; /* maximum length seen so far */
    char line[MAXLINE]; /* current input line */
    char longest[MAXLINE]; /* longest line saved here */

    max = 0;
    while ((len = getline_2(line, MAXLINE)) > 0)
        if (len > max) {
            max = len;
            copy(longest, line);
        }
    if (max > 0) /* there was a line */
        printf("The longest line is: %s\n", longest);
}

void print_trimmed() {
    int len, trimmed_len;
    char line[MAXLINE];
    char trimmed[MAXLINE];
    while ((len = getline_2(line, MAXLINE)) > 0)
    {
        trimmed_len = trim(trimmed, line);
        printf("Trimmed length of %d: %s\n", trimmed_len, trimmed);
    }
}

void print_reverse() {
    char str[] = "Hello world";
    inplace_reverse(str, 11);
    printf("Reversed is: %s\n", str);
    char reversed[11];
    reverse(reversed, str, 11);
    printf("Reverse of reverse is: %s\n", reversed);
}

void mystery(short arr[], int len)
{
    printf("%d ", len);
    printf("%d\n", sizeof(arr));
}

void main() {
    short nums[] = {1, 2, 3, 99, 100};
    printf("%d ", sizeof(nums));
    mystery(nums, sizeof(nums)/sizeof(short));
    short int *p;
    printf("Size of short int *: %d\n", sizeof(p));
}
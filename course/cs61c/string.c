#include <stdio.h>
#include "string.h"

int getline_2(char s[], int lim)
{
    int c, i;

    for (i = 0; i < lim - 1 && (c = getchar()) != EOF && c != '\n'; ++i)
        s[i] = c;
    
    s[i] = '\0';
    return i;
}

void copy(char to[], char from[])
{
    int i = 0;
    while ((to[i] = from[i]) != '\0')
        ++i;
}

int trim(char trimmed[], char s[])
{
    int c, i, j;
    i = j = 0;

    while ( (c = s[i]) != '\0') {
        if (c != ' ' && c != '\n' && c != '\t') {
            trimmed[j] = c;
            ++j;
        }
        ++i;
    }
    trimmed[j] = '\0';
    return j;
}

void swap(void *a, void *b, size_t len)
{
    char temp;
    char *ca = (char *)a, *cb = (char *)b;
    while (len--)
        temp = ca[len], ca[len] = cb[len], cb[len] = temp;
}

void array_swap(char a[], int i, int j)
{
    char temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

void reverse(char r[], char s[], int len)
{
    int i;
    for (i = len; i > 0; --i)
        r[len - i] = s[i - 1];
}

void inplace_reverse(char s[], int len)
{
    int i;
    for (i = 0; i < len / 2; i++)
        array_swap(s, i, len - i - 1);
}

#include "sorting.h"
#include "c_char.h"
#include "c_pointer.h"

void swap(int a[], int i, int j) {
    int temp;
    temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

/*
* partition a into two (possibly empty) subarrays,
* a[l..q-1] <= a[q] <= a[q+1..r]
* a[q] is called the pivot element.
* here, a[r] is choose as the pivot value.
* return the pivol index - q
*/
int partition(int a[], int l, int r) {
    int x = a[r]; /* pivot value */
    int i = l-1; /* keep a[l..i] < x, initialize i right before l */
    int j;
    for (j = l; j < r; j++) {
        if (a[j] <= x) {
            i++;
            swap(a, i, j);
        }
    }
    swap(a, i+1, r);
    return i+1;
}

int s_partition(char *lineptr[], int l, int r) {
    char *x = lineptr[r];
    int i = l-1;
    int j;
    for (j = l; j<r; j++) {
        if (strcomp(lineptr[j], x) <= 0) {
            i++;
            g_swap(lineptr, i, j);
        }
    }
    g_swap(lineptr, i+1, r);
    return i+1;
}

void qsort(int a[], int l, int r) {
    if (l < r) {
        int q = partition(a, l, r);
        qsort(a, l, q-1);
        qsort(a, q+1, r);
    }
}

void s_qsort(char *lineptr[], int l, int r) {
    if (l < r) {
        int q = s_partition(lineptr, l, r);
        s_qsort(lineptr, l, q-1);
        s_qsort(lineptr, q+1, r);
    }
}

int g_partition(void *v[], int l, int r, int (*comp) (void *, void *)) {
    void *x = v[r]; /* pivot value */
    int i = l-1; /* keep a[l..i] < x, initialize i right before l */
    int j;
    for (j = l; j < r; j++) {
        if ((*comp)(v[j], x) <= 0) {
            i++;
            g_swap(v, i, j);
        }
    }
    g_swap(v, i+1, r);
    return i+1;
}

void g_swap(void *v[], int i, int j) {
    v_swap(&v[i], &v[j]);
    // void *temp;
    // temp = v[i];
    // v[i] = v[j];
    // v[j] = temp;
}

void g_qsort(void *v[], int l, int r, int (*comp) (void *, void *)) {
    if (l < r) {
        int q = g_partition(v, l, r, comp);
        g_qsort(v, l, q-1, comp);
        g_qsort(v, q+1, r, comp);
    }
}
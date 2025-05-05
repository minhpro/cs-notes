#include "c_pointer.h"

void v_swap(void **x, void **y) {
    void *temp;
    temp = *x;
    *x = *y;
    *y = temp;
}

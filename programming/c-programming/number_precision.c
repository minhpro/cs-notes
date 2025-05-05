#include <stdio.h>

float sumtester();

float othersum();

void main() {
    sumtester();
    othersum();
}

float sumtester () {
    float sum; 
    int i;
    sum = 0.0;
    for (i = 0; i < 1000000000; i++) {sum = sum + 1.0;}
    printf ("%f\n", sum);
}

float othersum() {
    float sum, comp, oldsum; 
    int i;
    sum = comp = 0.0;
    for (i = 0; i < 10; i++) {
        comp = comp + 1.0;
        oldsum = sum;
        sum = oldsum + comp;
        comp = (sum - oldsum) + comp;
    }
    printf ("%f\n", sum);
}
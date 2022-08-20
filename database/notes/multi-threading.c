#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

int count = 0;

void *increase() 
{
    for (int i = 0; i < 100000; i++) {
        count++;
        printf("%lu: %d\n", (unsigned long)pthread_self(), count);
    }
}
// clang -pthread multi-threading.c -o multi-thread.out
int main()
{
    pthread_t thread_id_1;
    pthread_t thread_id_2;
    pthread_create(&thread_id_1, NULL, increase, NULL);
    pthread_create(&thread_id_2, NULL, increase, NULL);

    pthread_join(thread_id_1, NULL);
    pthread_join(thread_id_2, NULL);
    
    printf("Final value: %d\n", count);
    exit(0);
}
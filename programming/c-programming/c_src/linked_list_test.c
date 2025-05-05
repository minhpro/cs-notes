#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

int main() {
    // LinkedList *l = (LinkedList *)malloc(sizeof(LinkedList)); // dynamic allocation
    // l->count = 0;
    // l->head = NULL;
    // LinkedList *p = l;
    LinkedList l = {0, NULL}; // static allocation
    LinkedList *p = &l;
    addAtFront(p, 10);
    addAtFront(p, 13);
    addAtFront(p, 9);
    addAtFront(p, 1);
    addAtFront(p, 20);
    printList(p);
    reverseList(p);
    printList(p);
}
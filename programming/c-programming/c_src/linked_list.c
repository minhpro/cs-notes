#include <stdio.h>
#include <stdlib.h>
#include "linked_list.h"

void addAtFront(LinkedList *l, int value) {
    Node *new_node = (Node *) malloc(sizeof(Node));
    new_node->value = value;

    if (l->head == NULL) {
        l->head = new_node;
    } else {
        new_node->next = l->head;
        l->head = new_node;
    }
    (l->count)++;
}

void printList(LinkedList *l) {
    if (l->head == NULL) {
        printf("Empty list\n");
        return;
    }
    Node *t = l->head;
    while (t) {
        printf("%d -->", t->value);
        t = t->next;
    }
    printf(" NULL\n");
    printf("Total: %d\n", l->count);
}

void reverseList(LinkedList *l) {
    if (l->head == NULL)
        return;
    Node *previous = NULL;
    Node *current = l->head;
    while (current)
    {
        Node *temp = current;
        current = current->next;
        temp->next = previous;
        previous = temp;
    }
    l->head = previous;
}

void freeList(LinkedList *l) {
    Node *t = l->head;
    while (t) {
        Node *temp = t;
        t = t->next;
        free(temp);
    }
}
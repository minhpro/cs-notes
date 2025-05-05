#ifndef __LINKEDLIST_H
#define __LINKEDLIST_H
/* c_programing/src/linked_list.h */
typedef struct node Node;
typedef struct linked_list LinkedList;

struct node {
    int value;
    Node *next;
};

struct linked_list {
    int count;
    Node *head;
};

void addAtFront(LinkedList *l, int value);
void printList(LinkedList *l);
void reverseList(LinkedList *l);
void freeList(LinkedList *l);

#endif /* __LINKEDLIST_H */

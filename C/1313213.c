#include<stdio.h>
#include<stdlib.h>

struct LinkedList{
    int data;
    struct LinkedList *next;
};
void Insert(struct LinkedList** head,int newData){
    struct LinkedList* newnode;
    newnode=malloc(sizeof(struct LinkedList));
    newnode->data=newData;
    newnode->next=(*head);
    (*head)=newnode;
}

void Delete(struct LinkedList** head,int delData)
{
    while ((*head)->next != NULL) {
        if((*head)->data == delData)
        {
            (*head)=((*head)->next)->next;
        }
        else (*head) = (*head)->next;
        printf("%d is %d\n",(*head)->data,(*head)->next);
    }
    //if((*head)->data==delDate)
    printf("\n");
}
void printLinkedList(struct LinkedList* head) {
    while (head != NULL) {
        printf("%d -> ", head->data);
        head = head->next;
    }
    printf("NULL\n");
}
int main()
{
    struct LinkedList *head=NULL;
    int i,del_num;
    for(i=0;i<10;i++)
    {
        Insert(&head,i);
    }
    printLinkedList(head);

    printf("Enter del_num:");
    scanf("%d",&del_num);
    Delete(&head,del_num);
    printLinkedList(head);

}

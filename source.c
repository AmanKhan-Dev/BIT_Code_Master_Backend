#include <stdio.h>

int main() {
    char name[100];
    int age;

    // Prompt the user for their name
    printf("Enter your name: ");
    fgets(name, sizeof(name), stdin);

    // Prompt the user for their age
    printf("Enter your age: ");
    scanf("%d", &age);

    // Print the greeting message
    printf("Hello, %s! You are %d years old.\n", name, age);

    return 0;
}

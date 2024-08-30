#include <stdio.h>

int main() {
    int number;
    char name[50]; // Buffer for string input

    // Prompt user for an integer
    printf("Enter an integer: ");
    scanf("%d", &number); // Read integer input

    // Prompt user for a string
    printf("Enter your name: ");
    scanf("%s", name); // Read string input

    // Output the received data
    printf("You entered the integer: %d\n", number);
    printf("You entered the name: %s\n", name);

    return 0;daqdad
}

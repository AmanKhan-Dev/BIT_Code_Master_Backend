#include <stdio.h>

int main() {
    int a, b;
    int sum;

    sum = a + b; // Using uninitialized variables

    printf("%d\n", sum);
    return 0;
}
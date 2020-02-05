/*----------------------*/
/* Justin Voo           */
/* format_specifiers.c  */

#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    int age = 22;
    double average = 246.13579;
    
    printf("\nJustin Voo. Lab 3.\n\n");
    printf("Jane went to the store.\n\n");
    printf("My friend is %2d years old.\n\n", age);
    printf("\"So many books, so little time.\"\n-Frank Zappa\n\n");
    printf("The average = %5.1f\n\n", average);
    printf("The average = %7.3f\n\n", average);
    printf("The average = %.2e\n\n", average);

    return EXIT_SUCCESS;
}

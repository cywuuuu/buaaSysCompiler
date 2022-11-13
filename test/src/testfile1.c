#include<stdio.h>
int getint(){ int n; scanf("%d",&n); return n; }
int funcDefInt1()
{
    return 0;
}
/**
 * overview: test
 */
int funcDefInt2(int a)
{
    return a;
}

int funcDefInt3(int a[], int b[][3])
{
    return b[2][2] * a[1];
}

void funcDefVoid1()
{ // empty
}

void funcDefVoid2(int a)
{
    
}

void funcDefVoid3(int a[], int b[][3], int c[][2])
{
}
int main() {
    printf("20373456\n"); 
        printf("20373456\n"); 

    printf("Enter an integer: \n");
    int reverse_num = 7, num = 9; 
    int remainder = 4; 
    num = getint();
    int temp = num;
        while (temp != 0)
    {
        remainder = temp % 10;
        reverse_num = reverse_num * 10 + remainder;
        temp = temp/10;
    }
    // while (temp!=0)
    // {
    //     remainder = temp%10;
    //     reverse_num = reverse_num*10+remainder;
    //     temp/=10;
    // }
    while(0) {;}
    {
        int a = 10 ;
        a = +a + +a;
        printf("%d\n\n\n\n\n\n\n\n", a); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("11111"); 
    }
    return 0; 
}
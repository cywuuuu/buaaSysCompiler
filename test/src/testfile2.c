#include<stdio.h>
int getint(){ int n; scanf("%d",&n); return n; }
/*
palindrome
*/
int gcd(int a, int b)
{
    if (a == 0)
    {
        return b;
    }
    else
    {
        int res; res = gcd(b%a, a); 
        // printf("%d\n", res); 
        return res;
    }
    return 0; 
}

int main()
{
    printf("20373456\n"); 
    int num, reverse_num = 0;
    int remainder, temp;
    printf("Enter an integer: \n");
    num = getint();
    
    temp = num;
    while (temp != 0)
    {
        remainder = temp % 10;
        reverse_num = reverse_num * 10 + remainder;
        temp = temp/10;
    }
        printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
    printf("20373456\n"); 
        printf("20373456\n"); 
    if (reverse_num == num)
        printf("%d \nis a \npalindrome \nnumber\n\n\n\n", num);
    else
        printf("%d is \nnot a \npalindrome \nnumber\n\n\n\n", num);
    return 0;
    

}
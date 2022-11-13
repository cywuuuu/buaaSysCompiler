#include <stdio.h>
int getint(){ int n; scanf("%d",&n); return n; }
// int gcd(int a, int b)
// {
//     if (a == 0)
//     {
//         return b;
//     }
//     else
//     {
//         int res = 2;
//         res = gcd(b%a, a);
//         // printf("%d\n", res);
//         return res;
//     }
// }
int visit[5] = {0, 0, 0, 0, 0};
int a[5] = {0, 0, 0, 0, 0};
int t = 0;
int n = 4;


int gcd(int a, int b)
{
    if (a == 0)
    {
        return b;
    }
    else
    {
        int res;
        res = gcd(b % a, a);
        // printf("%d\n", res);
        return res;
    }
    return 0;
}
int main()
{
    printf("20373456\n");
    int res = 0;
    // res = gcd(203345, 1145);
    printf("%d\n\n\n\n\n\n\n\n", res);
    printf("20373456\n");
    printf("20373456\n");
    printf("20373456\n");
    printf("20373456\n");
    printf("20373456\n");
    printf("20373456\n");
    printf("20373456\n");
    return 0;
}
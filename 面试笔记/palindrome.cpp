#include <iostream>
using namespace std;

int palindrome(char *s){
    int len = strlen(s);
    int left = 0;
    int right = len - 1;
    while(s[left++] == s[right--]){
        if (left >= right){
            return 1;
        }
    }
    return 0;
}

int main(){
    char s[1000];
    getline(s);
    if(palindrome(s)){
        cout << "True" << endl;
    }else{
        cout << "False" << endl;
    }
    return 0;
}
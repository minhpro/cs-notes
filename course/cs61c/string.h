/* read a line into s, return length */
int getline_2(char s[], int lim);

/* copy 'from' into 'to'; assume to is big enough */
void copy(char to[], char from[]);

/* trimmed is a string after remove blank characters (spaces, newlines, tabs) from the head and trailing of s;
 assume trimmed is big enough, return the length of trimmed */
int trim(char trimmed[], char s[]);

/* Generic swap values between two pointers, assume a and b are same size (len) */
void swap(void *a, void *b, size_t len);

/* Swap two elements in the array */
void array_swap(char a[], int i, int j);

/* make reversed string r of a string s with length len */
void reverse(char r[], char s[], int len);

/* reverse the string s with length of len */
void inplace_reverse(char s[], int len);

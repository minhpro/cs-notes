int getch(void); /* get a (possibly pushed-back) character */

void ungetch(int c); /* push character back on input */
 
/*
* Read a line from the standard input.
* return the length of line has read.
*/
int get_line(char *line, int maxline);

/*
* Read input lines 
* lineptr: pointer to array of strings,
* each string is big enough to hold the input line.
* maxline: maximum length of input line.
* maxlines: maximum number of input lines to read.
*/
int readlines(char *lineptr[], int maxline, int maxlines);

/* write ouput lines */
void writelines(char *lineputr[], int nlines);
#include <stdarg.h>
#include <stdio.h>

void minprintf(char *fmt, ...) {
	va_list ap; /* is used to declare a variable that will refer to each argument in turn */
	char *p, *sval;
	int ival;
	double dval;
	

	va_start(ap, fmt); /* must be called once before ap can be used */
	for (p = fmt; *p; p++) {
		if (*p != '%') {
			putchar(*p);
			continue;
		}
		switch (*++p) {
			case 'd':
				ival = va_arg(ap, int);
				printf("%d", ival);
				break;
			case 'f':
				dval = va_arg(ap, double);
				printf("%f", dval);
				break;
			case 's':
				for (sval = va_arg(ap, char *); *sval; sval++)
					putchar(*sval);
				break;
			default:
				putchar(*p);
				break;
		}
	}
	va_end(ap);
}

int main() {
	minprintf("Number is %d\n", 10);
}
	

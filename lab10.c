/* Author(s): Justin Voo
 * This is a mini_shell.c
 * This program serves as a skeleton for starting for lab 10.
 * Student is required to use this program to build a mini shell
 * using the specification as documented in lab 8.
 * Date: Fall 2019
 
*/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

#define MAXLINE 80
#define MAXARGS 20
#define MAX_PATH_LENGTH 50
#define TRUE 1

/* function prototypes */
void process_input(int argc, char **argv);
int parseline(char *cmdline, char **argv);
void handle_redir(int count, char *argv[]);


/* ----------------------------------------------------------------- */
/*                  The main program starts here                     */
/* ----------------------------------------------------------------- */
int main(void)
{
    char cmdline[MAXLINE];
    char *argv[MAXARGS];
    int argc;
    int status;
    pid_t pid;

    /* Loop forever to wait and process commands */
    while (TRUE) 
    {
	printf("\ncsc60mini> ");

	/* read the command the user types in */
	fgets(cmdline, MAXLINE, stdin);

	/* Call parseline to builed argc/argv;  their limits declared above */
	argc = parseline(cmdline, argv);
    
	/* Just to make sure printfs */
	printf ("CommandLine = %s \nArgc = %i \n", cmdline, argc);
	int i;
	for(i = 0; i < argc; i++)
	{
	    printf ("Arg %i = %s \n", i, argv[i]);
	}
	
	/* If user hits enter key without a command, continue to loop again at the beginning */
	/*  Hint: if argc is zero, no command declared */

	if(argc == 0)
	    continue;
	
	/* Handle build-in command: exit, pwd, or cd  */
	if(strcmp(argv[0], "exit")==0)
	{
	    /* We have a built-in command of exit */
	    exit(EXIT_SUCCESS);
	}
	else if (strcmp(argv[0], "pwd")==0)
	{
	    /* We have a built-in command of pwd */
	    char path[MAX_PATH_LENGTH];
	    getcwd(path, MAX_PATH_LENGTH);
	    printf("%s \n", path);
	    continue;
	}
	else if (strcmp(argv[0], "cd")==0)
	{
	    char *dir;
	    if(argc == 1)
		dir = getenv("HOME");
	    else
		dir = argv[1];
	
	    if(chdir(dir) != 0)
		perror("error changing directory\n");
	    
	    continue;
	}


	    /* Else, fork off a process */
	else
	{
	    pid = fork();
	    switch(pid)
	    {
		case -1:
		    perror("Shell Program fork error \n");
		    exit(EXIT_FAILURE);
		case 0:
		    /* I am child process. 
		       I will execute the command, call: execvp */
		    process_input(argc, argv);
		    break;
		default:
		    /* I am parent process */
		    if (wait(&status) == -1)
			perror("Shell Program error \n");
		    else
			printf("Child returned status: %d\n",status);
			break;
	    }	/* end of the switch */
	}   	/* end of the if-else-if loop */
    }       	/* end of the while loop */
}   	        /* end of main */

/* ----------------------------------------------------------------- */
/*                  parseline                                        */
/* ----------------------------------------------------------------- */
/* parse input line into argc/argv format */

int parseline(char *cmdline, char **argv)
{
    int argc = 0;
    char *separator = " \n\t";
    argv[argc] = strtok(cmdline, separator);
	
    while ((argv[argc] != NULL) && (argc+1 < MAXARGS)) 
    {
	argv[++argc] = strtok((char *) 0, separator);
    }
  return argc;
}
/* ----------------------------------------------------------------- */
/*                  process_input                                    */
/* ----------------------------------------------------------------- */
void process_input(int argc, char **argv) 
{
    int ret;

    handle_redir(argc, argv);
    
    ret = execvp(argv[0], argv);
    if(ret == -1)
    {
	perror("Error executing command\n");
	_exit(EXIT_FAILURE);
    }
}
    /* end of function */

/* ----------------------------------------------------------------- */
/*                  handle_redir                                     */
/* ----------------------------------------------------------------- */
void handle_redir(int count, char *argv[])
{
    int i;		// loop counter
    int out_redir = 0;	// track position of out redirection
    int in_redir = 0;	// track postion of in redirection
    int fd;

    for (i=0; i < count; i++)
    {
	if(strcmp(argv[i], ">") == 0)
	{
	    if (out_redir != 0) 
	    {
		fprintf(stderr, "Cannot output to more than one file\n");
		_exit(EXIT_FAILURE);
	    }
	    else if (i == 0)
	    {
		fprintf(stderr, "No command\n");
		_exit(EXIT_FAILURE);
	    }
	    out_redir = i;
	} 
	else if (strcmp(argv[i], "<") == 0)
	{
	    if (in_redir != 0)
	    {
		fprintf(stderr, "Cannot take more than one file as input\n");
		_exit(EXIT_FAILURE);
	    }
	    else if (i  == 0)
	    {	
		fprintf(stderr, "No Command\n");
		_exit(EXIT_FAILURE);
	    }
	    in_redir = i;
	}
    }		/*end of for loop*/

    if (out_redir != 0)
    {
	if(argv[out_redir+1] == NULL)
	{
	    fprintf(stderr, "No outnput file specified\n");
	    _exit(EXIT_FAILURE);
	}

	fd = open(argv[out_redir+1], O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
	if(fd < 0)
	{
	    fprintf(stderr, "Error opening file\n");
	    _exit(EXIT_FAILURE);
	}
		
	dup2(fd, 1);  	// makes newfd be the copy of oldfd, closing
			// newfc first if necessary.  if oldfd is not valid,
			// call fails and newfd is not closed.
			// if oldfd is valid, and newfd has same value,
			// dup2 does nothing, and returns newfd
	close(fd);
	argv[out_redir] = NULL;
    }

    if(in_redir !=0)
    {
	if(argv[in_redir+1] == NULL)
	{
	    fprintf(stderr, "No input file specified\n");
	    _exit(EXIT_FAILURE);
	}

	fd = open(argv[in_redir+1], O_RDONLY);
	if (fd < 0)
	{
	    fprintf(stderr, "Error opening file\n");
	    _exit(EXIT_FAILURE);
	}

	dup2(fd, 0);
	close(fd);
	argv[in_redir] = NULL;
    }
}	
/*end of function*/


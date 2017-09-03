=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
Nets 150 README
PennKey: roopac
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

========================
Interacting with Program
========================

To begin, run the program main. The user will be prompted to enter a number of the question they want answered. The numbers correspond
to the questions from the homework instructions in Part 2 of the assignment. After entering a number, the user will be prompted to enter more
parameters if the question takes more input. Everything in italics from the homework instructions can be set by the user at this time. The program
will put the parameters given in the homework in parentheses so the user has a suggestion of what to enter and what the program is looking for.
In the program, there are no hardcoded URLS where the full url is entered. For questions that take the category as user input, the program uses
that to call the helper function getURL(userinput). Where there is no user input, then I just call the helper function with the category name such as
getURL("Best Director"), however, I do not code in the url of the best director wikipedia page. 

The program is set up so each question calls a different function in main that parses Wikipedia and returns the correct information. 
After answering the question and returning the answer, the program ends. 


============

Question 1: 
For this problem, the user does not have to enter any parameters since everything is set. Therefore, there are no assumptions for this quesion since
the page within wikipedia is set. 


Question 2: 
For this question, the user enters the name of the movie listed on the Best Original Screenplay screen and the program outputs which writers wrote the movie. 
While Divorce Italian Style is given as the prompt, this also works for other movies that are similarly listed on the page. While Divorce Italian Style won
Best Original Screenplay, the program also works on movies listed on the page that did not. For example, entering the titles The Band Wagon, Titanic, or
The Facts of Life all print out the correct writers. 


Question 3: 
For this question, the user enters the decade(the starting year) and category and the program outputs the winners. For this program, I assumed the inputs would
 be similar to 1980 and Best Documentary Feature. For example, Best Documentary lists winners utilizing tags in the HTML that are slightly different than other
 pages such as Best Leading Actress. Therefore, the inputs have to be similar to the ones given in the instructions. In order to parse pages such
 as Best Leading Actress I would have to had created an entirely different regex template since the tags I use in Best Documentary Feature are not
 used in this page. Some other prompts that are similar are Best Documentary Feature 1970 or 1960 or Best Film Editing 1940. 
 
Question 4: 
The user enters the name of the role they want to search and the program returns all Best Leading Actor Nominees who played that role. My program
prints out an actor multiple times if they acted in multiple movies where they played the specified role since each one represents a different
movie/character. For example, you can change king to something similar(like Dr.). 

Question 5: 
For this question, the user inputs an integer and the program outputs all the directors that have at least that many nominations and their
corresponding movies. I originally struggled with this since couple of directors are nominated for two different films the same year, but I
added a helper function called directorsTwoMovies() that checks for this and adds the second movie to the treeMap before continuing. 

Question 6: 
Takes the category as user input, and returns the highest grossing film that won the category. I assumed the category given would be
similar to the Best Picture page because some of the other pages present the information differently in the table which would invovle
an entirely different regex template for the different page. I used the box office number on the movie's wikipedia page to calculate how much the movie grossed. The number returned is converted from
its original format such as millions or billions to the actual number so comparisons are easier. For example 2 million would be returned as 2E6.

Question 7: 
For this question, the program takes the age and category as input and returns the actors/actresses who were over that age when they won. I used the actor's
birth year and the year the oscar was presented to calculate how old they were. In instances when the table lists two years for when the oscar was given
out such as 1928-1929, I use the first number. I print out duplicates so the user knows how many times the actor won over the given age. 

Question 8: 
For the wildcard question, I decided to compute how mant spose(s) an actor/actress have had. In today's era, it seems actors remarry more frequently
than the average person so I was interested in seeing the data over Oscar winners. This question takes a category and a cutoff number and returns
all the actors (along with the number of spouses they have had) that are over the cutoff number.  


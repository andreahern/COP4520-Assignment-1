# COP4520 Assignment 1
Calculate Primes up to 100,000,000

- [COP4520 Assignment 1](#cop4520-assignment-1)
    - [Compile And Run](#compile-and-run)
    - [Documentation](#documentation)


### Compile And Run

To compile program simply run the following:
```
javac assignment_1.java
```

A java class file name `assignment_1.class` will be created that can then be executed as such:
```
java assignment_1
```

Doing so will modify a text file (and create one if not present) called `primes.txt`, this file will contain output in this format:
```
<execution time>  <total number of primes found>  <sum of all primes found> 
<top ten maximum primes, listed in order from lowest to highest> 
```

This file can later be referred to without having to execute the program again.

### Documentation

This particular implementation leverages parallel programming with the segmented sieve of eratosthenes algorithm to calculate prime numbers in equal ranges in concurrently. 

This approach on average has a runtime of 1.5 seconds while an equally valid approach using the square root algorithm will take about 15 to 17 seconds to complete.

As you can clearly tell using the segmented sieve of eratosthenes saves an enormous amount of time especially when scaled upwards.

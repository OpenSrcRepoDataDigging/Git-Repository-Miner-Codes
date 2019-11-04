# dataconvert module

#### maybe models are never used during our coding...

This module aims to convert data format from csv files(Or other files, specified) to sql tuples. 

Csv files are generated in jgit repo module. 

One original-data-to-sql-format process should have one corresponding “ConverterImpl”.

After convert, tuples should be inserted to sqlite database by calling Methods in jdbc module.

---

#### NOTICE!

When you try to convert data, please reduce the number of data-COPY as much as you can. 
This will improve the performance of computing.
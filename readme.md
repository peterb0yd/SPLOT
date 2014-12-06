<h1>Project Files</h1>

<h4>MakeProducts.java</h4>
- Uses 3Sat DIMACS solver to create 1000 products
- Checks for duplicates, however it should never find any

<h4>SPL.java</h4>
- Our problem class for JMetal
- Sets decision variables
- Evalutates solutions
- Sets objectives

<h4>ProductSolution.java</h4>
- Prints optimal solution(s) to "ProductSolutionList" file

<h4>NSGAII_main</h4>
- Slightly modified to print out solutions
- Calls "ProductSolution.print()" function at line 154

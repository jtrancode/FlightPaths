# FlightPaths

You must have Java installed in order to run the jar from the command line.

When you run, you must give arguments with (flightData) and (pathsToCalc).

The flight data file should have the number of flights on the first line. The lines after should be the flight info in this format: (source)|(destination)|(cost)|(time).

For the paths to calcuate file, it should have on the first line the number of flights to calculate. The lines after should have the paths you want to calcuate in the format of: (source)|(destination)|(T or C).

T or C represent time or cost, depending on what you want to calculate by.

## Example file for flightData
4<br>
Dallas|Austin|98|47<br>
Austin|Houston|95|39<br>
Dallas|Houston|101|51<br>
Austin|Chicago|144|192<br>

## Example file for pathsToCalc
2<br>
Dallas|Houston|T<br>
Chicago|Dallas|C<br>

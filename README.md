xdebug-strace-profiler
======================

Uses XDebug strace output to build an XML performance report for viewing it in a browser

For instance, given XDebug strace https://dl.dropbox.com/u/1154823/trace/trace.txt it
generates document https://dl.dropbox.com/u/1154823/trace/trace.xml

The main goal of the project is to build a tree of function calls and calculate time each function call takes to execute. I used XML for tree represenation so it can easily be viewed by any browser at hand.

The most common profiling pattern is taking a measurement and digging into the most time consuming call and then repeating that process again until the most time consuming calls or blocks can be identified. Using xdebug-strace-profiler you can generate XML performance profile, collapse all top level nodes (which represent top level function calls made between xdebug_strace_start() and xdebug_strace_end()) and find out what was the most CPU consuming call. Then you can expand that node and continue the search.

TODO
* A handful of tests would be nice
* Refactoring those passionate s-expressions would be nice, too
* Abstract from XML; any output format, quite easy to implement
* Percentage of how much parent call time do subcalls take?
* Option to show only the most time consuming path of calls
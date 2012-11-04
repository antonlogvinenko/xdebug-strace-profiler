xdebug-strace-profiler
======================

Uses XDebug strace output to build an XML performance report for viewing it in a browser

For instance, given XDebug strace https://dl.dropbox.com/u/1154823/trace/trace.txt it
generates document https://dl.dropbox.com/u/1154823/trace/trace.xml

The main goal of the project is to build a tree of function calls and calculate time each function call takes to execute. I used XML for tree represenation so it can easily be viewed by any browser at hand.

The most common profiling pattern is taking a measurement and then digging into the most time consuming call and then repeating that process again until most time consuming calls or blocks can be identified. Using xdebug-strace-profiler you can generate XML performance profile, collapse top level nodes (representing top level function calls made between xdebug_strace_start() and xdebug_strace_end()) and see what's the most CPU greedy call. After that you expand that node and continue to search.
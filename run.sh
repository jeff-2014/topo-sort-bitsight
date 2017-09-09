#!/bin/bash

javac Graph.java

#echo "Trying bad input"
#cat bad-input.txt | java Graph

echo "Trying good input"
cat dg-input.txt |java Graph

#!/usr/bin/env bash

#cat EURUSD.txt - | nc -i 2 -lk 9999

pv -l -L1 ../src/main/resources/forex/EURUSD.txt | nc  -i 5 -lk 9999
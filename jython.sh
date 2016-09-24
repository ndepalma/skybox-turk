#!/bin/bash

CP=`lein classpath`

jython -Dpython.path=$CP -i $1

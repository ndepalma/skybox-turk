#!/bin/bash 

scp -r _site/* $1@nimbus.media.mit.edu:www/$2/

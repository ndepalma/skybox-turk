#!/bin/bash 

echo "Copying files to website"
scp -r _site/* $1@nimbus.media.mit.edu:www/$2/

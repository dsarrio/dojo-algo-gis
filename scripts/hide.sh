#!/bin/bash

echo -n Password: 
read -s password
echo
find . -name "*.kt" -exec openssl enc -aes-256-cbc -pass pass:$password -a -salt -in {} -out {}.hidden \;

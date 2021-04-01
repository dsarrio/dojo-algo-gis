#!/bin/bash

echo -n Password: 
read -s password
echo
find kotlin/ -name "*.kt" -exec openssl enc -aes-256-cbc -pass pass:$password -a -salt -in {} -out {}.hidden \;
find typescript/src -name "*.ts" -exec openssl enc -aes-256-cbc -pass pass:$password -a -salt -in {} -out {}.hidden \;

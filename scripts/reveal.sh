#!/bin/bash

set -e

echo -n Password: 
read -s password
echo
for i in $(find kotlin/ -name '*.kt.hidden')
do
    openssl enc -d -aes-256-cbc -pass pass:$password -a -in $i -out ${i%.*}
    rm $i
done

for i in $(find typescript/src/ -name '*.ts.hidden')
do
    openssl enc -d -aes-256-cbc -pass pass:$password -a -in $i -out ${i%.*}
    rm $i
done

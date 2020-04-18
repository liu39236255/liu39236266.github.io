#!/bin/bash
echo "Running shell script";
RESULT="";#变量两端不能直接接空格符
while read LINE; do
   RESULT=${RESULT}" "${LINE}
done

echo ${RESULT} > out123.txt
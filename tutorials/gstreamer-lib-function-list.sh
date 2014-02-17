#!/bin/bash
# regenerate gstreamer-lib-function-list.txt

ARCH=`arch | sed -e s/i.86/i386/`
[ $ARCH == i386 ] && DIR=/usr/lib || DIR=/usr/lib64
cd $DIR

for i in `find . -maxdepth 1 -name 'libgst*' -a -type f|sort`; do
	echo "================= $i ================="
	nm -D -C $i|grep T|cut -f3 -d' '
done

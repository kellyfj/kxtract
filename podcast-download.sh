#!/bin/bash
# 
#
# Description: A simple bash script to download all media from a podcast XML feed
#
# Usage: sh podcast-download.sh http://myfeed.com/rss /PATH/TO/FOLDER
#
# Author: Andrew Morton
# Url: https://github.com/mortocks/bash-podcast-download
#
# Licence: GNU v3.0
 

# Optional Variables
# You can hardcode the feed and url variables here to avoid sending them when envoking the script
FEED='' # URL TO THE RSS FEED
FOLDER='' # RELATIVE PATH OF FOLDER TO DOWNLOAD FILES TO

# Override hardcoded feeds with passed variables
[ -n "$1" ] && FEED=$1
[ -n "$2" ] && FOLDER=$2

# Check if feed is empty
if [ -z "$FEED" ]; then
	echo "Error: No feed specified"; exit
fi

# Check if path is empty
if [ -z "$FOLDER" ]; then
	echo "Error: No folder specified"; exit
fi

# Create destination folder if it doesn't exsist
if [ -d $FOLDER ]; then
	echo "$FOLDER exists"
else 
	echo "Creating directory $FOLDER"
	mkdir $FOLDER
fi 


STARTTIME=`date +%s`


# Get the full XML feed | extract the enclosure url attribute | extract the url
MEDIA=$(curl -s $FEED | xpath '/rss/channel/item/enclosure/@url' 2>/dev/null | egrep -o 'https?://[^"<]+' )


# Loop through and download file if not already downloaded
while IFS= read -r URL
do

	# Find the last part of the url using the / as delimiter
	AFTER_SLASH=${URL##*/}

	# Remove any additional query params in the filename by removing everything after ?
	FILE_NAME=${AFTER_SLASH%%\?*}

	DATE=$(date)

	# If file as already been downloaded ignore
	if [ -f $FOLDER/$FILE_NAME ]; then
		echo "Exsists $URL $FOLDER/$FILE_NAME $FILE_NAME $DATE"
	else 
		echo "Download $URL $FOLDER/$FILE_NAME $FILE_NAME $DATE"
		curl -s -L $URL > $FOLDER/$FILE_NAME
	fi 

done <<< "$MEDIA"

ENDTIME=`date +%s`
echo Finished total time `expr $ENDTIME - $STARTTIME`s.

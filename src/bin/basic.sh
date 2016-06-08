#!/bin/bash

#GLHF

HOST_IP=`echo $SSH_CONNECTION | awk {'print $1'}`
HOST_PORT=`echo $SSH_CONNECTION | awk {'print $2'}`


SSH_IP=`echo $SSH_CONNECTION | awk {'print $3'}`
SSH_PORT=`echo $SSH_CONNECTION | awk {'print $1'}`


# Node 1
java -jar openchordcmd.jar $SSH_IP 8089

# Node 2:
java -jar openchordcmd.jar $SSH_IP 8081 $SSH_IP 8080 &

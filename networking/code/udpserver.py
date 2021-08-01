#!usr/bin/python

# A simple client-server application to demonstrate socket programming for both UDP and TCP:
# 1. The client reads a line of characters (data) from its keyboard and sends the data to the server.
# 2. The server receives the data and converts the characters to uppercase.
# 3. The server sends the modified data to the client.
# 4. The client receives the modified data and displays the line on its screen.

import socket as s

serverSocket = s.socket(s.AF_INET, s.SOCK_DGRAM) # For UDP
serverSocket.bind(('', 12345))

while True:
    message, clientAddress = serverSocket.recvfrom(2048)
    modifiedMessage = message.decode().upper()
    print('Received Messages: {}, from {}'.format(message, clientAddress))
    serverSocket.sendto(modifiedMessage.encode(), clientAddress)

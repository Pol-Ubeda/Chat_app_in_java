# Chat-app-in-java
Using sockets to create a server that handles incoming data from clients and broadcasts it to all other clients (excluding sender).

We have Server side and client side. At the moment works at local network level. Ip address when creating both Socket and ServerScoket must be changed to IP address where server is hosted in the network.

Next step is to add TLS by using SSLSockets instead of Socket and ServerSocket. Right now all traffic is in cleartext!

Internet is a global internetwork - network of networks. It provides basic communication of messages (packets) between computers (devices) all around the world.

World Wide Web (WWW) is an application which uses Internet for communication. It is perhaps the most important application on Internet.

WWW is also a distributed system. It run on top of the Internet and presents a model in which everything looks like a document (Web page).

Networks called VPNs (Virtual Private Network) are used to join the individual networks at different sites into one extended network.

There are two types of transmission technology that are **broadcast** links and **point-to-point** links.

Wireless LAN: WiFi is very popular

Wired LAN: Copper wires and optical fibers - switched Ethernet 

## Internet's address assignment strategy

Classless Interdomain Routing (CIDR)

32-bit IP address is divided into two parts with form a.b.c.d/x, where the leftmost x bits identicates the **subnet prefix**.

A network is composed by many subnets called internetwork. In a subnet, every devices has the same subnet prefix of IP address.

Subnets compose together through routers. The subnet prefixes are used to forward packets across internetwork, between subnets. The remain bits of IP address are used to forward packets within the subnet.

## Get token by passwork flow

```
curl \
  -d "client_id=login-app" \
  -d "client_secret=tqKWkikAo5Y2wwRbY515OYxf7f84HUQ6" \
  -d "username=sysadmin@ebd.edu" \
  -d "password=12345678" \
  -d "grant_type=password" \
  "http://localhost:8180/realms/mylms/protocol/openid-connect/token"
```

```
curl \
  -d "client_id=lms-app" \
  -d "client_secret=lcqAZLjqv4K5A0tm8NHt4M2Dxb4GvyqC" \
  -d "username=hs01@ebd.vn" \
  -d "password=12345678" \
  -d "grant_type=password" \
  "https://login.ebdlms.edu.vn/realms/ebd/protocol/openid-connect/token"
```

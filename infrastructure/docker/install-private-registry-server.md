1. Start the registry container

`docker run -d -p 5000:5000 --restart=always --name registry registry:2`

2. Configure an insecure registry

Edit `/etc/docker/daemon.json` on Linux or open Preference in the Docker application on Mac (or Windows)

```
{
    "insecure-registries": ["localhost:5000"]
}
```

After that, restart Docker

3. Build or tag your image with the registry address

`docker tag <your-image> localhost:5000/<image-name>:<tag>`

OR

`docker build -t localhost:5000/<image-name>:<tag> <path-to-dockerfile or current path .>`

4. Push the image to the local registry

`docker push localhost:5000/<image-name>:<tag>`


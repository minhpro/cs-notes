As the README describes, you can reuse the Docker daemon from Minikube with eval $(minikube docker-env).

So to use an image without uploading it, you can follow these steps:

1. Set the environment variables with eval $(minikube docker-env)
2. Build the image with the Docker daemon of Minikube (eg docker build -t my-image .)
3. Set the image in the pod spec like the build tag (eg my-image)
4. Set the imagePullPolicy to Never, otherwise Kubernetes will try to download the image.

Important note: You have to run eval $(minikube docker-env) on each terminal you want to use, since it only sets the environment variables for the current shell session.
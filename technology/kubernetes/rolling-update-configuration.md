https://www.bluematador.com/blog/kubernetes-deployments-rolling-update-configuration

## Kubernetes Rolling Update

Rolling updates allow you to update your pods gradually. There are two options can be configured to fine-tune the update process (these value can be integers or percentages):
* **maxSurge**: The number of pods can be created above the desired amount of pods during a update process.
* **maxUnavailable**: The numbers of pods can be unavailable during a update process.

The default values of both options are 25%. For an example of updating a Deployment with 4 pods. During a update process, the following conditions will be met:
* At most 5 pods (4 desired + 25% maxSurge pods) will be Ready.
* At least 3 pods (4 desired - 25% maxUnavailble pods) will always be Ready.

Your service will always available during a update process course by at least 3 pods are Ready.

Pods are marked as *Ready* if they are fully deployed, *NotReady* if they are being created, and *Terminating* if they are being removed.

The default property of *Ready* is that a pod is ready as soon as its containers start. Kubernetes gives you the control do determine when a pod is ready by the *Readiness* configuration. The *Readiness* is confugred by two options: *readinessProbe* property and *spec.minReadySeconds* (time passed since the pod was created)

```
readinessProbe:
    periodSeconds: 15 // how often to perform the probe
    timeoutSeconds: 2 // the probe times out
    successThreshold: 2 // Min consecutive successes to be considered successful
    failureThreshold: 2 // try more before giving up
    httpGet: // probe by httpGet
        path: /health
        port: 80
```

Applying to the BFF application, we can configure like this

```
readinessProbe:
    timeoutSeconds: 2
    failureThreshold: 2
    tcpSocket:
        port: 8081 // BFF exposed port
```

# tdc-sp-2025-dapr


Thank you for attend the TDC São Paulo 2025.

## [Step by step to run the application on Kubernetes](/kubernetes/README.md)

Creating an Order:

```shell
curl --request POST --url http://localhost:8080/orders --header 'Content-Type: application/json' --data '{ "item": "Keyboard", "amount": 199.99 }'
```


Creating an Order with Outbox:

```shell
curl --request POST --url http://localhost:8080/outbox/orders --header 'Content-Type: application/json' --data '{ "item": "Keyboard", "amount": 199.99 }'
```

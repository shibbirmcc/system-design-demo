# system-design-demo


[ApplianceStatusStore](ApplianceStatusStore/README.md) : Application that represent stores Appliance Connectivity status in the database.

[Appliance](Appliance/README.md) : Application that represent an Appliance which sends ping requests to the [ApplianceStatusStore](ApplianceStatusStore/README.md) ping REST EndPoint

# Deployment Architecture

![Alt text](images/Deployment-Architecture.png?raw=true "Deployment Architecture")

To Support high transactions from high number of Appliances, a load balancer can be deployed to distribute the traffic between multiple [ApplianceStatusStore](ApplianceStatusStore/README.md) applications. 

## Improvements

![Alt text](images/Deployment-Architecture-Improvement.png?raw=true "Deployment Architecture")

Deploying a Key-value store like Etcd or Redis can improve db opertaion. Especially Redis could perform better since it is an In memory Database. All the ping requests from the [Appliance](Appliance/README.md) can be stored in the Key-Value store and then the appliance status can be updated with a background job from the [ApplianceStatusStore](ApplianceStatusStore/README.md)


## Alternate Solution

![Alt text](images/Deployment-Architecture-PublishSubscribe.png?raw=true "Deployment Architecture")

An alternate solution could be to design the system using Publish Subscribe pattern where [Appliance](Appliance/README.md) act as a publisher and publishes ping request to a specific topic in the Broker and then the Subscriber applications can read the request and update [Appliance](Appliance/README.md) status. In this case [ApplianceStatusStore](ApplianceStatusStore/README.md) will only need one REST Endpoint just to server the [Appliance](Appliance/README.md) details to whoever request for them. 
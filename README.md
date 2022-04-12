# system-design-demo


[ApplianceStatusStore](ApplianceStatusStore/README.md) : Application that represent stores Appliance Connectivity status in the database.

[Appliance](Appliance/README.md) : Application that represent an Appliance which sends ping requests to the [ApplianceStatusStore](ApplianceStatusStore/README.md) ping REST EndPoint

# Deployment Architecture

![Alt text](images/Deployment-Architecture.png?raw=true "Deployment Architecture")

To Support high transactions from high number of Appliances, a load balancer can be deployed to distribute the traffic between multiple [ApplianceStatusStore](ApplianceStatusStore/README.md) applications. 

## Improvements

![Alt text](images/Deployment-Architecture-Improvement.png?raw=true "Deployment Architecture")

Deploying a Key-value store like Etcd or Redis can improve db opertaion. Especially Redis could perform better since it is an In memory Database. All the ping requests from the [Appliance](Appliance/README.md) can be stored in the Key-Value store and then the appliance status can be updated with a background job from the [ApplianceStatusStore](ApplianceStatusStore/README.md)
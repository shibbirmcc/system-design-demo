# System Design Demo
![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)

This project aims to architect and implement a demo scenario of a System Design requirements.
- [System Requirements](#system-requirements)
- [Sample Data](#sample-data)
- [Solution Design And Discussion](#solution-design-and-discussion)

# System Requirements

### Scenario
Imagine you are one of our employees and you got assigned to develop a feature.
- The feature has a number of connected appliances that belongs to a number of customers.
- We have a need to be able to view the status of the connection among these appliances on a monitoring display
- The appliances send the status of the connection one time per minute
- The status can be compared with a ping (network trace). For example; no request from the appliance means no connection
- There is NO need to ping anything, it is just to understand the concept whether the Appliance is either Connected or Disconnected

### Task
- Your task will be to create a data store that keeps these appliances with their status and the respected customer
- An API that returns list of Appliaces with connected status and customer details
- Obviously, for this task, there are no real appliances available that can respond to your "ping" request. This can either be solved by using static values or ​by creating a separate machinery that returns random fake status
- Appliance could be separate service (Micro service)
- Can support high transactions (High number of appliances)

# Sample Data

| Appliance Id | Factory Nr | Customer | Address
| ------ | ------ | ------ | ------ 
| YS2R4X20005399401 | ABC123 <td rowspan="3">Kalles Grustransporter AB</td> <td rowspan="3">Cementvägen 8, 111 11 Södertälje</td>
| VLUR4X20009093588 | DEF456
| VLUR4X20009048066 | GHI789
| YS2R4X20005388011 | JKL012 <td rowspan="2">Johans Bulk AB</td> <td rowspan="2">Bulkvägen 12, 222 22 Stockholm</td>
| YS2R4X20005387949 | MN0345
| YS2R4X20009048066 | PQR678 <td rowspan="2">Haralds Värdetransporter AB</td> <td rowspan="2">Budgetvägen 1, 333 33 Uppsala</td>
| VLUR4X20005387055 | STU901

# Solution Design And Discussion

[ApplianceStatusStore](ApplianceStatusStore/README.md) : Application that  stores Appliance Connectivity status in the database.

[Appliance](Appliance/README.md) : Application that represents an Appliance which sends ping requests to the [ApplianceStatusStore](ApplianceStatusStore/README.md) ping REST EndPoint

## Deployment Architecture

![Alt text](images/Deployment-Architecture.png?raw=true "Deployment Architecture")

To Support high transactions from high number of Appliances, a load balancer can be deployed to distribute the traffic between multiple [ApplianceStatusStore](ApplianceStatusStore/README.md) applications. 

## Improvements

![Alt text](images/Deployment-Architecture-Improvement.png?raw=true "Deployment Architecture")

Deploying a Key-value store like Etcd or Redis can improve db operation. Especially Redis could perform better since it is an In memory Database. All the ping requests from the [Appliance](Appliance/README.md) can be stored in the Key-Value store and then the appliance status can be updated with a background job from the [ApplianceStatusStore](ApplianceStatusStore/README.md)


## Alternate Solution

![Alt text](images/Deployment-Architecture-PublishSubscribe.png?raw=true "Deployment Architecture")

An alternate solution could be to design the system using Publish Subscribe pattern where [Appliance](Appliance/README.md) act as a publisher and publishes ping request to a specific topic in the Broker and then the Subscriber applications can read the request and update [Appliance](Appliance/README.md) status. In this case [ApplianceStatusStore](ApplianceStatusStore/README.md) will only need one REST Endpoint just to server the [Appliance](Appliance/README.md) details to whoever request for them. 
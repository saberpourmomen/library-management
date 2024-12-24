# **Library Management**

## Overview:

####    APIs:
    * register books (book with same ISBN should have got same title and author)
    * search books
    * list all books
    * find book by ISBN
    * register borrower
    * borrow a book
    * return a book
**Note:** you can see the postman collection and samples [here](src/main/resources/Library Management Collection.postman_collection.json)

### **requirement:**
    1. JDK 23
    2. Docker desktop
    3. minikube
    4. postman
### Build and Run on Local:

#### Build:

`mvn clean install`

#### run:

`java jar library-management-1.0.0.jar`

### Deploy:
1. create image:

    `docker build -t library-management:1.0.0 .`

2. apply kubernetes files:

   1. `kubectl apply -f k8s/postgresql-config.yml`
   2. `kubectl apply -f k8s/postgresql-secret.yml`
   3. `kubectl apply -f k8s/postgresql.yml `
   4. `kubectl apply -f k8s/libraryManagement.yml`
   5. `kubectl get all` to ensure all dene successfully
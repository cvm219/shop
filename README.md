# Internet aggregator
Service provide REST interface for CRUD operation with entities, also provide method for getting list of products with prices in category


## Getting Started

Create PostgreSQL database with following tables: 
* **categories**
	* ***id*** (uuid, not null, primary key)
	* ***name*** (character varying 255, not null)
	* ***created_at*** (timestamp without timezone, not null)
	* ***updated_at*** (timestamp without timezone, not null)
* **retailers**
	* ***id*** (uuid, not null, primary key)
	* ***name*** (character varying 255, not null)
	* ***created_at*** (timestamp without timezone, not null)
	* ***updated_at*** (timestamp without timezone, not null)
* **products**
	* ***id*** (uuid, not null, primary key)
	* ***name*** (character varying 255, not null)
	* ***specification*** (text)
	* ***status*** (character varying 31, not null)
	* ***created_at*** (timestamp without timezone, not null)
	* ***updated_at*** (timestamp without timezone, not null)
* **offers**
	* ***id*** (uuid, not null, primary key)
	* ***retailer_id*** (uuid, not null, foreign key to retailers.id)
	* ***product_id*** (uuid, not null, foreign key to products.id)
	* ***price*** (double precision, not null)
	* ***created_at*** (timestamp without timezone, not null)
	* ***updated_at*** (timestamp without timezone, not null)
* **category_products**
	* ***id*** (uuid, not null, primary key)
	* ***category_id*** (uuid, not null, foreign key to categories.id)
	* ***product_id*** (uuid, not null, foreign key to products.id)


Download project. Go to folder and tune properties (server.port, database options) for your machine in application.yml file. Compile it with (tests will be run automatically):
```
mvn clean install
```
Then run it with following command from root folder (where application.yml is):
```
java -jar PATH_TO_JAR
```

### Requisites

In order to run application you must have following:
* Java 8
* PostgreSQL
* Maven

## REST API

### CATEGORY CRUD

**1. GET LIST OF ALL CATEGORIES**

**GET:** /category

**RESPONSE (JSON ARRAY):**
```JSON
[
	{
		"id": uuid,
		"name": string
	},
	...
]
```

**2. GET ONE CATEGORY**

**GET:** /category/{id}

***{id}*** - id of category

**RESPONSE (JSON):**

```JSON
{
	"id": uuid,
	"name": name
}
```
Return status code 404 if category with id does not exists


**3. CREATE CATEGORY**

**POST:** /category

**POST DATA (JSON):**

```JSON
{
	"name": string
}
```
**RESPONSE:** "Saved" (without qoutes) with status code 200

*Return status code 400 if category with same name already exists

**4. UPDATE CATEGORY**

**PUT:** /category/{id}

***{id}*** - id of category to change

**PUT DATA (JSON):**
```JSON
 {
	 "name": string
 }
```
**RESPONSE:** "Updated" (without qoutes) with status code 200

*Return status code 400 if category with same name already exists

*Return status code 404 if category with id does not exists

**5. DELETE CATEGORY**

**DELETE:** /category/{id}

***{id}*** - id of category to delete

**RESPONSE:** "Updated" (without qoutes) with status code 200

*Return status code 400 if category with same name already exists

*Return status code 404 if category with id does not exists

### RETAILER CRUD

Same functions as for categories.

**PATH:** /retailer

**MODEL:**

```JSON
{
	"id": uuid,
	"name": string
}
```

### OFFER CRUD

Same function as for categories

**PATH:** /offer

**MODEL:**

```JSON
{
	"id": uuid,
	"retailerId": uuid, // (for post requests)
	"retailer": {
		"id": uuid,
		"name": string
	}, // (for get requests)
	"productId": uuid,
	"price": double precision
}
```

### PRODUCT CRUD

Same functions as for categories

**PATH:** /product

**MODEL:**

```JSON
{
	"id": uuid,
	"name": string,
	"specification": string, // not required field
	"status": ("enabled"|"disabled"),
	"offers": [
		offer's objects
	] // only on get requests
}
```

### ADDITIONAL FUNCTIONS

**1. GET LIST OF AVAILABLE PRODUCTS WITH PRICE**

**GET:** /product/withPrice?categoryId={categoryId}

{categoryId} - id of category for search products (not required)

**RESPONSE (JSON ARRAY):**
```JSON
[
	{
		"id": uuid,
		"name": string,
		"specification": string,
		"status": string,
		"price": double precision
	}
]
```

# Generic CRUD

## What is it?

This is a proof-of-concept for a simple mechanism that allows a developer to easily create a collection of REST resources
in a simple CRUD API, by just implementing the "business" logic (validation + persistence).

It is built using Micronaut as a way to learn the framework and experiment with the Introspection capabilities.

## How does it work?

The main API Controller, GenericCrudController, exposes the 4 basic CRUD operations in a REST-over-HTTP API:

- Create (POST)
- Read (GET)
- Update (PUT)
- Destroy (DELETE)

Each request is made to the assigned collection's endpoint as ```<VERB> /<collection>{/<id>}```
Note: the ```<id>``` is only for the GET, PUT and DELETE methods

The API layer will then find the Manager for the specified collection and delegate the operation.

This is done by querying a Map that contains, for each collection, the implementation of CrudManager responsible for managing that collection

This Map is generated by using Micronaut's Introspection capabilities to attach annotation metadata to the bytecode at compilation time and then
retrieving that data (in this case, the name of the collection) at run-time

We then deserialize the request body, once again using Micronaut's Introspection to figure the proper Object Type.

## How do I add a new collection?

To add a new collection simply:
- Create a new class that extends ```CrudManager<T>```
- Annotate it with ```@Singleton```, ```@Instrospected``` and ```@Collection("<collection-name>")```
- Implement the ```post```, ```get```, ```put``` and ```delete``` methods

And you are done! The ```GenericCrudController``` and Micronaut's dependency injection should take care of the rest.

You may now call ```POST /<collection-name>/``` with a JSON representation of the resource you want to create
and the controller should delegate the creation to your CrudManager

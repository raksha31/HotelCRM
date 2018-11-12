# HotelCRM
About The Project :

It is a small API that provides an interface for hotels to enter the numbers of
Premium and Economy rooms that are available for the night and then tells them
immediately how many rooms of each category will be occupied and how much money they
will make in total.

---------------------------------------
Framework And Technology :
- Spring Boot Framework
- REST
- IDE : Ecllipse Luna
- Java 8
---------------------------------------
Installation Instructions :

- To run the project directly from Command Prompt go to the target folder and execute --> java -jar SmartHostApp-0.0.1-SNAPSHOT.jar
  OR
- Import the project in Ecllipse and run Application class as its a Spring Boot Project

----------------------------------------
Operating Instructions :

- To execute the smarthost service :
- URL : http://localhost:8080/smarthost/roomusage
  Request Type : POST
  Reques Body :
  {
	"availablePremiumRooms" : 3,
	"availableEconomyRooms" :3
}

Note : There are all the test cases covered in test class.

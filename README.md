<h1 align="center">
  <img src="static/logo.jpg" alt="Medical clinic" width="260px"></a>
  <br>
</h1>

<h4 align="center">A framework for performing automated penetration tests based on a prepared scenario script.</h4>

<p align="center">
  <a href="#usage">Usage</a> •
  <a href="#features">Features</a> •
  <a href="#setup">Setup</a> •
  <a href="#dockerfile">Dockerfile</a> •
  <a href="#running">Running</a> •
  <a href="#todo">Todo</a> •
  <a href="#license">License</a> •
</p>

---


## Medical clinic management
Desktop application written in JavaFx on top of the Spring Boot.</br>
The application consists of four interdependent modules. Each of them is to speed up communication between a doctor, receptionist and nurse. 
Thanks to the designed functionalities in the system, which automate and simplify the work of the above-mentioned positions.

## Functionalities

### Doctor module
The doctor has direct access to the schedule of meetings with patients along with their records. 
The module for issuing and viewing previously created prescriptions results in full automation of the basic duties of a doctor. 
After a prior appointment by the receptionist, the doctor has the option of planning the injections, which will ultimately be performed by the nurse.

### Nurse module
As mentioned above, the nurse's main task is to perform scheduled injections. 
A logged in user, performing his/her tasks is able to edit the status of both pending and already performed injections at any time. 
The nurse, compared to other modules, does not have access to information that may be generally considered confidential.

### Receptionist module
The receptionist module can be considered the core of the entire software. It is responsible for supervising and scheduling subsequent appointments with physicians. 
What's more, a newly registered patient along with the required data is added from this level. This results in direct communication with each of the modules.

### Admin module
The administrator module is part of the application designed to administer system users.
The user logged in as administrator has full access to the functionality of all modules. 
Moreover, in the admin panel it is possible to manage resources in the system.

## Technologies

- Java 11
- JavaFx
- Spring Boot
- Spring Data JPA/Hibernate
- MySQL

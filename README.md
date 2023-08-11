# Sistemas Distribuídos
Aluno: Lucas Martins Cardozo.          RA: 1996770.

Universidade Tecnológica Federal do Paraná


## Project Description
This project is an adaptation in the [Sinalgo](https://github.com/Sinalgo/) software, which included a new project, called "wsn1" that intends to use sinalgo resources to create a project with sensor nodes that have both Sink and Sensor functions.

The objective is to allow a particular sensor node to be responsible for receiving the packets sent by the other nodes (it will have the function of a sink). The sensor nodes established as sinks can be removed, added or changed in the scenario. The other sensor nodes, when they receive a route packet, must infinitely send data packets to the sensor nodes established as sink.


## How to Install and Run the Project
To run this project, it is necessary to run the code in an IDE, for the development the Eclipse IDE platform was used


## How to test this project
After running the code in the IDE, you need to choose the ws1 project, it is already configured by default to start with 2 dimensions and 1000x1000. After starting the ws1 project, it is necessary to generate 100 nodes with the Grid2D distribution, selecting the Node Implementation ws1:Sensor.

Then it is necessary to choose some nodes to have the sink function, then it is necessary to right-click on some node and click on "Construir Arvore de Roteamento" ( to set the node as sink), it will be possible to notice that the node will change colour.

Then it is necessary to run the code with a specific number of rounds or Run Forever.

## How to find the project implementation

To find the implementation of this project, you need to navigate to the project folder, which can be found [here](https://github.com/lucaocardozo/sistemas-distribuidos-sinalgo/tree/master/Sinalgo_Exame_1996770/src/projects/wsn1)

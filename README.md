![gruppo lineCode](https://imagizer.imageshack.com/img923/557/86bUrf.png)

# Server - PORTACS
Componente Server per l'applicativo PORTACS sviluppato come attività progettuale per il corso di Ingegneria del Software dell'Università degli Studi di Padova sotto il nominativo di Progetto _PORTACS_.

## Overview
L'applicativo si propone come motore di calcolo con lo scopo di coordinare unità robotiche a guida autonoma in un contesto di ristorazione, ed é pensato per funzionare in collegamento alle altre componenti sviluppate nello stesso contesto.

## Installazione, dipendenze ed esecuzione
Dipendenze:
 - Java 13
 - Maven
 
 Clonare repo con:
 ```shell
 git clone https://github.com/lineCode-swe/server.git
 ```
 
 Compilazione dell'applicativo con:
  ```shell
 mvn clean package
 ```
 
 Creazione della Docker image:
  ```shell
docker build -t portacs-server
 ```

Avvio della Docker image:
 ```shell
 docker run --interactive --tty --network portacs-net
 ```

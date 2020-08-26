# kafka-testing-java
Projeto para realizar testes em uma aplicação que se comunica com o Kafka. Podem ser validados o Consumer e/ou o Producer 

## Pré condição
A principal pré condição é ter um kafka com dois tópicos, no caso tdc-entrada e tdc-saida. De preferencia localhost para que seja mais fácil de entender como funciona.

Outra atividade necessária é subir a aplicação a ser testada. Para isso você necessitará ter o python 3 instalado na sua máquina, e também executar o projeto abaixo:
https://github.com/robsonagapito/kafka-app-python

## Executar testes
Para executar os testes basta utilizar o: mvn verify
O relatório dos testes estarão disponíveis após a execução no diretório: 
target/reports/cucumber-html-reports



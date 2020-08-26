Feature: Check if the application handles kafka message

  Scenario: Application handles a correct message
    Given kafka server uses the topic "tdc-entrada"
    And the message is "robsonagapito;Robson Agapito Correa;(11)98661-3181"
    And the message is produced
    And the application works for "1" seconds
    When kafka server uses the topic "tdc-saida"
    And the message is consumed
    Then the message has a field "login" with value "robsonagapito"
    And the message has a field "name" with value "Robson Agapito Correa"
    And the message has a field "phone" with value "(11)98661-3181"

  Scenario: Application handles a wrong message
    Given kafka server uses the topic "tdc-entrada"
    And the message is "robsonagapito;Only two fields"
    And the message is produced
    And the application works for "1" seconds
    When kafka server uses the topic "tdc-saida"
    And the message is consumed
    Then the message error has a field "message" with value "Fields qtty is lower than 3!"

  Scenario: Check contract created
    Given kafka server uses the topic "tdc-entrada"
    And the message is "robsonagapito;Check Schema;(11)98661-3181"
    And the message is produced
    And the application works for "1" seconds
    When kafka server uses the topic "tdc-saida"
    And the message is consumed
    Then the contract is ok
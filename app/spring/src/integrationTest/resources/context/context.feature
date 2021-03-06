Feature: Context Creation

  Background:
    * url baseUrl + "/v1/contexts"

  Scenario: Create Context - Error - Correlation id not provided
    Given request
      """
      {
        "channel": {
          "id": "missing-header-channel",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111111"
          }
        ]
      }
      """
    When method POST
    Then status 400
    And match response ==
      """
      {
        "status": 400,
        "title": "Bad request",
        "message": "mandatory header correlation-id not provided, mandatory header channel-id not provided"
      }
      """

  Scenario: Create Context - Error - Channel id not provided
    Given header correlation-id = "387178fe-d93b-4114-b96f-dd77325910d0"
    And request
      """
      {
        "channel": {
          "id": "missing-header-channel",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111111"
          }
        ]
      }
      """
    When method POST
    Then status 400
    And match response ==
      """
      {
        "status": 400,
        "title": "Bad request",
        "message": "mandatory header channel-id not provided"
      }
      """

  Scenario: Create Context - Error - Context policy not configured
    * def channelId = "context-test-channel1"
    Given header correlation-id = "40c6bf4d-0519-4f11-8e4f-5618e28884ee"
    And header channel-id = channelId
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111111"
          }
        ]
      }
      """
    When method POST
    Then status 422
    And match response ==
      """
      {
        "status": 422,
        "title": "Context policy not configured",
        "message": "Context policy not configured for channel, activity and alias combination",
        "meta": {
          "aliasTypes": [
            "credit-card-number"
          ],
          "activityName": "default-activity",
          "channelId": "#(channelId)"
        }
      }
      """

  Scenario: Create context - Error - Identity not found
    * def channelId = "context-test-channel2"
    * def policyId = "18044838-364b-484c-85ab-033e27a4d002"
    Given header correlation-id = "38725e3b-3ddf-4f7f-96d1-4f49a0f01478"
    And request
      """
      {
        "key": {
          "id": "#(policyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "sequencePolicies": [],
        "protectSensitiveData": false
      }
      """
    And url baseUrl + "/v1/context-policies"
    And header correlation-id = "ab4ebd5c-870f-406a-ba25-0ab9403fa3b2"
    And method POST
    And status 201
    And header correlation-id = "c27b0f0a-1e83-4bb9-a1a8-50b95d4d1138"
    And header channel-id = channelId
    And url baseUrl + "/v1/contexts"
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111112"
          }
        ]
      }
      """
    When method POST
    Then status 404
    And match response ==
      """
      {
        "status": 404,
        "title": "Identity not found",
        "message": "credit-card-number|4927111111111112"
      }
      """

  Scenario: Create context - Error - Lockout policy not configured
    * def policyId = "c05920f8-5ebe-469e-a0a3-c7e30cc8b7f1"
    * def channelId = "context-test-channel3"
    Given header correlation-id = "bfa3f8c2-1fc3-468a-a2b6-97f6abad28cb"
    And request
      """
      {
        "key": {
          "id": "#(policyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "sequencePolicies": [],
        "protectSensitiveData": false
      }
      """
    And url baseUrl + "/v1/context-policies"
    And method POST
    And status 201
    And header correlation-id = "c5ebcc3e-7189-419b-a401-9a5563ccdae6"
    And request
      """
      {
        "country": "GB",
        "aliases": [
          { "type": "credit-card-number", "value": "4927111111111113" }
        ]
      }
      """
    And url baseUrl + "/v1/identities"
    And method POST
    And status 201
    And header correlation-id = "3048b1ce-0b1d-4fae-99c2-6fe0267f3136"
    And header channel-id = channelId
    And url baseUrl + "/v1/contexts"
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111113"
          }
        ]
      }
      """
    When method POST
    Then status 422
    And match response ==
      """
      {
        "status": 422,
        "title": "Lockout policy not configured",
        "message": "Lockout policy not configured for channel, activity and alias combination",
        "meta": {
          "activityName":"default-activity",
          "aliasTypes":["credit-card-number"],
          "channelId":"#(channelId)"
        }
      }
      """

  Scenario: Create context - Success - Otp method returned
    * def contextPolicyId = "be2c6c3b-5347-4337-9194-cc5c803112a6"
    * def channelId = "context-test-channel4"
    Given url baseUrl + "/v1/context-policies"
    And header correlation-id = "1ca5443f-7172-4742-b2dc-b788e08bead4"
    And request
      """
      {
        "key": {
          "id": "#(contextPolicyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "sequencePolicies": [
          {
            "name": "one-time-passcode",
            "stagePolicies": [
              {
                "type": "all-methods",
                "methodPolicies": [
                  {
                    "name": "one-time-passcode",
                    "config": {
                      "maxNumberOfAttempts": 3,
                      "duration": 300000,
                      "passcodeConfig": {
                        "length": 8,
                        "duration": 120000,
                        "maxNumberOfDeliveries": 2
                      }
                    },
                    "deliveryMethodConfigs": [
                      {
                        "type": "sms",
                        "phoneNumberConfig": {
                          "country": "GB",
                          "allowInternational": false,
                          "lastUpdatedConfig": {
                            "allowUnknown": true,
                            "minDaysSinceUpdate": 5
                          },
                          "simSwapConfig": {
                            "acceptableStatuses": [
                              "success"
                            ],
                            "timeout": 2000,
                            "minDaysSinceSwap": 6,
                            "async": false
                          }
                        }
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ],
        "protectSensitiveData": false
      }
      """
    And method POST
    And status 201
    And header correlation-id = "4a326d3b-11fe-404f-8f16-2e2ded42657e"
    And url baseUrl + "/v1/identities"
    And request
      """
      {
        "country": "GB",
        "aliases": [
          { "type": "credit-card-number", "value": "4927111111111114" }
        ],
        "phoneNumbers": [
          { "value": "+4407808247749" },
          { "value": "+4407808247748" },
          { "value": "+4407808247747" },
          { "value": "+4407808247746" },
          { "value": "+4407808247745" },
          { "value": "+4407808247744" },
          { "value": "+4407808247743" }
        ]
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/lockout-policies"
    * def lockoutPolicyId = "9ddbd67e-6310-42aa-abf4-007eb09e8398"
    And header correlation-id = "20b11125-571d-48d5-a80b-83d6a236a608"
    And request
      """
      {
        "key": {
          "id": "#(lockoutPolicyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "stateCalculator": {
          "maxNumberOfAttempts": 5,
          "includeAttemptsPolicy": {
            "type": "all-attempts"
          },
          "type": "hard-lockout"
        },
        "recordAttemptPolicy": {
          "type": "always-record"
        }
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/contexts"
    And header correlation-id = "ee4dad1b-4ed8-48a9-b628-d29f2a7d2eef"
    And header channel-id = channelId
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111114"
          }
        ]
      }
      """
    When method POST
    Then status 201
    And match response ==
      """
      {
        "id": "#uuid",
        "created": "#notnull",
        "expiry": "#notnull",
        "eligible": true,
        "complete": false,
        "successful": false,
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111114"
          }
        ],
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "sequences": [
          {
            "name": "one-time-passcode",
            "stages": [
              {
                "type": "all-methods",
                "methods": [
                  {
                    "name": "one-time-passcode",
                    "deliveryMethods": [
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247743",
                        "eligibility": {
                          "eligible": true,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247744",
                        "eligibility": {
                          "reason": "sim swap status unknown not acceptable",
                          "eligible": false,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247745",
                        "eligibility": {
                          "reason": "sim swap status timeout not acceptable",
                          "eligible": false,
                          "complete": false
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247746",
                        "eligibility": {
                          "reason": "#notnull",
                          "eligible": false,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247747",
                        "eligibility": {
                          "reason": "sim swap status timeout is not allowed",
                          "eligible": false,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247748",
                        "eligibility": {
                          "reason": "sim swap status unknown is not allowed",
                          "eligible": false,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "+447808247749",
                        "eligibility": {
                          "reason": "sim swap status failure is not allowed",
                          "eligible": false,
                          "complete": true
                        }
                      }
                    ],
                    "config": {
                      "maxNumberOfAttempts": 3,
                      "duration": 300000,
                      "passcodeConfig": {
                        "length": 8,
                        "duration": 120000,
                        "maxNumberOfDeliveries": 2
                      }
                    },
                    "eligibility": {
                      "eligible": true
                    }
                  }
                ]
              }
            ],
            "duration": 300000,
            "eligibility": {
              "eligible": true
            }
          }
        ]
      }
      """

  Scenario: Create context - Success - Otp method returned - Masked Sensitive Data
    * def contextPolicyId = "8d6322ff-b8c8-4e2a-95b4-207fe5939f65"
    * def channelId = "context-test-channel5"
    Given url baseUrl + "/v1/context-policies"
    And header correlation-id = "e8533e1c-99d8-48e9-a0ed-e2491e14f380"
    And request
      """
      {
        "key": {
          "id": "#(contextPolicyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "sequencePolicies": [
          {
            "name": "one-time-passcode",
            "stagePolicies": [
              {
                "type": "all-methods",
                "methodPolicies": [
                  {
                    "name": "one-time-passcode",
                    "config": {
                      "maxNumberOfAttempts": 3,
                      "duration": 300000,
                      "passcodeConfig": {
                        "length": 8,
                        "duration": 120000,
                        "maxNumberOfDeliveries": 2
                      }
                    },
                    "deliveryMethodConfigs": [
                      {
                        "type": "sms",
                        "phoneNumberConfig": {
                          "country": "GB",
                          "allowInternational": false,
                          "lastUpdatedConfig": {
                            "allowUnknown": true,
                            "minDaysSinceUpdate": 5
                          },
                          "simSwapConfig": {
                            "acceptableStatuses": [
                              "success"
                            ],
                            "timeout": 2000,
                            "minDaysSinceSwap": 6,
                            "async": false
                          }
                        }
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ],
        "protectSensitiveData": true
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/identities"
    And header correlation-id = "c7abb8aa-a08f-454a-aa58-0f15613fe45c"
    And request
      """
      {
        "country": "GB",
        "aliases": [
          { "type": "credit-card-number", "value": "4927111111111115" }
        ],
        "phoneNumbers": [
          { "value": "+4407808247744" },
          { "value": "+4407808247743" }
        ]
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/lockout-policies"
    * def lockoutPolicyId = "2f96109d-2a8c-4f91-a18e-ca7b3863b4bb"
    And header correlation-id = "a764a038-eb1d-448c-b864-217524f325b9"
    And request
      """
      {
        "key": {
          "id": "#(lockoutPolicyId)",
          "priority": 1,
          "channelId": "#(channelId)",
          "type": "channel"
        },
        "stateCalculator": {
          "maxNumberOfAttempts": 5,
          "includeAttemptsPolicy": {
            "type": "all-attempts"
          },
          "type": "hard-lockout"
        },
        "recordAttemptPolicy": {
          "type": "always-record"
        }
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/contexts"
    And header channel-id = channelId
    And header correlation-id = "d2d8f6d9-c7af-4c42-b1f1-a72d1923144b"
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111115"
          }
        ]
      }
      """
    When method POST
    Then status 201
    And match response ==
      """
      {
        "id": "#uuid",
        "created": "#notnull",
        "expiry": "#notnull",
        "eligible": true,
        "complete": false,
        "successful": false,
        "channel": {
          "id": "#(channelId)",
          "country": "GB"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111115"
          }
        ],
        "activity": {
          "name": "default-activity",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "sequences": [
          {
            "name": "one-time-passcode",
            "stages": [
              {
                "type": "all-methods",
                "methods": [
                  {
                    "name": "one-time-passcode",
                    "deliveryMethods": [
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "**********743",
                        "eligibility": {
                          "eligible": true,
                          "complete": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "sms",
                        "value": "**********744",
                        "eligibility": {
                          "reason": "sim swap status unknown not acceptable",
                          "eligible": false,
                          "complete": true
                        }
                      }
                    ],
                    "config": {
                      "maxNumberOfAttempts": 3,
                      "duration": 300000,
                      "passcodeConfig": {
                        "length": 8,
                        "duration": 120000,
                        "maxNumberOfDeliveries": 2
                      }
                    },
                    "eligibility": {
                      "eligible": true
                    }
                  }
                ]
              }
            ],
            "duration": 300000,
            "eligibility": {
              "eligible": true
            }
          }
        ]
      }
      """

  Scenario: Create and get context - Success - context returned
    * def channelId = "abc"
    Given url baseUrl + "/v1/identities"
    And header correlation-id = "973787cf-a5ae-4d1f-8c08-e577599276d8"
    And request
      """
      {
        "country": "GB",
        "aliases": [
          { "type": "credit-card-number", "value": "4927111111111118" }
        ],
        "phoneNumbers": [
          { "value": "+4407808247743" }
        ]
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/contexts"
    And header channel-id = channelId
    And header correlation-id = "2924560f-830a-4205-a4b7-c736cd4c7ab0"
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB",
          "validCookie": true
        },
        "activity": {
          "name": "login",
          "system": "example-system",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111118"
          }
        ]
      }
      """
    And method POST
    And status 201
    * def contextId = response.id
    And url baseUrl + "/v1/contexts/" + contextId
    And header channel-id = channelId
    And header correlation-id = "d8684916-b21e-45db-ab79-7806b41cbe40"
    When method GET
    And status 200
    And match response ==
      """
      {
        "id": "#uuid",
        "created": "#notnull",
        "expiry": "#notnull",
        "eligible": true,
        "complete": false,
        "successful": false,
        "channel": {
          "id": "#(channelId)",
          "country": "GB",
          "validCookie": true
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111118"
          }
        ],
        "activity": {
          "name": "login",
          "system": "example-system",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "sequences": [
          {
            "duration":300000,
            "name":"one-time-passcode",
            "stages": [
              {
                "methods": [
                  {
                    "name":"one-time-passcode",
                    "eligibility": {
                      "eligible":true
                    },
                    "deliveryMethods": [
                      {
                        "id": "#uuid",
                        "type": "email",
                        "value": "joe.bloggs@hotmail.co.uk",
                        "eligibility": {
                          "eligible": true
                        }
                      },
                      {
                        "id": "#uuid",
                        "type": "email",
                        "value": "joebloggs@yahoo.co.uk",
                        "eligibility": {
                          "eligible":true
                        }
                      }
                    ],
                    "config": {
                      "duration": 300000,
                      "maxNumberOfAttempts": 3,
                      "passcodeConfig": {
                        "duration": 120000,
                        "length": 8,
                        "maxNumberOfDeliveries": 2
                      }
                    }
                  }
                ],
                "type":"all-methods"
              }
            ],
            "eligibility": {
              "eligible": true
            }
          }
        ]
      }
      """

  Scenario: Get context - Error - context not found
    * def contextId = "bacf1ac9-d33e-4e2f-9fff-a01bfab45bbd"
    And header channel-id = "abc"
    And header correlation-id = "1be07251-0ed9-469a-a929-96b454168fc9"
    Given url baseUrl + "/v1/contexts/" + contextId
    When method GET
    Then status 404
    And match response ==
      """
      {
        "status": 404,
        "title": "Context not found",
        "message": "#(contextId)"
      }
      """

  @sequential
  Scenario: Create and get context - Error - context expired
    Given url baseUrl + "/v1/identities"
    And header correlation-id = "a7f15983-d76a-4a9b-a09c-772d405f2d4d"
    And request
      """
      {
        "country": "GB",
        "aliases": [
          { "type": "credit-card-number", "value": "4927111111111119" }
        ],
        "phoneNumbers": [
          { "value": "+4407808247746" }
        ]
      }
      """
    And method POST
    And status 201
    And url baseUrl + "/v1/contexts"
    * def channelId = "abc"
    And header correlation-id = "53f4b96c-3bce-4390-9f2a-889a3c9e7f54"
    And header channel-id = channelId
    And request
      """
      {
        "channel": {
          "id": "#(channelId)",
          "country": "GB",
          "validCookie": true
        },
        "activity": {
          "name": "login",
          "system": "example-system",
          "timestamp": "2020-09-27T06:56:47.522Z"
        },
        "aliases": [
          {
            "type": "credit-card-number",
            "value": "4927111111111119"
          }
        ]
      }
      """
    And method POST
    And status 201
    * def contextId = response.id
    And url baseUrl + "/time-offset"
    And request
      """
      { "offset": 360000 }
      """
    And method PUT
    And status 200
    And header correlation-id = "53f4b96c-3bce-4390-9f2a-889a3c9e7f54"
    And header channel-id = channelId
    And url baseUrl + "/v1/contexts/" + contextId
    And method GET
    And status 410
    And match response ==
      """
      {
        "title": "Context expired",
        "message": "#notnull",
        "status": 410,
        "meta": {
          "id":"#uuid",
          "expiry":"#notnull"
        }
      }
      """
    And url baseUrl + "/time-offset"
    And method DELETE
    And status 200
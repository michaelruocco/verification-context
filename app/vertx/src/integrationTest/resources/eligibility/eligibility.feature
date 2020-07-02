Feature: Identity Maintenance

  Background:
    * url baseUrl + '/eligibility'

  Scenario: Create eligibility - Error - create eligibility not configured
    Given request
      """
      {
        channel: {
          id: 'default-channel',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111111'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ]
      }
      """
    When method POST
    Then status 500
    And match response ==
      """
      {
        status: 500,
        title: 'Internal server error',
        message: 'default-channel'
      }
      """

  Scenario: Create eligibility - Error - identity not found for internal data lookup
    Given request
      """
      {
        'channel': {
          id: 'gb-rsa',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111111'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ]
      }
      """
    When method POST
    Then status 404
    And match response ==
      """
      {
        status: 404,
        title: 'Identity not found',
        message: 'credit-card-number|4928111111111111'
      }
      """

  Scenario: Create eligibility - Success - identity created for internal data lookup
    Given url baseUrl + '/identities'
    And request
      """
      {
        country: 'GB',
        aliases: [
          { type: 'credit-card-number', value: '4928111111111111' }
        ]
      }
      """
    And method POST
    And status 200
    And url baseUrl + '/eligibility'
    And request
      """
      {
        channel: {
          id: 'gb-rsa',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111111'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ]
      }
      """
    When method POST
    Then status 201
    And match response ==
      """
      {
        channel: {
          id: 'gb-rsa',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111111'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ],
        identity: {
          idvId: '#uuid',
          country: 'GB',
          aliases: [
            {
              type: 'credit-card-number',
              value: '4928111111111111'
            },
            {
                type: 'idv-id',
                value: '#uuid'
            }
          ]
        }
      }
      """

  Scenario: Create eligibility - Success - Create with external data lookup
    Given request
      """
      {
        channel: {
          id: 'as3',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111112'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ]
      }
      """
    When method POST
    Then status 201
    And match response ==
      """
      {
        channel: {
          id: 'as3',
          country: 'GB'
        },
        aliases: [
          {
            type: 'credit-card-number',
            value: '4928111111111112'
          }
        ],
        requested: [
          'phone-numbers',
          'email-addresses'
        ],
        identity: {
          idvId: '#uuid',
          country: 'GB',
          aliases: [
            {
              type: 'credit-card-number',
              value: '4928111111111112'
            },
            {
              type: 'debit-card-number',
              value: '5928111111111112'
            }
            {
              type: 'idv-id',
              value: '#uuid'
            }
          ],
          emailAddresses: [
            'joe.bloggs@hotmail.co.uk',
            'joebloggs@yahoo.co.uk'
          ]
        }
      }
      """
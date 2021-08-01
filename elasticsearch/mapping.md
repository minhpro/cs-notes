## Create log index

curl -X PUT "localhost:9200/api-log?pretty" -H 'Content-Type: application/json' -d'
{
    "settings" : {
        "index.mapper.dynamic":false
    },
    "mappings" : {
        "log" : {
            "_source": {
                "enabled": false
            },
            "dynamic": false,
            "properties" : {
                "latencies" : {
                    "properties" : {
                        "kong" : {
                            "type" : "long"
                        },
                        "proxy" : {
                            "type" : "long"
                        },
                        "request" : {
                            "type" : "long"
                        }
                    }
                },
                "started_at" : {
                    "type" : "long"
                },
                "client_ip": {
                    "type": "keyword"
                },
                "request" : {
                    "properties" : {
                        "headers": {
                            "properties": {
                                "host": {
                                    "type": "text"
                                },
                                "user-agent": {
                                    "type": "text"
                                },
                                "x-authenticated-userid": {
                                    "type": "keyword"
                                }
                            }
                        },
                        "method": {
                            "type": "keyword"
                        },
                        "size": {
                            "type": "long"
                        }
                    }
                },
                "response": {
                    "properties": {
                        "status": {
                            "type": "long"
                        },
                        "size": {
                            "type": "long"
                        }
                    }
                },
                "service": {
                    "properties": {
                        "name": {
                            "type": "keyword"
                        }
                    }
                },
                "route": {
                    "properties": {
                        "id": {
                            "type": "keyword"
                        },
                        "name": {
                            "type": "keyword"
                        }
                    }
                },
                "consumer": {
                    "properties": {
                        "id": {
                            "type": "keyword"
                        },
                        "username": {
                            "type": "keyword"
                        }
                    }
                }
            }
        }
    }
}
'

## Disable auto dynamic mapping

curl -X PUT "localhost:9200/api-log/_settings?pretty" -H 'Content-Type: application/json' -d'
{
  "index.mapper.dynamic":false 
}
'

curl -X PUT "localhost:9200/_template/template_all?pretty" -H 'Content-Type: application/json' -d'
{
  "template": "*",
  "order":0,
  "settings": {
    "index.mapper.dynamic": false 
  }
}
'

## Search

curl -X GET "localhost:9200/api-log/log/_search?pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    },
    "stored_fields": "*"
}
'

## Disable _source field

_source field is where Elasticsearch save the original JSON document that will be returned when getting or searching

curl -X PUT "localhost:9200/api-log?pretty" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "log": {
      "_source": {
        "enabled": false
      }
    }
  }
}
'

## Get the index

curl "localhost:9200/api-log?pretty"

## Delete the index

curl -X DELETE "localhost:9200/api-log?pretty"


## Mapping with store field

curl -X PUT "localhost:9200/api-log?pretty" -H 'Content-Type: application/json' -d'
{
    "settings" : {
        "index.mapper.dynamic":false
    },
    "mappings" : {
        "log" : {
            "_source": {
                "enabled": false
            },
            "dynamic": false,
            "properties" : {
                "latencies" : {
                    "properties" : {
                        "kong" : {
                            "type" : "long",
                            "store": true
                        },
                        "proxy" : {
                            "type" : "long",
                            "store": true
                        },
                        "request" : {
                            "type" : "long",
                            "store": true
                        }
                    }
                },
                "started_at" : {
                    "type" : "long",
                    "store": true
                },
                "client_ip": {
                    "type": "keyword",
                    "store": true
                },
                "request" : {
                    "properties" : {
                        "headers": {
                            "properties": {
                                "host": {
                                    "type": "text",
                                    "store": true
                                },
                                "user-agent": {
                                    "type": "text",
                                    "store": true
                                },
                                "x-authenticated-userid": {
                                    "type": "keyword",
                                    "store": true
                                }
                            }
                        },
                        "method": {
                            "type": "keyword",
                            "store": true
                        },
                        "size": {
                            "type": "long",
                            "store": true
                        }
                    }
                },
                "response": {
                    "properties": {
                        "status": {
                            "type": "long",
                            "store": true
                        },
                        "size": {
                            "type": "long",
                            "store": true
                        }
                    }
                },
                "service": {
                    "properties": {
                        "name": {
                            "type": "keyword",
                            "store": true
                        }
                    }
                },
                "route": {
                    "properties": {
                        "id": {
                            "type": "keyword",
                            "store": true
                        },
                        "name": {
                            "type": "keyword",
                            "store": true
                        }
                    }
                },
                "consumer": {
                    "properties": {
                        "id": {
                            "type": "keyword",
                            "store": true
                        },
                        "username": {
                            "type": "keyword",
                            "store": true
                        }
                    }
                }
            }
        }
    }
}
'

## Configure http-log plugin on Kong


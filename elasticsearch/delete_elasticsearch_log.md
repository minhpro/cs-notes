1. Prepare data

Put some documents to the index

```
curl -X PUT "localhost:9200/test/type1/1?pretty" -H 'Content-Type: application/json' -d'
{
    "field1" : "test value 1"
}
'

curl -X PUT "localhost:9200/test/type1/2?pretty" -H 'Content-Type: application/json' -d'
{
    "field1" : "test value 2"
}
'

curl -X PUT "localhost:9200/test/type1/3?pretty" -H 'Content-Type: application/json' -d'
{
    "field1" : "GET xyz"
}
'

curl -X PUT "localhost:9200/test/type1/4?pretty" -H 'Content-Type: application/json' -d'
{
    "field1" : "CONSUMER xyz"
}
'

curl -X PUT "localhost:9200/test/type1/5?pretty" -H 'Content-Type: application/json' -d'
{
    "field1" : "GET CONSUMER xyz"
}
'
```

2. Query to get documents that contain `GET CONSUMER`

```
curl -X POST "localhost:9200/test/type1/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "field1": "GET"
                    }
                },
                {
                    "match": {
                        "field1": "CONSUMER"
                    }
                }
            ]
        }
  }
}
' 
```

3. Delete documents that contain `GET CONSUMER` by query

```
curl -X POST "localhost:9200/test/type1/_delete_by_query?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "field1": "GET"
                    }
                },
                {
                    "match": {
                        "field1": "CONSUMER"
                    }
                }
            ]
        }
  }
}
'
```

4. Query to get documents that contain `GET CONSUMER` again, zero result shoud be returned

Go to step 2
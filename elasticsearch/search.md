**Search exists**

curl -XGET "http://elasticsearch:9200/api-log/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "exists": {
      "field": "request.headers.x-account-id"
    }
  }
}'

**Script field searching**

curl -XGET "http://elasticsearch:9200/api-log/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "exists": {
      "field": "request.headers.x-authenticated-userid"
    }
  },
  "script_fields" : {
            "test1" : {
                "script" : "params[\"_source\"][\"request\"][\"headers\"][\"x-authenticated-userid\"]"
            }
        }
}'


**Painless script**

curl -XGET "http://elasticsearch:9200/api-log/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "exists": {
      "field": "request.headers.x-authenticated-userid"
    }
  },
  "script_fields": {
    "test1": {
      "script": {
        "lang": "painless",
        "source": "params[\"_source\"][\"request\"][\"headers\"][\"x-authenticated-userid\"]"
      }
    }
  }
}'


GET api-log/_search
{
  "query": {
    "match_all":{}
  },
   "stored_fields": [
        "*"
    ],
  "script_fields": {
    "user_id": {
            "script": {
                "source": "String authen = params['_source']['request']['headers']['x-authenticated-userid'];  if (authen == null) return \"no user\"; def mUser = /^user=([0-9]+)$/.matcher(authen);if (mUser.matches()) return mUser.group(1); def m = /^user=([0-9]+); account=([0-9]+)$/.matcher(authen);if ( m.matches() ) {return m.group(2)} else { return \"no user\"}",
                "lang": "painless"
            }
        },
        "account_id": {
            "script": {
                "source": "String authen = params['_source']['request']['headers']['x-authenticated-userid'];  if (authen == null) return \"no account\";  \ndef m = /^user=([0-9]+); account=([0-9]+)$/.matcher(authen);\nif ( m.matches() ) {\n   return m.group(2)\n} else {\n   return \"no account\"\n}",
                "lang": "painless"
            }
        }
  }
}



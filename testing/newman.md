Run Postman collection from the commandline

1. Install Node.js

2. Install newman

`node install -g newman`

3. Run newman command

`newman run <collection.json> -e <environment.json> --reporters cli,json --reporter-json-export report.json`
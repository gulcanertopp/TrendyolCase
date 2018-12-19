# TrendyolCase #

## To quickly see the results of case , please check these paths: ##

src/main/resources/file/resultGroupProduct.txt
src/main/resources/file/resultFullfilledEvents.txt
src/main/resources/file/resultFullfilledEventsUser47.txt
src/main/resources/file/resultFullfilledProductIdUser47.txt
src/main/resources/file/resultGroupEventName.txt


## To control the Docker 

## Run locally 

Get the __docker-compose.yml__ from Github and then use the following snippets

## Start JobManager and TaskManager
__docker-compose up -d__ 

will start in background a JobManager with a single TaskManager and the History Server.

## Scale TaskManagers
docker-compose scale taskmanager=5 will scale to 5 TaskManagers.

## Deploy and Run a Job
**Copy the Flink job JAR to the Job Manager
__docker cp target/trendyolCase-1.0-jar-with-dependencies.jar $(docker ps --filter name=jobmanager --format={{.ID}}):/trendyol.jar__


**Copy the data to each Flink node if necessary
__for i in $(docker ps --filter name=flink --format={{.ID}}); do
  docker cp src/main/resources/file/case.csv $I:/case.csv
done__

## Run the job
__docker exec -it $(docker ps --filter name=jobmanager --format={{.ID}}) flink run -c com.trendyol.BatchJob /trendyol.jar --input file:////case.csv —output file:///media__

where optional params could for example point to the dataset copied at the previous step.

## Accessing Flink Web Dashboard
Navigate to __http://localhost:8081__

## Stop Flink Cluster
docker-compose down shuts down the cluster.


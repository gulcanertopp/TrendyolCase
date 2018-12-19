# TrendyolCase

If you want to look the results of case quickly, Please check these paths:

src/main/resources/file/resultGroupProduct.txt
src/main/resources/file/resultFullfilledEvents.txt
src/main/resources/file/resultFullfilledEventsUser47.txt
src/main/resources/file/resultFullfilledProductIdUser47.txt
src/main/resources/file/resultGroupEventName.txt


If you want to control Docker

Run locally
Get the docker-compose.yml from Github and then use the following snippets

Start JobManager and TaskManager

docker-compose up -d will start in background a JobManager with a single TaskManager and the History Server.

Scale TaskManagers

docker-compose scale taskmanager=5 will scale to 5 TaskManagers.

Deploy and Run a Job

Copy the Flink job JAR to the Job Manager
docker cp target/trendyolCase-1.0-jar-with-dependencies.jar $(docker ps --filter name=jobmanager --format={{.ID}}):/trendyol.jar

Copy the data to each Flink node if necessary

for i in $(docker ps --filter name=flink --format={{.ID}}); do
  docker cp src/main/resources/file/case.csv $I:/case.csv
done

Run the job
docker exec -it $(docker ps --filter name=jobmanager --format={{.ID}}) flink run -c com.trendyol.BatchJob /trendyol.jar --input file:////case.csv —output file:///media

where optional params could for example point to the dataset copied at the previous step.

Accessing Flink Web Dashboard

Navigate to http://localhost:8081

Stop Flink Cluster

docker-compose down shuts down the cluster.


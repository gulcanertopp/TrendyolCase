all: flink
push: push-flink
.PHONY: push push-flink flink

CONTAINER_NAME = melentye/flink
TAG = latest

flink:
	docker build -t $(CONTAINER_NAME) .
	docker tag $(CONTAINER_NAME) $(CONTAINER_NAME):$(TAG)

push-flink: flink
	gcloud docker push $(CONTAINER_NAME)
	gcloud docker push $(CONTAINER_NAME):$(TAG)

clean:
	docker rmi $(CONTAINER_NAME):$(TAG) || :
	docker rmi $(CONTAINER_NAME) || :

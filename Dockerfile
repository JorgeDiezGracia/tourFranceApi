FROM ubuntu:latest
LABEL authors="Jorgeten"
EXPOSE 8080

ENTRYPOINT ["top", "-b"]
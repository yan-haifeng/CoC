FROM openjdk:8-jdk
COPY CoC-gateway/target/CoC-gateway-0.0.1-SNAPSHOT.jar /home/app/CoC-gateway-0.0.1-SNAPSHOT.jar
ENV LANG=UTF-8
ENV TZ=Asia/Shanghai
WORKDIR  /home/app/
CMD java -jar /home/app/CoC-gateway-0.0.1-SNAPSHOT.jar
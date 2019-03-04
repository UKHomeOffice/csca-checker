FROM quay.io/digitalpatterns/jdk:latest

ADD build/libs/csca-checker.jar /app/csca-checker.jar

EXPOSE 8080

USER 1000

ENTRYPOINT exec /bin/run.sh java $JAVA_OPTS -Dmasterlist.location=${MASTERLIST_LOCATION} \
                                            -Djava.security.egd=file:/dev/./urandom \
                                            -Dspring.profiles.active=${SPRING_ENV} \
                                            -jar /app/csca-checker.jar



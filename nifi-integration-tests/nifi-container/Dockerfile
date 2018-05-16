FROM mkobit/nifi:1.0.0-oracle-java8

ADD --chown=nifi:nifi ./start-nifi.sh ./
ADD --chown=nifi:nifi ./conf/* conf/
ADD --chown=nifi:nifi ./toolkit/nifi-*-keystore conf/
ADD --chown=nifi:nifi ./toolkit/nifi-*-truststore conf/
#ADD --chown=nifi:nifi ./tests tests/
RUN mv lib lib-org 
#&& ./tests/build-tests

EXPOSE 8080 8081 8082 9001

CMD ["./start-nifi.sh"]

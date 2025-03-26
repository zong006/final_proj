
# build angular app
FROM node:22 AS ngbuild

WORKDIR /final_frontend

# install angular cli
RUN npm i -g @angular/cli@19.2.1

COPY final_frontend/angular.json .
COPY final_frontend/package.json .
COPY final_frontend/tsconfig.json .
COPY final_frontend/tsconfig.app.json .
#COPY client/server.ts .
#COPY client/ngsw-config.json .
COPY final_frontend/src src

RUN npm i
RUN npm ci
RUN ng build

# Stage 2 - build spring boot
FROM openjdk:23 AS javabuild

WORKDIR /final_backend

COPY final_backend/pom.xml .
COPY final_backend/.mvn .mvn
COPY final_backend/mvnw .
COPY final_backend/src src

COPY --from=ngbuild /final_frontend/dist/final_frontend/browser src/main/resources/static

RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

## RUN container
FROM openjdk:23

WORKDIR /app

COPY --from=javabuild /final_backend/target/*.jar app.jar
COPY final_backend/src/main/resources/*.json /app/config/

ENV PORT=8080

EXPOSE ${PORT}

# start container
ENTRYPOINT [ "java", "-jar", "app.jar"]
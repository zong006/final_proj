
# ---------------------------- STAGE 1 ----------------------------
    FROM maven:3.9.9-eclipse-temurin-23 AS compiler

    ARG COMPIILE_DIR=/code_folder
    
    WORKDIR ${COMPIILE_DIR}
    
    COPY final_backend/pom.xml .
    COPY final_backend/mvnw .
    COPY final_backend/mvnw.cmd .
    COPY final_backend/src src
    COPY final_backend/.mvn .mvn 


    RUN mvn package -Dmaven.test.skip=true
    
    # ---------------------------- STAGE 1 ----------------------------
    
    # ---------------------------- STAGE 2 ----------------------------
    
    FROM maven:3.9.9-eclipse-temurin-23
    
    ARG DEPLOY_DIR=/app
    
    WORKDIR ${DEPLOY_DIR}
    
    COPY --from=compiler /code_folder/target/final_backend-0.0.1-SNAPSHOT.jar app.jar
    

    
    ENTRYPOINT java -jar app.jar
        
    # ---------------------------- STAGE 2 ----------------------------
    
            
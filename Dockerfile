FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /usr/src/app

# Cache dependencies
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

# Copy source
COPY . .

# Default command: run TestNG suite
CMD ["mvn", "-B", "clean", "test"]

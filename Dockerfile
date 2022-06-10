FROM openjdk:11
COPY ./target/java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot.jar java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot.jar
CMD ["java" ,"-jar","java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot.jar"]
RUN echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

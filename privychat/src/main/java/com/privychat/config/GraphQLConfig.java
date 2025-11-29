package com.privychat.config;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType objectIdScalar() {
        return GraphQLScalarType.newScalar()
                .name("ObjectID")
                .description("MongoDB ObjectId 24 hex characters")
                .coercing(new Coercing<ObjectId, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        if (dataFetcherResult instanceof ObjectId oid) {
                            return oid.toHexString();
                        }
                        throw new IllegalArgumentException("Expected ObjectId for serialization but got " + dataFetcherResult);
                    }
                    @Override
                    public ObjectId parseValue(Object input) {
                        if (input instanceof String s && ObjectId.isValid(s)) {
                            return new ObjectId(s);
                        }
                        throw new IllegalArgumentException("Invalid ObjectID value: " + input);
                    }
                    @Override
                    public ObjectId parseLiteral(Object input) {
                        if (input instanceof StringValue sv) {
                            String s = sv.getValue();
                            if (ObjectId.isValid(s)) return new ObjectId(s);
                        }
                        throw new IllegalArgumentException("Invalid ObjectID literal: " + input);
                    }
                }).build();
    }

    @Bean
    public GraphQLScalarType dateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("ISO-8601 DateTime mapped to java.time.Instant")
                .coercing(new Coercing<Instant, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        if (dataFetcherResult instanceof Instant inst) {
                            return inst.toString();
                        }
                        throw new IllegalArgumentException("Expected Instant for serialization but got " + dataFetcherResult);
                    }
                    @Override
                    public Instant parseValue(Object input) {
                        if (input instanceof String s) {
                            try { return Instant.parse(s); } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Invalid DateTime value: " + s);
                            }
                        }
                        throw new IllegalArgumentException("Invalid DateTime value: " + input);
                    }
                    @Override
                    public Instant parseLiteral(Object input) {
                        if (input instanceof StringValue sv) {
                            try { return Instant.parse(sv.getValue()); } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Invalid DateTime literal: " + sv.getValue());
                            }
                        }
                        throw new IllegalArgumentException("Invalid DateTime literal: " + input);
                    }
                }).build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType objectIdScalar, GraphQLScalarType dateTimeScalar) {
        return wiringBuilder -> wiringBuilder.scalar(objectIdScalar).scalar(dateTimeScalar);
    }
}


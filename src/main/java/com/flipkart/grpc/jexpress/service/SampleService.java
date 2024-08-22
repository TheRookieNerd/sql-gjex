package com.flipkart.grpc.jexpress.service;

import com.flipkart.gjex.core.filter.grpc.MethodFilters;
import com.flipkart.gjex.core.logging.Logging;
import com.flipkart.gjex.core.task.ConcurrentTask;
import com.flipkart.gjex.core.tracing.Traced;
import com.flipkart.grpc.jexpress.*;
import com.flipkart.grpc.jexpress.filter.CreateLoggingFilter;
import com.flipkart.grpc.jexpress.filter.GetLoggingFilter;
import com.google.inject.persist.Transactional;
import io.grpc.stub.StreamObserver;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named("SampleService")
public class SampleService extends UserServiceGrpc.UserServiceImplBase implements Logging {

    private final EntityManager entityManager;

    @Inject
    public SampleService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @MethodFilters({GetLoggingFilter.class})
    @Traced
    @ConcurrentTask(timeout = 300)
    public void getUser(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        User user = entityManager.find(User.class, (long) request.getId());
        if (user == null) {
            responseObserver.onError(new IllegalArgumentException("User not found"));
            return;
        }
        GetResponse response = GetResponse.newBuilder()
                .setId(Math.toIntExact(user.getId()))
                .setUserName(user.getName()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @MethodFilters({CreateLoggingFilter.class})
    @Traced
    @ConcurrentTask(timeout = 300)
    public void createUser(CreateRequest request, StreamObserver<CreateResponse> responseObserver) {
        entityManager.getTransaction().begin();
        Long userId = saveUser(User.builder().name(request.getUserName()).build()).getId();
        entityManager.getTransaction().commit();

        CreateResponse response = CreateResponse.newBuilder()
                .setId(Math.toIntExact(userId))
                .setIsCreated(true).
                build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Transactional
    public User saveUser(User user) {
        entityManager.persist(user);
        info("Test Object " + user);
        return user;
    }

}

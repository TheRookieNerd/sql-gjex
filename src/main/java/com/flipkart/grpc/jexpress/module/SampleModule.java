package com.flipkart.grpc.jexpress.module;

import com.flipkart.gjex.core.tracing.TracingSampler;
import com.flipkart.grpc.jexpress.filter.CreateLoggingFilter;
import com.flipkart.grpc.jexpress.filter.GetLoggingFilter;
import com.flipkart.grpc.jexpress.service.UserService;
import com.flipkart.grpc.jexpress.tracing.AllWhitelistTracingSampler;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.grpc.BindableService;
import com.flipkart.gjex.core.filter.grpc.GrpcFilter;

public class SampleModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BindableService.class).annotatedWith(Names.named("UserService")).to(UserService.class);
        bind(GrpcFilter.class).annotatedWith(Names.named("GetLoggingFilter")).to(GetLoggingFilter.class);
        bind(GrpcFilter.class).annotatedWith(Names.named("CreateLoggingFilter")).to(CreateLoggingFilter.class);
        bind(TracingSampler.class).to(AllWhitelistTracingSampler.class);
    }
}

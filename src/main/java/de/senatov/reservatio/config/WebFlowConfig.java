package de.senatov.reservatio.config;



import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.config.AbstractFacesFlowConfiguration;
import org.springframework.faces.webflow.FlowFacesContextLifecycleListener;
import org.springframework.faces.webflow.JsfFlowHandlerAdapter;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.springframework.webflow.security.SecurityFlowExecutionListener;



/**
 * @author Iakov Senatov
 * @since 03.2020
 */
@Configuration
@Slf4j
public class WebFlowConfig extends AbstractFacesFlowConfiguration {

    @Bean
    public FlowExecutor flowExecutor() {

        log.debug("flowExecutor() ");
        return getFlowExecutorBuilder(flowRegistry()).addFlowExecutionListener(new FlowFacesContextLifecycleListener())
                .addFlowExecutionListener(new SecurityFlowExecutionListener())
                .build();
    }



    @Bean
    public FlowDefinitionRegistry flowRegistry() {

        log.debug("flowRegistry() ");
        return getFlowDefinitionRegistryBuilder(flowBuilderServices()).setBasePath("/WEB-INF/flows").addFlowLocationPattern("/**/*-flow.xml").build();
    }



    @Bean
    public FlowBuilderServices flowBuilderServices() {

        log.debug("flowBuilderServices() ");
        return getFlowBuilderServicesBuilder().setDevelopmentMode(true).build();
    }



    @Bean
    public FlowHandlerMapping flowHandlerMapping() {

        FlowHandlerMapping mapping = new FlowHandlerMapping();
        mapping.setOrder(1);
        mapping.setFlowRegistry(flowRegistry());
        /* If no flow matches, map the path to a view, e.g. "/intro" maps to a view named "intro" */
        mapping.setDefaultHandler(new UrlFilenameViewController());
        return mapping;
    }



    @Bean
    public FlowHandlerAdapter flowHandlerAdapter() {

        JsfFlowHandlerAdapter adapter = new JsfFlowHandlerAdapter();
        adapter.setFlowExecutor(flowExecutor());
        return adapter;
    }

}

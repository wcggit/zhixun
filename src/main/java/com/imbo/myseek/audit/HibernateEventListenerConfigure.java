//package com.imbo.myseek.audit;
//
//import org.hibernate.event.service.spi.EventListenerRegistry;
//import org.hibernate.event.spi.EventType;
//import org.hibernate.internal.SessionFactoryImpl;
//import org.hibernate.jpa.HibernateEntityManagerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManagerFactory;
//
//
//@Component
//public class HibernateEventListenerConfigure {
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    @Autowired
//    HibernateAuditEventListener eventListener;
//
//    @PostConstruct
//    public void registerListeners() {
//        HibernateEntityManagerFactory hibernateEntityManagerFactory =
//            (HibernateEntityManagerFactory) this.entityManagerFactory;
//        SessionFactoryImpl sessionFactoryImpl =
//            (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
//        EventListenerRegistry registry =
//            sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
//        registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener(eventListener);
//        registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(eventListener);
//        registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(eventListener);
//        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(eventListener);
//        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(eventListener);
//        registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(eventListener);
//    }
//}

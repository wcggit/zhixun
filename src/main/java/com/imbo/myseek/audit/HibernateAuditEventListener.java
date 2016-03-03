//package com.imbo.myseek.audit;
//
//import com.google.common.base.CaseFormat;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//
//import com.mydreamplus.smartoffice.Application;
//import com.mydreamplus.smartoffice.global.domain.annotation.ArchivableIndicator;
//import com.mydreamplus.smartoffice.global.domain.annotation.AuditEventDataEntry;
//import com.mydreamplus.smartoffice.global.domain.annotation.DisableableIndicator;
//import com.mydreamplus.smartoffice.global.domain.entities.Auditable;
//import com.mydreamplus.smartoffice.global.domain.entities.Disableable;
//import com.mydreamplus.smartoffice.global.infrastructure.consts.AuditEventAction;
//import com.mydreamplus.smartoffice.global.infrastructure.consts.AuditEventConsts;
//import com.mydreamplus.smartoffice.global.infrastructure.consts.SpecialUsers;
//import com.mydreamplus.smartoffice.global.infrastructure.security.SecurityUtils;
//
//import org.hibernate.event.spi.PostCommitDeleteEventListener;
//import org.hibernate.event.spi.PostCommitInsertEventListener;
//import org.hibernate.event.spi.PostCommitUpdateEventListener;
//import org.hibernate.event.spi.PostDeleteEvent;
//import org.hibernate.event.spi.PostInsertEvent;
//import org.hibernate.event.spi.PostUpdateEvent;
//import org.hibernate.event.spi.PreDeleteEvent;
//import org.hibernate.event.spi.PreDeleteEventListener;
//import org.hibernate.event.spi.PreInsertEvent;
//import org.hibernate.event.spi.PreInsertEventListener;
//import org.hibernate.event.spi.PreUpdateEvent;
//import org.hibernate.event.spi.PreUpdateEventListener;
//import org.hibernate.persister.entity.EntityPersister;
//import org.reflections.ReflectionUtils;
//import org.reflections.Reflections;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Component;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Modifier;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
///**
// * Created by edison on 11/24/15.
// */
//@Component
//public class HibernateAuditEventListener implements PostCommitInsertEventListener,
//                                                    PostCommitUpdateEventListener,
//                                                    PostCommitDeleteEventListener,
//                                                    PreInsertEventListener,
//                                                    PreUpdateEventListener, PreDeleteEventListener,
//                                                    InitializingBean {
//    private static final Logger LOG = LoggerFactory.getLogger(HibernateAuditEventListener.class);
//
//    private static final Map<String, Class<? extends ApplicationEvent>> eventClassMap =
//        Maps.newHashMap();
//
//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//
//    @Override public void afterPropertiesSet() throws Exception {
//        new Reflections(Application.class.getPackage().getName())
//            .getSubTypesOf(ApplicationEvent.class)
//            .stream()
//            .forEach(eventClass -> eventClassMap.put(eventClass.getSimpleName(), eventClass));
//    }
//
//    @Override
//    public boolean onPreInsert(PreInsertEvent event) {
//        dispatchEvent("PreInsert", event.getEntity());
//        return false;
//    }
//
//    @Override
//    public boolean onPreUpdate(PreUpdateEvent event) {
//        String action = getActionForEvent(
//            event.getEntity(),
//            event.getPersister().getPropertyNames(),
//            event.getState(),
//            event.getOldState());
//
//        if (Objects.equals(action, AuditEventAction.ACTION_UPDATE)) {
//            dispatchEvent("PreUpdate", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_DISABLE)) {
//            dispatchEvent("PreDisable", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_ENABLE)) {
//            dispatchEvent("PreEnable", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_ARCHIVE)) {
//            dispatchEvent("PreArchive", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_RESTORE)) {
//            dispatchEvent("PreRestore", event.getEntity());
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean onPreDelete(PreDeleteEvent event) {
//        dispatchEvent("PreDelete", event.getEntity());
//        return false;
//    }
//
//    @Override
//    public void onPostInsert(PostInsertEvent event) {
//        dispatchEvent("PostInsert", event.getEntity());
//
//        String action = AuditEventAction.ACTION_CREATE;
//        auditActionForEntity(action, event.getEntity());
//    }
//
//    @Override
//    public void onPostUpdate(PostUpdateEvent event) {
//        String action = getActionForEvent(
//            event.getEntity(),
//            event.getPersister().getPropertyNames(),
//            event.getState(),
//            event.getOldState());
//
//        if (Objects.equals(action, AuditEventAction.ACTION_UPDATE)) {
//            dispatchEvent("PostUpdate", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_DISABLE)) {
//            dispatchEvent("PostDisable", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_ENABLE)) {
//            dispatchEvent("PostEnable", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_ARCHIVE)) {
//            dispatchEvent("PostArchive", event.getEntity());
//        } else if (Objects.equals(action, AuditEventAction.ACTION_RESTORE)) {
//            dispatchEvent("PostRestore", event.getEntity());
//        }
//
//        auditActionForEntity(action, event.getEntity());
//    }
//
//    @Override
//    public void onPostDelete(PostDeleteEvent event) {
//        dispatchEvent("PostDelete", event.getEntity());
//
//        String action = AuditEventAction.ACTION_REMOVE;
//        auditActionForEntity(action, event.getEntity());
//    }
//
//    @Override
//    public void onPostInsertCommitFailed(PostInsertEvent event) {
//    }
//
//    @Override
//    public void onPostUpdateCommitFailed(PostUpdateEvent event) {
//    }
//
//    @Override
//    public void onPostDeleteCommitFailed(PostDeleteEvent event) {
//    }
//
//    @Override
//    public boolean requiresPostCommitHanding(EntityPersister persister) {
//        return true;
//    }
//
//    private void dispatchEvent(String action, Object entity) {
//        // For examples:
//        //   PreInsertUserEvent
//        //   PreUpdateUserEvent
//        String entityName = entity.getClass().getSimpleName();
//        String eventClassName = action + entityName + "Event";
//
//        Class<?> eventType = eventClassMap.get(eventClassName);
//        if (eventType != null) {
//            try {
//                Constructor<?> constructor =
//                    eventType.getConstructor(Object.class, entity.getClass());
//                Object eventInstance = constructor.newInstance(this, entity);
//
//                if(LOG.isDebugEnabled()){
//                    LOG.debug("Dispatch event '{}'", eventClassName);
//                }
//
//                if (ApplicationEvent.class.isInstance(eventInstance)) {
//                    eventPublisher.publishEvent((ApplicationEvent) eventInstance);
//                } else {
//                    eventPublisher.publishEvent(eventInstance);
//                }
//            } catch (NoSuchMethodException | InstantiationException
//                | IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("Cannot find event type '{}' to dispatch", eventClassName);
//            }
//        }
//    }
//
//    private String getActionForEvent(Object entity, String[] propertyNames, Object[] state,
//        Object[] oldState) {
//        String defautlAction = AuditEventAction.ACTION_UPDATE;
//
//        if (!Auditable.class.isInstance(entity)) {
//            return defautlAction;
//        }
//
//        List<Integer> dirtyProperties = getDirtyPropertyIndexes(
//            propertyNames,
//            state,
//            oldState);
//
//        String action = getActionByIndicator(
//            entity,
//            propertyNames,
//            dirtyProperties,
//            state,
//            DisableableIndicator.class,
//            disabled -> disabled ?
//                AuditEventAction.ACTION_DISABLE :
//                AuditEventAction.ACTION_ENABLE);
//
//        if (action == null) {
//            action = getActionByIndicator(
//                entity,
//                propertyNames,
//                dirtyProperties,
//                state,
//                ArchivableIndicator.class,
//                archived -> archived ?
//                    AuditEventAction.ACTION_ARCHIVE :
//                    AuditEventAction.ACTION_RESTORE);
//        }
//
//        if (action == null) {
//            action = defautlAction;
//        }
//
//        return action;
//    }
//
//    private List<Integer> getDirtyPropertyIndexes(String[] propertyNames,
//        Object[] state, Object[] oldState) {
//        List<Integer> dirtyPropertyIndexes = Lists.newArrayList();
//        IntStream.range(0, propertyNames.length)
//            .forEach(index -> {
//                if (!Objects.equals(state[index], oldState[index])) {
//                    dirtyPropertyIndexes.add(index);
//                }
//            });
//        return dirtyPropertyIndexes;
//    }
//
//    private String getActionByIndicator(Object entity, String[] propertyNames,
//        List<Integer> dirtyProperties, Object[] state, Class<? extends Annotation> indicatorClass,
//        Function<Boolean, String> callback) {
//        String action = null;
//
//        int propertyIndex =
//            getIndexOfPropertyAnnotatedByIndicator(
//                entity,
//                propertyNames,
//                indicatorClass);
//
//        if (propertyIndex != -1 && dirtyProperties.contains(propertyIndex)) {
//            boolean indicatorValue = Boolean.parseBoolean(state[propertyIndex].toString());
//            action = callback.apply(indicatorValue);
//        }
//
//        return action;
//    }
//
//    private int getIndexOfPropertyAnnotatedByIndicator(Object entity, String[] propertyNames,
//        Class<? extends Annotation> indicatorAnnotation) {
//
//        if (Disableable.class.isInstance(entity)) {
//            List<Field> indicators =
//                ReflectionUtils
//                    .getFields(
//                        entity.getClass(),
//                        ReflectionUtils.withAnnotation(indicatorAnnotation))
//                    .stream()
//                    .collect(Collectors.toList());
//
//            if (indicators.size() == 1) {
//                return Arrays.asList(propertyNames).indexOf(indicators.get(0).getName());
//            }
//        }
//
//        return -1;
//    }
//
//    private void auditActionForEntity(String action, Object entity) {
//        if (!Auditable.class.isInstance(entity)) {
//            return;
//        }
//
//        eventPublisher.publishEvent(
//            new AuditApplicationEvent(
//                Optional
//                    .ofNullable(SecurityUtils.getCurrentLogin())
//                    .orElse(SpecialUsers.ACCOUNT_SYSTEM),
//                createEventType(action, entity.getClass().getSimpleName()),
//                buildData(entity)));
//    }
//
//    private String createEventType(String action, String type) {
//        return action + "_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type);
//    }
//
//    private Map<String, Object> buildData(Object entity) {
//        Map<String, Object> data = ((Auditable) entity).toAuditEventData();
//
//        if (data == null) {
//            data = Maps.newHashMap();
//        }
//
//        return buildData(entity, data);
//    }
//
//    private Map<String, Object> buildData(Object entity, Map<String, Object> data) {
//        List<Field> auditEventDataEntryFields =
//            ReflectionUtils
//                .getFields(
//                    entity.getClass(),
//                    ReflectionUtils.withAnnotation(AuditEventDataEntry.class))
//                .stream()
//                .collect(Collectors.toList());
//
//        auditEventDataEntryFields
//            .stream()
//            .forEach(field -> {
//                if (!data.containsKey(field.getName())) {
//                    try {
//                        boolean isPrivate = Modifier.isPrivate(field.getModifiers());
//                        if (isPrivate) {
//                            field.setAccessible(true);
//                        }
//
//                        data.put(field.getName(), field.get(entity));
//
//                        if (isPrivate) {
//                            field.setAccessible(false);
//                        }
//                    } catch (IllegalAccessException e) {
//                        LOG.warn("Cannot get value for field {} of entity with type {}: {}",
//                            field.getName(), entity.getClass().getName(), e.getMessage());
//                    }
//                }
//            });
//
//        data.put(AuditEventConsts.CATEGORY_PROPERTY_KEY,
//            AuditEventConsts.CATEGORY_ENTITY_MANIPULATION);
//        data.put(AuditEventConsts.CLASS_NAME_PROPERTY_KEY,
//            entity.getClass().getName());
//        data.put(AuditEventConsts.SIMPLE_CLASS_NAME_PROPERTY_KEY,
//            entity.getClass().getSimpleName());
//
//        return data;
//    }
//}

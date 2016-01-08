# Hibernate-Hazelcast 2nd Level Cache Configuration

**UPDATE**:
This issue was present in Spring Boot ```1.3.0.M5``` and was fixed in ```1.3.0.RC1 ```.
See [this issue](https://github.com/spring-projects/spring-boot/issues/4158).

---

This is a minimal project that demonstrates how to use ```HazelcastInstance``` configured by ```HazelcastAutoConfiguration``` as Second Level Cache for Hibernate.

By default, Spring Boot will initialize ```EntityManagerFactory``` before ```HazelcastInstance```, resulting with configuration being loaded from ```hazelcast-default.xml``` instead of provided ```Config``` ```@Bean```.

Run the project, log contains the following output:

```
2015-10-12 21:29:58.585  INFO 18375 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2015-10-12 21:29:58.593  INFO 18375 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2015-10-12 21:29:58.654  INFO 18375 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {4.3.11.Final}
2015-10-12 21:29:58.656  INFO 18375 --- [           main] org.hibernate.cfg.Environment            : HHH000205: Loaded properties from resource hibernate.properties: {hibernate.cache.hazelcast.instance_name=defaultInstance, hibernate.cache.region.factory_class=com.hazelcast.hibernate.HazelcastCacheRegionFactory, hibernate.cache.use_second_level_cache=true, hibernate.bytecode.use_reflection_optimizer=false, hibernate.hbm2ddl.auto=create-drop, hibernate.cache.hazelcast.shutdown_on_session_factory_close=false}
2015-10-12 21:29:58.657  INFO 18375 --- [           main] org.hibernate.cfg.Environment            : HHH000021: Bytecode provider name : javassist
2015-10-12 21:29:58.838  INFO 18375 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {4.0.5.Final}
2015-10-12 21:29:58.910  INFO 18375 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2015-10-12 21:29:59.128  INFO 18375 --- [           main] o.h.h.i.ast.ASTQueryTranslatorFactory    : HHH000397: Using ASTQueryTranslatorFactory
2015-10-12 21:29:59.151  INFO 18375 --- [           main] c.h.h.HazelcastCacheRegionFactory        : Starting up HazelcastCacheRegionFactory
2015-10-12 21:29:59.166  INFO 18375 --- [           main] com.hazelcast.config.XmlConfigLocator    : Loading 'hazelcast-default.xml' from classpath.
2015-10-12 21:29:59.446  INFO 18375 --- [           main] c.h.instance.DefaultAddressPicker        : [LOCAL] [dev] [3.5.2] Prefer IPv4 stack is true.
2015-10-12 21:29:59.452  INFO 18375 --- [           main] c.h.instance.DefaultAddressPicker        : [LOCAL] [dev] [3.5.2] Picked Address[172.17.42.1]:5701, using socket ServerSocket[addr=/0:0:0:0:0:0:0:0,localport=5701], bind any local is true
2015-10-12 21:29:59.593  INFO 18375 --- [           main] com.hazelcast.spi.OperationService       : [172.17.42.1]:5701 [dev] [3.5.2] Backpressure is disabled
2015-10-12 21:29:59.602  INFO 18375 --- [           main] c.h.s.i.o.c.ClassicOperationExecutor     : [172.17.42.1]:5701 [dev] [3.5.2] Starting with 2 generic operation threads and 4 partition operation threads.
2015-10-12 21:29:59.847  INFO 18375 --- [           main] com.hazelcast.system                     : [172.17.42.1]:5701 [dev] [3.5.2] Hazelcast 3.5.2 (20150826 - ba8dbba) starting at Address[172.17.42.1]:5701
2015-10-12 21:29:59.847  INFO 18375 --- [           main] com.hazelcast.system                     : [172.17.42.1]:5701 [dev] [3.5.2] Copyright (c) 2008-2015, Hazelcast, Inc. All Rights Reserved.
2015-10-12 21:29:59.852  INFO 18375 --- [           main] com.hazelcast.instance.Node              : [172.17.42.1]:5701 [dev] [3.5.2] Creating MulticastJoiner
2015-10-12 21:29:59.854  INFO 18375 --- [           main] com.hazelcast.core.LifecycleService      : [172.17.42.1]:5701 [dev] [3.5.2] Address[172.17.42.1]:5701 is STARTING
2015-10-12 21:30:02.031  INFO 18375 --- [           main] c.h.cluster.impl.MulticastJoiner         : [172.17.42.1]:5701 [dev] [3.5.2] 


Members [1] {
	Member [172.17.42.1]:5701 this
}

2015-10-12 21:30:02.064  INFO 18375 --- [           main] com.hazelcast.core.LifecycleService      : [172.17.42.1]:5701 [dev] [3.5.2] Address[172.17.42.1]:5701 is STARTED
```

To work around this, ```EntityManagerFactory``` needs to be manually configured and marked as dependent on ```HazelcastInstance``` using ```@DependsOn("hazelcastInstance")```, as shown in ```DemoApplication``` class. To demonstrate workaround run the project with ```workaround``` profile, log now contains the following output:

```
2015-10-12 21:42:49.284  INFO 18933 --- [           main] c.h.instance.DefaultAddressPicker        : [LOCAL] [dev] [3.5.2] Prefer IPv4 stack is true.
2015-10-12 21:42:49.293  INFO 18933 --- [           main] c.h.instance.DefaultAddressPicker        : [LOCAL] [dev] [3.5.2] Picked Address[172.17.42.1]:5701, using socket ServerSocket[addr=/0:0:0:0:0:0:0:0,localport=5701], bind any local is true
2015-10-12 21:42:49.544  INFO 18933 --- [           main] com.hazelcast.spi.OperationService       : [172.17.42.1]:5701 [dev] [3.5.2] Backpressure is disabled
2015-10-12 21:42:49.564  INFO 18933 --- [           main] c.h.s.i.o.c.ClassicOperationExecutor     : [172.17.42.1]:5701 [dev] [3.5.2] Starting with 2 generic operation threads and 4 partition operation threads.
2015-10-12 21:42:49.866  INFO 18933 --- [           main] com.hazelcast.system                     : [172.17.42.1]:5701 [dev] [3.5.2] Hazelcast 3.5.2 (20150826 - ba8dbba) starting at Address[172.17.42.1]:5701
2015-10-12 21:42:49.866  INFO 18933 --- [           main] com.hazelcast.system                     : [172.17.42.1]:5701 [dev] [3.5.2] Copyright (c) 2008-2015, Hazelcast, Inc. All Rights Reserved.
2015-10-12 21:42:49.871  INFO 18933 --- [           main] com.hazelcast.instance.Node              : [172.17.42.1]:5701 [dev] [3.5.2] Creating MulticastJoiner
2015-10-12 21:42:49.874  INFO 18933 --- [           main] com.hazelcast.core.LifecycleService      : [172.17.42.1]:5701 [dev] [3.5.2] Address[172.17.42.1]:5701 is STARTING
2015-10-12 21:42:52.051  INFO 18933 --- [           main] c.h.cluster.impl.MulticastJoiner         : [172.17.42.1]:5701 [dev] [3.5.2] 


Members [1] {
	Member [172.17.42.1]:5701 this
}

2015-10-12 21:42:52.067  INFO 18933 --- [           main] com.hazelcast.jmx.ManagementService      : [172.17.42.1]:5701 [dev] [3.5.2] Hazelcast JMX agent enabled.
2015-10-12 21:42:52.091  INFO 18933 --- [           main] com.hazelcast.core.LifecycleService      : [172.17.42.1]:5701 [dev] [3.5.2] Address[172.17.42.1]:5701 is STARTED
2015-10-12 21:42:52.510  INFO 18933 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2015-10-12 21:42:52.519  INFO 18933 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2015-10-12 21:42:52.593  INFO 18933 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {4.3.11.Final}
2015-10-12 21:42:52.597  INFO 18933 --- [           main] org.hibernate.cfg.Environment            : HHH000205: Loaded properties from resource hibernate.properties: {hibernate.cache.hazelcast.instance_name=defaultInstance, hibernate.cache.region.factory_class=com.hazelcast.hibernate.HazelcastCacheRegionFactory, hibernate.cache.use_second_level_cache=true, hibernate.bytecode.use_reflection_optimizer=false, hibernate.hbm2ddl.auto=create-drop, hibernate.cache.hazelcast.shutdown_on_session_factory_close=false}
2015-10-12 21:42:52.598  INFO 18933 --- [           main] org.hibernate.cfg.Environment            : HHH000021: Bytecode provider name : javassist
2015-10-12 21:42:52.819  INFO 18933 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {4.0.5.Final}
2015-10-12 21:42:53.028  INFO 18933 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2015-10-12 21:42:53.133  INFO 18933 --- [           main] o.h.h.i.ast.ASTQueryTranslatorFactory    : HHH000397: Using ASTQueryTranslatorFactory
2015-10-12 21:42:53.150  INFO 18933 --- [           main] c.h.h.HazelcastCacheRegionFactory        : Starting up HazelcastCacheRegionFactory
```

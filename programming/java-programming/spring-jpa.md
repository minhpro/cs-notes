## Create JPA repository programatically

```Java
SimpleJpaRepository<User, Serializable> jpaRepository;
jpaRepository = new SimpleJpaRepository<User, Serializable>(User.class, entityManager);
```

## References

* https://thorben-janssen.com/jpa-native-queries/

